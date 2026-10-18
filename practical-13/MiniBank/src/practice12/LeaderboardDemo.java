package practice12;
import java.util.*;
public class LeaderboardDemo {
    public static void main(String[] args){Map<String,Integer> scores=Map.of("Riya",95,"Aman",88,"Neha",95,"Kabir",72,"Mira",91);int top=args.length==0?3:Integer.parseInt(args[0]);List<Map.Entry<String,Integer>> rows=new ArrayList<>(scores.entrySet());rows.sort(Map.Entry.<String,Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()));rows.stream().limit(top).forEach(e->System.out.println(e.getKey()+" : "+e.getValue()));}
}
