/**
 * Created by Ray on 15-06-16.
 */
public class Package extends Item {

    // package size
    private Integer length = 0;
    // money to spend on transit cost and tip to persuade staff to process oversize package
    private Integer funds = 0;

    //default constructor, never use this one
    public Package() {
        super();
    }

    //Param constructor
    public Package(String sender, String recipient, String initialPostOffice, String destinationPostOffice, Integer funds, Integer length) {
        super(sender, recipient, initialPostOffice, destinationPostOffice);
        this.length = length;
        this.funds = funds;
    }


    //getters and setters
    public Integer getFunds() {
        return funds;
    }

    public void setFunds(Integer funds) {
        this.funds = funds;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
