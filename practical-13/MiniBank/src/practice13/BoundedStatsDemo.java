package practice13;
public class BoundedStatsDemo {
    public static <N extends Number> double average(N[] values){if(values.length==0)throw new IllegalArgumentException("Values cannot be empty");double sum=0;for(N value:values)sum+=value.doubleValue();return sum/values.length;}
    public static void main(String[] args){System.out.println("Integer average: "+average(new Integer[]{2,4,6}));System.out.println("Double average: "+average(new Double[]{1.5,2.5,3.5}));System.out.println("Long average: "+average(new Long[]{10L,20L,30L}));}
}
