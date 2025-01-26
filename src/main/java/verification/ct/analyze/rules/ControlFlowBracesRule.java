package verification.ct.analyze.rules;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithBody;
import com.github.javaparser.ast.stmt.*;
import verification.ct.analyze.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowBracesRule implements Rule{
    public List<ErrorMessage> apply(CompilationUnit cu) {
        List<ErrorMessage> issues = new ArrayList<>();

        checkIfStmtForBraces(cu.findAll(IfStmt.class), issues);
        checkStatementsForBraces(cu.findAll(ForStmt.class), "for", issues);
        checkStatementsForBraces(cu.findAll(WhileStmt.class), "while", issues);
        checkStatementsForBraces(cu.findAll(DoStmt.class), "do", issues);

        return issues;
    }

    private void checkIfStmtForBraces(List<IfStmt> statements, List<ErrorMessage> issues) {
        statements.stream()
                .filter(ifStmt -> !ifStmt.getThenStmt().isBlockStmt())
                .forEach(ifStmt -> issues.add(new ErrorMessage("Warning: 'if' statement without braces at line " + ifStmt.getBegin().get().line)));
    }

    private <T extends Statement & NodeWithBody> void checkStatementsForBraces(List<T> statements, String type, List<ErrorMessage> issues) {
        statements.stream()
                .filter(stmt -> !stmt.getBody().isBlockStmt())
                .forEach(stmt -> issues.add(new ErrorMessage("Warning: '" + type + "' statement without braces at line " + stmt.getBegin().get().line)));
    }

}

