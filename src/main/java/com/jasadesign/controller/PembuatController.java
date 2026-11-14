package com.jasadesign.controller;

import com.jasadesign.config.AppConfig;
import com.jasadesign.model.DesignRequest;
import com.jasadesign.model.User;
import com.jasadesign.service.AuthService;
import com.jasadesign.service.DesignRequestService;
import com.jasadesign.util.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PembuatController {

    private static final String SEMUA = "Semua";

    @FXML private Label userLabel;
    @FXML private Label totalLabel;
    @FXML private Label menungguLabel;
    @FXML private Label diprosesLabel;
    @FXML private Label selesaiLabel;
    @FXML private ComboBox<String> filterCombo;

    @FXML private TableView<DesignRequest> table;
    @FXML private TableColumn<DesignRequest, Integer> colId;
    @FXML private TableColumn<DesignRequest, String> colTitle;
    @FXML private TableColumn<DesignRequest, String> colPemesan;
    @FXML private TableColumn<DesignRequest, String> colType;
    @FXML private TableColumn<DesignRequest, Object> colDeadline;
    @FXML private TableColumn<DesignRequest, String> colStatus;

    private final DesignRequestService service = new DesignRequestService();
    private final AuthService authService = new AuthService();
    private final Map<Integer, String> nameCache = new HashMap<>();
    private User user;

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colType.setCellValueFactory(new PropertyValueFactory<>("designType"));
        colDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPemesan.setCellValueFactory(d ->
                new SimpleStringProperty(nameOf(d.getValue().getUserId())));

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

        filterCombo.getItems().addAll(SEMUA, AppConfig.STATUS_MENUNGGU,
                AppConfig.STATUS_DIPROSES, AppConfig.STATUS_SELESAI);
        filterCombo.setValue(SEMUA);

        // Klik dua kali pada baris = buka detail
        table.setRowFactory(tv -> {
            javafx.scene.control.TableRow<DesignRequest> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    handleDetail();
                }
            });
            return row;
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
            // Statistik selalu dihitung dari semua pesanan
            List<DesignRequest> all = service.getAllRequests();
            totalLabel.setText(String.valueOf(all.size()));
            menungguLabel.setText(String.valueOf(count(all, AppConfig.STATUS_MENUNGGU)));
            diprosesLabel.setText(String.valueOf(count(all, AppConfig.STATUS_DIPROSES)));
            selesaiLabel.setText(String.valueOf(count(all, AppConfig.STATUS_SELESAI)));

            // Tabel mengikuti filter (overloading getAllRequests)
            String filter = filterCombo.getValue();
            List<DesignRequest> shown = (filter == null || SEMUA.equals(filter))
                    ? all
                    : service.getAllRequests(filter);
            table.setItems(FXCollections.observableArrayList(shown));
        } catch (SQLException e) {
            AlertUtil.error("Gagal memuat pesanan: " + e.getMessage());
        }
    }

    @FXML
    private void handleFilter() {
        if (user != null) {
            loadData();
        }
    }

    @FXML
    private void handleDetail() {
        DesignRequest selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.warning("Pilih salah satu pesanan terlebih dahulu.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DetailPesanan.fxml"));
            Parent root = loader.load();

            DetailPesananController detail = loader.getController();
            detail.setData(user, selected, nameOf(selected.getUserId()));
            detail.setOnSaved(this::loadData);

            Scene scene = new Scene(root, 500, 620);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            Stage dialog = new Stage();
            dialog.setTitle("Detail Pesanan #" + selected.getId());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(table.getScene().getWindow());
            dialog.setScene(scene);
            dialog.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Gagal membuka detail: " + e.getMessage());
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

    // Username pemesan, disimpan di cache agar tidak query berulang
    private String nameOf(int userId) {
        if (nameCache.containsKey(userId)) {
            return nameCache.get(userId);
        }
        String name = "#" + userId;
        try {
            User u = authService.getUser(userId);
            if (u != null) {
                name = u.getUsername();
            }
        } catch (SQLException ignored) {
            // pakai nama cadangan
        }
        nameCache.put(userId, name);
        return name;
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