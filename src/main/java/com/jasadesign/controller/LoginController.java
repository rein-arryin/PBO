package com.jasadesign.controller;

import com.jasadesign.config.AppConfig;
import com.jasadesign.model.User;
import com.jasadesign.service.AuthService;
import com.jasadesign.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        try {
            User user = authService.login(usernameField.getText(), passwordField.getText());
            openDashboard(user);
        } catch (IllegalArgumentException e) {
            AlertUtil.warning(e.getMessage());
        } catch (SQLException e) {
            AlertUtil.error("Gagal terhubung ke database: " + e.getMessage());
        } catch (IOException e) {
            AlertUtil.error("Gagal membuka halaman: " + e.getMessage());
        }
    }

    private void openDashboard(User user) throws IOException {
        boolean pembuat = "pembuat".equals(user.getRole());
        String fxml = pembuat ? "/fxml/Pembuat.fxml" : "/fxml/Pemesan.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();

        if (pembuat) {
            PembuatController controller = loader.getController();
            controller.setUser(user);
        } else {
            PemesanController controller = loader.getController();
            controller.setUser(user);
        }

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setTitle(AppConfig.APP_NAME + " - " + user.getInfo());
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}