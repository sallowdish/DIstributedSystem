import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class Log {
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
			List<String> outBuffer=buffer.subList(base, seq);
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
	
}
