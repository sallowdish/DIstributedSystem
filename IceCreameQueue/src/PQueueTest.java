public class PQueueTest {

  public static void main(String[] args) {

    PQueue empty = new PQueue(0, 0);
    int points = 3;
    
    System.out.println("Trying to crash through removes from an empty queue");
    
    empty.remove();
    empty.remove();
    empty.remove();
    
    System.out.println("OK");
    

    PQueue small = new PQueue(4,3);
    small.add(new Person("A", 1));
    small.add(new Person("B", 2));
    small.add(new Person("C", 3));

    System.out.println("Testing priority queue order");
    boolean ok = true;
    if (!small.remove().equals(new Person("C", 3))) {
      ok = false;
    }
    if (!small.remove().equals(new Person("B", 2))) {
      ok = false;
    }
    if (!small.remove().equals(new Person("A", 1))) {
      ok = false;
    }

    if (true == ok) {
      System.out.println("Priority queue order correct");
    } else {
      System.out.println("Priority queue keeps wrong priority order");
      points = points - 1;
    }

    System.out.println("Testing space reuse / regular queue order");
    
    small.add(new Person("D", 3));
    small.add(new Person("E", 3));
    small.add(new Person("F", 3));
    
    ok = true;
    if (!small.remove().equals(new Person("D", 3))) {
      ok = false;
    }
    if (!small.remove().equals(new Person("E", 3))) {
      ok = false;
    }
    if (!small.remove().equals(new Person("F", 3))) {
      ok = false;
    }
    
    if (true == ok) {
      System.out.println("PQueue reuses space and keeps queue order within priorities");
    } else {
      System.out.println("PQueue does not reuse space properly or does not keep queue order within priorities");
      points = points - 1;
    }


    System.out.println("Testing giving up");

    PQueue tiny = new PQueue(1,0);
    tiny.give_up(new Person("X", 0));

    small.add(new Person("X", 1));
    small.add(new Person("Y", 0));
    small.add(new Person("Z", 3));
    
    small.give_up(new Person("X", 1));
    small.give_up(new Person("Z", 3));

    ok = true;
    if (!small.remove().equals(new Person("Y", 0))) {
      ok = false;
    }
    if (small.remove() != null) {
      ok = false;
    }
    
    if (true == ok) {
      System.out.println("Giving up seems to work");
    } else {
      System.out.println("Giving up test failed");
      points = points - 1;
    }



    
    System.out.println("Testing inner organization");
    System.out.println("A failure on this test means that you haven't done the PQueue properly.");

    ok = true;
    try {
      PQueue huge = new PQueue(10000000, 10000000);
      ok = false;
    } catch (OutOfMemoryError e) {
      
    }

    if (ok == true) {
      System.out.println("Memory exhaustion test passed");
    } else {
      System.out.println("Memory exhaustion test failed");
      System.out.println("You might have implemented the PQueue wrong!");
      points = points - 1;
    }

    if (points < 0) {
      points = 0;
    }

    System.out.println(points + " points earned on this component");
    
  }
  
}
