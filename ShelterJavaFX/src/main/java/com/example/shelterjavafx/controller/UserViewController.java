package com.example.shelterjavafx.controller;

import com.example.shelterjavafx.model.Animal;
import com.example.shelterjavafx.model.Student;
import com.example.shelterjavafx.model.AnimalCondition;
import com.example.shelterjavafx.model.AnimalShelter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.example.shelterjavafx.model.AnimalCondition.*;

public class UserViewController {

    @FXML
    private Button backButton;
    @FXML
    private Button filterButton;
    @FXML
    private TableView<AnimalShelter> shelterTable;
    @FXML
    private TableColumn<AnimalShelter, String> shelterNameColumn;
    @FXML
    private TableColumn<AnimalShelter, Integer> maxCapacityColumn;
    @FXML
    private TableColumn<AnimalShelter, Integer> currentAnimalsColumn;
    @FXML
    private TableView<Animal> animalTable;
    @FXML
    private TableColumn<Animal, String> animalNameColumn;
    @FXML
    private TableColumn<Animal, String> animalSpeciesColumn;
    @FXML
    private TableColumn<Animal, AnimalCondition> animalConditionColumn;
    @FXML
    private TableColumn<Animal, Integer> animalAgeColumn;
    @FXML
    private TableColumn<Animal, Integer> animalPriceColumn;
    @FXML
    private TextField shelterFilterTextField;
    @FXML
    private TextField animalFilterTextField;
    @FXML
    private ComboBox<AnimalCondition> stateComboBox;

    @FXML
    private void handleAdoptButtonClick(ActionEvent event) {
        Animal selectedAnimal = animalTable.getSelectionModel().getSelectedItem();

        if (selectedAnimal == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an animal for adoption.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Get the selected shelter
        AnimalShelter selectedShelter = shelterTable.getSelectionModel().getSelectedItem();
        if (selectedShelter == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a shelter.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Animal Adoption");

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);
        VBox vbox = new VBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("First Name");
        TextField surnameField = new TextField();
        surnameField.setPromptText("Last Name");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");

        vbox.getChildren().addAll(
                new Label("Enter your first name:"), nameField,
                new Label("Enter your last name:"), surnameField,
                new Label("Enter your phone number:"), phoneField,
                new Label("Enter your email address:"), emailField
        );
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                return new String[] {
                        nameField.getText(),
                        surnameField.getText(),
                        phoneField.getText(),
                        emailField.getText()
                };
            }
            return null;
        });

        Optional<String[]> result = dialog.showAndWait();
        result.ifPresent(data -> {
            String name = data[0];
            String surname = data[1];
            String phone = data[2];
            String email = data[3];

            Student loggedInStudent = new Student(name, surname);
            boolean adoptionSuccess = selectedShelter.adoptAnimal(selectedAnimal, loggedInStudent);

            if (adoptionSuccess) {
                updateAnimalTable(selectedShelter);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "The animal has been adopted!", ButtonType.OK);
                successAlert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to adopt the animal.", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }


    @FXML
    private void applyShelterFilters() {
        String filterText = shelterFilterTextField.getText().toLowerCase().trim();
        ObservableList<AnimalShelter> filteredShelters = FXCollections.observableArrayList();

        for (AnimalShelter shelter : shelters) {
            if (filterText.isEmpty() || shelter.getShelterName().toLowerCase().contains(filterText)) {
                filteredShelters.add(shelter);
            }
        }

        shelterTable.setItems(filteredShelters);
    }

    @FXML
    private void applyAnimalFilters() {
        String filterText = animalFilterTextField.getText().toLowerCase().trim();
        ObservableList<Animal> filteredAnimals = FXCollections.observableArrayList();

        AnimalShelter selectedShelter = shelterTable.getSelectionModel().getSelectedItem();
        if (selectedShelter != null) {
            List<Animal> animals = selectedShelter.getAnimals();

            for (Animal animal : animals) {
                boolean matchesNameOrSpecies = filterText.isEmpty() ||
                        animal.getName().toLowerCase().contains(filterText) ||
                        animal.getSpecies().toLowerCase().contains(filterText);

                if (matchesNameOrSpecies) {
                    filteredAnimals.add(animal);
                }
            }
        }

        animalTable.setItems(filteredAnimals);
    }

    @FXML
    private void applyAnimalFiltersByCondition() {
        AnimalCondition selectedCondition = stateComboBox.getValue();
        ObservableList<Animal> filteredAnimals = FXCollections.observableArrayList();

        AnimalShelter selectedShelter = shelterTable.getSelectionModel().getSelectedItem();
        if (selectedShelter != null) {
            List<Animal> animals = selectedShelter.getAnimals();

            for (Animal animal : animals) {
                boolean matchesCondition = selectedCondition == null || animal.getCondition() == selectedCondition;

                if (matchesCondition) {
                    filteredAnimals.add(animal);
                }
            }
        }

        animalTable.setItems(filteredAnimals);
    }

    @FXML
    private void handleBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shelterjavafx/view/login-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSortSheltersByMaxCapacity(ActionEvent event) {
        ObservableList<AnimalShelter> shelters = shelterTable.getItems();
        FXCollections.sort(shelters, Comparator.comparingInt(AnimalShelter::getMaxCapacity).reversed());
    }

    @FXML
    private void handleMouseClickOutsideTextField(MouseEvent event) {
        shelterFilterTextField.getScene().getRoot().requestFocus();
        animalFilterTextField.getScene().getRoot().requestFocus();
    }

    private List<AnimalShelter> shelters = new ArrayList<>();

    public void initialize() {
        shelterFilterTextField.setOnAction(event -> applyShelterFilters());
        animalFilterTextField.setOnAction(event -> applyAnimalFilters());

        stateComboBox.setItems(FXCollections.observableArrayList(AnimalCondition.values()));
        stateComboBox.setValue(null);
        AnimalShelter shelterA = new AnimalShelter("Shelter A", 50);
        shelterA.addAnimal(new Animal("Dog", "Bulldog", HEALTHY, 3, 122.12));
        shelterA.addAnimal(new Animal("Cat", "Siamese", SICK, 9, 12222));

        AnimalShelter shelterB = new AnimalShelter("Shelter B", 30);
        shelterB.addAnimal(new Animal("Dog", "Labrador", ADOPTION, 12, 9000));

        shelters.add(shelterA);
        shelters.add(shelterB);

        shelterNameColumn.setCellValueFactory(new PropertyValueFactory<>("shelterName"));
        maxCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
        currentAnimalsColumn.setCellValueFactory(new PropertyValueFactory<>("currentAnimals"));

        shelterTable.getItems().setAll(shelters);

        animalNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        animalSpeciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        animalConditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));
        animalAgeColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        animalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        shelterTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateAnimalTable(newValue);
            }
        });
    }

    private void updateAnimalTable(AnimalShelter shelter) {
        animalTable.getItems().setAll(shelter.getAnimals());
    }
}