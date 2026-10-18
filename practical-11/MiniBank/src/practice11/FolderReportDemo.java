package practice11;
import java.nio.file.*;import java.nio.file.attribute.BasicFileAttributes;import java.io.*;import java.util.*;
public class FolderReportDemo {
    public static void main(String[] args)throws IOException{Path base=Path.of("data/tree-demo");Files.createDirectories(base.resolve("nested"));Files.writeString(base.resolve("one.txt"),"sample");Files.writeString(base.resolve("nested/two.log"),"entry\n");Path report=Path.of("data/folder-tree-report.txt");StringBuilder text=new StringBuilder();try(var paths=Files.walk(base)){for(Path p:(Iterable<Path>)paths::iterator){if(Files.isRegularFile(p)){BasicFileAttributes a=Files.readAttributes(p,BasicFileAttributes.class);text.append(base.relativize(p)).append(" | ").append(a.size()).append(" bytes | ").append(a.lastModifiedTime()).append(System.lineSeparator());}}}Files.writeString(report,text.toString());System.out.println(text);System.out.println("Report saved to "+report);}
}
