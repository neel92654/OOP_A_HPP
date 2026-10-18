package util;
import java.io.*;import java.nio.charset.StandardCharsets;import java.nio.file.*;import java.nio.file.attribute.BasicFileAttributes;import java.util.*;
public final class ReportGenerator {
    private ReportGenerator(){}
    public static String generate(Path logsDirectory,Path reportFile)throws IOException{
        Files.createDirectories(logsDirectory);long deposits=0,withdrawals=0;StringBuilder details=new StringBuilder();
        try(DirectoryStream<Path> files=Files.newDirectoryStream(logsDirectory,"*.log")){
            for(Path file:files){BasicFileAttributes attrs=Files.readAttributes(file,BasicFileAttributes.class);details.append(file.getFileName()).append(" | size=").append(attrs.size()).append(" bytes | modified=").append(attrs.lastModifiedTime()).append(System.lineSeparator());
                try(BufferedReader reader=Files.newBufferedReader(file,StandardCharsets.UTF_8)){String line;while((line=reader.readLine())!=null){String[] parts=line.trim().split("\\s+");if(parts.length<3)continue;try{long amount=Long.parseLong(parts[parts.length-1]);if(parts[0].equalsIgnoreCase("DEPOSIT"))deposits+=amount;else if(parts[0].equalsIgnoreCase("WITHDRAW"))withdrawals+=amount;}catch(NumberFormatException ignored){}}
                }
            }
        }
        String report="MINIBANK END-OF-DAY REPORT"+System.lineSeparator()+"Total deposits: "+deposits+System.lineSeparator()+"Total withdrawals: "+withdrawals+System.lineSeparator()+"Net change: "+(deposits-withdrawals)+System.lineSeparator()+"Files:"+System.lineSeparator()+details;
        Path parent=reportFile.toAbsolutePath().getParent();if(parent!=null)Files.createDirectories(parent);Files.writeString(reportFile,report,StandardCharsets.UTF_8);return report;
    }
}
