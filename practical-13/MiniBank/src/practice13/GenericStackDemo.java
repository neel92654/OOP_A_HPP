package practice13;
public class GenericStackDemo {
    static class Stack<T>{private final Object[] values;private int size;Stack(int capacity){values=new Object[capacity];}void push(T value){if(size==values.length)throw new IllegalStateException("Stack full");values[size++]=value;}@SuppressWarnings("unchecked") T pop(){if(size==0)throw new IllegalStateException("Stack empty");T value=(T)values[--size];values[size]=null;return value;}boolean isEmpty(){return size==0;}}
    public static void main(String[] args){Stack<String> words=new Stack<>(3);words.push("first");words.push("second");System.out.println(words.pop());Stack<Integer> numbers=new Stack<>(3);numbers.push(10);numbers.push(20);System.out.println(numbers.pop());System.out.println("Words empty: "+words.isEmpty());}
}
