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
        assertThat(result).hasSize(5);
        assertThat(result.get(0).message()).contains("Parameter 'Lol' in method 'method' could be final.");
        assertThat(result.get(1).message()).contains("Local variable 'a' in method 'method' could be final.");
        assertThat(result.get(2).message()).contains("Local variable 'b' in method 'method' could be final.");
        assertThat(result.get(3).message()).contains("Local variable 'L' in method 'method' could be final.");
        assertThat(result.get(4).message()).contains("Field 'field' in class could be final.");
        printResults(result);
    }

    @Test
    void testFieldModification() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE+ "FieldModification.java"));
        assertThat(result).isEmpty();
        printResults(result);
    }

    @Test
    void testForLoopVariable() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE+ "ForLoopVariable.java"));
        assertThat(result).isEmpty();
        printResults(result);
    }

    @Test
    void testWhileLoopVariable() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE+ "WhileLoopVariable.java"));
        assertThat(result).isEmpty();
        printResults(result);
    }

    @Test
    void testImmutableVariables() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE+ "ImmutableVariables.java"));
        assertThat(result).hasSize(2);
        assertThat(result.get(0).message()).contains("Local variable 'x' in method 'check' could be final.");
        assertThat(result.get(1).message()).contains("Local variable 's' in method 'check' could be final.");
        printResults(result);
    }
}
