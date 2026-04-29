# Model Classes - UML Diagram

## Class Relationships

```
CartItem --> Product : references
Sale --> CartItem : contains list of
```

## Product Class

### Fields
- `id: int` - Unique product identifier
- `name: String` - Product name
- `category: String` - Product category
- `barcode: String` - Unique barcode (UPC/EAN)
- `purchasePrice: double` - Cost price
- `sellingPrice: double` - Retail price
- `quantity: int` - Available stock

### Methods
- Getters/Setters for all fields
- `toString()` - String representation

### Constructors
1. `Product()` - Default
2. `Product(name, barcode, purchasePrice, sellingPrice)` - Quick create
3. `Product(name, category, barcode, purchasePrice, sellingPrice, quantity)` - Full insert
4. `Product(id, name, category, barcode, purchasePrice, sellingPrice, quantity)` - DB retrieve

---

## CartItem Class

### Fields
- `product: Product` - Reference to product
- `quantity: int` - Quantity in cart

### Methods
- `getProduct()` - Get product reference
- `setProduct(Product)` - Set product reference
- `getQuantity()` - Get quantity
- `setQuantity(int)` - Set quantity
- `getSubtotal(): double` - Calculate subtotal (price × quantity)
- `toString()` - String representation

### Constructors
1. `CartItem()` - Default
2. `CartItem(product, quantity)` - With product and quantity

---

## Sale Class

### Fields
- `id: int` - Sale transaction ID
- `datetime: LocalDateTime` - Transaction timestamp (auto-set to now)
- `items: List<CartItem>` - List of items in sale
- `totalAmount: double` - Total sale amount
- `totalProfit: double` - Total profit from sale

### Methods
- `getId(), setId(int)` - ID getter/setter
- `getDatetime(), setDatetime(LocalDateTime)` - Datetime getter/setter
- `getItems(), setItems(List<CartItem>)` - Items getter/setter
- `addItem(CartItem)` - Add item to sale
- `removeItem(CartItem)` - Remove item from sale
- `getTotalAmount(), setTotalAmount(double)` - Total amount getter/setter
- `getTotalProfit(), setTotalProfit(double)` - Total profit getter/setter
- `toString()` - String representation

### Constructors
1. `Sale()` - Default (datetime = now)
2. `Sale(datetime)` - With timestamp
3. `Sale(id, datetime, items, totalAmount, totalProfit)` - Full constructor (DB retrieve)

---

## Design Principles

✅ **No Business Logic** - Models are pure data containers
✅ **Encapsulation** - Private fields with public getters/setters
✅ **Immutability Options** - Constructors support both insert and retrieve patterns
✅ **Clean API** - Simple, intuitive method names
✅ **Type Safety** - Proper type declarations for all fields
