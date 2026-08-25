package Low_Level_Design.Behavioural_Design_Patterns.Template;

// Our abstract template that defines the skeleton of beverage preparation
abstract class Beverage {
  // The template method - makes sure the algorithm steps are followed
    public final void prepare() {
        boilWater();
        pourInCup();
        brew();
        addCondiments();
    }

    public void boilWater() {
        System.out.println("Boiling water...");
    }
    public void pourInCup() {
        System.out.println("Pouring in cup..");
    }
    abstract void brew();
    abstract void addCondiments();
}

class TeaBeverage extends Beverage {
    @Override
    public void brew() {
        System.out.println("Stepping tea bag..");
    }

    @Override
    public void addCondiments() {
        System.out.println("Adding lemon.. \n - Tea is ready! \n");
    }
}

class CoffeeBeverage extends Beverage {
    @Override
    public void brew() {
        System.out.println("Adding Coffee..");
    }

    @Override
    public void addCondiments() {
        System.out.println("Adding sugar & milk.." + "\n - Coffee is ready!");
    }
}




public class Main {
    public static void main(String[] args) {
        Beverage teaBeverage = new TeaBeverage();
        System.out.println("Preparing tea ...");
        teaBeverage.prepare();

        Beverage coffeeBeverage = new CoffeeBeverage();
         System.out.println("Preparing coffee ... ");
        coffeeBeverage.prepare();
    }
}
