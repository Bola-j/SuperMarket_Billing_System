# Database Schema - Supermarket POS System

## Entity-Relationship Diagram

```
PRODUCTS ||--o{ SALE_ITEMS : "has many"
SALES ||--o{ SALE_ITEMS : "contains"
```

## Tables

### PRODUCTS
| Field | Type | Constraints |
|-------|------|-------------|
| id | INTEGER | PRIMARY KEY, AUTOINCREMENT |
| name | TEXT | NOT NULL |
| category | TEXT | |
| barcode | TEXT | UNIQUE, NOT NULL |
| purchase_price | REAL | NOT NULL |
| selling_price | REAL | NOT NULL |
| quantity | INTEGER | DEFAULT 0 |

### SALES
| Field | Type | Constraints |
|-------|------|-------------|
| id | INTEGER | PRIMARY KEY, AUTOINCREMENT |
| datetime | TEXT | NOT NULL |
| total_amount | REAL | NOT NULL |
| total_profit | REAL | NOT NULL |

### SALE_ITEMS
| Field | Type | Constraints |
|-------|------|-------------|
| id | INTEGER | PRIMARY KEY, AUTOINCREMENT |
| sale_id | INTEGER | NOT NULL, FK → sales(id) |
| product_id | INTEGER | NOT NULL, FK → products(id) |
| quantity | INTEGER | NOT NULL |
| price_at_sale | REAL | NOT NULL |

## Relationships

1. **PRODUCTS → SALE_ITEMS**: One-to-Many
   - One product can appear in many sale items

2. **SALES → SALE_ITEMS**: One-to-Many
   - One sale can contain many sale items

## Key Constraints

- **UNIQUE**: barcode (no duplicate product codes)
- **FOREIGN KEYS**: Referential integrity maintained
- **AUTOINCREMENT**: Auto-generated primary keys
