# Prototype Design Pattern

> [!NOTE]
> **Type:** Creational Pattern  
> **Core Intent:** Create new objects by cloning an existing prototype instance rather than instantiating them from scratch.

---

## 📖 Definition
The Prototype Pattern enables creating new objects by copying existing pre-configured instances (**Character**) instead of constructing them from scratch via class constructors. By leveraging Java's built-in `Cloneable` mechanism (`super.clone()`), it allows clients to generate customized object variants efficiently at runtime.

---

## 🏗️ Key Components

| Component | Example Class / Element | Role & Responsibility |
| :--- | :--- | :--- |
| **1. Prototype Interface** | `Cloneable` | Java marker interface that grants JVM permission to perform field-for-field cloning on the class. |
| **2. Concrete Prototype** | `Character` | Implements `clone()` using `super.clone()` to produce shallow copies of its primitive and reference fields. |
| **3. Prototype Factory** | `CharacterFactory` | Encapsulates a master `prototype` instance and exposes creation methods that clone and tweak specific properties. |
| **4. Client** | `Main` | Requests customized object copies from the factory without needing direct constructor parameter knowledge. |

---

## 🎯 Why Use It? (SOLID Principles & Benefits)

### i - Performance Optimization
Avoids costly constructor calls, database queries, or complex initialization logic by duplicating existing memory blocks directly using JVM native cloning.

### ii - Reduces Class Subclassing
Eliminates the need for extensive class hierarchies (e.g., `AliceCharacter`, `KnightCharacter`) by maintaining reusable, pre-configured prototype templates.

### iii - Open/Closed Principle (OCP)
Introduce new prototype variants or initial states without modifying existing factory code or client logic.

### iv - Dynamic Runtime Configuration
Allows client code to dynamically instantiate pre-configured object presets on the fly and override specific fields as needed.

---

## 📊 Architecture Diagram

```mermaid
classDiagram
    class Cloneable {
        <<interface>>
    }
    class Character {
        +String name
        +int health
        +int attackPower
        +int level
        +clone() Character
        +showInfo() String
    }
    class CharacterFactory {
        -Character prototype
        +createCharacterWithNewName(String) Character
        +createCharacterWithNewAttackPower(int) Character
    }

    Cloneable <|.. Character : implements
    CharacterFactory o-- Character : holds prototype