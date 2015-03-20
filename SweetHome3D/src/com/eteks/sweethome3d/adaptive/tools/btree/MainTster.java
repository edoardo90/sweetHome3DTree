package com.eteks.sweethome3d.adaptive.tools.btree;

public class MainTster {

  public static void main(String [] args)
  {
    MainTster mt = new MainTster();
    mt.x();
  }
  
  public void x()
  {
    
    BTree<Dog> dogs = new BTree<Dog>();
    
    dogs.insert(new Dog("Rex"));
    dogs.insert(new Dog("Fido"));
    dogs.insert(new Dog("Bob"));
    dogs.insert(new Dog("T-Rex"));
    
    dogs.toString();
    
    Dog rex = dogs.getNode(new Dog("REX"));
    System.out.println(rex);
    
    Dog nodob = dogs.getNode(new Dog("Lassie"));
   
    System.out.println(nodob);
    
    dogs.delete(new Dog("REX"));
    
    System.out.println(" All dogs: \n" + dogs);
    
    System.out.println(" Now I add ReX\n");
    dogs.insert(new Dog("ReX"));
    
    System.out.println(" All dogs: \n" + dogs);
    
    
  }
  
  public class Dog implements Comparable<Dog>
  {
    private String name;
    
    public Dog(String name)
    {
      this.name = name;
    }
    
    @Override
    public String toString()
    {
      return this.name + ", the dog!";
    }
    
    public int compareTo(Dog o) {
     
      return this.name.toUpperCase().compareTo(o.name.toUpperCase());
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + ((this.name == null)
          ? 0
          : this.name.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Dog other = (Dog)obj;
      if (!getOuterType().equals(other.getOuterType()))
        return false;
      if (this.name == null) {
        if (other.name != null)
          return false;
      } else if (!this.name.toUpperCase().equals(other.name.toUpperCase()))
        return false;
      return true;
    }

    private MainTster getOuterType() {
      return MainTster.this;
    }
    
    
    
    
  }
  

}
