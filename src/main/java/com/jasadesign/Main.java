package com.jasadesign;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/Login.fxml")
            );

            Scene scene = new Scene(loader.load());

            stage.setTitle("Sistem Pemesanan Jasa Desain");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

