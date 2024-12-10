package com.example.shelterjavafx.application;

import com.example.shelterjavafx.model.Animal;
import com.example.shelterjavafx.model.AnimalShelter;
import com.example.shelterjavafx.model.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ShelterApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        HibernateUtil.getSessionFactory();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/shelterjavafx/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Shelter Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}