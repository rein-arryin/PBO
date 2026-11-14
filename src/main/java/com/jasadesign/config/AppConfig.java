package com.jasadesign.config;

public final class AppConfig {

    public static final String APP_NAME = "Jasa Desain";

    public static final String DB_URL = "jdbc:mariadb://localhost:3306/jasa_desain";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "root123";

    public static final String STATUS_MENUNGGU = "Menunggu";
    public static final String STATUS_DIPROSES = "Diproses";
    public static final String STATUS_SELESAI = "Selesai";

    private AppConfig() {
    }
}
