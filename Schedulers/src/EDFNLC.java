/**
 * Created by Ray on 15-08-09.
 */
public class EDFNLC extends EDF {

    /**
     * Revise and update current scheduel
     * be called whenever the time given to a task to execute is up, a task completes
     * and also at the beginning of the simulation.
     *
     * @return int stands for how much time are assigned to task to use CPU
     */
    public int schedule() {
        // pause current task
        Task currentTask = Sim.on_cpu;
        Task nextTask = null;
        Sim.on_cpu = null;
        // if current task is completed or cpu is idle
        if ((currentTask != null && currentTask.finished) || currentTask == null) {
            PrioritizedObject<Task> candidate = schedulerBuffer.remove();
            nextTask = candidate != null ? candidate.object : null;
            while (nextTask != null && Sim.timestamp + nextTask.time_required > nextTask.deadline)
            {
                candidate = schedulerBuffer.remove();
                nextTask = candidate != null ? candidate.object : null;
            }
        }
        // new task arrived
        else if (currentTask != null && !currentTask.finished) {
            PrioritizedObject<Task> candidate = schedulerBuffer.remove();
            nextTask = candidate != null ? candidate.object : null;
            while (nextTask != null && Sim.timestamp + nextTask.time_required > nextTask.deadline)
            {
                candidate = schedulerBuffer.remove();
                nextTask = candidate != null ? candidate.object : null;
            }
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
        return nextTask == null? null :nextTask.time_required - nextTask.time_worked;
    }
}
