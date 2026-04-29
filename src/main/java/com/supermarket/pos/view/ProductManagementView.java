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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * ProductManagementView builds the product management UI.
 */
public class ProductManagementView extends BaseView {

    private final BorderPane root;
    private final TableView<Product> productTable;
    private final ObservableList<Product> products;

    private final TextField nameField;
    private final TextField categoryField;
    private final TextField barcodeField;
    private final TextField purchasePriceField;
    private final TextField sellingPriceField;
    private final TextField quantityField;

    private final Button addButton;
    private final Button editButton;
    private final Button deleteButton;

    public ProductManagementView() {
        this.root = new BorderPane();
        this.products = FXCollections.observableArrayList();
        this.productTable = new TableView<>(products);

        this.nameField = new TextField();
        this.categoryField = new TextField();
        this.barcodeField = new TextField();
        this.purchasePriceField = new TextField();
        this.sellingPriceField = new TextField();
        this.quantityField = new TextField();

        this.addButton = new Button("Add");
        this.editButton = new Button("Edit");
        this.deleteButton = new Button("Delete");

        buildView();
    }

    private void buildView() {
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #fdf2f8, #fff7ed);");
        root.setTop(createHeader());
        root.setCenter(createTableSection());
        root.setRight(createFormSection());
    }

    private Node createHeader() {
        Label title = new Label("Product Management");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #111827;");
        VBox header = new VBox(title);
        header.setPadding(new Insets(0, 0, 12, 0));
        return header;
    }

    private Node createTableSection() {
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        productTable.setPlaceholder(new Label("No products available"));

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

        productTable.getColumns().add(nameColumn);
        productTable.getColumns().add(categoryColumn);
        productTable.getColumns().add(barcodeColumn);
        productTable.getColumns().add(purchaseColumn);
        productTable.getColumns().add(sellingColumn);
        productTable.getColumns().add(quantityColumn);

        VBox.setVgrow(productTable, Priority.ALWAYS);
        VBox tableWrapper = new VBox(productTable);
        tableWrapper.setPadding(new Insets(0, 12, 0, 0));
        return tableWrapper;
    }

    private Node createFormSection() {
        nameField.setPromptText("Product name");
        categoryField.setPromptText("Category");
        barcodeField.setPromptText("Barcode");
        purchasePriceField.setPromptText("Purchase price");
        sellingPriceField.setPromptText("Selling price");
        quantityField.setPromptText("Quantity");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.add(new Label("Name"), 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(new Label("Category"), 0, 1);
        formGrid.add(categoryField, 1, 1);
        formGrid.add(new Label("Barcode"), 0, 2);
        formGrid.add(barcodeField, 1, 2);
        formGrid.add(new Label("Purchase Price"), 0, 3);
        formGrid.add(purchasePriceField, 1, 3);
        formGrid.add(new Label("Selling Price"), 0, 4);
        formGrid.add(sellingPriceField, 1, 4);
        formGrid.add(new Label("Quantity"), 0, 5);
        formGrid.add(quantityField, 1, 5);

        HBox buttons = new HBox(10, addButton, editButton, deleteButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox form = new VBox(12, formGrid, buttons);
        form.setPadding(new Insets(0, 0, 0, 12));
        form.setStyle("-fx-background-color: white; -fx-padding: 16; -fx-border-color: #e5e7eb; -fx-border-radius: 6; -fx-background-radius: 6;");
        VBox.setVgrow(formGrid, Priority.NEVER);
        return form;
    }

    public BorderPane getRoot() {
        return root;
    }

    public TableView<Product> getProductTable() {
        return productTable;
    }

    public ObservableList<Product> getProducts() {
        return products;
    }

    public TextField getNameField() {
        return nameField;
    }

    public TextField getCategoryField() {
        return categoryField;
    }

    public TextField getBarcodeField() {
        return barcodeField;
    }

    public TextField getPurchasePriceField() {
        return purchasePriceField;
    }

    public TextField getSellingPriceField() {
        return sellingPriceField;
    }

    public TextField getQuantityField() {
        return quantityField;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }
}
