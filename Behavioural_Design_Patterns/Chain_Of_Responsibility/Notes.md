# Chain of Responsibility Design Pattern

> [!NOTE]
> **Type:** Behavioral Pattern  
> **Core Intent:** Pass requests along a chain of handlers, allowing multiple objects a chance to handle the request without coupling the sender to a specific receiver.

---

## 📖 Definition
The Chain of Responsibility Pattern decouples the sender of a request from its receiver by chaining potential handler objects together (`Supervisor` $\rightarrow$ `Manager` $\rightarrow$ `Director`). Upon receiving a request (`processLeaveRequest`), each handler decides either to process it or pass it further down the chain to the next handler until the request is fulfilled or reaches the end of the chain.

---

## 🏗️ Key Components

| Component | Example Class | Role & Responsibility |
| :--- | :--- | :--- |
| **1. Handler Interface / Base** | `Approver` | Defines the request handling interface and maintains a reference to the successor (`nextApprover`). |
| **2. Concrete Handlers** | `Supervisor`<br>`Manager`<br>`Director` | Handle requests within their configured limits (e.g., $\le 3$ days, $\le 7$ days, $\le 14$ days); otherwise, delegate to `nextApprover`. |
| **3. Client** | `Main` | Assembles the processing chain and initiates the request by passing it to the head of the chain. |

---

## 🎯 Why Use It? (SOLID Principles & Benefits)

### i - Open/Closed Principle (OCP)
Introduce new handlers (e.g., `VicePresident`, `HRHead`) into the chain without modifying existing handler logic or breaking client code.

### ii - Single Responsibility Principle (SRP)
Decouples request invocation from request handling. Each concrete handler focuses purely on its own condition and threshold.

### iii - Loose Coupling
The client (`Main`) remains completely unaware of which specific handler ultimately fulfills the leave request—it only needs a reference to the first link.

### iv - Dynamic Chain Configuration
Handlers and execution sequences can be modified, reordered, or assigned dynamically at runtime using `setNextApprover(...)`.

---

## 📊 Architecture Diagram

```mermaid
classDiagram
    class Approver {
        <<abstract>>
        #Approver nextApprover
        +setNextApprover(Approver)
        +processLeaveRequest(int)*
    }
    class Supervisor {
        +processLeaveRequest(int)
    }
    class Manager {
        +processLeaveRequest(int)
    }
    class Director {
        +processLeaveRequest(int)
    }

    Approver <|-- Supervisor : extends
    Approver <|-- Manager : extends
    Approver <|-- Director : extends
    Approver o-- Approver : nextApprover