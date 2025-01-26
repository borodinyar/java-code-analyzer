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
        printResults(result);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).message()).contains("Empty block found");
    }

    @Test
    void testNoEmptyBlocks() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE + "NoEmptyBlock.java"));
        printResults(result);
        assertThat(result).isEmpty();
    }

    @Test
    void testNestedEmptyBlock() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE + "NestedEmptyBlock.java"));
        printResults(result);
        assertThat(result).hasSize(1);
    }

    @Test
    void testMultipleEmptyBlocks() {
        List<ErrorMessage> result = analyzer.analyzeFile(getResource(PATH_RESOURCE + "MultipleEmptyBlocks.java"));
        printResults(result);
        assertThat(result).hasSize(2);
    }
}
