package com.jasadesign.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class AlertUtil {

    private AlertUtil() {
    }

    public static void info(String message) {
        show(Alert.AlertType.INFORMATION, "Informasi", message);
    }

    public static void warning(String message) {
        show(Alert.AlertType.WARNING, "Peringatan", message);
    }

    public static void error(String message) {
        show(Alert.AlertType.ERROR, "Kesalahan", message);
    }

    public static boolean confirm(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText(null);
        return alert.showAndWait().filter(b -> b == ButtonType.YES).isPresent();
    }

    private static void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}