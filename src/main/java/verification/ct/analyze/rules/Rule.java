package verification.ct.analyze.rules;

import com.github.javaparser.ast.CompilationUnit;
import verification.ct.analyze.ErrorMessage;

import java.util.List;

public interface Rule {
    List<ErrorMessage> apply(CompilationUnit cu);
}