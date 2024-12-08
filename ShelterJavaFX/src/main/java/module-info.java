module com.example.shelterjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.naming;

    opens com.example.shelterjavafx.controller to javafx.fxml;
    opens com.example.shelterjavafx.model to org.hibernate.orm.core;
    exports com.example.shelterjavafx.application;
    exports com.example.shelterjavafx.model;
    exports com.example.shelterjavafx.data;
    opens com.example.shelterjavafx.data to org.hibernate.orm.core;
}
