
public class TransitItem {
    public Item item = null;
    public Integer estaimatedArriveDate = -1;

    //default, never use this one
    public TransitItem() {
    }

    ;

    //public constructor
    public TransitItem(Item i, Integer date) {
        this.item = i;
        this.estaimatedArriveDate = date;
    }


}
