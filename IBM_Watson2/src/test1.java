import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.String;

public class test1 {

	static HashMap<String, Integer> geneMap;
	
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    String s;
	    try {
			s=in.readLine();
			String[] parts = s.split(" ");
			Integer N=-1,p=-1,q=-1;
			for(int i=0;i<3;i++){
			    N = Integer.parseInt(parts[0]);
			    p = Integer.parseInt(parts[1]);
			    q = Integer.parseInt(parts[2]);
			    // validateing input
			}
			boolean flag1=false,flag2=false;
			for(Integer i =1;i<N+1;i++){
				if(i%q==0 && !i.toString().contains(p.toString())){flag1=true;}
				if(i%p==0 && !i.toString().contains(q.toString())){flag2=true;}
				if(flag1&&flag2){
					System.out.print("WATSON");
				}else if(flag1&&!flag2){
					System.out.print("WAT");
				}else if(!flag1&&flag2){
					System.out.print("SON");
				}else{
					System.out.print(i.toString());
				}
				System.out.print(" ");
				flag1=flag2=false;
			}
		} catch (Exception e) {
			// TODO: handle exception
//				System.err.println("Error");
			throw new IOException("Invalid Input!");
		}
	}
}