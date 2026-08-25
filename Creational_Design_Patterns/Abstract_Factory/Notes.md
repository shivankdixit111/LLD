# Abstract Factory Design Pattern

> [!NOTE]
> **Type:** Creational Pattern  
> **Core Intent:** Provide an interface for creating families of related or dependent objects without specifying their concrete classes.

---

## 📖 Definition
The Abstract Factory Pattern provides an interface (`GUIFactory`) for creating families of related or dependent objects (`Button`, `CheckBox`) without tying client code (`Application`) to concrete classes. It guarantees that product variants belonging to the same family (e.g., macOS vs. Windows) are always instantiated and used consistently together.

---

## 🏗️ Key Components

| Component | Example Class | Role & Responsibility |
| :--- | :--- | :--- |
| **1. Abstract Products** | `Button`<br>`CheckBox` | Declare common contracts for distinct product types in the product hierarchy (`paint()`, `render()`). |
| **2. Concrete Products** | `MacButton`, `MacCheckBox`<br>`WinButton`, `WinCheckBox` | Implement abstract product interfaces to supply platform-specific UI rendering behavior. |
| **3. Abstract Factory** | `GUIFactory` | Declares creation methods for every abstract product type in the family (`createButton()`, `createCheckBox()`). |
| **4. Concrete Factories** | `MacGUIFactory`<br>`WinGUIFactory` | Implement creation methods to instantiate and return matching concrete product variants belonging to a specific family. |
| **5. Client** | `Application` | Operates strictly via abstract factory and product interfaces, completely isolated from concrete class types. |

---

## 🎯 Why Use It? (SOLID Principles & Benefits)

### i - Enforces Product Family Consistency
Guarantees that products instantiated from the same factory belong to the exact same theme or platform variant (e.g., preventing mixing `MacButton` with `WinCheckBox`).

### ii - Open/Closed Principle (OCP)
Introduce new product families (e.g., `LinuxGUIFactory`, `LinuxButton`, `LinuxCheckBox`) without modifying existing client code or concrete factories.

### iii - Single Responsibility Principle (SRP)
Extracts multi-product construction logic away from business logic into dedicated, specialized factory classes.

### iv - Loose Coupling
The client (`Application`) depends exclusively on abstract interfaces (`GUIFactory`, `Button`, `CheckBox`), ensuring complete decoupling from platform-specific implementations.

---

## 📊 Architecture Diagram

```mermaid
classDiagram
    class Button {
        <<interface>>
        +paint()*
    }
    class CheckBox {
        <<interface>>
        +render()*
    }

    class MacButton {
        +paint()
    }
    class MacCheckBox {
        +render()
    }
    class WinButton {
        +paint()
    }
    class WinCheckBox {
        +render()
    }

    class GUIFactory {
        <<interface>>
        +createButton()* Button
        +createCheckBox()* CheckBox
    }
    class MacGUIFactory {
        +createButton() Button
        +createCheckBox() CheckBox
    }
    class WinGUIFactory {
        +createButton() Button
        +createCheckBox() CheckBox
    }

    class Application {
        -Button button
        -CheckBox checkBox
        +render()
    }

    Button <|.. MacButton : implements
    Button <|.. WinButton : implements
    CheckBox <|.. MacCheckBox : implements
    CheckBox <|.. WinCheckBox : implements

    GUIFactory <|.. MacGUIFactory : implements
    GUIFactory <|.. WinGUIFactory : implements

    MacGUIFactory ..> MacButton : creates
    MacGUIFactory ..> MacCheckBox : creates
    WinGUIFactory ..> WinButton : creates
    WinGUIFactory ..> WinCheckBox : creates

    Application o-- GUIFactory : uses
    Application o-- Button : uses
    Application o-- CheckBox : uses