

public class ServerConnection {
	private static String primaryIP="";
	private static Integer primaryPort=-1;
	private static String secondaryIP="";
	private static Integer secondaryPort=-1;
	
	public ServerConnection(String primaryIP, Integer primaryPort, String secondaryIP,Integer secondaryPort){
		setPrimaryIP(primaryIP);
		setSecondaryIP(secondaryIP);
		setPrimaryPort(primaryPort);
		setSecondaryPort(secondaryPort);
	}
	
	
	
	public static boolean updatePrimaryServerSocketAddress(String info){
		try {
			String[] lst=info.split(":");
			primaryIP=lst[0];
			primaryPort=Integer.valueOf(lst[1]);
			return true;
		} catch (Exception e) {
			// DONE: handle exception
			System.err.println("Fail to update primary server info");
			return false;
		}
	}

	public static boolean updateSecondaryServerSocketAddress(String info){
		try {
			String[] lst=info.split(":");
			secondaryIP=lst[0];
			secondaryPort=Integer.valueOf(lst[1]);
			return true;
		} catch (Exception e) {
			// DONE: handle exception
			System.err.println("Fail to update secondary server info");
			return false;
		}
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
