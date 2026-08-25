# Visitor Design Pattern

> [!NOTE]
> **Type:** Behavioral Pattern  
> **Core Intent:** Separate an algorithm from the object structure on which it operates by placing new operations into external visitor classes.

---

## 📖 Definition
The Visitor Design Pattern allows you to add new operations or behaviors to an existing group of classes without modifying their source code. It achieves this using **Double Dispatch**: the element class accepts a visitor, and the visitor dispatches the specific operation based on the concrete type of that element.

---

## 🏗️ Key Components

| Component | Example Class | Role & Responsibility |
| :--- | :--- | :--- |
| **1. Element Interface** | `Patient` | Defines the `accept(Visitor)` contract that allows visitors to enter concrete elements. |
| **2. Concrete Elements** | `ChildPatient`<br>`AdultPatient`<br>`SeniorPatient` | Implement `accept(Visitor)` by calling back the visitor's corresponding `visit` method (`visitor.visit(this)`). |
| **3. Visitor Interface** | `Visitor` | Declares overloaded `visit(...)` signatures for every concrete element type in the hierarchy. |
| **4. Concrete Visitors** | `DiagnosisVisitor`<br>`BillingVisitor` | Encapsulate specific algorithms/operations to be performed across all concrete elements. |
| **5. Client** | `Main` | Holds a collection of elements and passes concrete visitors to execute operations dynamically. |

---

## 🎯 Why Use It? (SOLID Principles & Benefits)

### i - Open/Closed Principle (OCP)
Add brand-new operations (e.g., `DischargeVisitor` or `InsuranceVisitor`) across the entire object structure without modifying any existing `Patient` classes.

### ii - Single Responsibility Principle (SRP)
Keeps domain entity classes (`Patient`) focused strictly on data holding while delegating secondary behaviors (billing, diagnosis) to specialized visitor classes.

### iii - Double Dispatch Mechanism
Resolves method execution dynamically based on both the runtime type of the `Patient` element and the runtime type of the `Visitor`, working around single-dispatch language limits.

### iv - Centralizes Algorithm Logic
Groups related logic for multiple target objects inside a single visitor class instead of spreading disparate operational code across the entire domain model.

---

## 📊 Architecture Diagram

```mermaid
classDiagram
    class Patient {
        <<interface>>
        +accept(Visitor)*
    }
    class ChildPatient {
        +accept(Visitor)
    }
    class AdultPatient {
        +accept(Visitor)
    }
    class SeniorPatient {
        +accept(Visitor)
    }

    class Visitor {
        <<interface>>
        +visit(ChildPatient)*
        +visit(AdultPatient)*
        +visit(SeniorPatient)*
    }
    class DiagnosisVisitor {
        +visit(ChildPatient)
        +visit(AdultPatient)
        +visit(SeniorPatient)
    }
    class BillingVisitor {
        +visit(ChildPatient)
        +visit(AdultPatient)
        +visit(SeniorPatient)
    }

    Patient <|.. ChildPatient : implements
    Patient <|.. AdultPatient : implements
    Patient <|.. SeniorPatient : implements
    Visitor <|.. DiagnosisVisitor : implements
    Visitor <|.. BillingVisitor : implements
    ChildPatient ..> Visitor : accepts
    AdultPatient ..> Visitor : accepts
    SeniorPatient ..> Visitor : accepts