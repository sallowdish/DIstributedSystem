import java.io.BufferedReader;
import java.io.IOException;



public class ResponseMessage {
	public ResponseHeader header;
	public String body="\r\n";
	
	public ResponseMessage(){
		
	}
	
	public ResponseMessage(RequestMessage request){
		header=new ResponseHeader(request!=null?request.header:null);
	}
	
	public ResponseMessage(int errorCode,RequestMessage request,String detail){
		this(errorCode,request);
		body+=": "+detail;
	}
	private ResponseMessage(int errorCode,RequestMessage request){
		this(request);
		header.method=ResponseHeader.MethodType.valueOf("ERROR");
		header.errorCode=errorCode;
		switch (errorCode) {
		case ResponseHeader.File_Not_Found:
			body="can not find the file named:"+request.data;
			break;
		case ResponseHeader.Wrong_Message_Format:
			body="something wrong with the request message";
			break;
		case ResponseHeader.Invalid_Transaction_ID:
			body="Invalid Trancastion ID";
			break;
		case ResponseHeader.Invalid_Operation:
			body="Invalid Operation";
			break;
		default:
			break;
		}
	}
	
	public ResponseMessage(String[] content,RequestMessage request) {
		this(request);
		header.method=ResponseHeader.MethodType.valueOf("ACK");
		body="";
		for (String string : content) {
			body+=string;
		}
	}
	
	public String toString(){
		header.contentLength=body.length();
		return (header.toString()+body+"\r\n");
	}
	
	public static ResponseMessage renderReponseMessage(BufferedReader buf){
		ResponseMessage response=new ResponseMessage();
		try {
			String headerString = buf.readLine();
			response.header=new ResponseHeader(headerString);
			buf.readLine();
			buf.readLine();
			String dataString=buf.readLine();
			response.body=dataString;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Cant parse response message ");
			return null;
		}
		return response;
	}
		
}
