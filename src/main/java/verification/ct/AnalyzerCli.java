package verification.ct;

import verification.ct.analyze.Analyzer;
import verification.ct.analyze.ErrorMessage;
import verification.ct.analyze.rules.ControlFlowBracesRule;
import verification.ct.analyze.rules.EmptyBlockRule;
import verification.ct.analyze.rules.VariableCouldBeFinalRule;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AnalyzerCli {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java AnalyzerCli <source-folder>");
            return;
        }

        Path sourcePath = Path.of(args[0]);

        Analyzer analyzer = new Analyzer(List.of(
                new EmptyBlockRule(),
                new ControlFlowBracesRule(),
                new VariableCouldBeFinalRule()
        ));

        List<ErrorMessage> errors = Files.isRegularFile(sourcePath)
                ? analyzer.analyzeFile(sourcePath)
                : analyzer.analyzeFolder(sourcePath);

        if (errors.isEmpty()) {
            System.out.println("All checks passed");
        } else {
            errors.forEach(System.err::println);
            System.exit(1);
        }
    }
}