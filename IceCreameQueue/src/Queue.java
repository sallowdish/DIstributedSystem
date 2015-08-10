/**
 * Created by Ray on 15-07-09.
 */

// a simple queue based on linked list(LL)
public class Queue {

    class Node{
        Person p;
        Node next = null;

        public Node(Person p){
            this.p = p;
        }
    }

    // pointer to the head of LL
    private Node head = null;
    //pointer to the end of LL
    private Node end = null;
    // var indicates capacity of queue
    Integer capacity = -1;
    // var indicates current size of queue
    Integer size = -1;

//    constructor
    public Queue(int n){
        capacity = n;
        size = 0;
    }

    public void add(Person p){
        if (size<capacity){
            Node newNode = new Node(p);
            if (!is_empty()){
                //there is at least one element in queue
                end.next = newNode;
                end = newNode;
            }
            else{
                // no element in queue
                head = newNode;
                end = newNode;
            }
            size++;
        }
        else{
            // queue is full
            return;
        }
    }

    public Person remove(){
        if (size>1)
        {
            // more than 1 elements in queue
            Person reval = head.p;
            head = head.next;
            size--;
            return reval;
        }
        else if (size == 1)
        {
            // only 1 element in queue
            Person reval = head.p;
            head = null;
            end = null;
            size--;
            return reval;
        }
        else
        {
            // empty queue
            return null;
        }
    }

    public void give_up(Person p){
        Node ptr = head;
        Node prev = null;
        while (ptr != null){
            if (ptr.p.equals(p)){
                //find the person
                if (prev != null){
                    // in the mid of queue
                    // skip current node
                    prev.next = ptr.next;
                }
                else{
                    // begining of the queu
                    head = head.next;
                }
                size--;
                break;
            }
            else{
                prev = ptr;
                ptr = ptr.next;
            }
        }
    }

    private boolean is_empty()
    {
        return size == 0;
    }

}
