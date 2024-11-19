module com.example.shelterjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.shelterjavafx.controller to javafx.fxml;
    exports com.example.shelterjavafx.application;
    exports com.example.shelterjavafx.model;
}
