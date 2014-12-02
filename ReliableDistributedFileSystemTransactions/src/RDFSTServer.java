import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.cli.CommandLine;


public class RDFSTServer extends TimerTask{
	static enum MODE {PRIMARY,SECONDARY};
	static final int MAX_CONNECTION=10;
	public static Path fileSystemPath;
	public static String ip="127.0.0.1";
	public static int port=8080;
	public static MODE serverMode=MODE.SECONDARY;
	public static Path primaryRecordPath;
	private static ServerSocket generalSocket;
	private static Socket toServerSocket;
	private static Timer timer;

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
				port=Integer.valueOf(cmd.getOptionValue("port","8080"));
				serverMode=cmd.hasOption("p")?MODE.PRIMARY:MODE.SECONDARY;
			} catch (Exception e) {
				System.err.println("Invalid Input");
			}
		}
		System.out.println("ip:"+ip+"\nport:"+port+"\ndir:"+fileSystemPath+"\nshare:"+primaryRecordPath+"\nserverMode:"+serverMode);

		try {
			generalSocket =new ServerSocket(port,MAX_CONNECTION,InetAddress.getByName(ip));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Create Server Socket failed");
			System.exit(-1);
		}

		if (serverMode==MODE.PRIMARY) {
			//			DONE: Write/Update primary.txt
			//			DONE: Update Server Connection
			//			TODO: Wait 4 Secondary notification 
			try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(primaryRecordPath.toString(),false)))) {
				out.flush();
				out.println("Primary Server:");
				out.println(ip.toString()+":"+port);
				//				out.close();
			}catch (Exception e) {
				System.err.println("Fail to write to primary.txt");
				System.exit(-1);
			}
			//	add primary server info to ServerConnection
			ServerConnection.setPrimaryIP(ip);
			ServerConnection.setPrimaryPort(port);
		}else{
			//			DONE: Read primary.txt
			//			DONE: Test Reachability
			//			DONE: Send notification
			//			DONE: Update Server Connection			
			String primaryIP="";
			Integer primaryPort=-1;
			try(BufferedReader in=new BufferedReader(new FileReader(primaryRecordPath.toString()))){
				//				Read primary.txt
				in.readLine();
				String[] lst=in.readLine().split(":");
				primaryIP=lst[0];
				primaryPort=Integer.valueOf(lst[1]);
			}catch(Exception e){
				System.err.println("Fail to read to primary.txt");
				System.exit(-1);
			}
			//Test Reachability
			//Send SYNC notification
			Socket testSocket=new Socket(primaryIP,primaryPort);
			testSocket.setKeepAlive(true);
			if(!isSocketConnected(testSocket)){
				System.err.println("Fail to setup socket with Primary Server");
				System.exit(-1);
			}else{
				//Update Server Connection
				toServerSocket=testSocket;
				ServerConnection.setPrimaryIP(primaryIP);
				ServerConnection.setPrimaryPort(primaryPort);
				System.out.println("Setup socket to Primary Serve at "+primaryIP+":"+primaryPort);
				//start timer
				//				timer=new Timer(true);
				//				timer.schedule(new RDFSTServer(), 0, 1000);
			}
		}

		while(true){
			try (Socket inSocket=generalSocket.accept();
					BufferedReader in = new BufferedReader(new InputStreamReader(inSocket.getInputStream()));
					PrintWriter out = new PrintWriter(inSocket.getOutputStream(), true);
					){
				String header, data;
				while((header=in.readLine())!=null){
					in.readLine();
					data=in.readLine();
					if (serverMode==MODE.PRIMARY) {
						try {
							RequestMessage request=new RequestMessage(header,data);
							if (request.header.method==RequestHeader.MethodType.SYNC) {
								// save socket for future use
								toServerSocket=inSocket;
								System.out.println("SYNC request from "+request.data);
								// update serverConnection info
								if (ServerConnection.updateSecondaryServerSocketAddress(request.data)) {
									System.out.println("Secondary Server is recorded.");
								}
							}else{
								(new Thread(new DFS(request,fileSystemPath,inSocket))).start();
							}
						} catch (Exception e) {
							//TODO: RESEND response
							(new Thread(new DFS(null,fileSystemPath,inSocket))).start();
						}
					}
					else{
						try {
							RequestMessage request=new RequestMessage(header,data);
							if (request.header.method==RequestHeader.MethodType.SYNC) {
								// save socket for future use
								toServerSocket=inSocket;
								System.out.println("SYNC request from "+request.data);
								// update serverConnection info

							}else{
								(new Thread(new DFS(request,fileSystemPath,inSocket))).start();
							}
						} catch (Exception e) {
							//TODO: RESEND response
							(new Thread(new DFS(null,fileSystemPath,inSocket))).start();
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				//				e.printStackTrace();
				System.err.println("no acceptable socket");
			}
		}
	}

	private static boolean isSocketConnected(Socket socket){
		try {
			DataOutputStream testOut=new DataOutputStream(socket.getOutputStream());
			testOut.writeBytes(RequestMessage.SYNCRequestMessage(ip, port).toString());
			return true;
		} catch (Exception e) {
			// DONE: handle exception
			e.printStackTrace();
			System.err.println("The socket is closed.");
			return false;
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Checking reachability: "+new Date());
		if (!isSocketConnected(toServerSocket)) {
			System.err.println("Primary Server is crashed.");
			timer.cancel();
		}
	}
}
