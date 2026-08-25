# Command Design Pattern

> [!NOTE]
> **Type:** Behavioral Pattern  
> **Core Intent:** Encapsulate a request as an object, thereby letting you parameterize clients with different requests, queue or log requests, and support undoable operations.

---

## 📖 Definition
The Command Pattern turns a request or action into a standalone object (`OnCommand`, `AdjustVolume`) that contains all details about the request. This transformation decouples the object invoking the command (**RemoteControl**) from the object that actually knows how to perform the action (**TV**).

---

## 🏗️ Key Components

| Component | Example Class | Role & Responsibility |
| :--- | :--- | :--- |
| **1. Command Interface** | `Command` | Declares the standard execution contract (`execute()`). |
| **2. Concrete Commands** | `OnCommand`<br>`OffCommand`<br>`ChangeChannel`<br>`AdjustVolume` | Bind the receiver (`TV`) with specific actions and parameters; implement `execute()` by delegating to receiver methods. |
| **3. Receiver** | `TV` | Contains the actual business logic to perform physical operations (e.g., turning on/off, changing channels). |
| **4. Invoker** | `RemoteControl` | Holds references to command objects and triggers their execution without knowing the underlying receiver logic. |

---

## 🎯 Why Use It? (SOLID Principles & Benefits)

### i - Decouples Invoker and Receiver
The `RemoteControl` (invoker) is completely detached from the `TV` (receiver). The invoker only knows how to trigger `execute()` without caring which object receives it or how it's executed.

### ii - Single Responsibility Principle (SRP)
Separates the component that triggers actions (`RemoteControl`) from the classes that define action commands and the actual execution logic (`TV`).

### iii - Open/Closed Principle (OCP)
Introduce brand-new commands (e.g., `MuteCommand`, `SetBrightnessCommand`) without modifying existing Invoker or Receiver code.

### iv - Supports Queuing, Logging & Undo/Redo
Because requests are encapsulated into objects, they can easily be stored in collections to build command queues, transaction logs, or multi-level undo/redo mechanisms.

---

## 📊 Architecture Diagram

```mermaid
classDiagram
    class Command {
        <<interface>>
        +execute()*
    }
    class OnCommand {
        -TV tv
        +execute()
    }
    class OffCommand {
        -TV tv
        +execute()
    }
    class ChangeChannel {
        -TV tv
        -int channel
        +execute()
    }
    class AdjustVolume {
        -TV tv
        -int volume
        +execute()
    }
    class TV {
        +turnOn()
        +turnOff()
        +changeChannel(int)
        +adjustVolume(int)
    }
    class RemoteControl {
        -Command OnCommand
        -Command OffCommand
        +setOnCommand(Command)
        +setOffCommand(Command)
        +pressOn()
        +pressOff()
    }

    Command <|.. OnCommand : implements
    Command <|.. OffCommand : implements
    Command <|.. ChangeChannel : implements
    Command <|.. AdjustVolume : implements
    OnCommand --> TV : delegates to
    OffCommand --> TV : delegates to
    ChangeChannel --> TV : delegates to
    AdjustVolume --> TV : delegates to
    RemoteControl o-- Command : invokes