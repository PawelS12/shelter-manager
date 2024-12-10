package com.example.shelterjavafx.controller;

import java.io.File;
import com.example.shelterjavafx.data.ShelterDataCSV;
import com.example.shelterjavafx.exception.FilterException;
import com.example.shelterjavafx.exception.ValidationException;
import com.example.shelterjavafx.model.*;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminViewController {

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
    private TableColumn<AnimalShelter, String> ratingColumn;
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

    private SessionFactory sessionFactory;

    public void initialize() {
        sessionFactory = HibernateUtil.getSessionFactory();
        ObservableList<AnimalCondition> conditions = FXCollections.observableArrayList(AnimalCondition.values());
        stateComboBox.setItems(conditions);

        List<AnimalShelter> sheltersFromDB = getSheltersFromDatabase();

        shelterNameColumn.setCellValueFactory(new PropertyValueFactory<>("shelterName"));
        maxCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
        ratingColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedAverageRating()));

        shelterTable.getItems().setAll(sheltersFromDB);

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

    @FXML
    private void handleExportButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                ShelterDataCSV.exportAllSheltersToCSV(file.getAbsolutePath());
                showInfoAlert("Export Completed", "Data has been successfully exported to a CSV file.");
            } catch (IOException e) {
                showErrorAlert("An issue occurred while exporting data to the CSV file.");
            }
        }
    }

    @FXML
    private void handleImportButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                ShelterDataCSV.importShelterFromCSV(file.getAbsolutePath());
                updateShelterTable();
                showInfoAlert("Import Completed", "Data has been successfully imported to a CSV file.");
            } catch (IOException e) {
                showErrorAlert("An issue occurred while importing data to the CSV file.");
            }
        }
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
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

    private List<AnimalShelter> getSheltersFromDatabase() {
        try (Session session = sessionFactory.openSession()) {
            String hqlShelters = "FROM AnimalShelter";
            List<AnimalShelter> shelters = session.createQuery(hqlShelters, AnimalShelter.class).getResultList();

            for (AnimalShelter shelter : shelters) {
                Hibernate.initialize(shelter.getAnimals());
                Hibernate.initialize(shelter.getRatings());
            }

            return shelters;
        }
    }

    private void updateAnimalTable(AnimalShelter shelter) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT a FROM Animal a WHERE a.shelter.id = :shelterId";
            Query<Animal> query = session.createQuery(hql, Animal.class);
            query.setParameter("shelterId", shelter.getId());
            List<Animal> animals = query.getResultList();
            animalTable.getItems().setAll(animals);
        }
    }

    private void updateShelterTable() {
        Session session = sessionFactory.openSession();
        Query<AnimalShelter> query = session.createQuery("FROM AnimalShelter", AnimalShelter.class);
        List<AnimalShelter> shelters = query.getResultList();
        shelterTable.getItems().setAll(shelters);
        session.close();
    }

    @FXML
    private void handleAddShelterButtonClick(ActionEvent event) {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add Shelter");
            dialog.setHeaderText("Enter Shelter Details:");

            TextField shelterNameField = new TextField();
            shelterNameField.setPromptText("Shelter Name");

            TextField shelterCapacityField = new TextField();
            shelterCapacityField.setPromptText("Maximum Capacity");

            VBox vbox = new VBox(10, shelterNameField, shelterCapacityField);
            dialog.getDialogPane().setContent(vbox);

            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == okButton) {
                String shelterName = shelterNameField.getText();
                String capacityInput = shelterCapacityField.getText();

                if (shelterName == null || shelterName.isEmpty() || capacityInput == null || capacityInput.isEmpty()) {
                    throw new ValidationException("Both fields must be filled.");
                }

                try {
                    int maxCapacity = Integer.parseInt(capacityInput);
                    Session session = sessionFactory.openSession();
                    List<AnimalShelter> existingShelters = session.createQuery("FROM AnimalShelter WHERE shelterName = :shelterName", AnimalShelter.class)
                            .setParameter("shelterName", shelterName)
                            .getResultList();

                    if (!existingShelters.isEmpty()) {
                        showErrorAlert("A shelter with the same name already exists in the database.");
                        session.close();
                        return;
                    }

                    AnimalShelter newShelter = new AnimalShelter(shelterName, maxCapacity);

                    session.beginTransaction();
                    session.save(newShelter);
                    session.getTransaction().commit();
                    session.close();

                    List<AnimalShelter> shelters = getSheltersFromDatabase();
                    shelterTable.getItems().setAll(shelters);

                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid number format for capacity.");
                }
            }
        } catch (ValidationException e) {
            showErrorAlert(e.getMessage());
        }
    }

    @FXML
    private void handleRemoveShelterButtonClick(ActionEvent event) {
        AnimalShelter selectedShelter = shelterTable.getSelectionModel().getSelectedItem();

        try {
            if (selectedShelter == null) {
                throw new ValidationException("No shelter selected.");
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Shelter");
            alert.setHeaderText("Are you sure you want to remove this shelter?");
            alert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Session session = sessionFactory.openSession();
                session.beginTransaction();
                session.delete(selectedShelter);
                session.getTransaction().commit();
                session.close();

                List<AnimalShelter> shelters = getSheltersFromDatabase();
                shelterTable.getItems().setAll(shelters);
            }
        } catch (ValidationException e) {
            showErrorAlert(e.getMessage());
        }
    }

    @FXML
    private void handleAddAnimalButtonClick(ActionEvent event) {
        AnimalShelter selectedShelter = shelterTable.getSelectionModel().getSelectedItem();
        try {
            if (selectedShelter == null) {
                throw new ValidationException("No shelter selected.");
            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add Animal");
            dialog.setHeaderText("Enter Animal Details:");

            TextField animalNameField = new TextField();
            animalNameField.setPromptText("Animal Name");

            TextField speciesField = new TextField();
            speciesField.setPromptText("Animal Species");

            TextField ageField = new TextField();
            ageField.setPromptText("Animal Age");

            TextField priceField = new TextField();
            priceField.setPromptText("Animal Price");

            ChoiceBox<AnimalCondition> conditionChoiceBox = new ChoiceBox<>();
            conditionChoiceBox.getItems().addAll(AnimalCondition.values());
            conditionChoiceBox.setValue(AnimalCondition.HEALTHY);

            VBox vbox = new VBox(10, animalNameField, speciesField, ageField, priceField, conditionChoiceBox);
            dialog.getDialogPane().setContent(vbox);

            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == okButton) {
                String animalName = animalNameField.getText();
                String animalSpecies = speciesField.getText();
                String ageInput = ageField.getText();
                String priceInput = priceField.getText();
                AnimalCondition animalCondition = conditionChoiceBox.getValue();

                if (animalName.isEmpty() || animalSpecies.isEmpty() || ageInput.isEmpty() || priceInput.isEmpty()) {
                    throw new ValidationException("All fields must be filled.");
                }

                try {
                    int age = Integer.parseInt(ageInput);
                    double price = Double.parseDouble(priceInput);

                    Animal newAnimal = new Animal(animalName, animalSpecies, animalCondition, age, price);
                    newAnimal.setAdopted(false);

                    boolean animalExists = selectedShelter.getAnimals().stream().anyMatch(existingAnimal -> existingAnimal.compareTo(newAnimal) == 0);

                    if (animalExists) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Animal Exists");
                        alert.setHeaderText("This Animal Already Exists");
                        alert.setContentText("An animal with the same name, species, and age already exists in the shelter.");
                        alert.showAndWait();
                        return;
                    }

                    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                        session.beginTransaction();
                        selectedShelter.addAnimal(newAnimal, session);
                        session.save(newAnimal);
                        session.getTransaction().commit();
                        updateAnimalTable(selectedShelter);
                        updateShelterTable();
                    }

                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid number format for age or price.");
                }
            }
        } catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Validation Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleRemoveAnimalButtonClick(ActionEvent event) {
        AnimalShelter selectedShelter = shelterTable.getSelectionModel().getSelectedItem();
        Animal selectedAnimal = animalTable.getSelectionModel().getSelectedItem();

        try {
            if (selectedAnimal == null) {
                throw new ValidationException("No animal selected.");
            }

            if (selectedShelter != null && selectedAnimal != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Remove Animal");
                alert.setHeaderText("Are you sure you want to remove this animal?");
                alert.setContentText("This action cannot be undone.");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    Transaction transaction = null;

                    try {
                        transaction = session.beginTransaction();
                        session.delete(selectedAnimal);
                        transaction.commit();
                        selectedShelter.getAnimals().remove(selectedAnimal);
                        updateAnimalTable(selectedShelter);
                        updateShelterTable();

                    } catch (Exception e) {
                        if (transaction != null && transaction.isActive()) {
                            transaction.rollback();
                        }
                        e.printStackTrace();
                        throw new ValidationException("Error occurred while removing the animal.");
                    } finally {
                        session.close();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Animal Selected");
                alert.setHeaderText("Please select an animal to remove.");
                alert.showAndWait();
            }
        } catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Validation Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleEditShelterButtonClick(ActionEvent event) {
        AnimalShelter selectedShelter = shelterTable.getSelectionModel().getSelectedItem();

        try {
            if (selectedShelter == null) {
                throw new ValidationException("No shelter selected.");
            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Shelter");
            dialog.setHeaderText("Edit Shelter Details:");

            TextField shelterNameField = new TextField(selectedShelter.getShelterName());
            shelterNameField.setPromptText("Shelter Name");

            TextField shelterCapacityField = new TextField(String.valueOf(selectedShelter.getMaxCapacity()));
            shelterCapacityField.setPromptText("Maximum Capacity");

            VBox vbox = new VBox(10, shelterNameField, shelterCapacityField);
            dialog.getDialogPane().setContent(vbox);

            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == okButton) {
                String newShelterName = shelterNameField.getText();
                String newCapacityInput = shelterCapacityField.getText();

                if (newShelterName.isEmpty() || newCapacityInput.isEmpty()) {
                    throw new ValidationException("All fields must be filled.");
                }

                try {
                    int newCapacity = Integer.parseInt(newCapacityInput);

                    Session session = HibernateUtil.getSessionFactory().openSession();
                    Transaction transaction = null;

                    try {
                        transaction = session.beginTransaction();
                        String hql = "FROM AnimalShelter WHERE shelterName = :shelterName AND maxCapacity = :maxCapacity AND id != :selectedShelterId";
                        List<AnimalShelter> shelters = session.createQuery(hql, AnimalShelter.class)
                                .setParameter("shelterName", newShelterName)
                                .setParameter("maxCapacity", newCapacity)
                                .setParameter("selectedShelterId", selectedShelter.getId())
                                .list();

                        if (!shelters.isEmpty()) {
                            throw new ValidationException("A shelter with this name and capacity already exists.");
                        }

                        selectedShelter.setShelterName(newShelterName);
                        selectedShelter.setMaxCapacity(newCapacity);
                        session.update(selectedShelter);
                        transaction.commit();

                        shelterTable.refresh();
                    } catch (Exception e) {
                        if (transaction != null) {
                            transaction.rollback();
                        }
                        throw new ValidationException("Error occurred while updating the shelter.");
                    } finally {
                        session.close();
                    }
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid number format for capacity.");
                }
            }
        } catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleEditAnimalButtonClick(ActionEvent event) {
        AnimalShelter selectedShelter = shelterTable.getSelectionModel().getSelectedItem();
        Animal selectedAnimal = animalTable.getSelectionModel().getSelectedItem();

        try {
            if (selectedShelter == null) {
                throw new ValidationException("No shelter selected.");
            }

            if (selectedAnimal != null) {
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Edit Animal");
                dialog.setHeaderText("Edit Animal Details:");

                TextField animalNameField = new TextField(selectedAnimal.getName());
                animalNameField.setPromptText("Animal Name");

                TextField speciesField = new TextField(selectedAnimal.getSpecies());
                speciesField.setPromptText("Animal Species");

                TextField ageField = new TextField(String.valueOf(selectedAnimal.getAge()));
                ageField.setPromptText("Animal Age");

                TextField priceField = new TextField(String.valueOf(selectedAnimal.getPrice()));
                priceField.setPromptText("Animal Price");

                ComboBox<AnimalCondition> conditionComboBox = new ComboBox<>();
                conditionComboBox.getItems().setAll(AnimalCondition.values());
                conditionComboBox.setValue(selectedAnimal.getCondition());

                VBox vbox = new VBox(10, animalNameField, speciesField, ageField, priceField, conditionComboBox);
                dialog.getDialogPane().setContent(vbox);

                ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

                Optional<ButtonType> result = dialog.showAndWait();

                if (result.isPresent() && result.get() == okButton) {
                    String newAnimalName = animalNameField.getText();
                    String newSpecies = speciesField.getText();
                    String newAgeInput = ageField.getText();
                    String newPriceInput = priceField.getText();

                    if (newAnimalName.isEmpty() || newSpecies.isEmpty() || newAgeInput.isEmpty() || newPriceInput.isEmpty()) {
                        throw new ValidationException("All fields must be filled.");
                    }

                    try {
                        int newAge = Integer.parseInt(newAgeInput);
                        double newPrice = Double.parseDouble(newPriceInput);

                        Animal newAnimal = new Animal(newAnimalName, newSpecies, conditionComboBox.getValue(), newAge, newPrice);

                        for (Animal animal : selectedShelter.getAnimals()) {
                            if (animal != selectedAnimal) {
                                if (animal.getName().equals(newAnimal.getName()) &&
                                        animal.getSpecies().equals(newAnimal.getSpecies()) &&
                                        animal.getAge() == newAnimal.getAge() &&
                                        animal.getPrice() == newAnimal.getPrice() &&
                                        animal.getCondition().equals(newAnimal.getCondition())) {
                                    throw new ValidationException("An animal with these details already exists.");
                                }
                            }
                        }

                        Session session = HibernateUtil.getSessionFactory().openSession();
                        Transaction transaction = null;

                        try {
                            transaction = session.beginTransaction();
                            selectedAnimal.setName(newAnimalName);
                            selectedAnimal.setSpecies(newSpecies);
                            selectedAnimal.setAge(newAge);
                            selectedAnimal.setPrice(newPrice);
                            selectedAnimal.setCondition(conditionComboBox.getValue());
                            if (selectedAnimal.getCondition() == AnimalCondition.ADOPTION) {
                                selectedAnimal.setAdopted(true);
                            } else {
                                selectedAnimal.setAdopted(false);
                            }

                            session.update(selectedAnimal);
                            transaction.commit();
                            animalTable.refresh();
                        } catch (Exception e) {
                            if (transaction != null) {
                                transaction.rollback();
                            }
                            throw new ValidationException("Error occurred while updating the animal.");
                        } finally {
                            session.close();
                        }
                    } catch (NumberFormatException e) {
                        throw new ValidationException("Invalid number format for age or price.");
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Animal Selected");
                alert.setHeaderText("Please select an animal to edit.");
                alert.showAndWait();
            }
        } catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void applyShelterFilters() {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            String filterText = shelterFilterTextField.getText().toLowerCase().trim();
            if (filterText.length() > 30) {
                throw new FilterException("Filter text is too long. Please use a shorter filter.");
            }

            String hql = "FROM AnimalShelter WHERE lower(shelterName) LIKE :filterText";
            var query = session.createQuery(hql, AnimalShelter.class);
            query.setParameter("filterText", "%" + filterText + "%");

            var filteredShelters = query.list();
            ObservableList<AnimalShelter> observableList = FXCollections.observableArrayList(filteredShelters);
            shelterTable.setItems(observableList);

        } catch (FilterException e) {
            showErrorAlert(e.getMessage());
        } catch (Exception e) {
            showErrorAlert("An error occurred while filtering shelters: " + e.getMessage());
        }
    }

    @FXML
    private void applyAnimalFilters() {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            String filterText = animalFilterTextField.getText().toLowerCase().trim();

            if (filterText.length() > 30) {
                throw new FilterException("Filter text is too long. Please use a shorter filter.");
            }

            AnimalShelter selectedShelter = shelterTable.getSelectionModel().getSelectedItem();
            if (selectedShelter == null) {
                throw new FilterException("Please select a shelter to filter animals.");
            }

            String hql = "FROM Animal WHERE shelter.id = :shelterId AND " + "(lower(name) LIKE :filterText OR lower(species) LIKE :filterText)";
            var query = session.createQuery(hql, Animal.class);
            query.setParameter("shelterId", selectedShelter.getId());
            query.setParameter("filterText", "%" + filterText + "%");

            var filteredAnimals = query.list();
            ObservableList<Animal> observableList = FXCollections.observableArrayList(filteredAnimals);
            animalTable.setItems(observableList);

        } catch (FilterException e) {
            showErrorAlert(e.getMessage());
        } catch (Exception e) {
            showErrorAlert("An error occurred while filtering animals: " + e.getMessage());
        }
    }

    @FXML
    private void applyAnimalFiltersByCondition() {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            AnimalCondition selectedCondition = stateComboBox.getValue();

            if (selectedCondition == null) {
                showErrorAlert("Please select a condition to filter animals.");
                return;
            }

            AnimalShelter selectedShelter = shelterTable.getSelectionModel().getSelectedItem();
            if (selectedShelter == null) {
                showErrorAlert("Please select a shelter first.");
                return;
            }

            String hql = "FROM Animal WHERE shelter.id = :shelterId AND condition = :condition";
            var query = session.createQuery(hql, Animal.class);
            query.setParameter("shelterId", selectedShelter.getId());
            query.setParameter("condition", selectedCondition);
            var filteredAnimals = query.list();

            ObservableList<Animal> observableList = FXCollections.observableArrayList(filteredAnimals);
            animalTable.setItems(observableList);

        } catch (Exception e) {
            showErrorAlert("An error occurred while filtering animals by condition: " + e.getMessage());
        }
    }

    @FXML
    private void handleSortSheltersByMaxCapacity(ActionEvent event) {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM AnimalShelter ORDER BY maxCapacity DESC";
            var query = session.createQuery(hql, AnimalShelter.class);

            var sortedShelters = query.list();
            ObservableList<AnimalShelter> observableList = FXCollections.observableArrayList(sortedShelters);
            shelterTable.setItems(observableList);

        } catch (Exception e) {
            showErrorAlert("An error occurred while sorting shelters: " + e.getMessage());
        }
    }

    @FXML
    private void handleMouseClickOutsideTextField(MouseEvent event) {
        shelterFilterTextField.getScene().getRoot().requestFocus();
        animalFilterTextField.getScene().getRoot().requestFocus();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}