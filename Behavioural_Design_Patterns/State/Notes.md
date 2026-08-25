# State Design Pattern

> [!NOTE]
> **Type:** Behavioral Pattern  
> **Core Intent:** Allow an object to alter its behavior when its internal state changes, appearing to change its class.

---

## 📖 Definition
The State Pattern allows an object (**Context**) to change its behavior dynamically at runtime based on its internal state. Instead of maintaining large `if-else` or `switch` statements to check the state, each state's behavior is encapsulated into its own dedicated class.

---

## 🏗️ Key Components

| Component | Example Class | Role & Responsibility |
| :--- | :--- | :--- |
| **1. State Interface** | `TrafficLightState` | Defines the common contract for state-dependent behaviors (`next`, `getColor`). |
| **2. Concrete States** | `RedLightState`<br>`GreenLightState`<br>`YellowLightState` | Encapsulate state-specific behavior and execute state transitions on the context. |
| **3. Context** | `TrafficLightContext` | Maintains a reference to the current state object and delegates state-dependent requests to it. |

---

## 🎯 Why Use It? (SOLID Principles & Benefits)

### i - Open/Closed Principle (OCP)
Add new states (e.g., `FlashingYellowState`) without modifying existing state classes or client logic.

### ii - Single Responsibility Principle (SRP)
Isolates the logic for individual states into separate classes, making each state easier to maintain and test.

### iii - Eliminates State Conditionals
Replaces cluttered `if (state == RED)` or `switch` statements with clean polymorphic method invocation.

### iv - Explicit State Transitions
Makes state transitions explicit and self-contained inside state classes, preventing invalid or inconsistent state combinations.

---

## 📊 Architecture Diagram

```mermaid
classDiagram
    class TrafficLightContext {
        -TrafficLightState currentState
        +setState(TrafficLightState)
        +next()
    }
    class TrafficLightState {
        <<interface>>
        +next(TrafficLightContext)*
        +getColor()* String
    }
    class RedLightState {
        +next(TrafficLightContext)
        +getColor() String
    }
    class GreenLightState {
        +next(TrafficLightContext)
        +getColor() String
    }
    class YellowLightState {
        +next(TrafficLightContext)
        +getColor() String
    }

    TrafficLightContext --> TrafficLightState : delegates to
    TrafficLightState <|.. RedLightState : implements
    TrafficLightState <|.. GreenLightState : implements
    TrafficLightState <|.. YellowLightState : implements