import java.io.PipedWriter;

/**
 * Created by JessicaWang on 15-07-12.
 */
public class Krakow {
    PQueue permitQueue = null;
    PQueue iceCreamPQueue = null;
    Queue hospitalQueue = null;
    Integer priorities = -1;

    final String kBazyliszek = "Bazyliszek";
    final String kSmokWawelski = "Smok Wawelski";
    final String kBabaJaga = "Baba Jaga";

    /**
     * constructor
     *
     * @param priorities unknown(?)
     * @param n          the capacity of queues
     */
    public Krakow(int priorities, int n) {
        this.priorities = priorities;
        permitQueue = new PQueue(priorities, n);
        iceCreamPQueue = new PQueue(priorities, n);
        hospitalQueue = new Queue(n);
    }

    /**
     * give permit to next person in permitQueue
     * and move him/her to iceCreameQueue
     */
    public void give_permit() {
        Person nextPerson = permitQueue.remove();
        if (nextPerson != null) {
            nextPerson.get_permit();
            try{
                if(bonusTreat(nextPerson, iceCreamPQueue)){
                    iceCreamPQueue.add(nextPerson);
                }
            }catch (StackOverflowError err)
            {
                System.err.println("Error:  StackOverFlow is watching you. (0_0");
            }
        }
    }

    /**
     * gives ice cream to the next person lining up for ice cream.
     * Then remove him/her from ice cream queue
     */
    public void give_ice_cream() {
        Person nextPerson = iceCreamPQueue.remove();
        if (nextPerson != null) {
            nextPerson.get_ice_cream();
        }
    }

    /**
     * gives ice cream to the next person in the hospital queue
     */
    public void give_hospital_ice_cream() {
        Person nextPerson = hospitalQueue.remove();
        if (nextPerson != null) {
            nextPerson.get_ice_cream();
        }
    }

    /**
     * enters the given person into the permit queue
     *
     * @param p a Person instance
     */
    public void enter_system(Person p) {
        try{
            if(bonusTreat(p, permitQueue)){
                permitQueue.add(p);
            }
        }catch (StackOverflowError err){
            System.err.println("Error:  StackOverFlow is watching you. (0_0");
        }
    }


    /**
     * enters the person into the hospital queue.
     * This person is removed from any other queues
     *
     * @param p a Person instance
     */
    public void fake_illness(Person p) {
        permitQueue.give_up(p);
        iceCreamPQueue.give_up(p);
        hospitalQueue.give_up(p);
        try{
            if(bonusTreat(p, hospitalQueue)){
                hospitalQueue.add(p);
            }
        }catch (StackOverflowError err){
            System.err.println("Error:  StackOverFlow is watching you. (0_0");
        }
    }

    /**
     *
     * @param p Person instance to be add into Queue
     * @param q target Queue
     * @return true indicates that Person to need be added into Queue manually, otherwise Person p has been added into Queue.
     * @throws StackOverflowError
     */
    private boolean bonusTreat(Person p, Object q) throws StackOverflowError{
        try {
            if (q != null && (p.name.equals(kBazyliszek) || p.name.equals(kSmokWawelski))) {
                Person removedPerson = null;
                //remove everyone currently in the queue
                while ((removedPerson = (q instanceof Queue) ? ((Queue) q).remove() : ((PQueue) q).remove()) != null) {
                    removedPerson.print_obituary();
                }
                return true;
            } else if (p.name.equals(kBabaJaga)) {
                //remove everyone currently in the queue
                if (q instanceof PQueue) {
                    Integer totalSize = 0;
                    for (Queue queue : ((PQueue) q).simpleQueue) {
                        totalSize += queue.size;
                    }
                    //remove all person from old queue
                    Person[] buffer = new Person[totalSize];
                    for (int i = 0; i < totalSize; i++) {
                        buffer[i] = ((PQueue) q).remove();
                    }
                    // add Baba Jada to the beginning of the queue
                    ((PQueue) q).add(p);
                    for (Person person : buffer) {
                        if(bonusTreat(person, q)) {
                            ((PQueue) q).add(person);
                        }
                    }
                    return false;
                } else if (q instanceof Queue) {
                    Integer totalSize = ((Queue) q).size;
                    //remove all persons from queue
                    Person[] buffer = new Person[totalSize];
                    for (int i = 0; i < totalSize; i++) {
                        buffer[i] = ((Queue) q).remove();
                    }
                    //add Baba Jada to the beginning of the queue
                    ((Queue) q).add(p);
                    for (Person person : buffer) {
                        if(bonusTreat(person, q)) {
                            ((Queue) q).add(person);
                        }
                    }
                    return false;
                } else {
                    throw new IllegalStateException("You should never be here.");
                }
            } else {
                return true;
            }
        } catch (StackOverflowError ex) {
            throw ex;
        } catch (IllegalStateException ex){
            System.err.println("Unexpected Error: " + ex.getMessage());
            return false;
        }
    }
}
