package com.example.shelterjavafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LoginViewController {

    // Pola logowania
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    // Pola rejestracji
    @FXML
    private TextField registerUsernameField;
    @FXML
    private PasswordField registerPasswordField;
    @FXML
    private PasswordField registerRepeatPasswordField;
    @FXML
    private Button registerButton;

    @FXML
    private Text messageText;

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> handleLogin());
        registerButton.setOnAction(event -> handleRegister());
    }

    // Metoda logowania
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Username and password cannot be empty.");
            return;
        }

        if ("admin".equals(username) && "123qwe456".equals(password)) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "You have successfully logged in!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    // Metoda rejestracji
    private void handleRegister() {
        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();
        String repeatPassword = registerRepeatPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required.");
            return;
        }

        if (!password.equals(repeatPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return;
        }

        // Logika rejestracji (można dodać zapis do bazy danych lub pliku)
        showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Account for " + username + " has been created!");
    }

    // Pomocnicza metoda do wyświetlania alertów
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
