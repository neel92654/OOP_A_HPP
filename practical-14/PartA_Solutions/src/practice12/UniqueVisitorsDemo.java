package practice12;
import java.util.*;
public class UniqueVisitorsDemo {
    public static void main(String[] args){List<String> logins=List.of("riya","aman","riya","neha","aman","kabir","neha");Set<String> unique=new HashSet<>(logins);LinkedHashSet<String> order=new LinkedHashSet<>(logins);System.out.println("Unique visitors: "+unique.size());System.out.println("First three in first-seen order: "+order.stream().limit(3).toList());}
}
