import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.cli.CommandLine;


public class RDFSTServer extends TimerTask{
	static enum MODE {PRIMARY,SECONDARY};
	static final int MAX_CONNECTION=10;
	static final int REACHABILITY_CHECK_PEROID=3000;
	public Path fileSystemPath;
	public String ip="127.0.0.1";
	public int port=8080;
	public MODE serverMode=MODE.SECONDARY;
	public Path primaryRecordPath;
	private ServerSocket generalSocket;
	private Timer timer;
	private DFS fileSystem;
	private int failCounter=0;
	public ServerConnection serverConnection;

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
				ip=cmd.getOptionValue("ip", "127.0.0.1");
				port=Integer.valueOf(cmd.getOptionValue("port","8080"));
				serverMode=cmd.hasOption("p")?MODE.PRIMARY:MODE.SECONDARY;
			} catch (Exception e) {
				System.err.println("Invalid Input");
			}
		}
		
		serverConnection=new ServerConnection("", -1, "", -1);
		System.out.println("ip:"+ip+"\nport:"+port+"\ndir:"+fileSystemPath+"\nshare:"+primaryRecordPath+"\nserverMode:"+serverMode);
		try {
			generalSocket =new ServerSocket(port,MAX_CONNECTION,InetAddress.getByName(ip));
//			generalSocket.setSoTimeout(10*1000);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Create Server Socket failed");
			System.exit(-1);
		}



		if (serverMode==MODE.PRIMARY) {
			//			DONE: Write/Update primary.txt
			//			DONE: Update Server Connection
			//			DONE: Wait 4 Secondary notification
			String primaryIP="";
			Integer primaryPort=-1;
			try(BufferedReader in=new BufferedReader(new FileReader(primaryRecordPath.toString()))){
				//				Read primary.txt
				in.readLine();
				String[] lst=in.readLine().split(":");
				primaryIP=lst[0];
				primaryPort=Integer.valueOf(lst[1]);
				if (primaryIP.equals(ip) && primaryPort.equals(port)) {
					throw new Exception();
				}
				Socket preSocket=new Socket(primaryIP,primaryPort);
				DataOutputStream outPreSocket=new DataOutputStream(preSocket.getOutputStream());
				outPreSocket.writeBytes((RequestMessage.PingRequestMessage()).toString());
				preSocket.close();
				System.err.println("Primary Server already exsist.");
				System.exit(-1);
			} catch (Exception e) {
				try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(primaryRecordPath.toString(),false)))) {
					out.flush();
					out.println("Primary Server:");
					out.println(ip.toString()+":"+port);
					out.close();
				}catch (Exception e1) {
					System.err.println("Fail to write to primary.txt");
					System.exit(-1);
				}
				//	add primary server info to ServerConnection
				serverConnection.setPrimaryIP(ip);
				serverConnection.setPrimaryPort(port);

			}
		}
		else{
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
//					testSocket.close();
					throw new Exception();
				}
				else{
					testSocket.close();
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

		//set up DFS
		fileSystem=new DFS(fileSystemPath);
		fileSystem.server=this;
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
							if (header!=null && data!=null) {
								RequestMessage request=new RequestMessage(header,data);
								if (request.header.method==RequestHeader.MethodType.PING) {
									if (server.serverConnection.updateSecondaryServerSocketAddress(request.data)) {
										System.out.println("Secondary Server is recorded.");
									}
									inSocket.close();
								}
								else{
									server.fileSystem.request=request;
									server.fileSystem.currentOpenSocket=inSocket;
									(new Thread(server.fileSystem)).start();
								}
							}
						} catch (Exception e) {
							//TODO: RESEND response
							server.fileSystem.request=null;
							server.fileSystem.currentOpenSocket=inSocket;
							(new Thread(server.fileSystem)).start();
						}
					}
					else{
						//Secondary Server behaviors on request
						try {
							if (header!=null && data!=null) {
								RequestMessage request=new RequestMessage(header,data);
								if (request.header.method==RequestHeader.MethodType.LOG) {
									System.out.println("LOG request from "+request.data);
									// TODO:update serverConnection info
									server.fileSystem.request=request;
									server.fileSystem.currentOpenSocket=inSocket;
									(new Thread(server.fileSystem)).start();
								}else{
									//TODO: forbid other request
									System.err.println("Unexpected request");
									//reply error response
									try {
										DataOutputStream outInSocket=new DataOutputStream(inSocket.getOutputStream());
										outInSocket.writeBytes((new ResponseMessage(ResponseHeader.Invalid_Operation,request,"connection to secondary server is forbiden.")).toString());
									} catch (Exception e) {
										// DONE: handle exception
										System.err.println("Failed to send Error response to Client. "+e.getLocalizedMessage());
									}
								}
							}
						} catch (Exception e) {
							//TODO: RESEND response
							server.fileSystem.request=null;
							server.fileSystem.currentOpenSocket=inSocket;
							(new Thread(server.fileSystem)).start();
							//							(new Thread(new DFS(null,server.fileSystemPath,inSocket))).start();
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				//				e.printStackTrace();
//				System.err.println("no acceptable socket. "+e.getLocalizedMessage());
			}
		}
	}

	private boolean isSocketConnected(Socket socket){
		try {
			DataOutputStream testOut=new DataOutputStream(socket.getOutputStream());
			testOut.writeBytes(RequestMessage.PingRequestMessage(ip+":"+port).toString());
			//Update Server Connection
			serverConnection.setPrimaryIP(socket.getInetAddress().toString().substring(1));
			serverConnection.setPrimaryPort(socket.getPort());
			socket.close();
			return true;
		} catch (Exception e) {
			// DONE: handle exception
			//			e.printStackTrace();
			System.err.println(e.getLocalizedMessage()+"The socket between servers is closed.");
			return false;
		}

	}
	
	private boolean updatePrimaryFile() {
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(primaryRecordPath.toString(),false)))) {
			out.flush();
			out.println("Primary Server:");
			out.println(ip.toString()+":"+port);
			out.close();
			serverConnection.setPrimaryIP(ip);
			serverConnection.setPrimaryPort(port);
			serverConnection.setSecondaryIP("");
			serverConnection.setPrimaryPort(-1);
			return true;
		}catch (Exception e) {
			System.err.println("Fail to modify to primary.txt");
			System.exit(-1);
			return false;
		}
		//	add primary server info to ServerConnection	
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(serverMode==MODE.PRIMARY){

		}else{
			try(Socket toPrimaryServer=new Socket(serverConnection.getPrimaryIP(),serverConnection.getPrimaryPort());) {

				if (!isSocketConnected(toPrimaryServer)) {
					throw new Exception();
				}else{
					System.out.println(new Date()+": Socket to Primary Server is up.");
				}
//				toPrimaryServer.close();
				failCounter=0;
			} catch (Exception e) {
				// TODO: handle exception
				failCounter++;
				if (failCounter>2) {
					System.err.println(new Date()+": Primary Server has crashed.");
					timer.cancel();
					serverMode=MODE.PRIMARY;
					updatePrimaryFile();
				}
			}
		}
	}
}
