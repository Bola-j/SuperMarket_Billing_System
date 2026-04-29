# Project Architecture - MVC Pattern

## Layer Structure

### 1. View Layer (JavaFX)
- **Components**: JavaFX UI controls (Stage, Scene, Button, TextField, etc.)
- **BaseView**: Abstract base class for all view components
- **Purpose**: Render UI and capture user interactions
- **Color**: Light Blue

### 2. Controller Layer
- **BaseController**: Abstract base class for controllers
- **Specific Controllers**: ProductController, SaleController, CartController, etc.
- **Purpose**: Handle user events and delegate to services
- **Color**: Purple

### 3. Service Layer
- **BaseService**: Abstract base class for business logic
- **Services**: ProductService, SaleService, CartService, ReportService, etc.
- **Purpose**: Implement business logic and calculations
- **Responsibilities**:
  - Calculate totals and profits
  - Validate transactions
  - Process sales
  - Manage inventory
- **Color**: Orange

### 4. DAO Layer (Data Access Object)
- **BaseDAO**: Abstract base class with common CRUD operations
- **DAOs**: ProductDAO, SaleDAO, SaleItemDAO
- **Purpose**: Handle all database operations
- **Responsibilities**:
  - Create (INSERT)
  - Read (SELECT)
  - Update (UPDATE)
  - Delete (DELETE)
- **Color**: Green

### 5. Model Layer
- **Product**: Product entity
- **CartItem**: Shopping cart item entity
- **Sale**: Sales transaction entity
- **Purpose**: Data representation (no logic)
- **Color**: Pink

### 6. Utility Layer
- **DatabaseManager**: Handles SQLite connections and initialization
- **Purpose**: Database connection management and setup
- **Color**: Light Green

## Data Flow

```
User Input
   ↓
View Layer
   ↓
Controller Layer (event handling)
   ↓
Service Layer (business logic)
   ↓
DAO Layer (database operations)
   ↓
Model Layer (data objects)
   ↓
Database (SQLite)
```

## Separation of Concerns

| Layer | Responsibility | Does NOT Handle |
|-------|----------------|-----------------|
| View | UI Rendering | Business Logic |
| Controller | Event Dispatch | Database Access |
| Service | Business Rules | UI Display |
| DAO | Database Operations | Business Logic |
| Model | Data Storage | Processing |

## Benefits

✅ **Maintainability** - Each layer has single responsibility
✅ **Testability** - Layers can be tested independently
✅ **Reusability** - Services can be used by multiple controllers
✅ **Scalability** - Easy to add new features
✅ **Clean Code** - Clear structure and dependencies
