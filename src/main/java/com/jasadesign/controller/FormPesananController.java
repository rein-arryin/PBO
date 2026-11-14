package com.jasadesign.controller;

import com.jasadesign.model.DesignRequest;
import com.jasadesign.model.User;
import com.jasadesign.service.DesignRequestService;
import com.jasadesign.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class FormPesananController {

    @FXML private TextField titleField;
    @FXML private ComboBox<String> typeCombo;
    @FXML private TextArea descArea;
    @FXML private TextField colorField;
    @FXML private TextField sizeField;
    @FXML private TextField refField;
    @FXML private DatePicker deadlinePicker;

    private final DesignRequestService service = new DesignRequestService();
    private User user;
    private Runnable onSaved;

    @FXML
    private void initialize() {
        typeCombo.getItems().addAll("Logo", "Poster", "Banner", "Kemasan", "Undangan", "Lainnya");
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOnSaved(Runnable onSaved) {
        this.onSaved = onSaved;
    }

    @FXML
    private void handleSave() {
        DesignRequest request = new DesignRequest(
                user.getId(),
                titleField.getText(),
                typeCombo.getValue(),
                descArea.getText(),
                colorField.getText(),
                sizeField.getText(),
                refField.getText(),
                deadlinePicker.getValue());

        try {
            service.createRequest(request);
            if (onSaved != null) {
                onSaved.run();
            }
            close();
        } catch (IllegalArgumentException e) {
            AlertUtil.warning(e.getMessage());
        } catch (SQLException e) {
            AlertUtil.error("Gagal menyimpan pesanan: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private void close() {
        ((Stage) titleField.getScene().getWindow()).close();
    }
}
