# Iterator Design Pattern

> [!NOTE]
> **Type:** Behavioral Pattern  
> **Core Intent:** Provide a way to access the elements of an aggregate object sequentially without exposing its underlying representation.

---

## 📖 Definition
The Iterator Pattern allows sequential access to elements of a collection (**PlayList**) without exposing its internal data structures (such as `ArrayList`). It decouples traversal algorithms (**Simple**, **Shuffled**) from the collection itself, allowing multiple traversal strategies over the exact same data without altering the collection class.

---

## 🏗️ Key Components

| Component | Example Class | Role & Responsibility |
| :--- | :--- | :--- |
| **1. Iterator Interface** | `Iterator` | Declares the standard contract for accessing and traversing elements (`hasNext()`, `next()`). |
| **2. Concrete Iterators** | `SimplePlayListIterator`<br>`ShuffledPlayListIterator` | Implement specific traversal algorithms (in-order vs. shuffled) while keeping track of current iteration state (`index`). |
| **3. Aggregate / Collection** | `PlayList` | Stores the underlying data (`songs`) and provides a factory method (`iterator(type)`) to instantiate the requested iterator. |

---

## 🎯 Why Use It? (SOLID Principles & Benefits)

### i - Single Responsibility Principle (SRP)
Extracts complex iteration and traversal logic out of the aggregate class (`PlayList`) into dedicated iterator classes.

### ii - Open/Closed Principle (OCP)
Add new iteration strategies (e.g., `ReversePlayListIterator`, `GenreFilteredIterator`) without modifying existing collection or client code.

### iii - Encapsulation & Information Hiding
Hides the internal data storage structure (e.g., whether `songs` is an `ArrayList`, `LinkedList`, or `Set`) completely from the client.

### iv - Uniform Traversal Interface
Provides a unified, standard interface (`hasNext()`, `next()`) so client code can iterate over various structures or algorithms seamlessly.

---

## 📊 Architecture Diagram

```mermaid
classDiagram
    class Iterator {
        <<interface>>
        +hasNext()* boolean
        +next()* String
    }
    class SimplePlayListIterator {
        -PlayList playlist
        -int index
        +hasNext() boolean
        +next() String
    }
    class ShuffledPlayListIterator {
        -PlayList playlist
        -int index
        -List~String~ shuffledSongs
        +hasNext() boolean
        +next() String
    }
    class PlayList {
        -List~String~ songs
        +addSongs(String)
        +iterator(String) Iterator
        +getSongs() List~String~
    }

    Iterator <|.. SimplePlayListIterator : implements
    Iterator <|.. ShuffledPlayListIterator : implements
    PlayList ..> Iterator : creates
    SimplePlayListIterator --> PlayList : traverses
    ShuffledPlayListIterator --> PlayList : traverses