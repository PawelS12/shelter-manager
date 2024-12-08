package com.example.shelterjavafx.controller;

import com.example.shelterjavafx.exception.AdoptionException;
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
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

        shelterFilterTextField.setOnAction(event -> applyShelterFilters());
        animalFilterTextField.setOnAction(event -> applyAnimalFilters());

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

        loadSheltersFromDatabase();

        shelterTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateAnimalTable(newValue);
            }
        });
    }

    private List<AnimalShelter> getSheltersFromDatabase() {
        Session session = sessionFactory.openSession();
        Query<AnimalShelter> query = session.createQuery("FROM AnimalShelter", AnimalShelter.class);
        List<AnimalShelter> shelters = query.getResultList();
        session.close();
        return shelters;
    }

    @FXML
    private void handleRateButton(ActionEvent event) {
        // Sprawdzenie, czy wybrane jest schronisko w tabeli
        AnimalShelter selectedShelter = shelterTable.getSelectionModel().getSelectedItem();

        try {
            if (selectedShelter == null) {
                throw new ValidationException("No shelter selected.");
            }

            // Tworzymy okno dialogowe
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Shelter Rating");
            dialog.setHeaderText("Rate the selected shelter");

            // Tworzymy kontrolki do wprowadzenia oceny i komentarza
            TextField ratingField = new TextField();
            ratingField.setPromptText("Rating (0-5)");

            TextArea commentArea = new TextArea();
            commentArea.setPromptText("Comment...");

            // Dodajemy kontrolki do okna dialogowego
            VBox vbox = new VBox(10, new Label("Rating (0-5):"), ratingField, new Label("Comment:"), commentArea);
            dialog.getDialogPane().setContent(vbox);

            // Przyciski OK i Cancel
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

            // Pokazujemy okno dialogowe
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == okButton) {
                String ratingText = ratingField.getText();
                String comment = commentArea.getText();

                // Sprawdzanie poprawności oceny
                try {
                    int ratingValue = Integer.parseInt(ratingText);
                    if (ratingValue < 0 || ratingValue > 5) {
                        throw new ValidationException("The rating must be a number between 0 and 5.");
                    }

                    // Dodanie oceny do bazy danych
                    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                        Transaction transaction = session.beginTransaction();

                        // Tworzymy nową ocenę
                        Rating rating = new Rating(ratingValue, selectedShelter, new java.util.Date(), comment);
                        selectedShelter.getRatings().add(rating);  // Dodanie oceny do listy ocen schroniska
                        session.save(rating);  // Zapisanie oceny w bazie danych

                        transaction.commit();
                        updateShelterTable();

                        showInfoAlert("Success", "The rating was successfully saved.");
                    }
                } catch (NumberFormatException e) {
                    throw new ValidationException("The rating must be a valid number between 0 and 5.");
                }
            }
        } catch (ValidationException e) {
            // Wyświetlenie błędu w przypadku problemu
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Validation Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void updateShelterTable() {
        Session session = sessionFactory.openSession();
        Query<AnimalShelter> query = session.createQuery("FROM AnimalShelter", AnimalShelter.class);
        List<AnimalShelter> shelters = query.getResultList();
        shelterTable.getItems().setAll(shelters);
        session.close();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    private void loadSheltersFromDatabase() {
        try (Session session = sessionFactory.openSession()) {
            List<AnimalShelter> shelters = session.createQuery("FROM AnimalShelter", AnimalShelter.class).list();
            shelterTable.setItems(FXCollections.observableArrayList(shelters));
        } catch (Exception e) {
            showErrorAlert("Failed to load shelters from database: " + e.getMessage());
        }
    }

    @FXML
    private void handleAdoptButtonClick(ActionEvent event) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Animal selectedAnimal = animalTable.getSelectionModel().getSelectedItem();
            if (selectedAnimal == null) {
                throw new AdoptionException("Please select an animal for adoption.");
            }

            if (selectedAnimal.isAdopted()) {
                throw new AdoptionException("This animal has already been adopted.");
            }

            Dialog<String[]> dialog = createAdoptionDialog();
            Optional<String[]> result = dialog.showAndWait();

            result.ifPresent(data -> {
                try {
                    String name = data[0];
                    String surname = data[1];
                    String phone = data[2];
                    String email = data[3];

                    // Walidacja danych użytkownika
                    validateAdoptionInputs(name, surname, phone, email);

                    // Tworzenie nowego użytkownika i zapisanie go do bazy
                    User user = new User(name, surname);
                    session.save(user);

                    // Ustawienie zwierzęcia jako adoptowane
                    selectedAnimal.setAdopted(true);
                    selectedAnimal.setCondition(AnimalCondition.ADOPTION); // Zmiana stanu na ADOPTION
                    session.update(selectedAnimal);

                    transaction.commit();

                    // Wyświetlenie komunikatu o powodzeniu
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Adoption successful!", ButtonType.OK);
                    successAlert.showAndWait();

                    // Aktualizacja tabeli zwierząt
                    updateAnimalTable(shelterTable.getSelectionModel().getSelectedItem());
                } catch (AdoptionException e) {
                    showErrorAlert(e.getMessage());
                }
            });
        } catch (Exception e) {
            showErrorAlert("Failed to process adoption: " + e.getMessage());
        }
    }

    private Dialog<String[]> createAdoptionDialog() {
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
                return new String[]{nameField.getText(), surnameField.getText(), phoneField.getText(), emailField.getText()};
            }
            return null;
        });

        return dialog;
    }

    private void validateAdoptionInputs(String name, String surname, String phone, String email) throws AdoptionException {
        if (name.isEmpty() || surname.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            throw new AdoptionException("All fields must be filled.");
        }

        if (!phone.matches("\\d{9}")) {
            throw new AdoptionException("Invalid phone number format. Please enter a 9-digit number.");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new AdoptionException("Invalid email address format.");
        }
    }

    private void updateAnimalTable(AnimalShelter shelter) {
        try (Session session = sessionFactory.openSession()) {
            List<Animal> animals = session.createQuery("FROM Animal WHERE shelter.id = :shelterId", Animal.class)
                    .setParameter("shelterId", shelter.getId())
                    .list();
            animalTable.setItems(FXCollections.observableArrayList(animals));
        } catch (Exception e) {
            showErrorAlert("Failed to load animals from database: " + e.getMessage());
        }
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
            showErrorAlert("Failed to load login view: " + e.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}