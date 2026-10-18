package practice14;
import practice14.service.ItemService;import java.util.Scanner;
public class Practice14Demo {
    public static void main(String[] args){ItemService service=new ItemService();try(Scanner sc=new Scanner(System.in)){while(true){System.out.println("1 Add  2 List  3 Exit");String input=sc.nextLine().trim();switch(input){case "1"->{System.out.print("Name: ");service.execute("ADD",sc.nextLine());}case "2"->service.execute("LIST","");case "3"->{System.out.println("Count: "+service.count());return;}default->System.out.println("Invalid menu action.");}}}}
}
