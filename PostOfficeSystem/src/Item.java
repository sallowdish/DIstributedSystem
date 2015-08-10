/**
 * Created by Ray on 15-06-16.
 */

//base class for mail and package, abstract only
public abstract class Item {

    // the original sender of item
    private String sender = "";
    // the target receiver of item
    private String recipient = "";
    // the PO where item is accepted
    private String initialPostOffice = "";
    // the PO where item is sent
    private String destinationPostOffice = "";
    // the days counter repr how many day item have been waiting for being picke up
    private Integer waitingDay = 0;

    public Integer getStartDate() {
        return startDate;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    // the date when item is accepted by initial PO
    private Integer startDate = 0;

    //default constructor
    public Item() {
    }

    //Param constructor
    public Item(String sender, String recipient, String initialPostOffice, String destinationPostOffice) {
        this.sender = sender;
        this.recipient = recipient;
        this.initialPostOffice = initialPostOffice;
        this.destinationPostOffice = destinationPostOffice;
    }

    //getters and setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getInitialPostOffice() {
        return initialPostOffice;
    }

    public void setInitialPostOffice(String initialPostOffice) {
        this.initialPostOffice = initialPostOffice;
    }

    public String getDestinationPostOffice() {
        return destinationPostOffice;
    }

    public void setDestinationPostOffice(String destinationPostOffice) {
        this.destinationPostOffice = destinationPostOffice;
    }

    public Integer getWaitingDay() {
        return waitingDay;
    }

    public void setWaitingDay(Integer day) {
        this.waitingDay = day;
    }

}
