

public class ResponseHeader {
	public static enum MethodType{ACK, ASK_RESEND, ERROR};
	public static final int Invalid_Transaction_ID=201;
	public static final int	Invalid_Operation=202;
	public static final int Wrong_Message_Format=204;
	public static final int File_IO_Error=205;
	public static final int File_Not_Found=206;
	public MethodType method;
	public int transactionID;
	public int sequenceNum;
	public int errorCode;
	public int contentLength;
	
	public ResponseHeader(RequestHeader requestHeader){

		transactionID=requestHeader!=null?requestHeader.transactionID:-1;
		sequenceNum=requestHeader!=null?requestHeader.sequenceNum:-1;
		
	}
	
	public String toString(){
		try {
			return method.name()+" "+transactionID+" "+sequenceNum+" "+errorCode+" "+contentLength+"\r\n\r\n";
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println(e.getLocalizedMessage());
			throw e;
		}
		
	}
}
