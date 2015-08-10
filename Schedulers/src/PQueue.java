import java.util.ArrayList;

/**
 * Created by Ray on 15-07-11.
 */
public class PQueue<T extends PrioritizedObject> {
    Integer priorities;
    Queue[] simpleQueue;

    /**
     * constructor
     * @param priorities the upper bound of priorites that can be handled by this PQueue
     * @param n capacity of priority queue
     */
    public PQueue(int priorities, int n){
        this.priorities = priorities;
        simpleQueue = new Queue[priorities];
        for (int i = 0; i < priorities; i++) {
            simpleQueue[i] = new Queue(n);
        }
    }

    /**
     * add a Person into PQueue, do nothing(?) if queue is full
     * @param p Generic object instance
     */
    public void add(T p){
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
    public T remove(){
        for (int i = priorities-1; i >= 0 ; i--) {
            if (simpleQueue[i].size>0){
                return (T)simpleQueue[i].remove();
            }
        }
        return null;
    }

    /**
     * search and remove the first Person in the queue which matches given arguments.
     * do nothing(?) if such person instance is not found.
     * @param p
     */
    public void give_up(T p){
        for (Queue q : simpleQueue){
            q.give_up(p);
        }
    }

    public Integer getTotalSize(){
        Integer res = 0;
        for (Queue q : simpleQueue){
            res += q.size;
        }
        return res;
    }

    public boolean contains(T p){
        for (Queue q : simpleQueue){
            if (q.contains(p)){
                return true;
            }
        }
        return  false;
    }

    public ArrayList<T> getAllObject()
    {
        ArrayList<T> result = new ArrayList<>();
        for(Queue q : simpleQueue)
        {
            result.addAll(q.getAllObject());
        }
        return result;
    }
}
