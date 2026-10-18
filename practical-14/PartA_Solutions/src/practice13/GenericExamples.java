package practice13;
import java.util.Arrays;
public class GenericExamples {
    static class Box<T>{private T value;Box(T value){this.value=value;}T get(){return value;}void set(T value){this.value=value;}}
    record Pair<A,B>(A first,B second){}
    static <T>T first(T[] values){if(values.length==0)throw new IllegalArgumentException("Array is empty");return values[0];}
    public static void main(String[] args){Box<String> box=new Box<>("hello");box.set("type safe");Pair<String,Integer> pair=new Pair<>("age",18);String[] names={"Riya","Aman"};System.out.println("Box: "+box.get());System.out.println("Pair: "+pair);System.out.println("First: "+first(names));System.out.println("Array: "+Arrays.toString(names));}
}
