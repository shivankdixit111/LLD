# Observer Design Pattern

> [!NOTE]
> **Type:** Behavioral Pattern  
> **Core Intent:** Define a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.

---

## 📖 Definition
The Observer Pattern establishes a subscription mechanism allowing multiple observer objects (**Subscribers**) to listen for state changes published by a subject object (**YouTubeChannel**). When an event occurs (e.g., uploading a new video), the subject automatically notifies all registered observers without coupling itself to their specific concrete implementations.

*(Note: This implementation hybridizes **Observer** with **Strategy** by embedding `NotificationChannel` strategies inside each subscriber).*

---

## 🏗️ Key Components

| Component | Example Class | Role & Responsibility |
| :--- | :--- | :--- |
| **1. Subject Interface** | `YouTubeChannel` | Defines the contract for registering, removing, and notifying observers. |
| **2. Concrete Subject** | `YoutubeChannelImp` | Stores state (`video`) and observer references; triggers `notifySubscribers()` when state changes. |
| **3. Observer Interface** | `Subscriber` | Defines the standard `update(String video)` method contract invoked by the subject. |
| **4. Concrete Observer** | `YouTubeSubscriber` | Implements `update(...)` to receive event notifications and delegates execution to configured notification channels. |
| **5. Strategy Extension** | `NotificationChannel`<br>`EmailNotification`<br>`PushNotification` | Encapsulates pluggable notification strategies (Email, Push) that subscribers use to process updates. |

---

## 🎯 Why Use It? (SOLID Principles & Benefits)

### i - Open/Closed Principle (OCP)
Add new subscriber types or notification strategies (e.g., SMS, WhatsApp) without modifying existing subject classes or client code.

### ii - Loose Coupling (SRP)
The Subject maintains zero knowledge of subscribers' internal concrete classes, keeping event generation completely isolated from event consumption.

### iii - One-to-Many Push Architecture
Replaces inefficient polling mechanisms by establishing an automated broadcast pipeline to push state changes to multiple listeners in real time.

### iv - Runtime Flexibility (Pattern Hybrid)
Combining Observer with Strategy allows observers to dynamically swap out or combine notification channels (Email, Push) on the fly without breaking core event handling.

---

## 📊 Architecture Diagram

```mermaid
classDiagram
    class YouTubeChannel {
        <<interface>>
        +addSubscriber(Subscriber)*
        +removeSubscriber(Subscriber)*
        +notifySubscribers()*
    }
    class YoutubeChannelImp {
        -List~Subscriber~ subscribers
        -String video
        +addSubscriber(Subscriber)
        +removeSubscriber(Subscriber)
        +notifySubscribers()
        +uploadNewVideo(String)
    }
    class Subscriber {
        <<interface>>
        +update(String)*
    }
    class YouTubeSubscriber {
        -String name
        -List~NotificationChannel~ channels
        +addNotificationChannel(NotificationChannel)
        +update(String)
    }
    class NotificationChannel {
        <<interface>>
        +notifySubscriber(String)*
    }
    class EmailNotification {
        +String email
        +notifySubscriber(String)
    }
    class PushNotification {
        +String deviceToken
        +notifySubscriber(String)
    }

    YouTubeChannel <|.. YoutubeChannelImp : implements
    Subscriber <|.. YouTubeSubscriber : implements
    NotificationChannel <|.. EmailNotification : implements
    NotificationChannel <|.. PushNotification : implements
    YoutubeChannelImp "1" o-- "*" Subscriber : notifies
    YouTubeSubscriber "1" o-- "*" NotificationChannel : delegates to