package com.example.shelterjavafx.controller;

import com.example.shelterjavafx.exception.PanelSwitchException;
import com.example.shelterjavafx.exception.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginViewController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> handleLogin());
    }

    private void switchToPanel(String fxmlFile) throws PanelSwitchException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new PanelSwitchException("Failed to load the panel: " + fxmlFile, e);
        }
    }

    private void handleLogin() {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                throw new ValidationException("Username and password cannot be empty.");
            }

            if ("admin".equals(username) && "123admin456".equals(password)) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome Admin!");
                switchToPanel("/com/example/shelterjavafx/view/admin-view.fxml");
            } else if ("user".equals(username) && "123user456".equals(password)) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome User!");
                switchToPanel("/com/example/shelterjavafx/view/user-view.fxml");
            } else {
                throw new ValidationException("Invalid username or password.");
            }
        } catch (ValidationException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", e.getMessage());
        } catch (PanelSwitchException e) {
        showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
