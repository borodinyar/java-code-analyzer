import org.junit.jupiter.api.Test;
import verification.ct.analyze.ErrorMessage;
import verification.ct.analyze.rules.EmptyBlockRule;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class EmptyBlockTest extends CommonAnalyzerTest {
    private final static String PATH_RESOURCE = "emptyblock/";

    EmptyBlockTest() {
        super(List.of(
                new EmptyBlockRule()));
    }

    @Test
    void testEmptyBlockPresent() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE + "EmptyBlock.java"));
        assertThat(result).hasSize(1);
        assertThat(result.get(0).message()).contains("Empty block found at line 3");
        printResults(result);
    }

    @Test
    void testNoEmptyBlocks() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE + "NoEmptyBlock.java"));
        assertThat(result).isEmpty();
        printResults(result);
    }

    @Test
    void testNestedEmptyBlock() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE + "NestedEmptyBlock.java"));
        assertThat(result).hasSize(1);
        assertThat(result.get(0).message()).contains("Empty block found at line 4");
        printResults(result);
    }

    @Test
    void testMultipleEmptyBlocks() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE + "MultipleEmptyBlocks.java"));
        assertThat(result).hasSize(2);
        assertThat(result.get(0).message()).contains("Empty block found at line 3");
        assertThat(result.get(1).message()).contains("Empty block found at line 6");
        printResults(result);
    }
}
