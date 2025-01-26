import org.junit.jupiter.api.Test;
import verification.ct.analyze.ErrorMessage;
import verification.ct.analyze.rules.ControlFlowBracesRule;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ControlFlowBracesTest extends CommonAnalyzerTest {
    private final static String PATH_RESOURCE = "controlflowbraces/";

    ControlFlowBracesTest() {
        super(List.of(
                new ControlFlowBracesRule()));
    }

    @Test
    void testIfWithoutBraces() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE + "IfWithoutBraces.java"));
        assertThat(result).hasSize(1);
        System.out.println(result);
    }

    @Test
    void testIfWithBraces() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE + "IfWithBraces.java"));
        assertThat(result).isEmpty();
    }

    @Test
    void testForWithoutBraces() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE + "ForWithoutBraces.java"));
        assertThat(result).hasSize(1);
        System.out.println(result);
    }

    @Test
    void testWhileWithoutBraces() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE + "WhileWithoutBraces.java"));
        assertThat(result).hasSize(1);
        System.out.println(result);
    }

    @Test
    void testDoWhileWithoutBraces() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE + "DoWhileWithoutBraces.java"));
        assertThat(result).hasSize(1);
        System.out.println(result);
    }
}
