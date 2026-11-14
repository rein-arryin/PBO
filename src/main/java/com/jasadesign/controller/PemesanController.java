
package com.jasadesign.controller;

import com.jasadesign.dao.DesignRequestDAO;
import com.jasadesign.model.DesignRequest;
import com.jasadesign.model.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PemesanController {

    @FXML
    private BorderPane root;

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnLogout;

    private final DesignRequestDAO requestDAO = new DesignRequestDAO();

    private User currentUser;

    private final ObservableList<DesignRequest> requestList =
            FXCollections.observableArrayList();

    // Menerima data pengguna dari LoginController
    public void setUser(User user) {
        this.currentUser = user;
        showDashboard();
    }

    // =========================
    // DASHBOARD
    // =========================

    @FXML
    private void handleDashboard() {
        showDashboard();
    }

    private void showDashboard() {

        VBox page = new VBox(22);

        Label title = new Label("Dashboard Pemesan");
        title.getStyleClass().add("page-title");

        String username = currentUser != null
                ? currentUser.getUsername()
                : "Pengguna";

        Label subtitle = new Label(
                "Selamat datang, " + username + "! Kelola pesanan desain kamu di sini."
        );
        subtitle.getStyleClass().add("page-subtitle");

        HBox statistics = new HBox(15);

        Label totalValue = new Label("0");
        Label processValue = new Label("0");
        Label completedValue = new Label("0");

        VBox totalCard = createStatCard("Total Pesanan", totalValue);
        VBox processCard = createStatCard("Dalam Proses", processValue);
        VBox completedCard = createStatCard("Selesai", completedValue);

        statistics.getChildren().addAll(
                totalCard,
                processCard,
                completedCard
        );

        Button createButton = new Button("+ Buat Pesanan");
        createButton.getStyleClass().add("primary-button");
        createButton.setOnAction(event -> showCreateForm());

        VBox content = new VBox(15);

        Label sectionTitle = new Label("Pesanan Saya");
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label sectionSubtitle = new Label(
                "Daftar pesanan desain terbaru kamu."
        );
        sectionSubtitle.getStyleClass().add("page-subtitle");

        HBox heading = new HBox(10);
        VBox headingText = new VBox(4, sectionTitle, sectionSubtitle);
        HBox.setHgrow(headingText, Priority.ALWAYS);
        heading.getChildren().addAll(headingText, createButton);

        TableView<DesignRequest> table = createRequestTable();

        content.getStyleClass().add("content-card");
        content.getChildren().addAll(heading, table);
        VBox.setVgrow(table, Priority.ALWAYS);

        page.getChildren().addAll(title, subtitle, statistics, content);
        VBox.setVgrow(content, Priority.ALWAYS);

        contentArea.getChildren().setAll(page);

        loadRequests(totalValue, processValue, completedValue);
    }

    private VBox createStatCard(String label, Label value) {

        Label title = new Label(label);
        title.getStyleClass().add("stat-label");

        value.getStyleClass().add("stat-value");

        VBox card = new VBox(10, title, value);
        card.getStyleClass().add("stat-card");

        HBox.setHgrow(card, Priority.ALWAYS);

        return card;
    }

    // =========================
    // FORM BUAT PESANAN
    // =========================

    @FXML
    private void handleBuatPesanan() {
        showCreateForm();
    }

    private void showCreateForm() {

        Label title = new Label("Buat Pesanan Desain");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label(
                "Isi informasi desain yang ingin kamu pesan."
        );
        subtitle.getStyleClass().add("page-subtitle");

        TextField txtTitle = new TextField();
        txtTitle.setPromptText("Contoh: Desain Logo Toko");

        ComboBox<String> cmbType = new ComboBox<>();
        cmbType.getItems().addAll(
                "Logo",
                "Poster",
                "Banner",
                "Brosur",
                "Undangan",
                "Lainnya"
        );
        cmbType.setPromptText("Pilih jenis desain");
        cmbType.setMaxWidth(Double.MAX_VALUE);

        TextArea txtDescription = new TextArea();
        txtDescription.setPromptText("Jelaskan kebutuhan desain...");
        txtDescription.setPrefRowCount(4);
        txtDescription.setWrapText(true);

        TextField txtColor = new TextField();
        txtColor.setPromptText("Contoh: Biru, putih, hitam");

        TextField txtSize = new TextField();
        txtSize.setPromptText("Contoh: A4 atau 1080 x 1080 px");

        TextField txtReference = new TextField();
        txtReference.setPromptText("Link referensi desain (opsional)");

        DatePicker dateDeadline = new DatePicker();
        dateDeadline.setPromptText("Pilih deadline");
        dateDeadline.setMaxWidth(Double.MAX_VALUE);
        dateDeadline.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                }
            }
        });

        Label message = new Label();
        message.setWrapText(true);

        Button saveButton = new Button("Simpan Pesanan");
        saveButton.getStyleClass().add("primary-button");

        Button cancelButton = new Button("Batal");
        cancelButton.setOnAction(event -> showDashboard());

        saveButton.setOnAction(event -> {

            if (currentUser == null) {
                showMessage(message, "Pengguna belum teridentifikasi.");
                return;
            }

            String name = txtTitle.getText().trim();
            String type = cmbType.getValue();
            String description = txtDescription.getText().trim();

            if (name.isEmpty()
                    || type == null
                    || description.isEmpty()) {

                showMessage(
                        message,
                        "Nama pesanan, jenis desain, dan deskripsi wajib diisi."
                );
                return;
            }

            LocalDate deadline = dateDeadline.getValue();

            if (deadline != null && deadline.isBefore(LocalDate.now())) {
                showMessage(message, "Deadline tidak boleh tanggal yang sudah lewat.");
                return;
            }

            DesignRequest request = new DesignRequest(
                    0,
                    currentUser.getId(),
                    name,
                    type,
                    description,
                    txtColor.getText().trim(),
                    txtSize.getText().trim(),
                    txtReference.getText().trim(),
                    deadline,
                    "Menunggu"
            );

            try {
                boolean success = requestDAO.create(request);

                if (success) {
                    showDashboard();
                    showAlert(
                            "Berhasil",
                            "Pesanan desain berhasil disimpan."
                    );
                } else {
                    showMessage(message, "Pesanan gagal disimpan.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showMessage(
                        message,
                        "Terjadi kesalahan database: " + e.getMessage()
                );
            }
        });

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(12);

        form.add(new Label("Nama Pesanan *"), 0, 0);
        form.add(txtTitle, 1, 0);

        form.add(new Label("Jenis Desain *"), 0, 1);
        form.add(cmbType, 1, 1);

        form.add(new Label("Deskripsi *"), 0, 2);
        form.add(txtDescription, 1, 2);

        form.add(new Label("Warna"), 0, 3);
        form.add(txtColor, 1, 3);

        form.add(new Label("Ukuran"), 0, 4);
        form.add(txtSize, 1, 4);

        form.add(new Label("Referensi"), 0, 5);
        form.add(txtReference, 1, 5);

        form.add(new Label("Deadline"), 0, 6);
        form.add(dateDeadline, 1, 6);

        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setMinWidth(120);

        ColumnConstraints inputColumn = new ColumnConstraints();
        inputColumn.setHgrow(Priority.ALWAYS);
        inputColumn.setFillWidth(true);

        form.getColumnConstraints().addAll(labelColumn, inputColumn);

        VBox card = new VBox(18, form, message);
        card.getStyleClass().add("content-card");

        HBox buttons = new HBox(10, saveButton, cancelButton);

        VBox page = new VBox(
                18,
                title,
                subtitle,
                card,
                buttons
        );

        contentArea.getChildren().setAll(page);
    }

    // =========================
    // PESANAN SAYA
    // =========================

    @FXML
    private void handlePesananSaya() {
        showMyRequests();
    }

    private void showMyRequests() {

        Label title = new Label("Pesanan Saya");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label(
                "Semua pesanan desain yang telah kamu ajukan."
        );
        subtitle.getStyleClass().add("page-subtitle");

        TableView<DesignRequest> table = createRequestTable();

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> loadTableData(table));

        VBox card = new VBox(15, refreshButton, table);
        card.getStyleClass().add("content-card");
        VBox.setVgrow(table, Priority.ALWAYS);

        VBox page = new VBox(18, title, subtitle, card);
        VBox.setVgrow(card, Priority.ALWAYS);

        contentArea.getChildren().setAll(page);

        loadTableData(table);
    }

    // =========================
    // TABLE PESANAN
    // =========================

    private TableView<DesignRequest> createRequestTable() {

        TableView<DesignRequest> table = new TableView<>();

        TableColumn<DesignRequest, Integer> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("id")
        );

        TableColumn<DesignRequest, String> nameColumn =
                new TableColumn<>("Nama Pesanan");

        nameColumn.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("title")
        );

        TableColumn<DesignRequest, String> typeColumn =
                new TableColumn<>("Jenis Desain");

        typeColumn.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("designType")
        );

        TableColumn<DesignRequest, LocalDate> deadlineColumn =
                new TableColumn<>("Deadline");

        deadlineColumn.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("deadline")
        );

        TableColumn<DesignRequest, String> statusColumn =
                new TableColumn<>("Status");

        statusColumn.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("status")
        );

        table.getColumns().addAll(
                idColumn,
                nameColumn,
                typeColumn,
                deadlineColumn,
                statusColumn
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        VBox.setVgrow(table, Priority.ALWAYS);

        return table;
    }

    // =========================
    // LOAD DATA DATABASE
    // =========================

    private void loadRequests(
            Label totalValue,
            Label processValue,
            Label completedValue
    ) {

        if (currentUser == null) {
            return;
        }

        try {
            List<DesignRequest> requests =
                    requestDAO.getByUserId(currentUser.getId());

            long total = requests.size();

            long completed = requests.stream()
                    .filter(request ->
                            "Selesai".equalsIgnoreCase(request.getStatus()))
                    .count();

            long inProcess = requests.stream()
                    .filter(request ->
                            !"Selesai".equalsIgnoreCase(request.getStatus())
                                    && !"Menunggu".equalsIgnoreCase(request.getStatus()))
                    .count();

            totalValue.setText(String.valueOf(total));
            processValue.setText(String.valueOf(inProcess));
            completedValue.setText(String.valueOf(completed));

            requestList.setAll(requests);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(
                    "Database Error",
                    "Gagal mengambil data pesanan: " + e.getMessage()
            );
        }
    }

    private void loadTableData(TableView<DesignRequest> table) {

        if (currentUser == null) {
            showAlert("Peringatan", "Pengguna belum teridentifikasi.");
            return;
        }

        try {
            List<DesignRequest> requests =
                    requestDAO.getByUserId(currentUser.getId());

            table.setItems(
                    FXCollections.observableArrayList(requests)
            );

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(
                    "Database Error",
                    "Gagal memuat pesanan: " + e.getMessage()
            );
        }
    }

    // =========================
    // LOGOUT
    // =========================

    @FXML
    private void handleLogout() throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/Login.fxml")
        );

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) btnLogout.getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Login - Jasa Desain");
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    // =========================
    // HELPER
    // =========================

    private void showMessage(Label label, String message) {
        label.setText(message);
    }

    private void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}