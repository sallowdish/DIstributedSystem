public class EDFSTest {


  public static int sched_call(EDFS sched) throws Exception {
    int time_to_spend = sched.schedule();
    if (time_to_spend == 0) {
      time_to_spend = Sim.on_cpu.time_required - Sim.on_cpu.time_worked;
    }
    if (time_to_spend == -1) {
      System.out.println("Your scheduler is attempting time travel.");
      System.out.println("That was A3.");
      throw new Exception();
    }
    return time_to_spend;
  }
  
  public static void main(String[] args) throws Exception {

    EDFS sched = new EDFS();
    Task first = new Task(3, 19);
    Task second = new Task(4, 20);
    Task third = new Task(6, 20);

    int time_to_spend;
    
    Sim.slice = -1;

    sched.new_task(second);


    
    time_to_spend = sched_call(sched);
    if (Sim.on_cpu != second) {
      System.out.println("Wrong task scheduled");
      throw new Exception();
    }
    if (time_to_spend != Sim.on_cpu.time_required) {
      System.out.println("Mis-scheduling of tasks, task should have run for " + Sim.on_cpu.time_required + ", but it ran for " + time_to_spend + " instead!");
      throw new Exception();
    }

    Sim.advance_time(1);
    
    sched.new_task(first);
    time_to_spend = sched_call(sched);
    sched.new_task(third);
    
    if (Sim.on_cpu != first) {
      System.out.println("Wrong task scheduled");
      throw new Exception();
    }
    if (time_to_spend != Sim.on_cpu.time_required) {
      System.out.println("Mis-scheduling of tasks, task should have run for " + Sim.on_cpu.time_required + ", but it ran for " + time_to_spend + " instead!");
      throw new Exception();
    }
    
    Sim.advance_time(19);

    time_to_spend = sched_call(sched);
    if (Sim.on_cpu != second) {
      System.out.println("Wrong task scheduled");
      throw new Exception();
    }

    System.out.println("All OK");
    
  }
  
}
