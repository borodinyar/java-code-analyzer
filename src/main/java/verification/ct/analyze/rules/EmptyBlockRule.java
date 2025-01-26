package verification.ct.analyze.rules;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.BlockStmt;
import verification.ct.analyze.ErrorMessage;

import java.util.List;
import java.util.stream.Collectors;

public class EmptyBlockRule implements Rule{
    @Override
    public List<ErrorMessage> apply(CompilationUnit cu) {
        return cu.findAll(BlockStmt.class).stream()
                .filter(BlockStmt::isEmpty)
                .map(block -> new ErrorMessage("Warning: Empty block found at line " + block.getBegin().get().line))
                .collect(Collectors.toList());
    }
}
