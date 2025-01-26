import org.junit.jupiter.api.Test;
import verification.ct.analyze.ErrorMessage;
import verification.ct.analyze.rules.VariableCouldBeFinalRule;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class VariableCouldBeFinalTest extends CommonAnalyzerTest {
    private final static String PATH_RESOURCE = "variablefinal/";

    VariableCouldBeFinalTest() {
        super(List.of(
                new VariableCouldBeFinalRule()
        ));
    }
    @Test
    void testVariableCouldBeFinal() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE+ "VariableCouldBeFinal.java"));
        printResults(result);
        assertThat(result).hasSize(5);
        assertThat(result.get(0).message()).contains("could be final");
    }

    @Test
    void testFieldModification() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE+ "FieldModification.java"));
        printResults(result);
        assertThat(result).isEmpty();
    }

    @Test
    void testForLoopVariable() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE+ "ForLoopVariable.java"));
        printResults(result);
        assertThat(result).isEmpty();
    }

    @Test
    void testWhileLoopVariable() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE+ "WhileLoopVariable.java"));
        printResults(result);
        assertThat(result).isEmpty();
    }

    @Test
    void testImmutableVariables() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE+ "ImmutableVariables.java"));
        printResults(result);
        assertThat(result).hasSize(2);
    }
}
