public class MonsterTest {

  public static void main(String[] args) {

    int points = 1;

    final int COMMONER = 0;
    
    Person[] people = new Person[5];
    people[0] = new Person("Smok Wawelski", COMMONER);
    people[1] = new Person("Baba Jaga", COMMONER);
    people[2] = new Person("Baba Jaga", COMMONER);
    people[3] = new Person("Baba Jaga", COMMONER);
    people[4] = new Person("Bazyliszek", COMMONER);
    
    Krakow city = new Krakow(1, 4);

    //Must be a two-headed dragon.
    city.enter_system(people[0]);
    city.enter_system(people[0]);
    
    if (people[0].dead == false) {
      System.out.println("Smok Wawelski behavior failed");
      points = points - 1;
    }


    city.fake_illness(people[4]);
    city.fake_illness(people[1]);

    if (people[1].dead == false) {
      System.out.println("Bazyliszek/Baba Jaga interaction failure");
      points = points - 1;
    }
    
    city = new Krakow(1, 4);
    
    System.out.println("Baba jaga test -- Your program must terminate gracefully here");
    city.fake_illness(people[2]);
    city.fake_illness(people[3]);

    if (points < 0) {
      points = 0;
    }
    
    System.out.println(points + " points earned");
    
  }



  
}
