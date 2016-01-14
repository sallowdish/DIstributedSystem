public class Sim {
    static int timestamp = 0;
    static int slice = 0;
    static Task on_cpu = null;

    static void advance_time(int steps) {
        for (int i = 0; i < steps; i++) {
            advanceSignStep();
        }
    }

    static void advanceSignStep() {
        timestamp++;
        if(on_cpu == null)
        {
        }
        else
        {
            on_cpu.time_worked++;
            // if task has been work enough time
            if (on_cpu.time_worked >= on_cpu.time_required)
            {
                on_cpu.finished = true;
                on_cpu.time_task_completed = Sim.timestamp;
                // fetch next task
            }
        }

    }

}
  
