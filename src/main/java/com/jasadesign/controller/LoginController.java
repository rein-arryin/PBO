package com.jasadesign.controller;

import com.jasadesign.dao.UserDAO;
import com.jasadesign.model.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import com.jasadesign.controller.PemesanController;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblMessage;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {

        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Username dan password wajib diisi.");
            return;
        }

        try {
            User user = userDAO.login(username, password);

            if (user == null) {
                lblMessage.setText("Username atau password salah.");
                return;
            }

            String fxml;

            switch (user.getRole()) {

                case "pemesan" -> fxml = "/fxml/Pemesan.fxml";

                case "pembuat" -> fxml = "/fxml/Pembuat.fxml";

                default -> {
                    lblMessage.setText("Role pengguna tidak dikenal.");
                    return;
                }
            }

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(fxml)
            );

            Parent page = loader.load();

            if (user.getRole().equals("pemesan")) {
                PemesanController controller = loader.getController();
                controller.setUser(user);
            }

            Scene scene = new Scene(page);

            Stage stage = (Stage) btnLogin.getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Dashboard - Jasa Desain");
            stage.setMaximized(true);
            stage.show();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            lblMessage.setText("Terjadi kesalahan: " + e.getMessage());
        }
    }
}
