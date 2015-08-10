public class RRTest {


  public static int sched_call(RR sched) throws Exception {
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

    RR sched = new RR();
    Task first = new Task(5, 10);
    Task second = new Task(5, 50);
    Task third = new Task(7, 20);

    int time_to_spend;
    
    Sim.slice = 2;

    
    sched.new_task(second);
    sched.new_task(third);
    
    time_to_spend = sched_call(sched);
    if (Sim.on_cpu != second) {
      System.out.println("Wrong task scheduled");
      throw new Exception();
    }
    if (time_to_spend != Sim.slice) {
      System.out.println("Mis-scheduling of tasks, task should have run for " + Sim.slice + ", but it ran for " + time_to_spend + " instead!");
      throw new Exception();
    }

    Sim.advance_time(2);
    
    time_to_spend = sched_call(sched);
    if (Sim.on_cpu != third) {
      System.out.println("Wrong task scheduled");
      throw new Exception();
    }
    if (time_to_spend != Sim.slice) {
      System.out.println("Mis-scheduling of tasks, task should have run for " + Sim.slice + ", but it ran for " + time_to_spend + " instead!");
      throw new Exception();
    }

    Sim.advance_time(2);

    
    sched.new_task(first);
    
    time_to_spend = sched_call(sched);
    if (Sim.on_cpu != second) {
      System.out.println("Wrong task scheduled");
      throw new Exception();
    }
    
    Sim.advance_time(2);

    time_to_spend = sched_call(sched);
    if (Sim.on_cpu != first) {
      System.out.println("Wrong task scheduled");
      throw new Exception();
    }

    System.out.println("All OK");
    
  }
  
}
