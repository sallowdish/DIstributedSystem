public class PFATest {
  
  public static void main(String[] in) throws Exception {
    //TSSU requirement here!
    //Do not change this line!
    boolean grade_withholding_mode = true;




    final int CONSTRUCTOR = 0;
    final int FLUSH = 1;
    final int IS_FULL = 2;
    final int SPACE_LEFT = 3;
    final int ADD_ITEM = 4;
    final int POSITION_IN_ARRAY = 5;
    final int REMOVE_ITEM = 6;
    final int UNSAFE_REMOVE_ITEM = 7;
    final int UNSAFE_POSITION_IN_ARRAY = 8;
    final int PRINT_ALL = 9;

    


    
    boolean[] working = new boolean[10];
    int current_position = 0;
    while (current_position != 10) {
      working[current_position] = true;
      current_position = current_position + 1;
    }
    
    //Test 1 - Constructor alone.
    System.out.println("Constructor test");
    
    try {
      PFArray time_travel_log = new PFArray(-1);
      working[CONSTRUCTOR] = false;
      System.out.println("PFArray constructor does not throw exceptions for negative sizes.");
    } catch (Exception e) {
    }

    try {
      PFArray time_travel_log = new PFArray(0);
    } catch (Exception e) {
      working[CONSTRUCTOR] = false;
      System.out.println("PFArray constructor does not allow size 0 PFArrays.");
    }

    PFArray blank_time_travel_log = new PFArray(0);
    PFArray minimal_time_travel_log = new PFArray(1);
    PFArray small_time_travel_log = new PFArray(3);
    PFArray time_travel_log = new PFArray(10);
    PFArray ginormous_time_travel_log = new PFArray(10000000);


    //Test 2 -- is full?
    System.out.println("is_full() test");
    if (minimal_time_travel_log.is_full() == true) {
      working[IS_FULL] = false;
      System.out.println("1. is_full() is broken");
    }
    if (blank_time_travel_log.is_full() == false) {
      working[IS_FULL] = false;
      System.out.println("2. is_full() is broken");
    }
    

    //Test 3 -- space left
    System.out.println("space_left() test");
    if (minimal_time_travel_log.space_left() != 1) {
      working[SPACE_LEFT] = false;
      System.out.println("space_left() is broken");
    }


    //Test 4 -- Adding items
    System.out.println("add_item() test");
    try {
      blank_time_travel_log.add_item(new Place("Wild West", 1885));
      working[ADD_ITEM] = false;
      System.out.println("I just added an item into a blank log!");
    } catch (Exception e) {
    }

    Place to_be_reused = new Place("Wilder West", 2085);
    
    try {
      minimal_time_travel_log.add_item(to_be_reused);
    } catch (Exception e) {
      working[ADD_ITEM] = false;
      System.out.println("An add that should have gone through threw an exception.");
    }

    if (minimal_time_travel_log.is_full() == false) {
      working[ADD_ITEM] = false;
      System.out.println("An added item did not fill up a log which should have become full!");
    }
    

    //Test 5 -- unsafe position in array.
    System.out.println("unsafe position test");
    small_time_travel_log.add_item(new Place("The day of Lavos", 1999));
    small_time_travel_log.add_item(new Place("Diktor's time gate", 301949));

    if (small_time_travel_log.unsafe_position_in_array(new Place("Diktor's time gate", 301949)) != 1) {
      working[UNSAFE_POSITION_IN_ARRAY] = false;
      System.out.println("Bad storage pattern for positions.");
    }

    if (small_time_travel_log.unsafe_position_in_array(new Place("King Arthur's court", 1000)) != -1) {
      working[UNSAFE_POSITION_IN_ARRAY] = false;
      System.out.println("Bad indication for nonexisting items.");
    }

    //test 6 safe position in array
    System.out.println("safe position test");
    try {
      small_time_travel_log.position_in_array(new Place("King Arthur's court", 1000));
      working[POSITION_IN_ARRAY] = false;
      System.out.println("No exception in safe position_in_array with nonexistent element");
    } catch (Exception e) {
    }


    //test 7 - print all
    System.out.println("print test");
    minimal_time_travel_log.print_all();
    if (to_be_reused.was_printed() == false) {
      working[PRINT_ALL] = false;
      System.out.println("Printing all does not work.");
    }
    
    
    //test 8 - unsafe element removal
    System.out.println("unsafe remove test");
    minimal_time_travel_log.unsafe_remove_item(0);
    if (minimal_time_travel_log.is_full() == true) {
      working[UNSAFE_REMOVE_ITEM] = false;
      System.out.println("Unsafe removal does not actually remove");
    }

    //test 9 - safe removal
    System.out.println("safe remove test");
    try {
      minimal_time_travel_log.remove_item(0);
      working[REMOVE_ITEM] = false;
      System.out.println("Removal of nonexistent items should have thrown an exception");
    } catch (Exception e) {
    }

    //test 10 - flush
    System.out.println("flush test");
    small_time_travel_log.flush();
    if (small_time_travel_log.space_left() != 3) {
      working[FLUSH] = false;
      System.out.println("Does not completely flush");
    }

    //test 11 - this is designed not to terminate if you don't do it right

    System.out.println("Testing speed");
    System.out.println("Your program should print requirement status within 120 seconds.");
    System.out.println("If the execution stops here for over a minute you might have not implemented the free list correctly.");
    



    int current_insert = 0;
    while (ginormous_time_travel_log.is_full() != true) {
        ginormous_time_travel_log.add_item(new Place("Groundhog day", current_insert));
	current_insert = current_insert + 1;
    }

    int iterations_left = 1000000;
    while (iterations_left != 0) {
      iterations_left = iterations_left - 1;
      ginormous_time_travel_log.unsafe_remove_item(7500000);
      ginormous_time_travel_log.unsafe_remove_item(2500000);
      ginormous_time_travel_log.unsafe_remove_item(5000000);
      ginormous_time_travel_log.add_item(new Place("Groundhog day", -1));
      ginormous_time_travel_log.add_item(new Place("Groundhog day", -2));
      ginormous_time_travel_log.add_item(new Place("Groundhog day", -3));
    }
    

    int sum = 0;
    int req_id = 0;
    for (boolean result : working) {
      req_id = req_id + 1;
      if (true == result) {
	System.out.println("Requirement " + req_id + " PASSED");
	sum = sum + 1;
      } else {
	System.out.println("Requirement " + req_id + " FAILED");
      }
    }

    if (false == grade_withholding_mode) {
      System.out.println("Grade: " + sum + "/10");
    }

    
  }
  
}
