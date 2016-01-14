/**
 * Created by Ray on 15-08-10.
 */
public class EDFS extends EDF implements Scheduler{
    Stack<Task> schedulerStack = null;

    public EDFS()
    {
        super();
        setup();
    }

    /**
     * Initializer
     */
    public void setup()
    {
        // worst case: 1024 tasks w/ same priority and 1024 tasks w/ unique priority
        super.setup();
        schedulerStack = new Stack<Task>(MAXTASKCOUNT);
    }

    /**
     * Revise and update current scheduel
     * be called whenever the time given to a task to execute is up, a task completes
     * and also at the beginning of the simulation.
     * @return int stands for how much time are assigned to task to use CPU
     */
    public int schedule() {
        // pause current task
        Task currentTask = Sim.on_cpu;
        Task nextTask = null;
        Sim.on_cpu = null;
        // if current task is completed or null
        if ((currentTask != null && currentTask.finished) || currentTask == null) {
            // if there is any task in stack
            if (schedulerStack.count() > 0) {
                nextTask = schedulerStack.pop();
            }
            // fetch next task from scheduler buffer
            else {
                PrioritizedObject<Task> candidate = schedulerBuffer.remove();
                nextTask = candidate != null ? candidate.object : null;
            }
        }
        // new task arrived
        else if (currentTask != null && !currentTask.finished) {
            PrioritizedObject<Task> candidate = schedulerBuffer.remove();
            nextTask = candidate != null ? candidate.object : null;
            // if new task is more urgent
            if (nextTask.deadline < currentTask.deadline) {
                // put current task into stack
                schedulerStack.push(currentTask);
            } else {
                // put next task back to buffer
                new_task(nextTask);
                nextTask = currentTask;
            }
        } else {
            nextTask = null;
        }
        Sim.on_cpu = nextTask;
        return nextTask == null ? null : nextTask.time_required - nextTask.time_worked;
    }

    /**
     * update the deadline of given task
     * @param t Task object which is going to be updated
     * @param old_deadline original deadline of the task?
     */
    public void change_deadline(Task t, int old_deadline)
    {
        // skip this part per requirement
    }
}
