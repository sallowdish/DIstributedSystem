public class Person {

  public String name;
  public int priority;

  public boolean has_hospital_ice_cream = false;
  public boolean has_ice_cream = false;
  public boolean has_permit = false;
  public boolean dead = false;

  Person(String name, int priority) {
    this.name = name;
    this.priority = priority;
  }

  public void get_ice_cream() {
    has_ice_cream = true;
  }

  public void get_permit() {
    has_permit = true;
  }

  public void get_hospital_ice_cream() {
    has_hospital_ice_cream = true;
  }

  public void print_obituary() {
    dead = true;
    System.out.println(name + " will never have ice cream ever again!");
  }
  
  public boolean equals(Person p) {
    return this.name.equals(p.name);
  }
}
