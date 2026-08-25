# Strategy Design Pattern

> [!NOTE]
> **Type:** Behavioral Pattern  
> **Core Intent:** Encapsulate a family of algorithms into separate classes and make them interchangeable at runtime.

---

## 📖 Definition
Defines a family of algorithms, encapsulates each one into a separate class, and makes them completely interchangeable at runtime. It enables the client (**Context**) to alter its behavior dynamically without modifying its underlying code.

---

## 🏗️ Key Components

| Component | Example Class | Role & Responsibility |
| :--- | :--- | :--- |
| **1. Strategy Interface** | `PaymentStrategy` | Defines the common contract/method signature for all concrete algorithms. |
| **2. Concrete Strategies** | `CreditCardPaymentStrategy`<br>`PayPalPaymentStrategy`<br>`StripePaymentStrategy` | Implement specific algorithmic logic adhering strictly to the Strategy Interface. |
| **3. Context** | `PaymentProcessor` | Maintains a reference to a Strategy instance and delegates execution to it instead of implementing the logic itself. |

---

## 📊 Architecture Diagram

```mermaid
classDiagram
    class PaymentProcessor {
        -PaymentStrategy paymentStrategy
        +processPayment()
        +setPaymentStrategy(PaymentStrategy)
    }
    class PaymentStrategy {
        <<interface>>
        +processPayment()*
    }
    class CreditCardPaymentStrategy {
        +processPayment()
    }
    class PayPalPaymentStrategy {
        +processPayment()
    }
    class StripePaymentStrategy {
        +processPayment()
    }

    PaymentProcessor --> PaymentStrategy : delegates to
    PaymentStrategy <|.. CreditCardPaymentStrategy : implements
    PaymentStrategy <|.. PayPalPaymentStrategy : implements
    PaymentStrategy <|.. StripePaymentStrategy : implements