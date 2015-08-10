import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;


public class PostOffice {

    // MARCOS
    private static final String masterLog = "log_master.txt";

    private static final String frontpageLog = "log_front.txt";

    //enum of commands
    public enum CMDTYPE {
        DAY, PICKUP, LETTER, PACKAGE, INVALID,
    }

    //static vars
    //repr today's date
    private static int countDays = 1;

    public static ArrayList<String> postOfficeNameList = null;

    private static ArrayList<PostOffice> instances = null;

    public static ArrayList<String> criminalNameList = null;

    private static ArrayList<TransitItem> itemsinTransit = null;

    //instance vars
    // name of PO instance
    private String name = "";
    // days required to transit from this PO to destination, weird
    private Integer transitTime = 0;
    // money need to send package
    private Integer postageRequired = 0;
    // storage max size
    private Integer capacity = 0;
    // money to bribe PO staff to handle oversize package
    private Integer persuasion = 0;
    // package size limit
    private Integer maxLength = 0;

    private ArrayList<Item> itemsToDelivery = null;
    private ArrayList<Item> itemToPickUp = null;

    //default constructor, never use this one
    public PostOffice() {
        if(postOfficeNameList == null){
            postOfficeNameList = new ArrayList<String>();
        }
        if(criminalNameList == null){
            criminalNameList = new ArrayList<String>();
        }
        if(itemsinTransit == null){
            itemsinTransit = new ArrayList<TransitItem>();
        }
        if(instances == null ){
            instances = new ArrayList<PostOffice>();
        }
    }

    //Param constructor
    public PostOffice(String name, Integer transitTime, Integer postageRequired, Integer capacity, Integer persuasion, Integer maxLength) {
        this();
        this.name = name;
        this.transitTime = transitTime;
        this.postageRequired = postageRequired;
        this.capacity = capacity;
        this.persuasion = persuasion;
        this.maxLength = maxLength;

        this.itemsToDelivery = new ArrayList<Item>();
        this.itemToPickUp = new ArrayList<Item>();


        //add new instance into static array
        addInstance(this);
        writeLog(getLocalLogName(),"", false);
    }

    //validate input cmd
    public static CMDTYPE validateCommand(String cmd) {
        try {
            String args[] = cmd.split(" ");
            if (args[0].equals("DAY")) {
                return args.length == 1 ? CMDTYPE.DAY : CMDTYPE.INVALID;
            }
            else if (args[0].equals("PICKUP")) {
                return args.length == 3 ? CMDTYPE.PICKUP : CMDTYPE.INVALID;
            }
            else if (args[0].equals("LETTER")) {
                return args.length == 5 ? CMDTYPE.LETTER : CMDTYPE.INVALID;
            }
            else if (args[0].equals("PACKAGE")) {
                return args.length == 6 && args[4].matches("^-?\\d+$") && args[5].matches("^-?\\d+$") ? CMDTYPE.PACKAGE : CMDTYPE.INVALID;
            }
            else{
                    throw new IllegalArgumentException("Command \"" + args[0] + "\" does not match any predefined commands");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Input cmd is invalid: " + e.getMessage());
            return CMDTYPE.INVALID;
        } catch (Exception e) {
            System.err.println("Unknown error: " + e.getMessage());
            System.exit(-1);
            return CMDTYPE.INVALID;
        }
    }


    //execute input command
    public static void invokeCommand(String cmd) {
        PostOffice po;
        String args[];
        switch (PostOffice.validateCommand(cmd)) {
            case DAY:
                for (PostOffice instance : instances) {
                    instance.endofDay();
                }
                writeLog(masterLog, "- - DAY " + countDays + " OVER - -");
                countDays++;
                for (PostOffice instance : instances) {
                    instance.startofDay();
                }
                break;
            case LETTER:
                args = cmd.split(" ");
                po = getPostOfficebyName(args[1]);
                if (po != null){
                    po.receiveNewItem(new Letter(args[4], args[2], args[1], args[3]));
                }
                break;
            case PACKAGE:
                args = cmd.split(" ");
                po = getPostOfficebyName(args[1]);
                if (po != null)
                {
                    po.receiveNewItem(new Package("None", args[2], args[1], args[3], Integer.valueOf(args[4]), Integer.valueOf(args[5])));
                }
                break;
            case PICKUP:
                args = cmd.split(" ");
                po = getPostOfficebyName(args[1]);
                if (po != null)
                {
                    po.pickUpItemFromPostOffice(args[2]);
                }
                break;
            case INVALID:
                break;
            default:
                break;
        }
    }

    //Helper function

    // Logs
    protected String getLocalLogName() {
        return "log_" + name + ".txt";
    }

    private static void writeLog(String logName, String message){
        writeLog(logName, message, true);
    }

    private static void writeLog(String logName, String message, boolean isAppend) {
        try {
            PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(logName, isAppend)));
            if (message.equals("")){
                w.print("");
            }else{
                w.println(message);
            }
            w.close();
        } catch (FileNotFoundException e) {
            System.err.println("Fail to create static log files: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unknown error: " + e.getMessage());
        }
    }

    public static void initializeStaticLogs() {
        writeLog(masterLog, "", false);
        writeLog(frontpageLog, "", false);
    }

    // feature methods
    private void endofDay() {
        // 2.5.1
        for (Item item : itemsToDelivery) {
            itemsinTransit.add(new TransitItem(item, countDays+transitTime));
            writeLog(getLocalLogName(), "Standard transit departure");
        }
        itemsToDelivery.clear();
        ArrayList<Item> toDelete = new ArrayList<Item>();
        for (int i = 0; i < itemToPickUp.size(); i++) {
            Item item = itemToPickUp.get(i);
            if (item.getWaitingDay() >= 14) {
                // 2.6.1
                if (item instanceof Package) {
                    toDelete.add(item);
                    writeLog(getLocalLogName(), "- Incinerated package -");
                    writeLog(getLocalLogName(), "- Destoryed at: " + name + " -");
                    writeLog(masterLog, "- Incinerated package -");
                    writeLog(masterLog, "- Destoryed at: " + name + " -");
                } else {
                    Letter letter = (Letter) item;
                    // 2.6.2 letter has no return addr
                    if (item.getSender().equals("")) {
                        toDelete.add(item);
                        writeLog(getLocalLogName(), "- Incinerated letter -");
                        writeLog(getLocalLogName(), "- Destoryed at: " + name + " -");
                        writeLog(masterLog, "- Incinerated package -");
                        writeLog(masterLog, "- Destoryed at: " + name + " -");
                    }
                    // 2.6.4 letter has return addr
                    else {
                        Letter returnLetter = new Letter("", letter.getSender(), letter.getDestinationPostOffice(), letter.getInitialPostOffice());
                        toDelete.add(item);
                        // TODO revise estimated delivery date
                        itemsinTransit.add(new TransitItem(returnLetter, countDays+transitTime));
                    }
                }
            }
            else{
                item.setWaitingDay(item.getWaitingDay()+1);
            }
        }
        for (Item item : toDelete) {
            itemToPickUp.remove(item);
        }
        writeLog(getLocalLogName(), "- - DAY " + countDays + " OVER - -");
    }

    private void startofDay(){
        // the morning of the 2nd day
        receiveTransitItem();
    }

    // A receive new Item from sender
    // accept new item if it match all following conditions:
    // 1. destination is a valid PostOffice
    // 2. recipient is not a criminal
    // 3. there is still space in the initial PostOffice
    // for a package, additional checking is required
    public boolean receiveNewItem(Item newItem) {
        logReceivedNewItem(newItem);
        if (isDestinationValid(newItem) && !isRecipientCriminal(newItem.getRecipient()) && itemsToDelivery.size() + itemToPickUp.size() < capacity) {
            if (newItem instanceof Letter) {
                acceptNewItem(newItem, false);
                return true;
            } else {
                if (isPackageLengthValid((Package) newItem) && isFundsSufficienttoPostage((Package) newItem)) {
                    acceptNewItem(newItem, false);
                    return true;
                } else if (!isPackageLengthValid((Package) newItem) && isFundsSufficienttoPostageandPersuasion((Package) newItem)) {
                    acceptNewItem(newItem, true);
                    return true;
                } else {
                    rejectItem(newItem);
                    return false;
                }
            }

        } else {
            if (isRecipientCriminal(newItem.getRecipient())) {
                writeLog(frontpageLog, "Crinimal " + newItem.getRecipient() + " is found at Post Office " + this.name + ".");
            }
            rejectItem(newItem);
            return false;
        }
    }

    private void logReceivedNewItem(Item newItem){
        if (newItem instanceof Letter){
            writeLog(getLocalLogName(), "- New letter -");
        }
        else
        {
            writeLog(getLocalLogName(), "- New package -");
        }
        writeLog(getLocalLogName(), "Source: " + newItem.getInitialPostOffice());
        writeLog(getLocalLogName(), "Destination: " + newItem.getDestinationPostOffice());
    }

    private void acceptNewItem(Item i, boolean isOverSize) {
        i.setStartDate(countDays);
        itemsToDelivery.add(i);
        //write to local log
        if (i instanceof Package) {
            writeLog(getLocalLogName(), "- Accepted package -");
        } else {
            writeLog(getLocalLogName(), "- Accepted letter -");
        }
        writeLog(getLocalLogName(), "Destination: " + i.getDestinationPostOffice());
        //write to master log if need
        if (isOverSize) {
            writeLog(masterLog, "- Something funny going on... -");
            writeLog(masterLog, "Where did that extra money at " + name + " come from?");
        }

    }


    private void rejectItem(Item item) {
        //rejected letter entry to log of initial post office
        if (item instanceof Letter) {
            writeLog(getLocalLogName(), "- Rejected letter -");
            writeLog(masterLog, "- Rejected letter -");
        } else {
            writeLog(getLocalLogName(), "- Rejected package -");
            writeLog(masterLog, "- Rejected package -");
        }
        writeLog(getLocalLogName(), "Source: " + name);
        writeLog(masterLog, "Source: " + name);
    }

    //check if the destination if valid by traverse postOfficeNameList
    private static boolean isDestinationValid(Item item) {
        boolean isDestinationValid = false;
        for (int i = 0; i < postOfficeNameList.size(); i++) {
            if (postOfficeNameList.get(i).equals(item.getDestinationPostOffice())) {
                isDestinationValid = true;
                break;
            }
        }
        return isDestinationValid;
    }

    //1. check transit buffer, pick up all items that matches PO's name and transit days
    //2. write transit complete log
    //3. attempt to accept each item
    private void receiveTransitItem() {
        //2.5.2
        ArrayList<Item> pickUpItem = new ArrayList<Item>();
        for (int i = 0; i < itemsinTransit.size(); i++) {
            if (itemsinTransit.get(i).item.getDestinationPostOffice().equals(name)
                    && itemsinTransit.get(i).estaimatedArriveDate < countDays) {
                pickUpItem.add(itemsinTransit.get(i).item);
                writeLog(getLocalLogName(), "- Standard transit arrival -");
            }
        }
        //2.5.3
        for (int i = 0; i < pickUpItem.size(); i++) {
            if (pickUpItem.get(i) instanceof Package) {
                Package p = (Package) pickUpItem.get(i);
                if (p.getLength() > maxLength) {
                    //Destroy package since it's too long
                    writeLog(getLocalLogName(), "- Incinerated package -");
                    writeLog(getLocalLogName(), "- Destoryed at: " + name + " -");
                    writeLog(masterLog, "- Incinerated package -");
                    writeLog(masterLog, "- Destoryed at: " + name + " -");
                    break;
                }
            }
            //2.5.4
            if (itemsToDelivery.size() + itemToPickUp.size() >= capacity) {
                if (pickUpItem.get(i) instanceof Package) {
                    writeLog(getLocalLogName(), "- Incinerated package -");
                    writeLog(getLocalLogName(), "- Destoryed at: " + name + " -");
                    writeLog(masterLog, "- Incinerated package -");
                    writeLog(masterLog, "- Destoryed at: " + name + " -");
                } else {
                    writeLog(getLocalLogName(), "- Incinerated letter -");
                    writeLog(getLocalLogName(), "- Destoryed at: " + name + " -");
                    writeLog(masterLog, "- Incinerated letter -");
                    writeLog(masterLog, "- Destoryed at: " + name + " -");
                }

            } else {
                itemToPickUp.add(pickUpItem.get(i));
            }
        }
    }

    //recipient pick up item from destination PO
    private void pickUpItemFromPostOffice(String recipientName) {
        //2.6.3 check if recipient is a criminal
        if (isRecipientCriminal(recipientName)) {
            writeLog(frontpageLog, "Crinimal " + recipientName + " is found at Post Office " + this.name + ".");
            return;
        }
        // 2.7
        else {
            // note for all to delete array
            ArrayList<Item> toDelete = new ArrayList<Item>();
            for (int i = 0; i < itemToPickUp.size(); i++) {
                if (itemToPickUp.get(i).getRecipient().equals(recipientName)) {
//						storage.remove(i);
                    toDelete.add(itemToPickUp.get(i));
                    writeLog(getLocalLogName(), "- Delivery process complete -");
                    // TODO need transit days + waiting days
                    writeLog(getLocalLogName(), "Delivery took " + (countDays - itemToPickUp.get(i).getStartDate()) + " days.");
                }
            }
            // remove picked up item from storage
            for (Item item : toDelete) {
                itemToPickUp.remove(item);
            }
        }
    }


    //check if the recipient is a criminal by traverse crimialNameList
    private static boolean isRecipientCriminal(String recipientName) {
        boolean isCriminal = false;
        for (int i = 0; i < criminalNameList.size(); i++) {
            if (criminalNameList.get(i).equals(recipientName)) {
                isCriminal = true;
                break;
            }
        }
        return isCriminal;
    }

    //check if package has a valid length
    private boolean isPackageLengthValid(Package p) {
        return p.getLength() <= maxLength;
    }

    //check if package has enough funds for postage
    private boolean isFundsSufficienttoPostage(Package p) {
        return p.getFunds() >= postageRequired;
    }

    //check if package has enough funds for postage and persuasion
    private boolean isFundsSufficienttoPostageandPersuasion(Package p) {
        return p.getFunds() >= postageRequired + persuasion;
    }


    /**
     * helper function to add instance to static array
     */
    private static void addInstance(PostOffice po){
        try {
            postOfficeNameList.add(po.name);
            instances.add(po);
        }
        catch (Exception e)
        {
            System.err.println("Fail to add instance to static array: " + e.getMessage());
        }
    }

    /**
     * helper function to find instance by name
     */
    private static PostOffice getPostOfficebyName(String postOfficeName){
        for (PostOffice instance : instances) {
            if (postOfficeName.equals(instance.name)){
                return instance;
            }
        }
        return null;
    }

    public  static void cleanUp(){
        instances = null;
        postOfficeNameList = null;
        itemsinTransit = null;
        criminalNameList = null;

        countDays = 1;
    }
}
    

  






