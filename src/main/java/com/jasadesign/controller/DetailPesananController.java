package com.jasadesign.controller;

import com.jasadesign.config.AppConfig;
import com.jasadesign.model.DesignRequest;
import com.jasadesign.model.User;
import com.jasadesign.service.DesignRequestService;
import com.jasadesign.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class DetailPesananController {

    @FXML private Label titleLabel;
    @FXML private Label pemesanLabel;
    @FXML private Label typeLabel;
    @FXML private Label descLabel;
    @FXML private Label colorLabel;
    @FXML private Label sizeLabel;
    @FXML private Label refLabel;
    @FXML private Label deadlineLabel;
    @FXML private Label createdLabel;
    @FXML private ComboBox<String> statusCombo;
    @FXML private Button saveButton;

    private final DesignRequestService service = new DesignRequestService();
    private User actor;
    private DesignRequest request;
    private Runnable onSaved;

    @FXML
    private void initialize() {
        statusCombo.getItems().addAll(AppConfig.STATUS_MENUNGGU,
                AppConfig.STATUS_DIPROSES, AppConfig.STATUS_SELESAI);
    }

    public void setData(User actor, DesignRequest request, String pemesanName) {
        this.actor = actor;
        this.request = request;

        titleLabel.setText(request.getTitle());
        pemesanLabel.setText(pemesanName);
        typeLabel.setText(request.getDesignType());
        descLabel.setText(request.getDescription());
        colorLabel.setText(orDash(request.getColor()));
        sizeLabel.setText(orDash(request.getSize()));
        refLabel.setText(orDash(request.getReferenceLink()));
        deadlineLabel.setText(request.getDeadline() == null ? "-" : request.getDeadline().toString());
        createdLabel.setText(request.getCreatedAt() == null ? "-"
                : request.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")));
        statusCombo.setValue(request.getStatus());
    }

    public void setOnSaved(Runnable onSaved) {
        this.onSaved = onSaved;
    }

    @FXML
    private void handleSave() {
        try {
            service.updateStatus(actor, request.getId(), statusCombo.getValue());
            if (onSaved != null) {
                onSaved.run();
            }
            handleClose();
        } catch (IllegalArgumentException e) {
            AlertUtil.warning(e.getMessage());
        } catch (SQLException e) {
            AlertUtil.error("Gagal mengubah status: " + e.getMessage());
        }
    }

    @FXML
    private void handleClose() {
        ((Stage) titleLabel.getScene().getWindow()).close();
    }

    private String orDash(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }
}