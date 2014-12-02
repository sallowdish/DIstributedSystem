

public class RequestHeader {
	//enum
	public static enum MethodType{READ, NEW_TXN, WRITE,COMMIT,ABORT,SYNC};

	
	//fields
	public MethodType method=null;
	public int transactionID=-1;
	public int sequenceNum=-1;
	public int contentLength=-1;
	
	public RequestHeader(){
		transactionID=-1;
		sequenceNum=-1;
		contentLength=-1;
	}
	
	public RequestHeader(String str){
		this();
		try {
			String[] info=str.split(" ");
			method=MethodType.valueOf(info[0]);
			switch (method) {
			case READ:
				contentLength=Integer.parseInt(info[3]);
				break;
			case NEW_TXN:
				sequenceNum=0;
				break;
			case WRITE:
				transactionID=Integer.parseInt(info[1]);
				sequenceNum=Integer.parseInt(info[2]);
				break;
			case COMMIT:
				transactionID=Integer.parseInt(info[1]);
				sequenceNum=Integer.parseInt(info[2]);
				break;
			case ABORT:
				transactionID=Integer.parseInt(info[1]);
				sequenceNum=Integer.parseInt(info[2]);
				break;
				
			default:
				break;
			}
		}
		catch(Exception e){
			throw e;
		}
	}
	public String toString(){
		try {
			return method.name()+" "+transactionID+" "+sequenceNum+" "+contentLength+"\r\n\r\n";
		} catch (Exception e) {
			// DONE: handle exception
			System.err.println(e.getLocalizedMessage());
			throw e;
		}
	}
}
