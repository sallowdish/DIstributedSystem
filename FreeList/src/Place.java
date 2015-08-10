public class Place {
  private String location;
  private int time;
  private boolean printed = false;
  
  Place(String location, int time) {
      this.location = location;
      this.time = time;
  }
  
  public boolean equals(Place other) {
    return location.equals(other.location) && (time == other.time);
  }

  public void print() {
    System.out.println(" " + location + " " + time);
    printed = true;
  }

  public boolean was_printed() {
    return printed;
  }
  
}
