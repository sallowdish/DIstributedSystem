import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;


public class RDFSTServer {
	static enum MODE {PRIMARY,SECONDARY};
	static final int MAX_CONNECTION=10;
	public static Path path;
	public static String ip="127.0.0.1";
	public static int port=8080;
	public static MODE serverMode=MODE.SECONDARY;
	private static ServerSocket socket;
		
	public static void main(String[] args) throws Exception, IOException{
//		ServerSocket socket;
		CLI scanner=new CLI(args);
		
		if(scanner.parse()){
			try {
				CommandLine cmd=scanner.cmd;
				path=Paths.get(cmd.getOptionValue("dir"));
				if (path.startsWith("~" + File.separator)) {
				    path = Paths.get(System.getProperty("user.home") + path.toString().substring(1));
				}
				ip=cmd.getOptionValue("ip", "127.0.0.1");
				port=Integer.parseInt(cmd.getOptionValue("port","8080"));
				serverMode=MODE.valueOf(cmd.getOptionValue("primary","SECONDARY"));
			} catch (Exception e) {
				System.err.println("Invalid Input");
			}
		}
		System.out.println("ip:"+ip+"\nport:"+port+"\ndir:"+path);
		
		socket =new ServerSocket(port,MAX_CONNECTION,InetAddress.getByName(ip));
		
		while(true){
			Socket DFSSocket=socket.accept();
			BufferedReader inFromClient =
		               new BufferedReader(new InputStreamReader(DFSSocket.getInputStream()));
			
			try {
				RequestMessage request=new RequestMessage(inFromClient);
				(new Thread(new DFS(request,path,DFSSocket))).start();
			} catch (Exception e) {
				(new Thread(new DFS(null,path,DFSSocket))).start();
			}
			
		}
		
	}
	
}
