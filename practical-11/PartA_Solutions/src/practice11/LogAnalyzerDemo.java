package practice11;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Locale;

public class LogAnalyzerDemo {
    public static void main(String[] args) throws IOException {
        Path dir = Path.of("data/log-analysis");
        Files.createDirectories(dir);
        Files.write(dir.resolve("app1.log"), List.of("INFO app started", "ERROR disk full", "INFO ready"));
        Files.write(dir.resolve("app2.log"), List.of("WARN retry", "ERROR timeout"));
        String keyword = args.length == 0 ? "ERROR" : args[0];
        long lines = 0;
        long matches = 0;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.log")) {
            for (Path file : stream) {
                long fileLines = 0;
                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines++;
                        fileLines++;
                        if (line.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT))) {
                            matches++;
                        }
                    }
                }
                BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
                System.out.println(file.getFileName() + ": lines=" + fileLines + ", size=" + attributes.size() + " bytes");
            }
        }
        System.out.println("Total lines: " + lines);
        System.out.println("Lines containing '" + keyword + "': " + matches);
    }
}
