package com.example.shelterjavafx.application;

import com.example.shelterjavafx.model.Animal;
import com.example.shelterjavafx.model.AnimalShelter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.IOException;

public class ShelterApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        initHibernate();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/shelterjavafx/view/admin-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Shelter Manager");
        stage.setScene(scene);
        stage.show();
    }

    private void initHibernate() {
        try {
            SessionFactory factory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Animal.class)
                    .addAnnotatedClass(AnimalShelter.class)
                    .buildSessionFactory();

            System.out.println("Hibernate initialized successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Hibernate initialization failed!");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}