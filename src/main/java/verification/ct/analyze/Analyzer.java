package verification.ct.analyze;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import verification.ct.analyze.rules.Rule;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Analyzer {
    private final List<Rule> rules;

    public Analyzer(List<Rule> rules) {
        this.rules = rules;
    }

    public List<ErrorMessage> analyze(CompilationUnit cu) {
        return rules.stream()
                .flatMap(rule -> rule.apply(cu).stream())
                .collect(Collectors.toList());
    }

    public List<ErrorMessage> analyzeFile(Path file) {
        try {
            CompilationUnit cu = StaticJavaParser.parse(file);
            return analyze(cu).stream()
                    .map(error -> errorInFile(file, error))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of(new ErrorMessage("Error parsing file: " + file));
        }
    }

    public List<ErrorMessage> analyzeFolder(Path folder) {
        try {
            List<Path> files = Files.walk(folder)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .collect(Collectors.toList());

            return files.stream()
                    .flatMap(path -> {
                        List<ErrorMessage> errors = analyzeFile(path);
                        return errors.stream().map(error -> errorInFile(path.getFileName(), error));
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of(new ErrorMessage("Error reading folder: " + folder));
        }
    }

    private ErrorMessage errorInFile(Path file, ErrorMessage error) {
        return new ErrorMessage("Error in file " + file.getFileName() + ": " + error.message());
    }

}

