package com.example;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class Main extends Application {
    private final ObservableList<Customer> customers =
            FXCollections.observableArrayList();
    @Override
    public void start(Stage stage) {
        // Name field
        TextField nameField = new TextField();
        nameField.setPromptText("Enter customer's name");
        // Province list
        ComboBox<String> provinceBox = new ComboBox<>();
        provinceBox.getItems().addAll(
                "Central",
                "Copperbelt",
                "Eastern",
                "Luapula",
                "Lusaka",
                "Muchinga",
                "Northern",
                "North-Western",
                "Southern",
                "Western"
        );
        provinceBox.setPromptText("Select province");
        // Buttons
        Button addButton = new Button("Add Customer");
        Button deleteButton = new Button("Delete Selected");
        // Table
        TableView<Customer> table = new TableView<>();
        TableColumn<Customer, String> nameColumn =
                new TableColumn<>("Name");
        nameColumn.setCellValueFactory(
                data -> data.getValue().nameProperty()
        );
        TableColumn<Customer, String> provinceColumn =
                new TableColumn<>("Province");
        provinceColumn.setCellValueFactory(
                data -> data.getValue().provinceProperty()
        );
        table.getColumns().addAll(nameColumn, provinceColumn);
        table.setItems(customers);
        nameColumn.setPrefWidth(200);
        provinceColumn.setPrefWidth(200);
        // Add customer
        addButton.setOnAction(event -> {
            String name = nameField.getText().trim();
            String province = provinceBox.getValue();
            if (name.isEmpty()) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid Input",
                        "Please enter the customer's name."
                );
                nameField.requestFocus();
                return;
            }
            if (province == null) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid Input",
                        "Please select the customer's province."
                );
                provinceBox.requestFocus();
                return;
            }
            customers.add(new Customer(name, province));
            nameField.clear();
            provinceBox.setValue(null);
            nameField.requestFocus();
        });
        // Delete customer
        deleteButton.setOnAction(event -> {
            Customer selectedCustomer =
                    table.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "No Customer Selected",
                        "Please select a customer to delete."
                );
                return;
            }
            Alert confirmation =
                    new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText("Delete Customer?");
            confirmation.setContentText(
                    "Are you sure you want to delete "
                            + selectedCustomer.getName() + "?"
            );
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    customers.remove(selectedCustomer);
                }
            });
        });
        // Keyboard access
        nameField.setOnAction(event ->
                provinceBox.requestFocus()
        );
        provinceBox.setOnAction(event ->
                addButton.requestFocus()
        );
        // Layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        Label title = new Label("Customer Manager");
        title.setStyle(
                "-fx-font-size: 24px; -fx-font-weight: bold;"
        );
        root.getChildren().addAll(
                title,
                new Label("Customer Name:"),
                nameField,
                new Label("Province:"),
                provinceBox,
                addButton,
                table,
                deleteButton
        );
        // Scene
        Scene scene = new Scene(root, 500, 550);
        stage.setTitle("Customer Manager");
        stage.setScene(scene);
        stage.show();
    }
    // Alert method
    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}