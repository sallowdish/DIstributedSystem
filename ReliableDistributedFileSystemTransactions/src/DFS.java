

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DFS implements Runnable{
	public RequestMessage request;
	public ResponseMessage response;
	public static Path root=Paths.get(System.getProperty("user.dir"));
	private Socket currentOpenSocket;
	public static List<Integer> workingTrancationID=new ArrayList<Integer>(0);
	public static Set<Integer> commitedTrancationID=new HashSet<Integer>(0);
	public static Map<Integer, Log> workingLog=new ConcurrentHashMap<Integer,Log>(0);
	
	
	public DFS(RequestMessage inRequest,Path root, Socket socket){
			request=inRequest;
			DFS.root=root;
			currentOpenSocket=socket;
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
				// TODO Auto-generated catch block
//				e.printStackTrace();
				
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
			if (targetLog.sequenceNum<request.header.sequenceNum || targetLog.base>request.header.sequenceNum) {
				response=new ResponseMessage(202,request,"Invalid Sequence #. Current base is at:"+targetLog.base+" and head is at:"+targetLog.sequenceNum);
			}else{
				//Write data to dish
				targetLog.writeToDisk(request.header.sequenceNum);
				
				//record commited TNX
				commitedTrancationID.add(request.header.transactionID);
				File COMMITED_TNX_DB=new File(root.toFile(),".TNX_DB");
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(COMMITED_TNX_DB.getPath(),false)));
				out.println(commitedTrancationID.toString());
				out.close();
				
				response=new ResponseMessage(request);
				response.header.method=ResponseHeader.MethodType.valueOf("ACK");
				response.body="Write to local file :"+targetLog.filepath;
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
				response=new ResponseMessage(202,request,"Invalid Sequence #");
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

}
