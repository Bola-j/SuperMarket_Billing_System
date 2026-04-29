# Service Layer Implementation

## Overview

The service layer contains all business logic for the Supermarket POS System. It acts as the intermediary between controllers and the DAO layer, ensuring clean separation of concerns.

## Services Implemented

### 1. InventoryService

**Purpose**: Manage product inventory and stock levels

**Methods**:

| Method | Parameters | Returns | Description |
|--------|-----------|---------|-------------|
| `addProduct()` | Product | void | Add new product with validation |
| `restockProduct()` | productId, quantity | void | Increase stock for existing product |
| `getLowStockProducts()` | (none) | List<Product> | Get products below default threshold (10) |
| `getLowStockProducts()` | threshold | List<Product> | Get products below custom threshold |
| `getAllProducts()` | (none) | List<Product> | Retrieve all products |
| `getProductById()` | productId | Product | Get specific product by ID |
| `getProductByBarcode()` | barcode | Product | Get product by barcode |
| `updateProduct()` | Product | void | Update product information |
| `deleteProduct()` | productId | void | Remove product from inventory |

**Validations**:
- Product name not empty
- Barcode not empty and unique
- Purchase and selling prices > 0
- No duplicate barcodes
- Stock levels >= 0

**Example Usage**:
```java
Product p = new Product("Milk", "100ml", "1234567890", 50.0, 75.0, 20);
InventoryService.addProduct(p);
InventoryService.restockProduct(1, 10);
List<Product> lowStock = InventoryService.getLowStockProducts();
```

---

### 2. CartService

**Purpose**: Manage shopping cart operations and calculations

**Methods**:

| Method | Parameters | Returns | Description |
|--------|-----------|---------|-------------|
| `addToCart()` | product, quantity | void | Add item to cart |
| `removeFromCart()` | productId | void | Remove item from cart |
| `updateCartItemQuantity()` | productId, quantity | void | Update quantity of item |
| `calculateTotal()` | (none) | double | Get cart total (selling price) |
| `calculateCost()` | (none) | double | Get cart cost (purchase price) |
| `calculateProfit()` | (none) | double | Get profit from cart |
| `getCart()` | (none) | List<CartItem> | Get all cart items |
| `getCartItemCount()` | (none) | int | Number of unique products |
| `getTotalQuantity()` | (none) | int | Total items across all products |
| `clearCart()` | (none) | void | Empty the cart |
| `isCartEmpty()` | (none) | boolean | Check if cart is empty |
| `getCartItem()` | productId | CartItem | Get specific cart item |

**Features**:
- Static cart maintained in memory
- Stock validation before adding
- Automatic merge if product already in cart
- Real-time profit calculation
- Prevents negative quantities and insufficient stock

**Example Usage**:
```java
Product milk = InventoryService.getProductByBarcode("1234567890");
CartService.addToCart(milk, 2);
CartService.addToCart(milk, 1); // Quantity becomes 3
double total = CartService.calculateTotal();
CartService.clearCart();
```

---

### 3. SalesService

**Purpose**: Handle sales transactions and record sales

**Methods**:

| Method | Parameters | Returns | Description |
|--------|-----------|---------|-------------|
| `completeSale()` | (none) | int | Process current cart as sale, return ID |
| `calculateProfit()` | Sale | double | Calculate profit for specific sale |
| `getSaleById()` | saleId | Sale | Retrieve sale by ID |
| `getAllSales()` | (none) | List<Sale> | Get all sales |
| `getSalesByDateRange()` | startDate, endDate | List<Sale> | Get sales in date range |
| `deleteSale()` | saleId | void | Remove sale record |

**Features**:
- Automatic timestamp on sale creation
- Updates inventory after sale
- Calculates profit automatically
- Clears cart after successful sale
- Date range filtering support
- Validates cart before completing sale

**Example Usage**:
```java
// Add items to cart
CartService.addToCart(product1, 2);
CartService.addToCart(product2, 1);

// Complete sale
int saleId = SalesService.completeSale();

// Check sale details
Sale sale = SalesService.getSaleById(saleId);
double profit = SalesService.calculateProfit(sale);
```

---

### 4. DashboardService

**Purpose**: Analytics and reporting for business metrics

**Methods**:

| Method | Parameters | Returns | Description |
|--------|-----------|---------|-------------|
| `getTotalRevenue()` | (none) | double | Sum of all sale amounts |
| `getTotalProfit()` | (none) | double | Sum of all profits |
| `getTopSellingProducts()` | limit | List<Product> | Top products by quantity sold |
| `getUnsoldProducts()` | (none) | List<Product> | Products with zero sales |
| `getTotalSalesCount()` | (none) | int | Number of transactions |
| `getAverageTransactionValue()` | (none) | double | Mean sale amount |
| `getAverageProfitPerTransaction()` | (none) | double | Mean profit per sale |
| `getTotalInventoryValue()` | (none) | double | Total inventory worth (selling price) |
| `getTotalInventoryCost()` | (none) | double | Total inventory cost (purchase price) |
| `getProfitMargin()` | (none) | double | Profit margin as percentage |
| `getTotalUniqueProducts()` | (none) | int | Number of different products |
| `getTotalItemsInStock()` | (none) | int | Sum of all quantities |

**Features**:
- Aggregates data from sales and inventory
- Calculates business metrics
- Stream-based processing for efficiency
- Handles empty data gracefully
- Provides percentage calculations
- Stock and financial analysis

**Example Usage**:
```java
double revenue = DashboardService.getTotalRevenue();
double profit = DashboardService.getTotalProfit();
double margin = DashboardService.getProfitMargin();
List<Product> top5 = DashboardService.getTopSellingProducts(5);
int total = DashboardService.getTotalItemsInStock();
```

---

## Data Access Objects (DAO)

### ProductDAO

Static methods for product database operations:
- `addProduct()` - Insert new product
- `getProductById()` - Select by ID
- `getProductByBarcode()` - Select by barcode
- `getAllProducts()` - Select all
- `updateProduct()` - Update record
- `updateProductQuantity()` - Update stock
- `deleteProduct()` - Delete record
- `getLowStockProducts()` - Query by threshold
- `productExists()` - Check existence

### SaleDAO

Static methods for sales database operations:
- `addSale()` - Insert new sale
- `getSaleById()` - Select by ID
- `getAllSales()` - Select all
- `getSalesByDateRange()` - Query by date
- `getTotalRevenue()` - Sum aggregation
- `getTotalProfit()` - Sum aggregation
- `deleteSale()` - Delete record

---

## Error Handling

All services implement comprehensive validation:

```java
// Type validation
validateNotNull(product, "Product");
validateNotEmpty(name, "Product name");

// Value validation
validatePositive(price, "Selling price");
validatePositive(quantity, "Quantity");

// Business validation
if (CartService.isCartEmpty())
    throw new IllegalArgumentException("Cart is empty");
```

---

## Architecture Flow

```
User Action (Controller)
      ↓
Service Layer (Business Logic)
  ├─ Inventory: Stock management
  ├─ Cart: Item management
  ├─ Sales: Transaction handling
  └─ Dashboard: Analytics
      ↓
DAO Layer (Database Access)
  ├─ ProductDAO
  └─ SaleDAO
      ↓
Database (SQLite)
```

---

## Key Design Patterns

1. **Singleton Pattern**: Static services (no instantiation needed)
2. **Validation Pattern**: Input validation before processing
3. **Encapsulation**: Business logic hidden from controllers
4. **Separation of Concerns**: Each service handles specific domain
5. **Error Handling**: Comprehensive exception handling with meaningful messages
6. **Stream Processing**: Efficient data filtering and aggregation (DashboardService)

---

## Summary

✅ **4 Service Classes** with clean business logic  
✅ **2 DAO Classes** for database abstraction  
✅ **Comprehensive Validation** for all inputs  
✅ **Error Handling** with descriptive messages  
✅ **No UI Dependencies** - pure business logic  
✅ **Stream Processing** for efficient data handling  
✅ **Stateful Cart Management** for transactions  
✅ **Analytics & Reporting** capabilities  

The service layer is now complete and ready for integration with controllers!
