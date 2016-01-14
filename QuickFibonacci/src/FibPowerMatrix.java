import java.util.ArrayList;

/**
 * Created by Ray on 15-09-18.
 */
public class FibPowerMatrix {
    static final int[][] identityMatrix = {{1,1},{1,0}};

    static int fibPowerMatrix(int n)
    {
        int[][] power = power(identityMatrix, n);
        return power[0][1];
    }

    static private int[][] power(int[][] base, int p)
    {
        // 2*2 Identity Matrix
        int[][] res = {{1,0},{0,1}};
        for (int i = 0; i < p; i++) {
            res = product(res,base);
        }
        return res;
    }

    static private int[][] product(int[][] op1, int[][] op2)
    {
        int[][] res = new int[2][2];
        res[0][0] = op1[0][0] * op2[0][0] + op1[0][1] * op2[1][0];
        res[0][1] = op1[0][0] * op2[0][1] + op1[0][1] * op2[1][1];
        res[1][0] = op1[1][0] * op2[0][0] + op1[1][1] * op2[1][0];
        res[1][1] = op1[1][0] * op2[0][1] + op1[1][1] * op2[1][1];
        return res;
    }
}
