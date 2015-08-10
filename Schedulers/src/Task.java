public class Task {
  final public static int NO_DEADLINE = 1023;
  public int deadline = NO_DEADLINE;
  public int time_required;
  public boolean has_deadline = false;
  public boolean finished = false;
  public int time_worked = 0;
  
  private int time_arrived;
  public int time_task_completed = NO_DEADLINE;
  private boolean deadline_expired = false;


  Task(int time_required, int deadline) {
    this.deadline = deadline;
    this.time_required = time_required;
    this.time_arrived = Sim.timestamp;

    
    if (deadline != 1023) {
      has_deadline = true;
    } else {
      has_deadline = false;
    }
    
  }
}
  
