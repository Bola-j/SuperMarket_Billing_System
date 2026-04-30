# Supermarket POS & Billing System - Project Documentation

This document provides a comprehensive overview of the Supermarket POS & Billing System, a graduation project built with Java, JavaFX, and SQLite. It outlines the architecture, use cases, technology stack, business logic flow, and a recommended division of labor for a 5-member engineering team.

---

## 1. System Architecture & Directory Hierarchy

The project follows a strict **Model-View-Controller (MVC)** architectural pattern combined with a **Service-Oriented Architecture (SOA)** for business logic separation.

```text
SuperMarket_Billing_System/
├── pom.xml                   # Maven build configuration and dependencies
├── build_exe.ps1             # Script to package the app into a Windows EXE using jpackage
└── src/main/java/com/supermarket/pos/
    ├── Launcher.java         # Standalone entry point for fat-JARs and executables
    ├── Main.java             # Main JavaFX Application class (Tab routing and setup)
    │
    ├── model/                # Data Transfer Objects (DTOs)
    │   ├── Product.java      # Represents a product in inventory
    │   ├── Sale.java         # Represents a completed transaction
    │   ├── CartItem.java     # Represents an item added to the active POS cart
    │   └── ProductSalesSummary.java # Aggregated data for dashboard charts
    │
    ├── dao/                  # Data Access Objects (Direct Database Interaction)
    │   ├── ProductDAO.java   # CRUD operations for products
    │   └── SaleDAO.java      # CRUD operations for sales and sale_items
    │
    ├── service/              # Core Business Logic & Validation
    │   ├── CartService.java  # Manages active POS cart state and totals
    │   ├── DashboardService.java # Calculates analytics (revenue, top selling)
    │   ├── InventoryService.java # Manages stock levels and product lookups
    │   └── SalesService.java # Finalizes checkout and triggers DAO inserts
    │
    ├── controller/           # UI Event Handlers (Connecting View to Service)
    │   ├── BaseController.java # Shared alert/dialog functionality
    │   ├── DashboardController.java # Loads analytics data into the dashboard
    │   ├── PosController.java # Manages scanning, cart updates, and checkout
    │   ├── InventoryController.java # Handles stock replenishment
    │   └── ProductManagementController.java # Handles product CRUD forms
    │
    ├── view/                 # JavaFX UI Components (Programmatic UI)
    │   ├── BaseView.java     # Base layout configurations
    │   ├── DashboardView.java # Charts and metrics layout
    │   ├── PosView.java      # Cashier interface layout
    │   ├── InventoryView.java # Stock monitoring layout
    │   └── ProductManagementView.java # Product data entry layout
    │
    └── util/                 # Utility Classes
        └── DatabaseManager.java # SQLite connection pooling and initialization
```

---

## 2. Technology Stack: Packages, Libraries & Build Tools

### Build & Packaging Tools
*   **Apache Maven**: The primary build automation tool. It resolves dependencies, compiles the code, and manages the build lifecycle via `pom.xml`.
*   **Maven Daemon (`mvnd`)**: Used for significantly faster background builds.
*   **jpackage (JDK Tool)**: Used in the `build_exe.ps1` script to bundle the Java Runtime Environment (JRE) and the compiled application into a self-contained, native Windows Executable (`.exe`).
*   **maven-dependency-plugin**: Configured in `pom.xml` to gather all external `.jar` files into a single directory for the `jpackage` bundling phase.
*   **javafx-maven-plugin**: Facilitates running the JavaFX application directly from Maven during development.

### Core Frameworks & Libraries
*   **Java (JDK 25)**: The core programming language.
*   **JavaFX (v21.0.2)**: The modern UI toolkit used for building the graphical user interface.
    *   `javafx-controls`: Provides the standard UI elements (Buttons, Tables, Charts).
    *   `javafx-fxml`: Included for potential FXML support, though the current UI is built programmatically.
*   **SQLite JDBC (v3.45.3.0)**: The official Java Database Connectivity (JDBC) driver for SQLite. It embeds a lightweight, serverless SQL database engine directly into the application, requiring no external database installation for the client.

---

## 3. Customer Use Case Scenarios

The system supports several distinct user flows based on the operational role.

### A. Cashier Scenario (Point of Sale)
1.  **Start Transaction**: The cashier navigates to the POS tab.
2.  **Item Entry**:
    *   *Path 1 (Barcode)*: The cashier scans or types the product's barcode and presses Enter.
    *   *Path 2 (Manual)*: The cashier selects the product from the dropdown menu, enters a custom quantity, and clicks "Add to Cart".
3.  **Cart Management**: The system calculates the subtotal for each item and updates the grand total in EGP.
4.  **Checkout**: The cashier clicks "Checkout".
5.  **Completion**: The system validates the cart is not empty, records the sale in the database, deducts the purchased quantities from the inventory, clears the cart, and displays a digital receipt.

### B. Inventory Management Scenario
1.  **Monitor Stock**: The manager navigates to the Dashboard to view the "Low Stock" widget, or directly to the Inventory tab.
2.  **Select Product**: The manager selects a product from the inventory table.
3.  **Restock**: The manager enters the quantity of newly arrived stock and clicks "Restock".
4.  **Update**: The system adds the new quantity to the existing stock and instantly refreshes the inventory view.

### C. Administrator / Data Entry Scenario
1.  **Navigate to Products**: The admin opens the Products tab.
2.  **Add New Product**: The admin fills in Name, Category, Barcode, Purchase Price, Selling Price, and Initial Quantity, then clicks "Add".
3.  **Edit Product**: The admin selects an existing product, modifies its prices or details in the form, and clicks "Update".
4.  **Delete Product**: The admin selects an obsolete product and clicks "Delete" to remove it from the system.

### D. Store Manager Analytics Scenario
1.  **View Dashboard**: The manager opens the application (Dashboard is the default tab).
2.  **Review High-Level Metrics**: The manager checks Total Revenue (EGP), Total Profit (EGP), and Total Items Sold.
3.  **Analyze Trends**: The manager reviews the "Best Selling Categories" pie chart to see which departments perform best, and the "Top Selling Products" bar chart for individual product performance.
4.  **Actionable Insights**: The manager reviews the "Unsold Products" list to decide on promotions or discontinuations.

---

## 4. Business Logic: Flow & Location

The business logic acts as the "brain" of the application, ensuring that data is valid, calculations are correct, and different parts of the system stay synchronized.

**Where it lives**: The `src/main/java/com/supermarket/pos/service/` directory.

**Flow Example (Checkout Process)**:
1.  **UI Event (`PosController.handleCheckout()`)**: User clicks checkout. Controller checks if the cart is empty.
2.  **Delegation (`SalesService.completeSale()`)**: Controller calls the Service layer.
3.  **Business Rules (`SalesService`)**:
    *   The service loops through the active `CartService`.
    *   It calculates the total revenue and total profit (Selling Price - Purchase Price) for the entire transaction.
    *   It creates a `Sale` object.
4.  **Data Persistence (`SaleDAO.addSale()`)**: The Service passes the `Sale` object to the Data Access Object. The DAO writes to the `sales` and `sale_items` SQLite tables.
5.  **Inventory Update**: The DAO loops through the cart items and dynamically reduces the `quantity` field in the `products` table.
6.  **State Reset (`CartService.clearCart()`)**: The Service clears the active memory cart.
7.  **UI Feedback (`PosController`)**: The Controller shows the receipt dialog. Cross-tab listeners in `Main.java` ensure the Dashboard and Inventory views fetch the newly updated data from the database.

---

## 5. Team Work Breakdown (5 Members)

As a Senior Software Engineer, I recommend dividing the project based on architectural layers and feature domains to minimize merge conflicts and allow members to specialize.

### Member 1: Database & Persistence Engineer (The Data Foundation)
*   **Role**: Manage how data is stored, retrieved, and structured.
*   **Tasks**:
    *   Maintain `DatabaseManager.java` (connection pooling, table creation).
    *   Write and optimize all SQL queries inside `ProductDAO.java` and `SaleDAO.java`.
    *   Ensure database locks/transactions are handled safely using `conn.setAutoCommit(false)` and `conn.commit()`.
*   **Skills Focus**: SQL, SQLite, JDBC, Database Design.
*   **Prerequisite Study Topics (Before Discussion)**:
    *   Java JDBC API (specifically `Connection`, `PreparedStatement`, `ResultSet`).
    *   Database Transactions (understanding `setAutoCommit(false)`, `commit()`, `rollback()`).
    *   SQLite specific data types and syntax (e.g., `AUTOINCREMENT`).

### Member 2: Core Business Logic Engineer (The Brains)
*   **Role**: Build the `service/` and `model/` packages. Ensure all calculations are mathematically sound and data is validated before hitting the database.
*   **Tasks**:
    *   Implement `CartService.java` (adding items, calculating EGP subtotals).
    *   Implement `SalesService.java` (processing the checkout logic).
    *   Implement complex aggregations in `DashboardService.java` (e.g., calculating profit margins, aggregating category sales).
*   **Skills Focus**: Java Streams API, Data Structures, Algorithms, Business Rule Enforcement.
*   **Prerequisite Study Topics (Before Discussion)**:
    *   Service-Oriented Architecture (SOA) and how it fits into MVC.
    *   Java Collections Framework (`List`, `Map`, `Set`).
    *   Java 8+ Streams API (understanding `map`, `filter`, `collect`, `groupingBy`).

### Member 3: Point of Sale (POS) UI/UX Developer (The Cashier Experience)
*   **Role**: Build the most critical user-facing screen—the checkout system.
*   **Tasks**:
    *   Build the layout in `PosView.java` (Tables, Dropdowns, TextFields).
    *   Wire up the interactions in `PosController.java` (Barcode scanning, adding to cart, showing the receipt dialog).
    *   Ensure the checkout process is fast, ergonomic, and error-proof (e.g., handling invalid number formats).
*   **Skills Focus**: JavaFX Event Handling, UI Layouts (BorderPane, HBox, VBox), User Experience.
*   **Prerequisite Study Topics (Before Discussion)**:
    *   JavaFX Core Concepts (`Stage`, `Scene`, `Node`).
    *   JavaFX Layout Panes (differences between `BorderPane`, `HBox`, `VBox`).
    *   Event Handling in JavaFX using Lambda expressions (e.g., `button.setOnAction(e -> ...)`).

### Member 4: Inventory & Admin UI Developer (The Back-Office)
*   **Role**: Build the management screens for products and stock.
*   **Tasks**:
    *   Build `ProductManagementView.java` and `InventoryView.java`.
    *   Implement `ProductManagementController.java` (Validating form inputs before allowing a product to be saved).
    *   Implement `InventoryController.java` (Handling the restock logic).
*   **Skills Focus**: JavaFX Forms, TableView rendering, Input Validation, CRUD interface design.
*   **Prerequisite Study Topics (Before Discussion)**:
    *   JavaFX `TableView` and `ObservableList` binding.
    *   Input sanitization and validation techniques in Java (e.g., handling `NumberFormatException`).
    *   Designing CRUD (Create, Read, Update, Delete) workflows.

### Member 5: Analytics & DevOps Specialist (The Manager Experience & Release)
*   **Role**: Visualize the data for management and ensure the app can be distributed easily to end-users.
*   **Tasks**:
    *   Build the `DashboardView.java` (Bar charts, Pie charts, Metric cards).
    *   Implement `DashboardController.java` to dynamically pull data from Member 2's services and map it to JavaFX charts.
    *   Manage `Main.java` tab event listeners to ensure real-time dashboard updates.
    *   Maintain the `pom.xml` and the `build_exe.ps1` script to ensure the team can reliably generate the Windows `.exe`.
*   **Skills Focus**: JavaFX Charts (PieChart, BarChart), Build Tools (Maven), Scripting (PowerShell), System Integration.
*   **Prerequisite Study Topics (Before Discussion)**:
    *   JavaFX Charting components (`PieChart`, `BarChart`, `XYChart.Series`).
    *   Apache Maven build lifecycle and how `pom.xml` handles dependencies and plugins.
    *   Basics of creating self-contained Java executables using the `jpackage` tool.
