

public class ServerConnection {
	private String primaryIP="";
	private Integer primaryPort=-1;
	private String secondaryIP="";
	private Integer secondaryPort=-1;
	
	public ServerConnection(String primaryIP, Integer primaryPort, String secondaryIP,Integer secondaryPort){
		setPrimaryIP(primaryIP);
		setSecondaryIP(secondaryIP);
		setPrimaryPort(primaryPort);
		setSecondaryPort(secondaryPort);
	}
	
	
	
	public boolean updatePrimaryServerSocketAddress(String info){
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

	public boolean updateSecondaryServerSocketAddress(String info){
		try {
			String[] lst=info.split(":");
			if (secondaryIP.equals(lst[0]) && secondaryPort.equals(Integer.valueOf(lst[1]))) {
				return false;
			}
			secondaryIP=lst[0];
			secondaryPort=Integer.valueOf(lst[1]);
			return true;
		} catch (Exception e) {
			// DONE: handle exception
			System.err.println("Fail to update secondary server info");
			return false;
		}
	}

	
	public String getPrimaryIP() {
		return primaryIP;
	}

	public void setPrimaryIP(String primaryIP) {
		this.primaryIP = primaryIP;
	}

	public String getSecondaryIP() {
		return secondaryIP;
	}

	public void setSecondaryIP(String secondaryIP) {
		this.secondaryIP = secondaryIP;
	}

	public Integer getPrimaryPort() {
		return primaryPort;
	}

	public void setPrimaryPort(Integer primaryPort) {
		this.primaryPort = primaryPort;
	}

	public Integer getSecondaryPort() {
		return secondaryPort;
	}

	public void setSecondaryPort(Integer secondaryPort) {
		this.secondaryPort = secondaryPort;
	}
}
