import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Log {
	public static final String delimiter="~123456789";
	public int transcationID=-1;
	public int sequenceNum=0;
	public String filepath="";
	public List<String> buffer=new ArrayList<String>(0);
	public int base=0;
	
	public Log(int tID, String path){
		transcationID=tID;
		filepath=path;
	}
	
	public void writeToLog(String data){
		sequenceNum++;
		buffer.add(data);
	}
	
	public boolean writeToDisk(int seq){
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filepath,true)))) {
			List<String> outBuffer=buffer.subList(0, seq-base);
			buffer=buffer.subList(seq-base, buffer.size());
			for (String string : outBuffer) {
				out.println(string+" ");
				base++;
			}
			
		    return true;
		}catch (Exception e) {
			System.err.println("Fail to write into disk");
			return false;
		}
	}
	
	public String toString(){
		return transcationID+" "+sequenceNum+" "+filepath+" "+base+delimiter+buffer.toString();
	}
	
	public Log(String requestBody) {
		String[] lst=requestBody.split(delimiter);
		String[] header=lst[0].split(" ");
		transcationID=Integer.valueOf(header[0]);
		sequenceNum=Integer.valueOf(header[1]);
		filepath=header[2];
		base=Integer.valueOf(header[3]);
		buffer=Arrays.asList(lst[1].substring(1, lst[1].length()-1).split("\\s*,\\s*"));
	}
}
