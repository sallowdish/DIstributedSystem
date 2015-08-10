
import java.util.ArrayList;
import java.util.Scanner;

public class Part2 {
	public static void main(String [] args){
		int number;
		ArrayList<Double> data = new ArrayList<Double>();
		System.out.println("Please input the num of data:");
		Scanner a = new Scanner(System.in);
		number = a.nextInt();
		//TODO: update to num>1
		if(number>0){
			System.out.println("Plase input the data, seperated by space or enter:");
			for(int i=0;i<number;i++){
				// update to nextDouble()
				data.add(a.nextDouble());
			}
			
			//find mean of all data
			double mean=0,sum=0;
			for(int i=0;i<number;i++){
				sum= sum + (double)data.get(i);
			}
			mean = sum/number;
			
			//find difference square
			double squareSum=0;
			for(int i=0;i<number;i++){
				squareSum= squareSum + Math.pow((double)data.get(i)-mean,2);
			}
			double output = Math.sqrt(squareSum/(number-1));
			System.out.println("Standard Divation is :"+ output);
	}
		else{
			System.err.println("You must input valid integer.");
		}
		a.close();
	}
}
