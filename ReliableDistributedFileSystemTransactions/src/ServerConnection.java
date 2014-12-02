import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;


public class ServerConnection {
	private static String primaryIP="";
	private static Integer primaryPort=-1;
	private static String secondaryIP="";
	private static Integer secondaryPort=-1;
	public static SocketAddress primaryServer=null;
	public static SocketAddress secondaryServer=null;
	
	public ServerConnection(String primaryIP, Integer primaryPort, String secondaryIP,Integer secondaryPort){
		setPrimaryIP(primaryIP);
		setSecondaryIP(secondaryIP);
		setPrimaryPort(primaryPort);
		setSecondaryPort(secondaryPort);
	}
	
	public SocketAddress updatePrimaryServerSocketAddress(){
		try {
			primaryServer=new InetSocketAddress(InetAddress.getByName(primaryIP),primaryPort);
		} catch (Exception e) {
			// DONE: handle exception
			System.err.println("Fail to update primary server socketaddress");
			primaryServer=null;
		}
		return primaryServer;
	}

	public SocketAddress updateSecondServerSocketAddress(){
		try {
			secondaryServer=new InetSocketAddress(InetAddress.getByName(secondaryIP),secondaryPort);
		} catch (Exception e) {
			// DONE: handle exception
			System.err.println("Fail to update secondary server socketaddress");
			secondaryServer=null;
		}
		return secondaryServer;
	}
	
	public void updateServerSocketAddress(){
		updatePrimaryServerSocketAddress();
		updateSecondServerSocketAddress();
	}
	
	public static String getPrimaryIP() {
		return primaryIP;
	}

	public static void setPrimaryIP(String primaryIP) {
		ServerConnection.primaryIP = primaryIP;
	}

	public static String getSecondaryIP() {
		return secondaryIP;
	}

	public static void setSecondaryIP(String secondaryIP) {
		ServerConnection.secondaryIP = secondaryIP;
	}

	public static Integer getPrimaryPort() {
		return primaryPort;
	}

	public static void setPrimaryPort(Integer primaryPort) {
		ServerConnection.primaryPort = primaryPort;
	}

	public static Integer getSecondaryPort() {
		return secondaryPort;
	}

	public static void setSecondaryPort(Integer secondaryPort) {
		ServerConnection.secondaryPort = secondaryPort;
	}
}
