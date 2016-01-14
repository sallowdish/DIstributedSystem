import java.util.ArrayList;

/**
 * Created by Ray on 15-09-18.
 */
public class FibLinearTableAlgo {
    static int fibLinearTableAlgo(int n){
        if(n >= 0 ) {
            return fibLinearTable(n).get(n);
        }
        throw new IllegalArgumentException("Expect n >= 0");
    }

    static ArrayList<Integer> fibLinearTable(int n){
        if(n == 0){
            return new ArrayList<Integer>(){{add(1);}};
        }
        else if(n == 1)
        {
            return new ArrayList<Integer>(){{add(1);add(2);}};
        }
        else
        {
            ArrayList<Integer> res = new ArrayList<Integer>(){{add(1);add(2);}};
            for (int i = 2; i < n; i++) {
                res.add( res.get(i-1) + res.get(i-2));
            }
            return res;
        }
    }
}
