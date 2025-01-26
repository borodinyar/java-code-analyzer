package verification.ct.analyze.visitor;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import verification.ct.analyze.ErrorMessage;

import java.util.*;

public class VariableCouldBeFinalVisitor extends VoidVisitorAdapter<Void> {
    private final List<ErrorMessage> warnings;
    private final Set<String> modifiedFields;
    private final Map<String, ErrorMessage> fieldsToCheck = new HashMap<>();

    public VariableCouldBeFinalVisitor(List<ErrorMessage> warnings, Set<String> modifiedFields) {
        this.warnings = warnings;
        this.modifiedFields = modifiedFields;
    }

    public List<ErrorMessage> getWarnings() {
        return warnings;
    }

    @Override
    public void visit(FieldDeclaration field, Void arg) {
        super.visit(field, arg);

        field.getVariables().forEach(variable -> {
            String fieldName = variable.getNameAsString();

            // Игнорируем static поля или уже объявленные как final
            if (field.isStatic() || field.isFinal()) {
                return;
            }

            // Учитываем случаи, когда поле действительно изменяется в методах
            if (!modifiedFields.contains(fieldName)) {
                fieldsToCheck.put(fieldName, new ErrorMessage(String.format("Field '%s' in class could be final.", fieldName)));
            }
        });
    }

    @Override
    public void visit(MethodDeclaration method, Void arg) {
        super.visit(method, arg);

        // Список имен параметров метода
        Set<String> methodParameters = new HashSet<>();
        method.getParameters().forEach(param -> methodParameters.add(param.getNameAsString()));

        method.getBody().ifPresent(body -> {
            methodParameters.forEach(param -> {
                boolean isModified = body.findAll(AssignExpr.class).stream()
                        .anyMatch(assignExpr -> isVariableModified(assignExpr, param));

                // Добавляем предупреждения для параметров, которые не изменялись
                if (!isModified) {
                    warnings.add(new ErrorMessage(String.format(
                            "Parameter '%s' in method '%s' could be final.",
                            param, method.getNameAsString()
                    )));
                }
            });

            body.findAll(VariableDeclarationExpr.class).forEach(varDecl -> {
                varDecl.getVariables().forEach(variable -> {
                    String variableName = variable.getNameAsString();

                    // Проверяем, изменяется ли переменная в теле метода, включая циклы while и for
                    boolean isModified = body.findAll(AssignExpr.class).stream()
                            .anyMatch(assignExpr -> isVariableModified(assignExpr, variableName));

                    if (!isModified && !varDecl.isFinal() &&
                            !isPartOfLoop(variableName, body.findAll(ForStmt.class), body.findAll(WhileStmt.class))) {
                        warnings.add(new ErrorMessage(String.format(
                                "Local variable '%s' in method '%s' could be final.",
                                variableName, method.getNameAsString()
                        )));
                    }
                });
            });
        });
    }

    @Override
    public void visit(ForStmt forStmt, Void arg) {
        super.visit(forStmt, arg);

        forStmt.getInitialization().forEach(initStmt -> {
            if (initStmt instanceof VariableDeclarationExpr varDeclExpr) {
                varDeclExpr.getVariables().forEach(variable -> {
                    String variableName = variable.getNameAsString();
                    modifiedFields.add(variableName); // Помечаем, что изменяется в цикле
                });
            }
        });

        // Обрабатываем обновления внутри for-цикла
        forStmt.getUpdate().forEach(updateExpr -> {
            if (updateExpr instanceof AssignExpr assignExpr && assignExpr.getTarget() instanceof NameExpr nameExpr) {
                modifiedFields.add(nameExpr.getNameAsString());
            }
        });
    }

    @Override
    public void visit(WhileStmt whileStmt, Void arg) {
        super.visit(whileStmt, arg);

        // Проверяем, изменяются ли переменные в цикле while
        whileStmt.getBody().findAll(AssignExpr.class).forEach(assignExpr -> {
            if (assignExpr.getTarget() instanceof NameExpr nameExpr) {
                modifiedFields.add(nameExpr.getNameAsString());
            }
        });
    }

    @Override
    public void visit(AssignExpr assignExpr, Void arg) {
        super.visit(assignExpr, arg);

        if (assignExpr.getTarget() instanceof NameExpr nameExpr) {
            modifiedFields.add(nameExpr.getNameAsString());
        }
    }

    private boolean isVariableModified(AssignExpr assignExpr, String variableName) {
        return assignExpr.getTarget() instanceof NameExpr nameExpr &&
                nameExpr.getNameAsString().equals(variableName);
    }

    private boolean isPartOfLoop(String variableName, List<ForStmt> forLoops, List<WhileStmt> whileLoops) {
        return forLoops.stream().anyMatch(forStmt ->
                forStmt.getInitialization().stream().anyMatch(init -> init.toString().contains(variableName)) ||
                        forStmt.getCompare().map(compareExpr -> compareExpr.toString().contains(variableName)).orElse(false) ||
                        forStmt.getUpdate().stream().anyMatch(update -> update.toString().contains(variableName))
        ) || whileLoops.stream().anyMatch(whileStmt ->
                whileStmt.getCondition().toString().contains(variableName) ||
                        whileStmt.getBody().toString().contains(variableName)
        );
    }

    public void performFinalChecks() {
        for (String field : fieldsToCheck.keySet()) {
            // Проверяем, если поле еще не было добавлено в modifiedFields
            if (!modifiedFields.contains(field)) {
                warnings.add(new ErrorMessage(String.format("Field '%s' in class could be final.", field)));
            }
        }
        // После проверки очищаем временный список
        fieldsToCheck.clear();
    }
}
