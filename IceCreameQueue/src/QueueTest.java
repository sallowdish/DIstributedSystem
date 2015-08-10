public class QueueTest {

  public static void main(String[] args) {

    Queue empty = new Queue(0);
    int points = 2;
    
    System.out.println("Trying to crash through removes from an empty queue");
    
    empty.remove();
    empty.remove();
    empty.remove();
    
    System.out.println("OK");
    

    Queue small = new Queue(3);
    small.add(new Person("A", 0));
    small.add(new Person("B", 0));
    small.add(new Person("C", 0));

    System.out.println("Testing queue order");
    boolean ok = true;
    if (!small.remove().equals(new Person("A", 0))) {
      ok = false;
    }
    if (!small.remove().equals(new Person("B", 0))) {
      ok = false;
    }
    if (!small.remove().equals(new Person("C", 0))) {
      ok = false;
    }

    if (true == ok) {
      System.out.println("Queue order correct");
    } else {
      System.out.println("Queue keeps wrong order");
      points = points - 1;
    }

    System.out.println("Testing space reuse");
    
    small.add(new Person("D", 0));
    small.add(new Person("E", 0));
    small.add(new Person("F", 0));
    
    ok = true;
    if (!small.remove().equals(new Person("D", 0))) {
      ok = false;
    }
    if (!small.remove().equals(new Person("E", 0))) {
      ok = false;
    }
    if (!small.remove().equals(new Person("F", 0))) {
      ok = false;
    }
    
    if (true == ok) {
      System.out.println("Queue reuses space");
    } else {
      System.out.println("Queue does not reuse space properly");
      points = points - 1;
    }


    System.out.println("Testing giving up");

    empty.give_up(new Person("X", 0));

    small.add(new Person("X", 0));
    small.add(new Person("Y", 0));
    small.add(new Person("Z", 0));
    
    small.give_up(new Person("X", 0));
    small.give_up(new Person("Z", 0));

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


    System.out.println("Testing speed");
    System.out.println("Failing this test means that you haven't done the Queue properly.");
    System.out.println("If this does not terminate in under a minute, you will not get any points for this component");

    //A queue typical of Poland.
    Queue huge = new Queue(10000000);
    int left = 10000000;
    ok = true;
    
    while (left != 0) {
      huge.add(new Person("new", left));
      left = left - 1;
    }

    left = 10000000;
    while (left != 0) {
      Person out = huge.remove();
      if (out.priority != left) {
	System.out.println("Bad results with large queues, your code needs fixing.");
	points = points - 1;
	ok = false;
	break;
      }
	  
      left = left - 1;
    }

    System.out.println("Time test passed");

    if (points < 0) {
      points = 0;
    }

    System.out.println(points + " points earned on this component");
    
  }
  
}
