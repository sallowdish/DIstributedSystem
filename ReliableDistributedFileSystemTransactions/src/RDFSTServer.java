import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.cli.CommandLine;


public class RDFSTServer {
	static enum MODE {PRIMARY,SECONDARY};
	static final int MAX_CONNECTION=10;
	public static Path fileSystemPath;
	public static String ip="127.0.0.1";
	public static int port=8080;
	public static MODE serverMode=MODE.SECONDARY;
	public static Path primaryRecordPath;
	private static ServerSocket socket;
		
	public static void main(String[] args) throws Exception{
//		ServerSocket socket;
		CLI scanner=new CLI(args);
		
		if(scanner.parse()){
			try {
				CommandLine cmd=scanner.cmd;
				fileSystemPath=Paths.get(cmd.getOptionValue("dir"));
				if (fileSystemPath.startsWith("~" + File.separator)) {
				    fileSystemPath = Paths.get(System.getProperty("user.home") + fileSystemPath.toString().substring(1));
				}
				primaryRecordPath=Paths.get(cmd.getOptionValue("share"));
				if (primaryRecordPath.startsWith("~" + File.separator)) {
					primaryRecordPath = Paths.get(System.getProperty("user.home") + primaryRecordPath.toString().substring(1));
				}
				ip=cmd.getOptionValue("ip", "127.0.0.1");
				port=Integer.parseInt(cmd.getOptionValue("port","8080"));
				serverMode=MODE.valueOf(cmd.getOptionValue("primary","SECONDARY"));
			} catch (Exception e) {
				System.err.println("Invalid Input");
			}
		}
		System.out.println("ip:"+ip+"\nport:"+port+"\ndir:"+fileSystemPath+"\nshare:"+primaryRecordPath+"\nserverMode:"+serverMode);
		
		try {
			socket =new ServerSocket(port,MAX_CONNECTION,InetAddress.getByName(ip));
		} catch (Exception e) {
			System.err.println("Create Server Socket failed");
			System.exit(-1);
		}
		
		if (serverMode==MODE.PRIMARY) {
			try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(primaryRecordPath.toString(),true)))) {
				out.println("Primary Server:\n");
				out.println(ip.toString()+"\n");
				out.println(port);
			}catch (Exception e) {
				System.err.println("Fail to write to primary.txt");
				System.exit(-1);
			}
		}
		
		
		while(true){
			Socket DFSSocket=socket.accept();
			BufferedReader inFromClient =
		               new BufferedReader(new InputStreamReader(DFSSocket.getInputStream()));

			try {
				RequestMessage request=new RequestMessage(inFromClient);
				(new Thread(new DFS(request,fileSystemPath,DFSSocket))).start();
			} catch (Exception e) {
				(new Thread(new DFS(null,fileSystemPath,DFSSocket))).start();
			}
			
		}
		
	}
	
}
