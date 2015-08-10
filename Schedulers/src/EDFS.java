import java.util.ArrayList;

/**
 * Created by Ray on 15-08-10.
 */
public class EDFS implements Scheduler{
    PQueue<PrioritizedObject<Task>> schedulerBuffer = null;
    Stack<Task> schedulerStack = null;

    public EDFS()
    {
        setup();
    }

    /**
     * Initializer
     */
    public void setup()
    {
        // worst case: 1024 tasks w/ same priority and 1024 tasks w/ unique priority
        schedulerBuffer = new PQueue<PrioritizedObject<Task>>(MAXTASKCOUNT,MAXTASKCOUNT);
        schedulerStack = new Stack<Task>(MAXTASKCOUNT);
    }

    /**
     * Revise and update current scheduel
     * be called whenever the time given to a task to execute is up, a task completes
     * and also at the beginning of the simulation.
     * @return int stands for how much time are assigned to task to use CPU
     */
    public int schedule()
    {
        // if current task is not finished with the time assigned,
        // put it back to task queue
        Task currentTask = Sim.on_cpu;
        Sim.on_cpu = null;
        // if current task is completed or null
        if((currentTask != null && currentTask.finished) || currentTask == null)
        {
            Task nextTask = null;
            // if there is any task in stack
            if (schedulerStack.count() > 0)
            {
                nextTask = schedulerStack.pop();
            }
            else
            {
                nextTask = schedulerBuffer.remove().object;
            }
            if(nextTask != null) {
                Sim.on_cpu = nextTask;
                return nextTask.time_required - nextTask.time_worked;
            }
            else
            {
                return -1;
            }
        }
        // new task arrived
        else if( currentTask != null && !currentTask.finished)
        {
            PrioritizedObject<Task> nextTask = schedulerBuffer.remove();
            if (nextTask.object.deadline < currentTask.deadline)
            {
                // put current task into stack
                schedulerStack.push(currentTask);
                Sim.on_cpu = nextTask.object;
                return nextTask.object.time_required - nextTask.object.time_worked;
            }
            else
            {
                // put next task back to buffer
                schedulerBuffer.add(nextTask);
                return currentTask.time_required - currentTask.time_worked;
            }
        }
        else
        {
            Sim.on_cpu = null;
            return -1;
        }
    }

    /**
     * Add a new task in to task queue
     * @param t Task instance which stands for the new arrived task
     */
    public void new_task(Task t)
    {
        schedulerBuffer.add(new PrioritizedObject<Task>(t, MAXTIMECOUNT-(t.deadline - Sim.timestamp)));
    }

    /**
     * update the deadline of given task
     * @param t Task object which is going to be updated
     * @param old_deadline original deadline of the task?
     */
    public void change_deadline(Task t, int old_deadline)
    {
//
//        PrioritizedObject<Task> nextTask = schedulerBuffer.remove();
//        while(nextTask != null && nextTask.object != t)
//        {
//            temp.add(nextTask);
//            nextTask = schedulerBuffer.remove();
//        }
//        if (nextTask == null)
//        // no such task in scheduler right now
//        {
//            return;
//        }
//        else
//        {
//            schedulerBuffer.add(new PrioritizedObject<Task>(t, MAXTIMECOUNT-(t.deadline - Sim.timestamp)));
//            for(PrioritizedObject<Task> task : temp)
//            {
//                schedulerBuffer.add(task);
//            }
//        }
    }
}
