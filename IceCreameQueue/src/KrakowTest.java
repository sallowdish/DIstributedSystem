public class KrakowTest {

  public static void main(String[] args) {

    int points = 4;

    final int KING = 1;
    final int COMMONER = 0;
    
    Person[] people = new Person[7];
    people[0] = new Person("Elvis", KING);
    people[1] = new Person("Stanislaw August Poniatowski", KING);
    people[2] = new Person("Bartosz Glowacki", COMMONER);
    people[3] = new Person("Piast Kolodziej", KING);
    people[4] = new Person("Zawisza Czarny", COMMONER);
    people[5] = new Person("Mikolaj Kopernik", COMMONER);
    people[6] = new Person("", KING); //Nameless king.
    
    Krakow city = new Krakow(2, 4);

    System.out.println("Trying to crash the program");
    city.give_hospital_ice_cream();
    city.give_ice_cream();
    city.give_permit();
    System.out.println("OK");

    System.out.println("Testing regular functioning with one person");
    city.enter_system(people[0]);
    if (people[0].has_permit != false) {
      System.out.println("Permit given too early");
      points = points - 1;
    }

    city.give_hospital_ice_cream();
    city.give_ice_cream();
    if (people[0].has_ice_cream == true || people[0].has_hospital_ice_cream == true) {
      System.out.println("Ice cream given to a person who should be in the permit queue");
      points = points - 1;
    }

    city.give_permit();
    if (people[0].has_permit == false) {
      System.out.println("Permit not given when required");
      points = points - 1;
    }
    
    city.give_hospital_ice_cream();
    if (people[0].has_ice_cream == true) {
      System.out.println("Wrong type of ice cream dispensed");
      points = points - 1;
    }

    city.give_ice_cream();

    if (people[0].has_ice_cream != true) {
      System.out.println("No ice cream dispensed");
      points = points - 1;
    }

    if (points != 4) {
      System.out.println("You lost points early in the testing.");
      System.out.println("You should fix your code.");
    }


    

    System.out.println("Testing queues with many people");

    city.enter_system(people[1]);
    city.enter_system(people[2]);
    city.enter_system(people[3]);
    city.enter_system(people[4]);

    city.give_permit();
    city.give_ice_cream();
    city.give_permit();
    city.give_ice_cream();

    if (people[3].has_ice_cream == false) {
      System.out.println("Priority queues not behaving properly");
      points = points - 1;
    }

    if (people[2].has_ice_cream == true) {
      System.out.println("Priority queues not behaving properly");
      points = points - 1;
    }

    city.give_permit();
    city.give_ice_cream();
    city.give_permit();
    city.give_ice_cream();


    if (people[1].has_ice_cream != true || people[2].has_ice_cream != true || people[3].has_ice_cream != true || people[4].has_ice_cream != true) {
      System.out.println("Queueing with multiple people not working.");
      points = points - 1;
    }

    System.out.println("Testing faking an illness");

    city.enter_system(people[5]);
    city.enter_system(people[6]);
    city.give_permit();
    city.fake_illness(people[6]);
    city.fake_illness(people[5]);
    city.fake_illness(people[6]);

    city.give_hospital_ice_cream();

    if (people[6].has_hospital_ice_cream == true) {
      System.out.println("Strange order in hospital queue");
      points = points - 1;
    }
    System.out.println("Final score: " + points + "/4");    
  }



  
}
