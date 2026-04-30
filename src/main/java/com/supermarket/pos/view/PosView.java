package com.supermarket.pos.view;

import com.supermarket.pos.model.CartItem;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import com.supermarket.pos.model.Product;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * PosView builds the JavaFX POS screen using a clean BorderPane layout.
 */
public class PosView extends BaseView {

    private final BorderPane root;
    private final TextField barcodeField;
    private final ComboBox<Product> productComboBox;
    private final TextField quantityField;
    private final Button addButton;
    private final TableView<CartItem> cartTable;
    private final ObservableList<CartItem> cartItems;
    private final Label totalLabel;
    private final Button checkoutButton;

    public PosView() {
        this.root = new BorderPane();
        this.barcodeField = new TextField();
        this.productComboBox = new ComboBox<>();
        this.quantityField = new TextField("1");
        this.addButton = new Button("Add to Cart");
        this.cartItems = FXCollections.observableArrayList();
        this.cartTable = new TableView<>(cartItems);
        this.totalLabel = new Label("Total Price: EGP 0.00");
        this.checkoutButton = new Button("Checkout");
        buildView();
    }

    private void buildView() {
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #f8fafc, #eef2ff);");

        root.setTop(createTopSection());
        root.setCenter(createCenterSection());
        root.setBottom(createBottomSection());
    }

    private Node createTopSection() {
        Label title = new Label("Supermarket POS");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1f2937;");

        Label prompt = new Label("Scan barcode OR select product from dropdown");
        prompt.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 13px;");

        barcodeField.setPromptText("Enter barcode");
        barcodeField.setPrefWidth(200);
        barcodeField.setStyle("-fx-font-size: 14px; -fx-padding: 8px 12px;");

        productComboBox.setPromptText("Select product");
        productComboBox.setPrefWidth(250);
        productComboBox.setStyle("-fx-font-size: 14px;");
        productComboBox.setConverter(new StringConverter<Product>() {
            @Override
            public String toString(Product product) {
                return product == null ? "" : product.getName() + " - EGP " + String.format("%.2f", product.getSellingPrice());
            }
            @Override
            public Product fromString(String string) {
                return null;
            }
        });

        quantityField.setPromptText("Qty");
        quantityField.setPrefWidth(60);
        quantityField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        addButton.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16;");

        HBox barcodeRow = new HBox(12, 
            new Label("Barcode:"), barcodeField, 
            new Label("OR Product:"), productComboBox,
            new Label("Qty:"), quantityField,
            addButton
        );
        barcodeRow.setAlignment(Pos.CENTER_LEFT);

        VBox top = new VBox(8, title, prompt, barcodeRow);
        top.setPadding(new Insets(0, 0, 16, 0));
        return top;
    }

    private Node createCenterSection() {
        cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        cartTable.setPlaceholder(new Label("Cart is empty"));

        TableColumn<CartItem, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                cellData.getValue().getProduct() != null ? cellData.getValue().getProduct().getName() : ""
        ));

        TableColumn<CartItem, Number> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getQuantity()));

        TableColumn<CartItem, Number> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                cellData.getValue().getProduct() != null ? cellData.getValue().getProduct().getSellingPrice() : 0.0
        ));

        TableColumn<CartItem, Number> subtotalColumn = new TableColumn<>("Subtotal");
        subtotalColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getSubtotal()));

        cartTable.getColumns().add(nameColumn);
        cartTable.getColumns().add(quantityColumn);
        cartTable.getColumns().add(priceColumn);
        cartTable.getColumns().add(subtotalColumn);
        VBox.setVgrow(cartTable, Priority.ALWAYS);

        VBox center = new VBox(cartTable);
        center.setPadding(new Insets(0, 0, 16, 0));
        return center;
    }

    private Node createBottomSection() {
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #111827;");
        checkoutButton.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 18;");

        HBox footer = new HBox(16, totalLabel, checkoutButton);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(16, 0, 0, 0));
        return footer;
    }

    public BorderPane getRoot() {
        return root;
    }

    public TextField getBarcodeField() {
        return barcodeField;
    }

    public ComboBox<Product> getProductComboBox() {
        return productComboBox;
    }

    public TextField getQuantityField() {
        return quantityField;
    }

    public Button getAddButton() {
        return addButton;
    }

    public TableView<CartItem> getCartTable() {
        return cartTable;
    }

    public ObservableList<CartItem> getCartItems() {
        return cartItems;
    }

    public Label getTotalLabel() {
        return totalLabel;
    }

    public Button getCheckoutButton() {
        return checkoutButton;
    }
}