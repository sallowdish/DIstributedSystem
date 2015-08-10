public class Sim {
  static int timestamp = 0;
  static int slice = 0;
  static Task on_cpu = null;
  
  static void advance_time(int steps) {
    timestamp = timestamp + steps;
    on_cpu.time_worked = on_cpu.time_worked + steps;
    
    if (on_cpu.time_worked > on_cpu.time_required) {
      on_cpu.finished = true;
      on_cpu.time_task_completed = Sim.timestamp;
    }
  }

}
  
