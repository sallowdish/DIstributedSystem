

import java.io.BufferedReader;
import java.io.IOException;

public class RequestMessage {
	public RequestHeader header;
	public String data;
	
	public RequestMessage(){
		header=new RequestHeader();
		data=null;
	}
	
	public RequestMessage(BufferedReader buf) throws Exception{
		try {
			String headerString = buf.readLine();
			header=new RequestHeader(headerString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Cant parse request message header");
			throw e;
		}
		try {
			String dataString=buf.readLine();
			dataString=buf.readLine();
			data=dataString;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Cant parse request message body");
			throw e;
		}
	}
	
	public static RequestMessage SYNCRequestMessage(String secondaryIP,Integer secondaryPort){
		RequestMessage SYNCRequest=new RequestMessage();
		SYNCRequest.header.method=RequestHeader.MethodType.SYNC;
		SYNCRequest.data=secondaryIP+":"+secondaryPort;
		return SYNCRequest;
	}
	
	public String toString(){
		header.contentLength=data.length();
		return (header.toString()+data);
	}
}
