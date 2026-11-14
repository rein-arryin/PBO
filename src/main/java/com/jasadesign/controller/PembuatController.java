package com.jasadesign.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class PembuatController {

    @FXML
    private Button btnLogout;

    @FXML
    private void handleLogout() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/Login.fxml")
        );

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login - Jasa Desain");
    }
}

