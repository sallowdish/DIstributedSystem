/**
 * Created by Ray on 15-08-09.
 */
public class PrioritizedObject<T> {
    int priority = 0;
    T object;

    public PrioritizedObject(T obj)
    {
        this.object = obj;
    }

    public PrioritizedObject(T obj, int priority)
    {
        this.object = obj;
        this.priority = priority;
    }
}
