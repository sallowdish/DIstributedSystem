

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DFS implements Runnable{
	public RequestMessage request;
	public ResponseMessage response;
	public static Path root=Paths.get(System.getProperty("user.dir"));
	public Socket currentOpenSocket;
	public static Set<Integer> workingTrancationID=new HashSet<Integer>(0);
	public static Set<Integer> commitedTrancationID=new HashSet<Integer>(0);
	public static Map<Integer, Log> workingLog=new ConcurrentHashMap<Integer,Log>(0);
	public RDFSTServer server;



	public DFS(Path root) {
		DFS.root=root;
		RecoverFromDB();
	}



	public DFS(RequestMessage inRequest,Path root, Socket socket){
		request=inRequest;
		DFS.root=root;
		currentOpenSocket=socket;
		//read current commited TNX_id
		RecoverFromDB();
	}

	public void run(){
		if (request!=null) {
			switch (request.header.method) {
			case READ:
				try {
					handleReadRequest();
				} catch (Exception e) {
					//  Auto-generated catch block
					e.printStackTrace();
					System.err.println("Fail to deal with READ request:"+e.getLocalizedMessage());
					//					System.exit(-1);
				}
				break;
			case NEW_TXN:
				try {
					handleNewTranscationRequest();

				} catch (Exception e) {
					// : handle exception
					System.err.println("Fail to deal with NEW_TNX request: "+e.getLocalizedMessage());
					//					System.exit(-1);
				}
				break;
			case WRITE:
				try {
					handleWriteRequest();
				} catch (Exception e) {
					// : handle exception
					System.err.println("Fail to deal with WRITE request: "+e.getLocalizedMessage());
				}
				break;
			case COMMIT:
				try {
					handleCommitRequest();
				} catch (Exception e) {
					// : handle exception
					System.err.println("Fail to deal with Commit request: "+e.getLocalizedMessage());
				}
				break;
			case ABORT:
				try {
					handleAbortRequest();
				} catch (Exception e) {
					// : handle exception
					System.err.println("Fail to deal with Abort request: "+e.getLocalizedMessage());
				}
				break;
			case LOG:
				try{
					handleLogRequest();
				}
				catch (Exception e) {
					// : handle exception
					System.err.println("Fail to deal with Log request: "+e.getLocalizedMessage());
				}
			default:
				break;
			}
		}
		else {
			// : handle exception
			System.err.println("Fail to parse request");
			try {
				handleMessageFormatWrong();
			} catch (IOException e) {
				System.err.println("Fail to deal with wrong formatted request, "+e.getLocalizedMessage());
			}
		}

	}

	private void handleAbortRequest() throws IOException{
		if (workingTrancationID.contains(request.header.transactionID)) {
			Log targetLog=workingLog.get(request.header.transactionID);
			workingTrancationID.remove(Integer.valueOf(request.header.transactionID));
			workingLog.remove(targetLog.transcationID);

			response=new ResponseMessage(request);
			response.header.method=ResponseHeader.MethodType.valueOf("ACK");
			response.body="Abort Transaction# :"+targetLog.transcationID;
		}
		else{
			response=new ResponseMessage(201, request," ");
		}
		sendResponseMessage();
	}

	private void handleCommitRequest() throws IOException {
		if (workingTrancationID.contains(request.header.transactionID)) {
			Log targetLog=workingLog.get(request.header.transactionID);
			
			if (//commit unreceiced seq
					targetLog.sequenceNum<request.header.sequenceNum 
					//commit commited seq
					|| targetLog.base>request.header.sequenceNum) {
				response=new ResponseMessage(202,request,"Invalid Sequence #. Current base is at:"+targetLog.base+" and head is at:"+targetLog.sequenceNum+"\n");
			}
			else if(targetLog.base==request.header.sequenceNum){
				response=new ResponseMessage(request);
				response.header.method=ResponseHeader.MethodType.valueOf("ACK");
				response.body="Commited: Transaction#"+request.header.transactionID+" Sequence#"+request.header.sequenceNum+" was commited. Please check: "+targetLog.filepath+"\n";
			}
			else{
				String syncLogString=targetLog.toString();
				//Write data to dish
				targetLog.writeToDisk(request.header.sequenceNum);

				//record commited TNX
				commitedTrancationID.add(request.header.transactionID);
				File COMMITED_TNX_DB=new File(root.toFile(),".TNX_DB");
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(COMMITED_TNX_DB.getPath(),false)));
				for (Integer tID : commitedTrancationID) {
					Log co_log=workingLog.get(tID);
					out.println(tID+" "+co_log.filepath+" "+co_log.base);
				}
				out.close();

				//TODO: send log to back-up
				syncLogString=syncLogString+Log.delimiter+request.header.sequenceNum;
				RequestMessage logRequest=RequestMessage.logRequestMessage(syncLogString);
				try {
					Socket toSecondaryServerSocket=new Socket(server.serverConnection.getSecondaryIP(),server.serverConnection.getSecondaryPort());
					DataOutputStream outSecondaryServer=new DataOutputStream(toSecondaryServerSocket.getOutputStream());
					outSecondaryServer.writeBytes(logRequest.toString());
					toSecondaryServerSocket.close();
					response=new ResponseMessage(request);
					response.header.method=ResponseHeader.MethodType.valueOf("ACK");
					response.body="Commited:Write to local file :"+targetLog.filepath+"\n";
				} catch (Exception e) {
					// TODO: handle exception
					System.err.println("Secondary Server has crashed."+e.getLocalizedMessage());
					server.serverConnection.setSecondaryIP("");
					server.serverConnection.setSecondaryPort(-1);
					response=new ResponseMessage(request);
					response.header.method=ResponseHeader.MethodType.valueOf("ACK");
					response.body="Commited:Write to local file :"+targetLog.filepath+". But failed to sync Secondary Server.\n";
				}
			}
			
		}else{
			response=new ResponseMessage(201, request," ");
		}
		sendResponseMessage();
	}

	private void handleWriteRequest() throws IOException{
		if (workingTrancationID.contains(request.header.transactionID)) {
			Log targetLog=workingLog.get(request.header.transactionID);
			if (targetLog.sequenceNum!=request.header.sequenceNum-1) {
				response=new ResponseMessage(202,request,"Invalid Sequence #, current log at seq#"+targetLog.sequenceNum);
			}else{
				targetLog.writeToLog(request.data);

				response=new ResponseMessage(request);
				response.header.method=ResponseHeader.MethodType.valueOf("ACK");
				response.body=request.data;
			}
		}else{
			response=new ResponseMessage(201, request,"");
		}
		sendResponseMessage();
	}

	private void handleMessageFormatWrong() throws IOException{
		response=new ResponseMessage(ResponseHeader.Wrong_Message_Format,request,"can not parse request");
		sendResponseMessage();
	}

	private void handleReadRequest() throws IOException {
		String[] content=getFileContent();
		if (content!=null) {
			response=new ResponseMessage(content,request);
		}
		sendResponseMessage();
	}

	private String[] getFileContent(){
		String filename=request.data;
		File f = new File(root.toFile(),filename);
		if (f.exists() && !f.isDirectory()) {
			String[] fileContent;
			try {
				fileContent = Files.readAllLines(f.toPath()).toArray(new String[0]);
				return fileContent;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				response=new ResponseMessage(ResponseHeader.File_IO_Error,request,e.getLocalizedMessage());
			}
			return null;
		}else{
			response=new ResponseMessage(ResponseHeader.File_Not_Found,request,"cannot find file on disk");
			return null;
		}
	}

	private void sendResponseMessage() throws IOException  {
		DataOutputStream outBuffer=new DataOutputStream(currentOpenSocket.getOutputStream());
		String outString=response.toString()+"\r\n";
		outBuffer.writeBytes(outString);
		//		currentOpenSocket.close();
	}

	private void handleNewTranscationRequest() throws Exception{
		Random r=new Random();
		int tID=r.nextInt(1000);
		while(workingTrancationID.contains(tID)){
			tID=r.nextInt(1000);
		}

		response=new ResponseMessage(request);
		response.header.method=ResponseHeader.MethodType.valueOf("ACK");
		response.header.transactionID=tID;
		workingTrancationID.add(tID);
		Log newLog=new Log(tID, (new File(root.toFile(),request.data)).toString());
		workingLog.putIfAbsent(tID, newLog);
		response.body="New Transcation:"+tID+"@"+newLog.filepath+" has initialized.";
		sendResponseMessage();
	}
	private void handleLogRequest(){
		//TODO: recover log
		//TODO: add tnx
		//TODO: commit tnx
		//TODO: write to disk
		Log syncLog=new Log(request.data);
		syncLog.filepath=root+"/"+(new File(syncLog.filepath).getName());
		Integer commitSeq=Integer.valueOf(request.data.split(Log.delimiter)[2]);
		workingLog.put(syncLog.transcationID, syncLog);
		workingTrancationID.add(syncLog.transcationID);
		syncLog.writeToDisk(commitSeq);
		commitedTrancationID.add(syncLog.transcationID);
	}

	private boolean RecoverFromDB(){
		File COMMITED_TNX_DB=new File(root.toFile(),".TNX_DB");
		if (COMMITED_TNX_DB.exists()) {
			try(BufferedReader in=new BufferedReader(new FileReader(COMMITED_TNX_DB.getPath()))){
				////			in.readLine();
				String nextLine=null;
				while ((nextLine=in.readLine())!=null) {
					String[] lst=nextLine.split(" ");
					String tID=lst[0];
					String filepath=lst[1];
					commitedTrancationID.add(Integer.valueOf(tID));
					workingTrancationID.add(Integer.valueOf(tID));
					//create corresponding log
					Log targetLog=workingLog.get(Integer.valueOf(tID));
					if (targetLog==null) {
						Log log=new Log(Integer.valueOf(tID),filepath);
						log.base=Integer.valueOf(lst[2]);
						log.sequenceNum=log.base;
						workingLog.put(log.transcationID, log);
					}
				}
				return true;
			}catch(Exception e){
				e.printStackTrace();
				System.err.println("Fail to read to TNX_DB");
				System.exit(-1);
			}
		}
		return false;
	}
}