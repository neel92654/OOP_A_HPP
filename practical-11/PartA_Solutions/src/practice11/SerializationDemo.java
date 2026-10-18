package practice11;
import java.io.*;import java.nio.file.*;
public class SerializationDemo {
    static class Student implements Serializable {private static final long serialVersionUID=1L;String name;transient String secret;Student(String name,String secret){this.name=name;this.secret=secret;}}
    public static void main(String[] args)throws Exception{Path file=Path.of("data/save-demo.bin");Files.createDirectories(file.getParent());Student[] students={new Student("Riya","hidden"),new Student("Aman","private")};try(ObjectOutputStream out=new ObjectOutputStream(Files.newOutputStream(file))){out.writeObject(students);}try(ObjectInputStream in=new ObjectInputStream(Files.newInputStream(file))){Student[] loaded=(Student[])in.readObject();for(Student s:loaded)System.out.println(s.name+", transient secret="+s.secret);}System.out.println("Saved to "+file);}
}
