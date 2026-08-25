# Builder Design Pattern

> [!NOTE]
> **Type:** Creational Pattern  
> **Core Intent:** Separate the construction of a complex object from its representation so that the same construction process can create different representations step-by-step.

---

## 📖 Definition
The Builder Pattern provides a step-by-step approach to construct complex objects (**Car**) using a static inner builder class (`Car.Builder`). It avoids telescoping constructor anti-patterns and parameter clutter by exposing a fluent method-chaining API while enforcing object immutability through a `private` constructor in the target product class.

---

## 🏗️ Key Components

| Component | Example Class / Element | Role & Responsibility |
| :--- | :--- | :--- |
| **1. Product** | `Car` | The complex target object featuring private fields, getter methods, and a private constructor taking the builder. |
| **2. Concrete Builder** | `Car.Builder` | Static inner class containing matching fields with default values and fluent setter methods returning `this`. |
| **3. Build Method** | `build()` | Method inside the builder that passes the builder instance into `Car`'s private constructor to instantiate the final product. |

---

## 🎯 Why Use It? (SOLID Principles & Benefits)

### i - Eliminates Telescoping Constructors
Avoids messy, error-prone constructor overloading with long parameter lists (e.g., `new Car("V8", 4, "White", false, true)`).

### ii - Method Chaining & Readable Syntax (Fluent API)
Provides a clean, self-documenting syntax where method names clearly indicate which optional or required parameters are being set.

### iii - Encapsulation & Immutability
Keeps the product constructor `private`, ensuring that the object is fully configured before instantiation and cannot be left in an inconsistent state.

### iv - Single Responsibility Principle (SRP)
Separates complex creation and configuration logic from the core domain operations of the product class.

---

## 📊 Architecture Diagram

```mermaid
classDiagram
    class Car {
        -String engine
        -int seats
        -String color
        -boolean sunroof
        -boolean navigationSystem
        -Car(Builder builder)
        +getEngine() String
        +getSeats() int
        +getColor() String
        +getSunroof() boolean
        +getNavigationSystem() boolean
        +getDetails() String
    }
    class Builder {
        -String engine
        -int seats
        -String color
        -boolean sunroof
        -boolean navigationSystem
        +setEngine(String) Builder
        +setSeats(int) Builder
        +setColor(String) Builder
        +setSunroof(boolean) Builder
        +setNavigationSystem(boolean) Builder
        +build() Car
    }

    Car +-- Builder : static inner class
    Builder ..> Car : creates