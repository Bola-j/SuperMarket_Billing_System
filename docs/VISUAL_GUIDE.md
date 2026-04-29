# Supermarket POS System - Visual Documentation

## Quick Reference Guide

### Database Schema (ER Diagram)

```
┌─────────────────────────┐
│      PRODUCTS           │
├─────────────────────────┤
│ PK  id                  │
│     name                │
│     category            │
│ UK  barcode             │
│     purchase_price      │
│     selling_price       │
│     quantity            │
└─────────────┬───────────┘
              │ (1:N)
              │
         ┌────┴─────┐
         │           │
         ▼           ▼
    ┌───────────────────────┐
    │   SALE_ITEMS (JT)     │
    ├───────────────────────┤
    │ PK  id                │
    │ FK  sale_id    ◄──────┼─────┐
    │ FK  product_id ◄──────┼─┐   │
    │     quantity          │ │   │
    │     price_at_sale     │ │   │
    └───────────────────────┘ │   │
         ▲                     │   │
         │                     │   │
         └─────────────────────┘   │
                (1:N)              │
                                   │
                            ┌──────┴────────┐
                            │     SALES     │
                            ├───────────────┤
                            │ PK  id        │
                            │     datetime  │
                            │ total_amount  │
                            │ total_profit  │
                            └───────────────┘
```

---

### Model Classes (Class Diagram)

```
┌────────────────────────────────┐
│         Product                │
├────────────────────────────────┤
│ - id: int                      │
│ - name: String                 │
│ - category: String             │
│ - barcode: String              │
│ - purchasePrice: double        │
│ - sellingPrice: double         │
│ - quantity: int                │
├────────────────────────────────┤
│ + getId/setId()                │
│ + getName/setName()            │
│ + getCategory/setCategory()    │
│ + getBarcode/setBarcode()      │
│ + getPurchasePrice/...()       │
│ + getSellingPrice/...()        │
│ + getQuantity/setQuantity()    │
│ + toString()                   │
└────────────────────────────────┘
           ▲
           │ (references)
           │
┌──────────┴──────────────────────┐
│         CartItem                │
├─────────────────────────────────┤
│ - product: Product              │
│ - quantity: int                 │
├─────────────────────────────────┤
│ + getProduct/setProduct()       │
│ + getQuantity/setQuantity()     │
│ + getSubtotal(): double         │
│ + toString()                    │
└─────────────────────────────────┘
           ▲
           │ (List)
           │
┌──────────┴──────────────────────┐
│          Sale                   │
├─────────────────────────────────┤
│ - id: int                       │
│ - datetime: LocalDateTime       │
│ - items: List<CartItem>         │
│ - totalAmount: double           │
│ - totalProfit: double           │
├─────────────────────────────────┤
│ + getId/setId()                 │
│ + getDatetime/setDatetime()     │
│ + getItems/setItems()           │
│ + addItem(CartItem)             │
│ + removeItem(CartItem)          │
│ + getTotalAmount/...()          │
│ + getTotalProfit/...()          │
│ + toString()                    │
└─────────────────────────────────┘
```

---

### Architecture Layers (MVC Pattern)

```
┌─────────────────────────────────────────────────────┐
│             VIEW LAYER (JavaFX)                     │
│  - Stage, Scene, Button, TextField, TableView      │
│  - BaseView (Abstract)                              │
└────────────────┬──────────────────────────────────┘
                 │ User Input/Events
                 ▼
┌─────────────────────────────────────────────────────┐
│          CONTROLLER LAYER                           │
│  - BaseController (Abstract)                        │
│  - ProductController, SaleController, etc.          │
│  - Delegates to Services                            │
└────────────────┬──────────────────────────────────┘
                 │ Method Calls
                 ▼
┌─────────────────────────────────────────────────────┐
│          SERVICE LAYER                              │
│  - BaseService (Abstract)                           │
│  - ProductService, SaleService, etc.                │
│  - Business Logic & Calculations                    │
└────────────────┬──────────────────────────────────┘
                 │ CRUD Operations
                 ▼
┌─────────────────────────────────────────────────────┐
│          DAO LAYER                                  │
│  - BaseDAO (Abstract)                               │
│  - ProductDAO, SaleDAO, SaleItemDAO                 │
│  - Database Operations (CRUD)                       │
└────────────────┬──────────────────────────────────┘
                 │ SQL Queries
                 ▼
┌─────────────────────────────────────────────────────┐
│        MODEL LAYER                                  │
│  - Product, CartItem, Sale                          │
│  - Pure Data Containers (No Logic)                  │
└────────────────┬──────────────────────────────────┘
                 │ Row Mapping
                 ▼
┌─────────────────────────────────────────────────────┐
│      DATABASE LAYER (SQLite)                        │
│  - DatabaseManager (JDBC Connection)                │
│  - supermarket_pos.db                               │
└─────────────────────────────────────────────────────┘
```

---

### Project Directory Structure

```
SuperMarket_Billing_System/
│
├── pom.xml                                 Maven Build Config
│
├── README.md                               Project Overview
│
├── supermarket_pos.db                      SQLite Database File
│
├── docs/
│   ├── DATABASE_SCHEMA.md                  DB ER Diagram & Tables
│   ├── MODEL_CLASSES.md                    Class Definitions
│   └── ARCHITECTURE.md                     MVC Architecture
│
└── src/
    └── main/
        ├── java/com/supermarket/pos/
        │   ├── Main.java                   Entry Point
        │   │
        │   ├── model/
        │   │   ├── Product.java
        │   │   ├── CartItem.java
        │   │   └── Sale.java
        │   │
        │   ├── dao/
        │   │   └── BaseDAO.java            (To Implement)
        │   │
        │   ├── service/
        │   │   └── BaseService.java        (To Implement)
        │   │
        │   ├── controller/
        │   │   └── BaseController.java     (To Implement)
        │   │
        │   ├── view/
        │   │   └── BaseView.java           (To Implement)
        │   │
        │   └── util/
        │       ├── DatabaseManager.java
        │       └── DatabaseTest.java
        │
        └── resources/                       (Styling & Assets)
```

---

### Technology Stack

| Layer | Technology |
|-------|-----------|
| GUI | JavaFX 22 |
| Backend | Java 17 |
| Database | SQLite 3 |
| Database Driver | JDBC (xerial/sqlite-jdbc) |
| Build Tool | Maven 3.6+ |
| Architecture | MVC Pattern |

---

### Key Design Patterns

1. **MVC (Model-View-Controller)**
   - Separation of concerns
   - Independent layer testing

2. **DAO (Data Access Object)**
   - Abstract database operations
   - Easy database swapping

3. **Service Pattern**
   - Business logic isolation
   - Reusable components

4. **Singleton Pattern** (DatabaseManager)
   - Single connection instance
   - Resource management

---

### Database Relationships

**One-to-Many (1:N)**
- 1 Product → Many SaleItems
- 1 Sale → Many SaleItems

**Join Table**
- SaleItems bridges Products and Sales

**Constraints**
- Foreign Keys: Referential Integrity
- Unique Barcode: No duplicate products
- Primary Keys: Auto-increment IDs

