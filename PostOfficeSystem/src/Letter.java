/**
 * Created by Ray on 15-06-16.
 */
public class Letter extends Item {

    //default constructor
    public Letter() {
        super();
    }

    //Param constructor
    public Letter(String sender, String recipient, String initialPostOffice, String destinationPostOffice) {
        super(sender, recipient, initialPostOffice, destinationPostOffice);
//        this.returnAddress = returnAddress;
    }

}
