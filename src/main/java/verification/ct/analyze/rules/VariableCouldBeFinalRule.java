package verification.ct.analyze.rules;

import com.github.javaparser.ast.CompilationUnit;
import verification.ct.analyze.ErrorMessage;
import verification.ct.analyze.visitor.VariableCouldBeFinalVisitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VariableCouldBeFinalRule implements Rule {
    private final List<ErrorMessage> warnings = new ArrayList<>();
    private final Set<String> modifiedFields = new HashSet<>(); // Поля класса, которые изменялись

    public List<ErrorMessage> apply(CompilationUnit cu) {
        // Обходим дерево с помощью единого визитора
        var visitor = new VariableCouldBeFinalVisitor(warnings, modifiedFields);
        cu.accept(visitor, null);
        return visitor.getWarnings();
    }
}
