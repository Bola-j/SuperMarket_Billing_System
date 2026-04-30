You are a senior Java software engineer. Build a complete desktop application for a **Supermarket POS & Management System** using clean architecture and best practices.

# 🧱 TECH STACK

* Language: Java 17+
* GUI: JavaFX
* Database: SQLite
* DB Access: JDBC
* Architecture: MVC (Model-View-Controller)
* Build Tool: Maven or Gradle

---

# 🎯 SYSTEM OVERVIEW

The system is a desktop POS application that supports:

* Inventory management
* Barcode-based checkout (keyboard simulated)
* Sales processing
* Financial analytics (revenue & profit)
* Dashboard for insights

---

# 📦 CORE FEATURES

## 1. Product Management

* Add, edit, delete products
* Fields:

  * id (auto-generated)
  * name
  * category
  * barcode (unique)
  * purchasePrice
  * sellingPrice
  * quantity

---

## 2. Inventory

* Restock products
* Low stock alert (threshold configurable, default = 5)

---

## 3. POS / Checkout

* Add product via barcode input or dropdown selection
* Add/remove/update cart items
* Calculate subtotal, tax (14%), total
* Complete transaction
* Deduct stock automatically

---

## 4. Receipt

* Generate receipt (console + UI view)
* Include:

  * datetime
  * items
  * totals
  * tax
  * final amount

---

## 5. Financial Tracking

* Revenue = total sales
* Profit = (sellingPrice - purchasePrice) * quantity sold

---

## 6. Dashboard

Display:

* Total revenue
* Total profit
* Total items sold
* Best-selling products
* Least-selling products
* Unsold products
* Low-stock alerts

Use charts if possible (JavaFX charts).

---

# 🗄️ DATABASE SCHEMA

Create SQLite tables:

TABLE products (
id INTEGER PRIMARY KEY AUTOINCREMENT,
name TEXT,
category TEXT,
barcode TEXT UNIQUE,
purchase_price REAL,
selling_price REAL,
quantity INTEGER
);

TABLE sales (
id INTEGER PRIMARY KEY AUTOINCREMENT,
datetime TEXT,
total_amount REAL,
total_profit REAL
);

TABLE sale_items (
id INTEGER PRIMARY KEY AUTOINCREMENT,
sale_id INTEGER,
product_id INTEGER,
quantity INTEGER,
price_at_sale REAL
);

---

# 🧩 UML DIAGRAMS

## Class Diagram (textual UML)

Classes:

Product

* id: int
* name: String
* category: String
* barcode: String
* purchasePrice: double
* sellingPrice: double
* quantity: int

InventoryService

* addProduct()
* updateProduct()
* deleteProduct()
* findByBarcode()
* restockProduct()

CartItem

* product: Product
* quantity: int
* getSubtotal()

Cart

* items: List<CartItem>
* addItem()
* removeItem()
* calculateTotal()

Sale

* id: int
* datetime: LocalDateTime
* items: List<CartItem>
* totalAmount: double
* totalProfit: double

SalesService

* completeSale(cart)
* calculateProfit()

DashboardService

* getRevenue()
* getProfit()
* getTopSellingProducts()
* getLowStockProducts()

DatabaseManager

* connect()
* executeQuery()
* executeUpdate()

---

## Sequence Diagram (Checkout Flow)

1. User enters barcode
2. Controller calls InventoryService.findByBarcode()
3. Product returned
4. Controller adds item to Cart
5. User clicks checkout
6. Controller calls SalesService.completeSale(cart)
7. Stock updated in DB
8. Sale + sale_items inserted
9. Dashboard updated

---

# 🖥️ UI DESIGN (JavaFX)

## Main Layout

Use BorderPane:

TOP:

* Navigation bar (POS | Products | Inventory | Dashboard)

CENTER:

* Dynamic content panel

---

## POS Screen

* TextField (barcode input)
* ComboBox (product selection)
* TableView (cart items)
  columns: name, quantity, price, subtotal
* Label (total)
* Button (Checkout)

---

## Product Management Screen

* TableView (all products)
* Buttons:
  Add | Edit | Delete

Form fields:

* name
* category
* barcode
* purchase price
* selling price
* quantity

---

## Inventory Screen

* TableView
* Restock button
* Low stock highlight (red row)

---

## Dashboard Screen

* Labels:
  Revenue, Profit, Items Sold
* Charts:
  Bar chart (top products)
  Pie chart (category distribution)
* List:
  Low stock
  Unsold products

---

# ⚙️ IMPLEMENTATION RULES

* Follow MVC strictly
* Separate:

  * Models
  * Services
  * Controllers
  * Views
* Use DAO pattern for DB operations
* No business logic inside UI classes
* Use observable lists for JavaFX tables
* Handle exceptions properly
* Validate all inputs

---

# 📁 PROJECT STRUCTURE

src/
├── model/
├── dao/
├── service/
├── controller/
├── view/
├── util/
└── Main.java

---

# 🚀 OUTPUT REQUIREMENTS

Generate:

1. Full project structure
2. All Java classes (well organized)
3. JavaFX UI code
4. Database initialization code
5. Sample data
6. Clear comments

Focus on clean, maintainable, scalable code.

Do NOT skip parts. Build step by step starting from database → models → services → UI.
