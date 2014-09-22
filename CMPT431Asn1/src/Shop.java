
public class Shop {
	String shopName;
	int numWizardInShop;
	public enum ShopState{
		byAuror,byDeathEater,none
	};
	ShopState occupiedBy;
	public boolean makeReservation(Wizard wizard){
		ShopState nextState=wizard.type==Wizard.WizardType.valueOf("Auror")?ShopState.valueOf("byAuror"):ShopState.valueOf("byDeathEater");
		if (this.occupiedBy==ShopState.valueOf("none")) {
			this.occupiedBy=nextState;
			return true;
		}else if(this.occupiedBy==nextState){
			return true;
		}else{
			return false;
		}
//		TODO add lock
	}
	public Shop(String name){
		this.shopName=name;
		this.occupiedBy=ShopState.valueOf("none");
	}
	public void incCount(Wizard customer){
		this.numWizardInShop++;
	}
	public void decCount(Wizard customer){
		this.numWizardInShop--;
		if (this.numWizardInShop==0) {
			this.occupiedBy=ShopState.valueOf("none");
		}
	}
}
