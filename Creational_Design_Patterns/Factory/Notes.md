# Factory Method Design Pattern

> [!NOTE]
> **Type:** Creational Pattern  
> **Core Intent:** Define an interface for creating an object, but let subclasses decide which concrete class to instantiate.
> **Goal:** The main goal of a Factory is to hide the creation logic of complex objects (and their dependencies) from the client (Main).

---

## 📖 Definition
The Factory Method Pattern defines an interface or abstract class for creating a single product object, while deferring the actual instantiation logic to concrete subclass factories (`CarFactory`, `BikeFactory`). This enables client code (`Main`) to depend entirely on abstract contracts (`Vehicle`, `VehicleFactory`) rather than concrete product classes.

---

## 🏗️ Key Components

| Component | Example Class | Role & Responsibility |
| :--- | :--- | :--- |
| **1. Product Interface** | `Vehicle` | Declares the common interface for all created objects (`start()`, `stop()`). |
| **2. Concrete Products** | `Car`<br>`Bike` | Implement the Product interface to provide concrete operational behavior. |
| **3. Creator Interface** | `VehicleFactory` | Declares the factory method (`createVehicle()`) that returns a `Vehicle` interface reference. |
| **4. Concrete Creators** | `CarFactory`<br>`BikeFactory` | Override the factory method to instantiate and return specific concrete product objects. |

---

## 🎯 Why Use It? (SOLID Principles & Benefits)

### i - Open/Closed Principle (OCP)
Introduce new product types (e.g., `Truck`, `TruckFactory`) into the application without altering existing client code or breaking existing factories.

### ii - Single Responsibility Principle (SRP)
Extracts product creation code into dedicated creator classes, separating object construction logic from operational usage logic.

### iii - Loose Coupling
Decouples client code (`Main`) from concrete product classes. The client relies strictly on abstract interfaces (`Vehicle`, `VehicleFactory`).

### iv - Subclass Autonomy
Allows subclasses to take ownership of object instantiation decisions, offering fine-grained control over which specific variant is created.

---

## 📊 Architecture Diagram

```mermaid
classDiagram
    class Vehicle {
        <<interface>>
        +start()*
        +stop()*
    }
    class Car {
        +start()
        +stop()
    }
    class Bike {
        +start()
        +stop()
    }

    class VehicleFactory {
        <<interface>>
        +createVehicle()* Vehicle
    }
    class CarFactory {
        +createVehicle() Vehicle
    }
    class BikeFactory {
        +createVehicle() Vehicle
    }

    Vehicle <|.. Car : implements
    Vehicle <|.. Bike : implements
    VehicleFactory <|.. CarFactory : implements
    VehicleFactory <|.. BikeFactory : implements
    CarFactory ..> Car : creates
    BikeFactory ..> Bike : creates