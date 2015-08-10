/**
 * Created by Ray on 15-07-09.
 */
public class QueueArray {

    Integer size = -1;
    Integer capacity = -1;
    Integer head = -1;
    Integer end = -1;
    Person[] container = null;

    public QueueArray(int n)
    {
        if(n >= 0){
            size = 0;
            capacity = n;
            container = new Person[n];
        }
    }

    public void add(Person p){
        if(is_empty()){
            if(head == -1){
                //first element
                container[0] = p;
                head = 0;
                end = 0;
            }
            else{
                // queue happens to be empty in runtime
                container[countToIndex(end+1)] = p;
                head ++;
                end ++;
            }
            size++;
        }
        else if (size<capacity){
            container[countToIndex(end+1)] = p;
            //end points to the new element
            end++;
            size++;
        }
        else{
            return;
        }
    }

    public Person remove(){
        if(is_empty()) {
            return null;
        }
        else {
            Person reval = container[countToIndex(head)];
            if (head < end) {
                head++;
            }
            size--;
            return reval;
        }
    }

    public void give_up(Person p){
        for (int i = head; i < end; i++) {
            if (container[countToIndex(i)].equals(p)){
                //find the right person
                if(head == i){
                    remove();
                }
                else{
                    for (int j = i; j < end; j++) {
                        container[countToIndex(j)] = container[countToIndex(j+1)];
                    }
                    end--;
                }
                size--;
                break;
            }
        }
    }

    private boolean is_empty()
    {
        return size == 0;
    }

    private Integer countToIndex(Integer count){
        return count%capacity;
    }
}
