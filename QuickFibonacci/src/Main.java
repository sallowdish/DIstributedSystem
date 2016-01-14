import java.util.Scanner;

/**
 * Created by Ray on 15-09-18.
 */
public class Main {
    public static void main(String args[]){
        Scanner s = new Scanner(System.in);
        int x = s.nextInt();
        while(x != -1)
        {
            System.out.println(String.format("Fibs = %s", FibLinearTableAlgo.fibLinearTable(x)));
            System.out.println(String.format("Fib(%s) = %s", x, FibPowerMatrix.fibPowerMatrix(x)));
            x = s.nextInt();
        }
        s.close();
    }
}
