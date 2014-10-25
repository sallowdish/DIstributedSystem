import java.util.Random;


public class Wizard implements Runnable {
	String name;
	boolean isInAShop=false;
	public enum WizardType{
		Auror,DeathEater
	};
	WizardType type; 
	String[] shoppingList;
	long startTime;
	int numShopVisited=0;
	private void setType(String type){
		if (type!=null) {
			this.type=WizardType.valueOf(type);
		}
	}
	private String leadingInfo(){
		long ThreadID=Thread.currentThread().getId();
		return "tid: "+(ThreadID)+" second "+(System.nanoTime()-this.startTime)/1000000000+": "+this.name+"("+this.type+") ";
	}
	private void shopAtShop(Shop shop) throws InterruptedException{
		this.enterShop(shop);
		Thread.sleep(1000);	
	}
//	enter the shop
	private void enterShop(Shop shop){
		shop.incCount(this);
		this.isInAShop=true;
		System.out.println( this.leadingInfo()+"enters the "+shop.shopName+" shop.");
	}
//	leave the shop
	private void leaveShop(Shop shop){
		shop.decCount(this);
		this.isInAShop=false;
		System.out.println(this.leadingInfo()+"leaves the "+shop.shopName+" shop.");
	}
	
	private boolean reserveNextShop(Shop shop){
		boolean result=shop.makeReservation(this);
		if (result) {
			System.out.println(this.leadingInfo()+"makes a reservation at the "+shop.shopName+" shop");
		}else{
			System.out.println(this.leadingInfo()+"fails to make a reservation at the "+shop.shopName+" shop");
		}
		return result;
	}
	private void shop() throws InterruptedException{
		for (int i = 0; i < this.shoppingList.length; i++) {
			
			int failCount=0;
			String shopname = this.shoppingList[i];
			Shop nextShop=Street.allShops.get(shopname);
			
			while(!this.reserveNextShop(nextShop)){
				failCount++;
				if (failCount>new Random().nextInt(3)&&this.isInAShop) {
					System.out.println(this.leadingInfo()+"is bored talking to the salesperson, so she leaves the "+this.shoppingList[i-1]+" shop without a a reservation for the next shop to go for a walk.");
					this.leaveShop(Street.allShops.get(this.shoppingList[i-1]));
				}else{
					if (failCount==1) {
						System.out.println(this.leadingInfo()+"starts talking to the salesperson at the "+this.shoppingList[i-1]+" shop");
					}
				}
				Thread.sleep(1000);
			}
			
			if (this.isInAShop) {
				this.leaveShop(Street.allShops.get(this.shoppingList[i-1]));
			}
			this.shopAtShop(nextShop);
		}
		this.leaveShop(Street.allShops.get(this.shoppingList[this.shoppingList.length-1]));
		System.out.println(this.leadingInfo()+"is done with shopping, so she leaves the "+this.shoppingList[this.shoppingList.length-1]+" shop");
	}
	public Wizard(String name,String type,String[] shoppingList){
		this.name=name;
		this.setType(type);
		this.shoppingList=shoppingList;
//		this.startTime=System.nanoTime();
		for(String i:shoppingList){
			Street.shopnameList.add(i);
		}
	}
	
	public void run(){
		try {
			this.shop();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
