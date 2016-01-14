/**
 * Created by Ray on 15-08-09.
 */
public class EDF implements Scheduler {
    PQueue<PrioritizedObject<Task>> schedulerBuffer = null;

    public EDF()
    {
        setup();
    }
    /**
     * Initializer
     */
    public void setup()
    {
        // worst case: 1024 tasks w/ same priority and 1024 tasks w/ unique priority
        schedulerBuffer = new PQueue<PrioritizedObject<Task>>(1024,1024);
    }

    /**
     * Revise and update current scheduel
     * be called whenever the time given to a task to execute is up, a task completes
     * and also at the beginning of the simulation.
     * @return int stands for how much time are assigned to task to use CPU
     */
    public int schedule()
    {
// pause current task
        Task currentTask = Sim.on_cpu;
        Task nextTask;
        // if current task is completed or cpu is idle
        if (currentTask == null || (currentTask != null && currentTask.finished)) {
            PrioritizedObject<Task> candidate = schedulerBuffer.remove();
            nextTask = candidate != null ? candidate.object : null;
        }
        // new task arrived
        else if (currentTask != null && !currentTask.finished) {
            PrioritizedObject<Task> candidate = schedulerBuffer.remove();
            nextTask = candidate != null ? candidate.object : null;
            if (nextTask != null && nextTask.deadline < currentTask.deadline) {
                // put current task back to buffer
                new_task(currentTask);
            }
            else
            {
                // put next task back to buffer
                // keep working on current task
                if (nextTask != null) {
                    new_task(nextTask);
                }
                nextTask = currentTask;
            }
        }
        else
        {
            nextTask = null;
        }

        Sim.on_cpu = nextTask;
        return nextTask == null ? null :nextTask.time_required - nextTask.time_worked;
    }

    /**
     * Add a new task in to task queue
     * @param t Task instance which stands for the new arrived task
     */
    public void new_task(Task t)
    {
        int priority = t.deadline - Sim.timestamp > 0 ? MAXTIMECOUNT - (t.deadline - Sim.timestamp) - 1 : MAXTIMECOUNT - 1;
        schedulerBuffer.add(new PrioritizedObject<Task>(t, priority));
    }

    /**
     * update the deadline of given task
     * @param t Task object which is going to be updated
     * @param old_deadline original deadline of the task?
     */
    public void change_deadline(Task t, int old_deadline)
    {
//        ArrayList<PrioritizedObject<Task>> temp = new ArrayList<PrioritizedObject<Task>>();
        PrioritizedObject<Task>[] temp = new PrioritizedObject[schedulerBuffer.getTotalSize()];
        PrioritizedObject<Task> nextTask = schedulerBuffer.remove();
        int count = 0;
        while(nextTask != null && nextTask.object != t)
        {
            temp[count] = nextTask;
            count++;
            nextTask = schedulerBuffer.remove();
        }
        if (nextTask == null)
        // no such task in scheduler right now
        {
            return;
        }
        else
        {
            new_task(t);
            for(PrioritizedObject<Task> task : temp)
            {
                if(task != null)
                {
                    schedulerBuffer.add(task);
                }
            }
        }
    }
}
