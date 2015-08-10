/**
 * Created by Ray on 15-07-11.
 */
public class PQueue {
    Integer priorities;
    Queue[] simpleQueue;

    /**
     * constructor
     * @param priorities the upper bound of priorites that can be handled by this PQueue
     * @param n capacity of priority queue
     */
    public PQueue(int priorities, int n){
        // todo wait for clarification
        this.priorities = priorities;
        simpleQueue = new Queue[priorities];
        for (int i = 0; i < priorities; i++) {
            simpleQueue[i] = new Queue(n);
        }
    }

    /**
     * add a Person into PQueue, do nothing(?) if queue is full
     * @param p Person instance
     */
    public void add(Person p){
//        if (simpleQueue.size < simpleQueue.capacity) {
//            // pop out all elements from simple queue
//            Integer newSize = simpleQueue.size + 1;
//            Person[] tempContainer = new Person[newSize];
//            boolean isAdd = false;
//            Integer count = 0;
//            for (int i = 0; i < newSize-1; i++) {
//                Person element = simpleQueue.remove();
//                if (!isAdd){
//                    if(element.priority >= p.priority){
//                        tempContainer[count] = element;
//                    }
//                    else {
//                        tempContainer[count] = p;
//                        count++;
//                        tempContainer[count] = element;
//                        isAdd = true;
//                    }
//                }else{
//                    tempContainer[count] = element;
//                }
//                count++;
//            }
//            // if p.priority is smaller than every element in queue
//            if(!isAdd){
//                tempContainer[newSize-1] = p;
//            }
//            // put elements in temperContainer back to queue
//            for (Person e : tempContainer){
//                simpleQueue.add(e);
//            }
//        }else{
//            return;
//        }
        if (p.priority < priorities){
            if (simpleQueue[p.priority].size < simpleQueue[p.priority].capacity){
                simpleQueue[p.priority].add(p);
            }
            else
            {
                System.err.println("Simple Queue of priority " + p.priority + " is full.");
            }
        }
        else {
            System.err.println("Invalid Person w/ priority " + p.priority);
        }
    }

    /**
     * remove the person w/ highest priority in the queue, do nothing(?) if queue is empty
     */
    public Person remove(){
        for (int i = priorities-1; i >= 0 ; i--) {
            if (simpleQueue[i].size>0){
                return simpleQueue[i].remove();
            }
        }
        return null;
    }

    /**
     * search and remove the first Person in the queue which matches given arguments.
     * do nothing(?) if such person instance is not found.
     * @param p
     */
    public void give_up(Person p){
        for (Queue q : simpleQueue){
            q.give_up(p);
        }
    }

}
