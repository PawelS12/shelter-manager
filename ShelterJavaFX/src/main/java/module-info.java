module com.example.shelterjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.example.shelterjavafx.controller to javafx.fxml;
    exports com.example.shelterjavafx.application;
    exports com.example.shelterjavafx.model;
}
