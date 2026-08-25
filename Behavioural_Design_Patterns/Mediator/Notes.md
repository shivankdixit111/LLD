# Mediator Design Pattern

> [!NOTE]
> **Type:** Behavioral Pattern  
> **Core Intent:** Reduce direct dependencies and tight coupling between objects by forcing them to communicate exclusively through a central mediator object.

---

## 📖 Definition
The Mediator Pattern encapsulates how a set of objects (**Bidders**) interact by routing all communication through a central coordinator (**AuctionHouse**). Instead of objects holding direct references to one another and creating a complex mesh network ($O(N^2)$ connections), they only hold a reference to the mediator ($O(N)$ connections), keeping components loosely coupled and easily reusable.

---

## 🏗️ Key Components

| Component | Example Class | Role & Responsibility |
| :--- | :--- | :--- |
| **1. Mediator Interface** | `AuctionMediator` | Declares the contract for registering components and broadcasting events (`registerBidder`, `placeBid`). |
| **2. Concrete Mediator** | `AuctionHouse` | Maintains references to all colleagues (`List<Bidder>`) and orchestrates multi-way interactions when a bid is placed. |
| **3. Colleague Class** | `Bidder` | Represents an individual participant. Communicates strictly with the mediator rather than notifying rival bidders directly. |

---

## 🎯 Why Use It? (SOLID Principles & Benefits)

### i - Loose Coupling
Eliminates direct references between colleagues. Bidders don't need to know who or how many other bidders exist in the auction.

### ii - Single Responsibility Principle (SRP)
Centralizes complex interaction, routing, and notification logic inside a single class (`AuctionHouse`), leaving individual domain entities clean.

### iii - Open/Closed Principle (OCP)
Introduce new concrete mediators (e.g., `SilentAuctionHouse`) or new colleague types without modifying existing colleague code.

### iv - Hub-and-Spoke (Star) Topology
Transforms a tangled, hard-to-maintain many-to-many mesh interaction into a clean, centralized star architecture.

---

## 📊 Architecture Diagram

```mermaid
classDiagram
    class AuctionMediator {
        <<interface>>
        +registerBidder(Bidder)*
        +placeBid(Bidder, int)*
    }
    class AuctionHouse {
        -List~Bidder~ bidders
        +registerBidder(Bidder)
        +placeBid(Bidder, int)
    }
    class Bidder {
        -String name
        -AuctionMediator auctionMediator
        +placeBid(int)
        +receiveBid(Bidder, int)
        +getName() String
        +setAuctionHouse(AuctionMediator)
    }

    AuctionMediator <|.. AuctionHouse : implements
    AuctionHouse "1" o-- "*" Bidder : manages & notifies
    Bidder --> AuctionMediator : communicates via