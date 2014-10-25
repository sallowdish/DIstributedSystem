import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Shop {
	String shopName;
	int numWizardInShop;
	Lock shopLock;
	public enum ShopState{
		byAuror,byDeathEater,none
	};
	ShopState occupiedBy;
	public boolean makeReservation(Wizard wizard){
		ShopState nextState=wizard.type==Wizard.WizardType.valueOf("Auror")?ShopState.valueOf("byAuror"):ShopState.valueOf("byDeathEater");
		boolean result = false;
		this.shopLock.lock();
		try {
			if (this.occupiedBy==ShopState.valueOf("none")) {
//				this.shopLock.lock();
				this.occupiedBy=nextState;
				result=true;
			}else if(this.occupiedBy==nextState){
				result= true;
			}else{
				result= false;
			}	
		} catch (Exception e) {
			System.err.println("Fail to make reservation.");
			} finally{
			this.shopLock.unlock();
		}
		return result;
	}
	public Shop(String name){
		this.shopName=name;
		this.occupiedBy=ShopState.valueOf("none");
		this.shopLock=new ReentrantLock();
		this.numWizardInShop=0;
	}
	public void incCount(Wizard customer){
		this.shopLock.lock();
		try {
			this.numWizardInShop++;
		} catch (Exception e) {
			System.err.println("fail to increase the counter");
		}finally{
//			System.out.println(this.shopName+": "+this.numWizardInShop+" now;");

			this.shopLock.unlock();
		}
	}
	public void decCount(Wizard customer){
		this.shopLock.lock();
		try {
			this.numWizardInShop--;
			if (this.numWizardInShop==0) {
				this.occupiedBy=ShopState.valueOf("none");
			}
		} catch (Exception e) {
			System.err.println("fail to decrese the counter");
		}finally{
//			System.out.println(this.shopName+": "+this.numWizardInShop+" now;");

			this.shopLock.unlock();
		}		
	}
}
