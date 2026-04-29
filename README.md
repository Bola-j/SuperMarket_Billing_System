# Supermarket POS System

## Project Structure

```
src/main/java/com/supermarket/pos/
├── Main.java                    # JavaFX application entry point
├── model/                       # Data models
│   ├── Product.java
│   ├── CartItem.java
│   └── Sale.java
├── dao/                         # Data Access Objects
│   └── BaseDAO.java
├── service/                     # Business Logic
│   └── BaseService.java
├── controller/                  # Controllers
│   └── BaseController.java
├── view/                        # JavaFX Views
│   └── BaseView.java
└── util/                        # Utilities
    └── DatabaseManager.java
```

## Build and Run

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build
```bash
mvn clean install
```

### Run
```bash
mvn javafx:run
```

### Package
```bash
mvn clean package
```

## Project Information

- **Group ID**: com.supermarket
- **Artifact ID**: supermarket-pos-system
- **Version**: 1.0.0

## Dependencies

- JavaFX 22 - GUI framework
- SQLite JDBC 3.45.0.0 - Database driver
- JUnit 4.13.2 - Testing framework
