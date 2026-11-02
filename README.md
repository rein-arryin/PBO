# Penyimpanan Gudang

Aplikasi desktop untuk mengelola data barang pada gudang yang dibuat menggunakan **Java 21, JavaFX, Maven, dan MariaDB**.

## 1. Persyaratan

Sebelum menjalankan aplikasi, pastikan sudah menginstal:

- Java JDK 21
- Git
- IntelliJ IDEA (opsional, jika ingin menggunakan IDE)
- MariaDB atau XAMPP
- Maven Wrapper (sudah tersedia di dalam project)

## 2. Clone Repository

Buka PowerShell atau terminal, kemudian jalankan:

```powershell
git clone URL_REPOSITORY
```

Masuk ke folder project:

```powershell
cd PenyimpananGudang
```

Ganti `URL_REPOSITORY` dengan URL repository GitHub project ini.

## 5. Menjalankan JavaFX di Windows

Pastikan terminal berada di dalam folder utama project, yaitu folder yang memiliki file `pom.xml`.

Buka PowerShell, lalu jalankan:

```powershell
.\mvnw.cmd clean javafx:run
```

Maven Wrapper akan mengunduh Maven dan dependency yang diperlukan secara otomatis jika belum tersedia.

Jika proses berhasil, aplikasi Penyimpanan Gudang akan terbuka.

## 6. Menjalankan Melalui IntelliJ IDEA

Jika ingin menjalankan aplikasi melalui IntelliJ IDEA:

1. Buka IntelliJ IDEA.
2. Pilih **Open**.
3. Pilih folder `PenyimpananGudang`.
4. Tunggu hingga Maven selesai mengunduh dependency.
5. Buka `src/main/java/com/penyimpanangudang/Main.java`.
6. Klik tombol Run pada class `Main`.

## 7. Jika Terjadi Error

Beberapa hal yang perlu diperiksa:

**Error: Java tidak ditemukan**

Pastikan Java JDK 21 sudah terinstal dan periksa dengan:

```powershell
java -version
```

**Error: Database connection failed**

Periksa beberapa hal berikut:
- MariaDB atau MySQL sudah berjalan.
- Database `storage_organizer` sudah dibuat.
- Nama database, username, password, dan port sudah benar.
- Tabel `items` sudah tersedia.

**Error: Dependency atau Maven bermasalah**

Coba jalankan:

```powershell
.\mvnw.cmd clean
.\mvnw.cmd javafx:run
```

---

## Teknologi yang Digunakan

- Java 21
- JavaFX
- Maven
- MariaDB
- JDBC

