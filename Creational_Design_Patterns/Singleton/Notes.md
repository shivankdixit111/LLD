# Singleton Design Pattern

> [!NOTE]
> **Type:** Creational Pattern  
> **Core Intent:** Ensure a class has only one instance while providing a global point of access to that instance.

---

## 📖 Definition
The Singleton Pattern restricts the instantiation of a class to a single instance across the entire application lifetime. Using **Double-Checked Locking (DCL)** with thread-safe lazy initialization ensures that the instance is created on-demand only when first accessed, while preventing costly synchronization overhead on subsequent reads.

---

## 🏗️ Key Components

| Component | Example Element | Role & Responsibility |
| :--- | :--- | :--- |
| **1. Private Constructor** | `private Logger()` | Blocks direct instantiation from outside the class via the `new` operator. |
| **2. Static Volatile Instance** | `private static volatile Logger logger` | Holds the single global reference; `volatile` guarantees thread visibility and prevents instruction reordering. |
| **3. Global Access Method** | `getLogger()` | Provides global access and executes Double-Checked Locking (`synchronized`) to safely instantiate the object lazily. |

---

## 🎯 Why Use It? (SOLID Principles & Benefits)

### i - Controlled Single Instance
Ensures strict single-instance state management across the JVM for shared system resources (e.g., Loggers, Database Connections, Configuration Managers).

### ii - Thread Safety via Double-Checked Locking (DCL)
Protects against race conditions in multi-threaded environments by locking (`synchronized(Logger.class)`) only during initial creation.

### iii - Performance Optimization & Lazy Loading
Avoids eager loading costs at startup by instantiating the resource only when `getLogger()` is invoked for the first time.

### iv - Instruction Reordering Protection (`volatile`)
The `volatile` keyword ensures that memory allocation and object initialization finish completely before exposing the instance reference to other threads.

---

## 📊 Architecture Diagram

```mermaid
classDiagram
    class Logger {
        -volatile static Logger logger
        -Logger()
        +static getLogger() Logger
        +log(String)
    }