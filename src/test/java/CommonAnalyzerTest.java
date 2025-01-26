import verification.ct.analyze.Analyzer;
import verification.ct.analyze.ErrorMessage;
import verification.ct.analyze.rules.Rule;

import java.nio.file.Path;
import java.util.List;

class CommonAnalyzerTest {

    protected final Analyzer analyzer;

    CommonAnalyzerTest(List<Rule> rules) {
        this.analyzer = new Analyzer(rules);
    }

    protected Path getResource(String resource) {
        try {
            return Path.of(getClass().getClassLoader().getResource(resource).toURI());
        } catch (Exception e) {
            throw new IllegalArgumentException("Resource not found: " + resource, e);
        }
    }

    protected void printResults(List<ErrorMessage> results) {
        if (results.isEmpty()) {
            System.out.println("No warnings found.");
        } else {
            System.out.println("Warnings found:");
            results.forEach(error -> System.out.println("- " + error.message()));
        }
    }
}