

import java.io.BufferedReader;
import java.io.IOException;


public class RequestMessage {
	public RequestHeader header;
	public String data;
	
	public RequestMessage(){
		header=new RequestHeader();
		data=null;
	}
	
	public RequestMessage(RequestHeader.MethodType method){
		header=new RequestHeader(method);
		data="";
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
	
	public RequestMessage(String header,String data){
		this.header=new RequestHeader(header);
		this.data=data;
	}
	
	public static RequestMessage logRequestMessage(String secondaryIP,Integer secondaryPort){
		RequestMessage SYNCRequest=new RequestMessage();
		SYNCRequest.header.method=RequestHeader.MethodType.LOG;
		SYNCRequest.data=secondaryIP+":"+secondaryPort;
		return SYNCRequest;
	}
	
	public static RequestMessage logRequestMessage(String body){
		RequestMessage SYNCRequest=new RequestMessage();
		SYNCRequest.header.method=RequestHeader.MethodType.LOG;
		SYNCRequest.data=body;
		return SYNCRequest;
	}
	
	public static RequestMessage PingRequestMessage() {
		return new RequestMessage(RequestHeader.MethodType.PING); 
	}
	
	public static RequestMessage PingRequestMessage(String body) {
		RequestMessage ping=PingRequestMessage();
		ping.data=body;
		return ping;
	}
	
	public String toString(){
		header.contentLength=data.length();
		return (header.toString()+data+"\r\n");
	}
}
