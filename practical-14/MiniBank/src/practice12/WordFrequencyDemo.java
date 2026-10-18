package practice12;
import java.util.*;
public class WordFrequencyDemo {
    public static void main(String[] args){String paragraph="java makes code reusable and java supports object oriented programming";Map<String,Integer> counts=new HashMap<>();for(String word:paragraph.toLowerCase(Locale.ROOT).split("\\W+"))if(!word.isBlank())counts.merge(word,1,Integer::sum);List<Map.Entry<String,Integer>> sorted=new ArrayList<>(counts.entrySet());sorted.sort(Map.Entry.<String,Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()));sorted.forEach(e->System.out.println(e.getKey()+" : "+e.getValue()));}
}
