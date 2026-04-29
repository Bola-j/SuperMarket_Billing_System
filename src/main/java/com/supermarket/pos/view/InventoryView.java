package com.supermarket.pos.view;

import com.supermarket.pos.model.Product;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * InventoryView builds the inventory screen with low stock highlighting.
 */
public class InventoryView extends BaseView {

    private static final int LOW_STOCK_THRESHOLD = 5;

    private final BorderPane root;
    private final TableView<Product> inventoryTable;
    private final ObservableList<Product> products;
    private final TextField restockQuantityField;
    private final Button restockButton;
    private final Label alertLegend;

    public InventoryView() {
        this.root = new BorderPane();
        this.products = FXCollections.observableArrayList();
        this.inventoryTable = new TableView<>(products);
        this.restockQuantityField = new TextField();
        this.restockButton = new Button("Restock");
        this.alertLegend = new Label("Low stock: quantity < " + LOW_STOCK_THRESHOLD);
        buildView();
    }

    private void buildView() {
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #eff6ff, #fff7ed);");
        root.setTop(createHeader());
        root.setCenter(createTableSection());
        root.setBottom(createFooter());
    }

    private Node createHeader() {
        Label title = new Label("Inventory Overview");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        alertLegend.setStyle("-fx-text-fill: #b91c1c; -fx-font-size: 12px;");

        VBox header = new VBox(6, title, alertLegend);
        header.setPadding(new Insets(0, 0, 12, 0));
        return header;
    }

    private Node createTableSection() {
        inventoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        inventoryTable.setPlaceholder(new Label("No inventory data"));
        inventoryTable.setRowFactory(table -> new TableRow<>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else if (item.getQuantity() < LOW_STOCK_THRESHOLD) {
                    setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #991b1b;");
                } else {
                    setStyle("");
                }
            }
        });

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));

        TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCategory()));

        TableColumn<Product, String> barcodeColumn = new TableColumn<>("Barcode");
        barcodeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getBarcode()));

        TableColumn<Product, Number> purchaseColumn = new TableColumn<>("Purchase Price");
        purchaseColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPurchasePrice()));

        TableColumn<Product, Number> sellingColumn = new TableColumn<>("Selling Price");
        sellingColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getSellingPrice()));

        TableColumn<Product, Number> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getQuantity()));

        inventoryTable.getColumns().add(nameColumn);
        inventoryTable.getColumns().add(categoryColumn);
        inventoryTable.getColumns().add(barcodeColumn);
        inventoryTable.getColumns().add(purchaseColumn);
        inventoryTable.getColumns().add(sellingColumn);
        inventoryTable.getColumns().add(quantityColumn);

        VBox tableWrapper = new VBox(inventoryTable);
        VBox.setVgrow(inventoryTable, Priority.ALWAYS);
        return tableWrapper;
    }

    private Node createFooter() {
        restockQuantityField.setPromptText("Restock quantity");
        restockQuantityField.setPrefWidth(160);
        restockButton.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox footer = new HBox(12, new Label("Restock Selected:"), restockQuantityField, restockButton);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(12, 0, 0, 0));
        return footer;
    }

    public BorderPane getRoot() {
        return root;
    }

    public TableView<Product> getInventoryTable() {
        return inventoryTable;
    }

    public ObservableList<Product> getProducts() {
        return products;
    }

    public TextField getRestockQuantityField() {
        return restockQuantityField;
    }

    public Button getRestockButton() {
        return restockButton;
    }

    public int getLowStockThreshold() {
        return LOW_STOCK_THRESHOLD;
    }
}
