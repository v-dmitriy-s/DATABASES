# 🧩 User Story: Seamless Shopping Experience on an AI-Powered E-Commerce Platform

### As an

**online shopper** seeking a personalized, intuitive, and fast experience,

### I want to

**browse flexible product listings, receive personalized recommendations, manage my cart in real-time, and checkout instantly**

### So that

**I can enjoy a convenient and intelligent shopping journey that feels tailor-made for me.**

---

## 🛠️ Microservice Architecture Overview

```mermaid
graph TD
  subgraph "Frontend (UI)"
    UI[User Interface]
  end

  subgraph "Microservices"
    OrderService(Order Service\nPostgreSQL)
    InventoryService(Inventory Service\nDynamoDB)
    CartService(Cart Service\nRedis)
    CatalogService(Product Catalog Service\nMongoDB)
    ActivityService(User Activity Service\nCassandra)
    RecommendationService(Recommendation Service\nPinecone)
  end

  UI --> CatalogService
  UI --> RecommendationService
  UI --> CartService
  UI --> OrderService
  UI --> InventoryService
  UI --> ActivityService

  CartService --> InventoryService
  CartService --> CatalogService
  CartService --> Redis[Redis]
  OrderService --> InventoryService
  OrderService --> PostgreSQL[PostgreSQL]
  OrderService --> CartService
  OrderService --> ActivityService
  InventoryService --> DynamoDB[DynamoDB]
  CatalogService --> MongoDB[MongoDB]
  ActivityService --> Cassandra[Cassandra]
  RecommendationService --> Pinecone[Pinecone]
  RecommendationService --> ActivityService
```

---

## 🔍 Microservices & Their Purpose

### 🛒 **Order Service** (PostgreSQL)

* Handles placing and tracking of user orders.
* Supports address, payment, item list, and total.
* ACID-compliant and relational for strong consistency.
* Interacts with Cart, Inventory, and User Activity.

---

### 📦 **Inventory Service** (DynamoDB)

* Manages real-time product stock per region/location.
* Designed for high throughput and low-latency writes.
* Enables instant stock checks and updates after purchases.

---

### ⚡ **Cart Service** (Redis)

* Manages temporary user shopping carts.
* In-memory store with TTL (30 min expiration).
* Ultra-fast access and real-time updates.

---

### 📚 **Product Catalog Service** (MongoDB)

* Stores flexible product data: names, specs, variants, categories.
* Schema-less design for evolving product structures.
* Powers all product listings and detail pages.

---

### 📈 **User Activity Service** (Cassandra)

* Logs user interactions: clicks, views, navigation.
* Built for append-only high-volume write scenarios.
* Used for analytics and feedback to recommendation engine.

---

### 🧠 **Recommendation Service** (Pinecone)

* Provides vector-based product similarity search.
* Uses OpenAI embeddings to compute product vectors.
* Returns personalized results based on user behavior.

---

## 🔄 UI → API Interaction (Mermaid Diagrams)

### 🏠 Home Page

```mermaid
sequenceDiagram
  participant UI as HomePage.tsx
  participant Catalog as Product Catalog
  participant Rec as Recommendation
  participant Activity as User Activity

  UI->>Catalog: GET /api/trending
  UI->>Rec: GET /api/recommendations
  UI->>Activity: POST /api/view-event
```

---

### 🛍️ Product Listing Page

```mermaid
sequenceDiagram
  participant UI as CategoryPage.tsx
  participant Catalog as Product Catalog
  participant Inventory as Inventory

  UI->>Catalog: GET /api/products?category=shoes
  UI->>Inventory: GET /api/stock?productIds=...
```

---

### 📄 Product Detail Page

```mermaid
sequenceDiagram
  participant UI as ProductPage.tsx
  participant Catalog as Product Catalog
  participant Cart as Cart Service
  participant Inventory as Inventory
  participant Rec as Recommendation
  participant Activity as User Activity

  UI->>Catalog: GET /api/products/:id
  UI->>Inventory: GET /api/stock/:productId
  UI->>Cart: POST /api/cart/add
  UI->>Rec: GET /api/similar?productId=...
  UI->>Activity: POST /api/view-event
```

---

### 🛒 Cart Page

```mermaid
sequenceDiagram
  participant UI as CartPage.tsx
  participant Cart as Cart Service
  participant Inventory as Inventory Service
  participant Catalog as Product Catalog

  UI->>Cart: GET /api/cart
  UI->>Inventory: GET /api/stock?productIds=...
  UI->>Catalog: GET /api/products?ids=...
```

---

### 💳 Checkout Page

```mermaid
sequenceDiagram
  participant UI as CheckoutPage.tsx
  participant Cart as Cart Service
  participant Order as Order Service
  participant Inventory as Inventory Service

  UI->>Cart: GET /api/cart
  UI->>Order: POST /api/orders
  Order->>Inventory: PATCH /api/stock/decrement
  Order->>Cart: DELETE /api/cart
```

---

### 🧑 My Orders Page

```mermaid
sequenceDiagram
  participant UI as OrdersPage.tsx
  participant Order as Order Service

  UI->>Order: GET /api/orders (X-User-Email)
```

---

### 🧠 Recommendation Panel

```mermaid
sequenceDiagram
  participant UI as Sidebar.tsx
  participant Rec as Recommendation Service
  participant Activity as User Activity Service

  UI->>Rec: GET /api/recommendations/contextual
  UI->>Activity: POST /api/click
```

---

## 📦 Microservices and Their Databases (Quick Reference)

| Microservice            | Database   | Description                                                  |
| ----------------------- | ---------- | ------------------------------------------------------------ |
| Order Service           | PostgreSQL | Transactions, relationships (users, products, payments)      |
| Inventory Service       | DynamoDB   | Regional stock management, fast writes                       |
| Cart Service            | Redis      | Temporary cart storage with TTL                              |
| Product Catalog Service | MongoDB    | Flexible, nested product structures                          |
| User Activity Service   | Cassandra  | Time-series logs, clickstream, behavior tracking             |
| Recommendation Service  | Pinecone   | Vector search via AI embeddings for personalized suggestions |


