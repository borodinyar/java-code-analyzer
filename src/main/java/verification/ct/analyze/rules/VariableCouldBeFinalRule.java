package verification.ct.analyze.rules;

import com.github.javaparser.ast.CompilationUnit;
import verification.ct.analyze.ErrorMessage;
import verification.ct.analyze.visitor.VariableCouldBeFinalVisitor;

import java.util.*;

public class VariableCouldBeFinalRule implements Rule {
    private final List<ErrorMessage> warnings = new ArrayList<>();
    private final Set<String> modifiedFields = new HashSet<>(); // Поля класса, которые изменялись

    public List<ErrorMessage> apply(CompilationUnit cu) {
        // Обходим дерево с помощью единого визитора
        var visitor = new VariableCouldBeFinalVisitor(warnings, modifiedFields);
        cu.accept(visitor, null);
        visitor.performFinalChecks();
        return visitor.getWarnings();
    }
}
