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
	static final int REACHABILITY_CHECK_PEROID=5000;
	public Path fileSystemPath;
	public String ip="127.0.0.1";
	public int port=8080;
	public MODE serverMode=MODE.SECONDARY;
	public Path primaryRecordPath;
	private ServerSocket generalSocket;
	private Timer timer;

	public RDFSTServer(){

	}
	public RDFSTServer(String[] args){
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
			//			DONE: Wait 4 Secondary notification 
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
//			timer=new Timer();
//			timer.schedule(this, 0, REACHABILITY_CHECK_PEROID);
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
			try {
				Socket testSocket=new Socket(primaryIP,primaryPort);
				testSocket.setKeepAlive(true);
				if(!isSocketConnected(testSocket)){
					throw new Exception();
				}
				else{
					System.out.println("Setup socket to Primary Serve at "+primaryIP+":"+primaryPort);
					//start timer
					timer=new Timer(true);
					timer.schedule(this, 0, REACHABILITY_CHECK_PEROID);
				}
			} catch (Exception e) {
				// DONE: handle exception
				System.err.println("Fail to setup socket to Primary Server");
				System.exit(-1);
			}
		}

	}
	public static void main(String[] args) throws Exception{
		//		ServerSocket socket;
		RDFSTServer server=new RDFSTServer(args);

		while(true){
			try (Socket inSocket=server.generalSocket.accept();
					BufferedReader in = new BufferedReader(new InputStreamReader(inSocket.getInputStream()));
					PrintWriter out = new PrintWriter(inSocket.getOutputStream(), true);
					){
				String header, data;
				while((header=in.readLine())!=null){
					in.readLine();
					data=in.readLine();
					if (server.serverMode==MODE.PRIMARY) {
						try {
							RequestMessage request=new RequestMessage(header,data);
							if (request.header.method==RequestHeader.MethodType.SYNC) {
								// update serverConnection info
								if (ServerConnection.updateSecondaryServerSocketAddress(request.data)) {
//									System.out.println("Secondary Server is recorded.");
								}
							}
							else{
								(new Thread(new DFS(request,server.fileSystemPath,inSocket))).start();
							}
						} catch (Exception e) {
							//TODO: RESEND response
							(new Thread(new DFS(null,server.fileSystemPath,inSocket))).start();
						}
					}
					else{
						try {
							RequestMessage request=new RequestMessage(header,data);
							if (request.header.method==RequestHeader.MethodType.SYNC) {
								System.out.println("SYNC request from "+request.data);
								// update serverConnection info

							}else{
								(new Thread(new DFS(request,server.fileSystemPath,inSocket))).start();
							}
						} catch (Exception e) {
							//TODO: RESEND response
							(new Thread(new DFS(null,server.fileSystemPath,inSocket))).start();
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

	private boolean isSocketConnected(Socket socket){
		try {
			DataOutputStream testOut=new DataOutputStream(socket.getOutputStream());
			testOut.writeBytes(RequestMessage.SYNCRequestMessage(ip, port).toString());
			//Update Server Connection
			ServerConnection.setPrimaryIP(socket.getInetAddress().toString().substring(1));
			ServerConnection.setPrimaryPort(socket.getPort());
			socket.close();
			return true;
		} catch (Exception e) {
			// DONE: handle exception
//			e.printStackTrace();
			System.err.println("The socket between servers is closed.");
			return false;
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(serverMode==MODE.PRIMARY){

		}else{
			try {
				Socket toPrimaryServer=new Socket(ServerConnection.getPrimaryIP(),ServerConnection.getPrimaryPort());
				if (!isSocketConnected(toPrimaryServer)) {
					System.err.println(new Date()+": Primary Server is crashed.");
					timer.cancel();
				}else{
					System.out.println(new Date()+": Socket to Primary Server is up.");
				}
//				toPrimaryServer.close();
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println(e.getLocalizedMessage());
			}
			
		}
	}
}
