# Template Method Design Pattern

> [!NOTE]
> **Type:** Behavioral Pattern  
> **Core Intent:** Define the skeleton of an algorithm in a base class, deferring specific steps to subclasses without changing the overall algorithm structure.

---

## 📖 Definition
The Template Method Pattern defines the sequential steps of an algorithm in an abstract base class using a `final` method. It allows subclasses to override specific step implementations (like custom brewing or adding condiments) while guaranteeing that the high-level execution order remains unchanged.

---

## 🏗️ Key Components

| Component | Example Class | Role & Responsibility |
| :--- | :--- | :--- |
| **1. Abstract Class** | `Beverage` | Defines the `final` template method (`prepare()`), implements invariant steps (`boilWater`, `pourInCup`), and declares abstract primitive operations (`brew`, `addCondiments`). |
| **2. Concrete Implementations** | `TeaBeverage`<br>`CoffeeBeverage` | Implement the abstract primitive steps to supply custom algorithmic behavior without modifying the execution sequence. |
| **3. Client** | `Main` | Invokes the high-level template method on the base class reference to execute the entire algorithm pipeline. |

---

## 🎯 Why Use It? (SOLID Principles & Benefits)

### i - Don't Repeat Yourself (DRY Principle)
Centralizes common invariant logic (e.g., boiling water, pouring in a cup) in the abstract base class, eliminating code duplication across implementations.

### ii - Open/Closed Principle (OCP)
Easily add new beverage variants (e.g., `GreenTeaBeverage`) by creating a new subclass without modifying existing base classes or client code.

### iii - Hollywood Principle ("Don't call us, we'll call you")
The high-level abstract class controls the execution flow and calls into concrete subclass operations, preventing subclasses from driving the control logic.

### iv - Enforces Algorithm Structure
Declaring the template method (`prepare()`) as `final` prevents subclasses from accidentally breaking, reordering, or overriding the overall sequence.

---

## 📊 Architecture Diagram

```mermaid
classDiagram
    class Beverage {
        <<abstract>>
        +prepare() final
        +boilWater()
        +pourInCup()
        +brew()*
        +addCondiments()*
    }
    class TeaBeverage {
        +brew()
        +addCondiments()
    }
    class CoffeeBeverage {
        +brew()
        +addCondiments()
    }

    Beverage <|-- TeaBeverage : extends
    Beverage <|-- CoffeeBeverage : extends