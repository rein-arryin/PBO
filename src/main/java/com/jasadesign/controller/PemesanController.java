package com.jasadesign.controller;

import com.jasadesign.config.AppConfig;
import com.jasadesign.model.DesignRequest;
import com.jasadesign.model.User;
import com.jasadesign.service.DesignRequestService;
import com.jasadesign.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PemesanController {

    @FXML private Label userLabel;
    @FXML private Label totalLabel;
    @FXML private Label menungguLabel;
    @FXML private Label diprosesLabel;
    @FXML private Label selesaiLabel;

    @FXML private TableView<DesignRequest> table;
    @FXML private TableColumn<DesignRequest, Integer> colId;
    @FXML private TableColumn<DesignRequest, String> colTitle;
    @FXML private TableColumn<DesignRequest, String> colType;
    @FXML private TableColumn<DesignRequest, Object> colDeadline;
    @FXML private TableColumn<DesignRequest, String> colStatus;

    private final DesignRequestService service = new DesignRequestService();
    private User user;

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colType.setCellValueFactory(new PropertyValueFactory<>("designType"));
        colDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Kolom status ditampilkan sebagai badge berwarna
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    return;
                }
                Label badge = new Label(status);
                badge.getStyleClass().addAll("badge", badgeClass(status));
                setGraphic(badge);
            }
        });
    }

    // Dipanggil LoginController setelah FXML dimuat
    public void setUser(User user) {
        this.user = user;
        userLabel.setText(user.getInfo());
        loadData();
    }

    private void loadData() {
        try {
            List<DesignRequest> list = service.getRequestsByUser(user);
            table.setItems(FXCollections.observableArrayList(list));

            totalLabel.setText(String.valueOf(list.size()));
            menungguLabel.setText(String.valueOf(count(list, AppConfig.STATUS_MENUNGGU)));
            diprosesLabel.setText(String.valueOf(count(list, AppConfig.STATUS_DIPROSES)));
            selesaiLabel.setText(String.valueOf(count(list, AppConfig.STATUS_SELESAI)));
        } catch (SQLException e) {
            AlertUtil.error("Gagal memuat pesanan: " + e.getMessage());
        }
    }

    @FXML
    private void handleNew() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FormPesanan.fxml"));
            Parent root = loader.load();

            FormPesananController form = loader.getController();
            form.setUser(user);
            form.setOnSaved(this::loadData);

            Scene scene = new Scene(root, 480, 640);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            Stage dialog = new Stage();
            dialog.setTitle("Pesanan Baru");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(table.getScene().getWindow());
            dialog.setScene(scene);
            dialog.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Gagal membuka form: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            Scene scene = new Scene(root, 480, 360);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            Stage stage = (Stage) table.getScene().getWindow();
            stage.setTitle(AppConfig.APP_NAME);
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertUtil.error("Gagal kembali ke login: " + e.getMessage());
        }
    }

    private long count(List<DesignRequest> list, String status) {
        return list.stream().filter(r -> status.equals(r.getStatus())).count();
    }

    private String badgeClass(String status) {
        if (AppConfig.STATUS_DIPROSES.equals(status)) return "badge-diproses";
        if (AppConfig.STATUS_SELESAI.equals(status)) return "badge-selesai";
        return "badge-menunggu";
    }
}
