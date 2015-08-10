
import java.lang.String;
import java.util.Scanner;
public class Part1 { 
		public static void main(String [] args){
			String firstname;
			int age;
			System.out.println("Please input your first name:");
			Scanner a = new Scanner(System.in);
			firstname = a.nextLine();
			if(!firstname.isEmpty()){
				String output;
				output = "Hello,"+firstname;
				System.out.println(output);
			}
			else{
				System.err.println("You must input as least one character.");
			}
			System.out.println("Please input your age:");
			age = a.nextInt();
			if(age>0){
		        int output;
				output = 10+age;
				System.out.println("After ten years, you are:" + output);
			}
			else{
				System.err.println("You must input valid integer.");
			}
			a.close();
		}
}

