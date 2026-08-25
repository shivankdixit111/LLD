interface Vehicle {
    void start();
    void stop();
}

class Car implements Vehicle {
    public void start() {
        System.out.println("Car starts");
    }
    public void stop() {
        System.out.println("Car stops");
    }
}

class Bike implements Vehicle {
    public void start() {
        System.out.println("Bike starts");
    }
    public void stop() {
        System.out.println("Bike stops");
    }
}

interface VehicleFactory {
    public Vehicle createVehicle();
}

class CarFactory implements VehicleFactory {
    public Vehicle createVehicle() {
        return new Car();
    }
}
class BikeFactory implements VehicleFactory {
    public Vehicle createVehicle() {
        return new Bike();
    }
}


public class Main{
    public static void main(String args[]){ 
        VehicleFactory factory;
        factory = new CarFactory();
        Vehicle car = factory.createVehicle();
        car.start();
        car.stop();
        
        factory = new BikeFactory();
        Vehicle bike = factory.createVehicle();
        bike.start();
        bike.stop();
    }
}

/*

FACTORY METHOD DESIGN PATTERN

Intent & Definition:
---------------------
    A creational design pattern that defines an interface or abstract class for
    creating a single object, but allows subclasses to decide which concrete class
    to instantiate.
    
    It defers object creation logic to subclasses, enabling client
    code to depend on abstract contracts rather than specific concrete types.


Primary Purpose:
-----------------
    Focus on Extensibility: 
    -----------------------
        (Decouples object creation from object usage for a single product hierarchy.)
        "It lets you introduce new object types into the application without altering the 
        code that uses them (adhering to the Open-Closed Principle)."
*/

