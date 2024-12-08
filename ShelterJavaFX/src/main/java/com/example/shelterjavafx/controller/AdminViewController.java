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

    // Inicjalizacja
    public void initialize() {
        sessionFactory = HibernateUtil.getSessionFactory(); // Używamy klasy HibernateUtil do uzyskania sesji

        ObservableList<AnimalCondition> conditions = FXCollections.observableArrayList(AnimalCondition.values());
        stateComboBox.setItems(conditions);

        // Pobieranie danych z bazy
        List<AnimalShelter> sheltersFromDB = getSheltersFromDatabase();

        // Inicjalizacja tabel
        shelterNameColumn.setCellValueFactory(new PropertyValueFactory<>("shelterName"));
        maxCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
        ratingColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFormattedAverageRating()));

        shelterTable.getItems().setAll(sheltersFromDB);

        // Inicjalizacja tabeli zwierząt
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
        // Otwórz okno wyboru pliku
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(null); // Zwraca wybrany plik

        if (file != null) {
            try {
                // Pobierz wszystkie schroniska
                ShelterDataCSV.exportAllSheltersToCSV(file.getAbsolutePath());
                showInfoAlert("Eksport zakończony", "Dane zostały pomyślnie wyeksportowane do pliku CSV.");
            } catch (IOException e) {
                showErrorAlert("Wystąpił problem podczas eksportowania danych do pliku CSV.");
            }
        }
    }


    @FXML
    private void handleImportButtonClick(ActionEvent event) {
        // Otwórz okno wyboru pliku
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(null);  // Zwraca wybrany plik

        if (file != null) {
            try {
                ShelterDataCSV.importShelterFromCSV(file.getAbsolutePath());
                updateShelterTable();
                showInfoAlert("Import zakończony", "Dane zostały pomyślnie zaimportowane z pliku CSV do bazy danych.");
            } catch (IOException e) {
                showErrorAlert("Wystąpił problem podczas importowania danych z pliku CSV.");
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

    // Pobieranie schronisk z bazy danych
    private List<AnimalShelter> getSheltersFromDatabase() {
        Session session = sessionFactory.openSession();
        Query<AnimalShelter> query = session.createQuery("FROM AnimalShelter", AnimalShelter.class);
        List<AnimalShelter> shelters = query.getResultList();
        session.close();
        return shelters;
    }

    private void updateAnimalTable(AnimalShelter shelter) {
        Session session = sessionFactory.openSession();
        Query<Animal> query = session.createQuery("FROM Animal WHERE shelter = :shelter", Animal.class);
        query.setParameter("shelter", shelter);
        List<Animal> animals = query.getResultList();
        animalTable.getItems().setAll(animals);
        session.close();
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

                    // Sprawdzenie, czy schronisko o tej samej nazwie już istnieje w bazie
                    Session session = sessionFactory.openSession();
                    List<AnimalShelter> existingShelters = session.createQuery("FROM AnimalShelter WHERE shelterName = :shelterName", AnimalShelter.class)
                            .setParameter("shelterName", shelterName)
                            .getResultList();

                    if (!existingShelters.isEmpty()) {
                        // Użycie showErrorAlert, aby pokazać komunikat, jeśli schronisko już istnieje
                        showErrorAlert("A shelter with the same name already exists in the database.");
                        session.close();
                        return; // Zakończ dodawanie schroniska
                    }

                    // Tworzenie nowego schroniska
                    AnimalShelter newShelter = new AnimalShelter(shelterName, maxCapacity);

                    // Zapisanie schroniska do bazy danych
                    session.beginTransaction();
                    session.save(newShelter);
                    session.getTransaction().commit();
                    session.close();

                    // Uaktualnianie tabeli
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
                // Usuwanie schroniska z bazy danych
                Session session = sessionFactory.openSession();
                session.beginTransaction();
                session.delete(selectedShelter);
                session.getTransaction().commit();
                session.close();

                // Uaktualnianie tabeli
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

                    // Sprawdzenie, czy zwierzę już istnieje w schronisku
                    boolean animalExists = selectedShelter.getAnimals().stream()
                            .anyMatch(existingAnimal -> existingAnimal.compareTo(newAnimal) == 0);

                    if (animalExists) {
                        // Jeśli zwierzę istnieje, pokaż komunikat w okienku
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Animal Exists");
                        alert.setHeaderText("This Animal Already Exists");
                        alert.setContentText("An animal with the same name, species, and age already exists in the shelter.");
                        alert.showAndWait();
                        return; // Zakończ dodawanie zwierzęcia
                    }

                    // Zapisywanie nowego zwierzęcia do bazy danych za pomocą Hibernate
                    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                        session.beginTransaction();

                        // Dodanie zwierzęcia do schroniska w bazie
                        selectedShelter.addAnimal(newAnimal, session);
                        session.save(newAnimal); // Zapisz zwierzę do bazy danych

                        session.getTransaction().commit();

                        // Odświeżenie tabeli z zwierzętami i schroniskami
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
                // Wyświetlenie okna dialogowego potwierdzającego usunięcie zwierzęcia
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Remove Animal");
                alert.setHeaderText("Are you sure you want to remove this animal?");
                alert.setContentText("This action cannot be undone.");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Otwieranie sesji Hibernate i rozpoczynanie transakcji
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    Transaction transaction = null;

                    try {
                        // Rozpoczęcie transakcji
                        transaction = session.beginTransaction();

                        // Usuwanie zwierzęcia z bazy danych
                        session.delete(selectedAnimal);

                        // Zatwierdzenie transakcji
                        transaction.commit();

                        // Usuwanie zwierzęcia z listy w pamięci
                        selectedShelter.getAnimals().remove(selectedAnimal);

                        // Zaktualizowanie tabeli zwierząt i schronisk
                        updateAnimalTable(selectedShelter);
                        updateShelterTable();

                    } catch (Exception e) {
                        // W przypadku błędu, wycofanie transakcji
                        if (transaction != null) {
                            transaction.rollback();
                        }
                        e.printStackTrace();
                        throw new ValidationException("Error occurred while removing the animal.");
                    } finally {
                        // Zamknięcie sesji
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
                                .setParameter("selectedShelterId", selectedShelter.getId()) // Wykluczamy to samo schronisko
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

                        // Sprawdzenie, czy takie zwierzę już istnieje w schronisku
                        for (Animal animal : selectedShelter.getAnimals()) {
                            // Sprawdzamy, czy zwierzę jest inne niż to, które edytujemy
                            if (animal != selectedAnimal) {
                                // Porównujemy wszystkie istotne pola (name, species, age, price, condition)
                                if (animal.getName().equals(newAnimal.getName()) &&
                                        animal.getSpecies().equals(newAnimal.getSpecies()) &&
                                        animal.getAge() == newAnimal.getAge() &&
                                        animal.getPrice() == newAnimal.getPrice() &&
                                        animal.getCondition().equals(newAnimal.getCondition())) {
                                    throw new ValidationException("An animal with these details already exists.");
                                }
                            }
                        }

                        // Otwarcie sesji Hibernate
                        Session session = HibernateUtil.getSessionFactory().openSession();
                        Transaction transaction = null;

                        try {
                            // Rozpoczęcie transakcji
                            transaction = session.beginTransaction();

                            // Aktualizacja obiektu zwierzęcia w bazie danych
                            selectedAnimal.setName(newAnimalName);
                            selectedAnimal.setSpecies(newSpecies);
                            selectedAnimal.setAge(newAge);
                            selectedAnimal.setPrice(newPrice);
                            selectedAnimal.setCondition(conditionComboBox.getValue());

                            // Zapisanie zmian w bazie danych
                            session.update(selectedAnimal);

                            // Zatwierdzenie transakcji
                            transaction.commit();

                            // Zaktualizowanie tabeli
                            animalTable.refresh();
                        } catch (Exception e) {
                            // W przypadku błędu, wycofanie transakcji
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

            // Query schronisk z filtrem
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

            // Query zwierząt powiązanych z wybranym schroniskiem
            String hql = "FROM Animal WHERE shelter.id = :shelterId AND " +
                    "(lower(name) LIKE :filterText OR lower(species) LIKE :filterText)";
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
                // Jeśli stan nie został wybrany, po prostu wyświetl wszystkie zwierzęta
                showErrorAlert("Please select a condition to filter animals.");
                return;
            }

            AnimalShelter selectedShelter = shelterTable.getSelectionModel().getSelectedItem();
            if (selectedShelter == null) {
                showErrorAlert("Please select a shelter first.");
                return;
            }

            // Zapytanie HQL, które filtruje zwierzęta na podstawie schroniska i stanu
            String hql = "FROM Animal WHERE shelter.id = :shelterId AND condition = :condition";
            var query = session.createQuery(hql, Animal.class);
            query.setParameter("shelterId", selectedShelter.getId());
            query.setParameter("condition", selectedCondition);

            // Wykonanie zapytania
            var filteredAnimals = query.list();

            // Przekształcenie wyników do ObservableList
            ObservableList<Animal> observableList = FXCollections.observableArrayList(filteredAnimals);
            animalTable.setItems(observableList);

        } catch (Exception e) {
            showErrorAlert("An error occurred while filtering animals by condition: " + e.getMessage());
        }
    }

    @FXML
    private void handleSortSheltersByMaxCapacity(ActionEvent event) {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            // Query schronisk posortowanych po maksymalnej pojemności
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