import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Titik masuk aplikasi EmbedLab Manager (menu konsol).
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<User> users = new ArrayList<>();
    private static final Room room = new Room("LAB-ES1", "Embedded System Lab", 30);

    public static void main(String[] args) {
        seedData();
        boolean running = true;

        while (running) {
            System.out.println("\n=== EMBEDLAB MANAGER ===");
            System.out.println("1. Login");
            System.out.println("0. Keluar");
            int choice = readInt("Pilih menu: ");

            if (choice == 1) {
                String id = readLine("ID       : ");
                String password = readLine("Password : ");
                User user = login(id, password);
                if (user == null) {
                    System.out.println("Login gagal. ID atau password salah.");
                } else {
                    System.out.println("Login berhasil. Selamat datang, " + user.getName()
                            + " (" + user.getRole() + ").");
                    showMenuByRole(user);
                }
            } else if (choice == 0) {
                running = false;
                System.out.println("Program selesai. Data di memori dihapus.");
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    // Menu mengikuti hasil getRole()
    private static void showMenuByRole(User user) {
        String role = user.getRole();
        if (role.equals("Admin")) {
            adminMenu((Admin) user);
        } else if (role.equals("Assistant")) {
            assistantMenu((Assistant) user);
        } else if (role.equals("Student")) {
            studentMenu((Student) user);
        }
    }

    // ---------------------------------------------------------------- Admin
    private static void adminMenu(Admin admin) {
        boolean active = true;
        while (active) {
            System.out.println("\n--- MENU ADMIN ---");
            System.out.println("1. Tambah komponen ke lab");
            System.out.println("2. Validasi peminjaman ruangan");
            System.out.println("3. Lihat laporan");
            System.out.println("4. Lihat daftar komponen");
            System.out.println("0. Logout");
            int choice = readInt("Pilih menu: ");

            switch (choice) {
                case 1:
                    addComponentFlow(admin);
                    break;
                case 2:
                    validateBookingFlow(admin);
                    break;
                case 3:
                    System.out.println(admin.viewReport(room));
                    break;
                case 4:
                    listComponents();
                    break;
                case 0:
                    admin.logout();
                    active = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void addComponentFlow(Admin admin) {
        System.out.println("Jenis komponen: 1. Microcontroller  2. Sensor");
        int type = readInt("Pilih jenis: ");
        try {
            String code = readLine("Kode      : ");
            String name = readLine("Nama      : ");
            int qty = readInt("Jumlah    : ");
            Component c;

            if (type == 1) {
                String chip = readLine("Chip type          : ");
                double operating = readDouble("Tegangan kerja (V) : ");
                Microcontroller mc = new Microcontroller(code, name, qty, chip, operating);
                c = mc;
            } else if (type == 2) {
                String measureType = readLine("Jenis ukuran : ");
                double min = readDouble("Rentang min  : ");
                double max = readDouble("Rentang max  : ");
                Sensor s = new Sensor(code, name, qty, measureType, min, max);
                c = s;
            } else {
                System.out.println("Jenis tidak valid.");
                return;
            }
            admin.addComponentToRoom(room, c);
            System.out.println("Komponen berhasil ditambahkan ke " + room.getName() + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Gagal menambah komponen. " + e.getMessage());
        }
    }

    private static void validateBookingFlow(Admin admin) {
        List<RoomBooking> pending = room.getPendingBookings();
        if (pending.isEmpty()) {
            System.out.println("Tidak ada pengajuan peminjaman yang menunggu.");
            return;
        }
        for (int i = 0; i < pending.size(); i++) {
            System.out.println((i + 1) + ". " + pending.get(i));
        }
        int idx = readInt("Pilih nomor pengajuan (0 untuk batal): ");
        if (idx < 1 || idx > pending.size()) {
            return;
        }
        RoomBooking booking = pending.get(idx - 1);
        boolean approved = admin.validateBooking(booking);
        if (approved) {
            System.out.println("Peminjaman DISETUJUI.");
        } else {
            System.out.println("Peminjaman DITOLAK (jadwal bentrok atau melebihi kapasitas).");
        }
    }

    private static void listComponents() {
        List<Component> list = room.getComponents();
        if (list.isEmpty()) {
            System.out.println("Belum ada komponen.");
            return;
        }
        for (Component c : list) {
            System.out.println("- [" + c.getCode() + "] " + c.getName() + " (jumlah: "
                    + c.getQuantity() + ") " + c.getClass().getSimpleName());
        }
    }

    // ------------------------------------------------------------ Assistant
    private static void assistantMenu(Assistant assistant) {
        boolean active = true;
        while (active) {
            System.out.println("\n--- MENU ASSISTANT ---");
            System.out.println("1. Buat jadwal kelas (ajukan peminjaman ruangan)");
            System.out.println("2. Lihat komponen lab");
            System.out.println("3. Lihat jadwal saya dan kehadiran");
            System.out.println("0. Logout");
            int choice = readInt("Pilih menu: ");

            switch (choice) {
                case 1:
                    requestBookingFlow(assistant);
                    break;
                case 2:
                    List<String> results = assistant.viewComponents(room);
                    if (results.isEmpty()) {
                        System.out.println("Belum ada komponen di lab.");
                    }
                    for (String r : results) {
                        System.out.println("- " + r);
                    }
                    break;
                case 3:
                    List<RoomBooking> mine = assistant.viewMySchedules(room);
                    if (mine.isEmpty()) {
                        System.out.println("Belum ada jadwal yang Anda buat.");
                    }
                    for (RoomBooking b : mine) {
                        System.out.println("- " + b);
                        for (Student st : b.getAttendees()) {
                            System.out.println("    hadir: " + st.getName() + " (" + st.getId() + ")");
                        }
                    }
                    break;
                case 0:
                    assistant.logout();
                    active = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void requestBookingFlow(Assistant assistant) {
        try {
            LocalDate date = LocalDate.parse(readLine("Tanggal (yyyy-MM-dd) : "));
            LocalTime start = LocalTime.parse(readLine("Jam mulai (HH:mm)    : "));
            LocalTime end = LocalTime.parse(readLine("Jam selesai (HH:mm)  : "));
            int participants = readInt("Kuota peserta        : ");
            String purpose = readLine("Nama kelas/kegiatan  : ");
            assistant.requestBooking(room, date, start, end, participants, purpose);
            System.out.println("Jadwal diajukan dengan status PENDING. Menunggu persetujuan Admin.");
        } catch (DateTimeParseException e) {
            System.out.println("Format tanggal atau jam salah.");
        } catch (IllegalArgumentException e) {
            System.out.println("Pengajuan gagal. " + e.getMessage());
        }
    }

    // -------------------------------------------------------------- Student
    private static void studentMenu(Student student) {
        boolean active = true;
        while (active) {
            System.out.println("\n--- MENU STUDENT ---");
            System.out.println("1. Lihat kelas yang tersedia");
            System.out.println("2. Absen (pilih kelas)");
            System.out.println("3. Lihat riwayat kehadiran");
            System.out.println("0. Logout");
            int choice = readInt("Pilih menu: ");

            switch (choice) {
                case 1:
                    listClasses(student);
                    break;
                case 2:
                    checkInFlow(student);
                    break;
                case 3:
                    List<RoomBooking> history = student.viewAttendanceHistory(room);
                    if (history.isEmpty()) {
                        System.out.println("Belum ada riwayat kehadiran.");
                    }
                    for (RoomBooking b : history) {
                        System.out.println("- " + b.getPurpose() + " | " + b.getDate() + " "
                                + b.getStartTime() + "-" + b.getEndTime());
                    }
                    break;
                case 0:
                    student.logout();
                    active = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void listClasses(Student student) {
        List<RoomBooking> classes = student.viewAvailableClasses(room);
        if (classes.isEmpty()) {
            System.out.println("Belum ada kelas yang dijadwalkan.");
            return;
        }
        for (int i = 0; i < classes.size(); i++) {
            RoomBooking b = classes.get(i);
            System.out.println((i + 1) + ". " + b.getPurpose() + " | " + b.getDate() + " "
                    + b.getStartTime() + "-" + b.getEndTime() + " | pengampu "
                    + b.getAssistant().getName() + " | hadir " + b.getAttendees().size()
                    + (b.hasAttended(student) ? " | SUDAH ABSEN" : ""));
        }
    }

    private static void checkInFlow(Student student) {
        List<RoomBooking> classes = student.viewAvailableClasses(room);
        if (classes.isEmpty()) {
            System.out.println("Belum ada kelas yang dijadwalkan.");
            return;
        }
        listClasses(student);
        int idx = readInt("Pilih nomor kelas (0 untuk batal): ");
        if (idx < 1 || idx > classes.size()) {
            return;
        }
        if (student.checkIn(classes.get(idx - 1))) {
            System.out.println("Absen berhasil dicatat.");
        } else {
            System.out.println("Absen gagal (sudah absen atau kuota kelas penuh).");
        }
    }

    // ------------------------------------------------------------ Seed data
    private static void seedData() {
        users.add(new Admin("admin01", "Admin Lab", "admin123"));
        users.add(new Assistant("asisten01", "Asisten Satu", "asisten123"));
        users.add(new Assistant("asisten02", "Asisten Dua", "asisten123"));
        users.add(new Student("mhs01", "Mahasiswa Satu", "mhs123"));
        users.add(new Student("mhs02", "Mahasiswa Dua", "mhs123"));

        Microcontroller uno = new Microcontroller("MC-01", "Arduino Uno", 10, "ATmega328P", 5.0);
        Microcontroller esp = new Microcontroller("MC-02", "ESP32 DevKit", 5, "ESP32", 3.3);
        Sensor dht = new Sensor("SN-01", "DHT22", 8, "Suhu", -40, 80);
        Sensor hc = new Sensor("SN-02", "HC-SR04", 6, "Jarak (cm)", 2, 400);
        room.addComponent(uno);
        room.addComponent(esp);
        room.addComponent(dht);
        room.addComponent(hc);
    }

    private static User login(String id, String password) {
        for (User u : users) {
            if (u.getId().equals(id) && u.login(password)) {
                return u;
            }
        }
        return null;
    }

    // -------------------------------------------------------- Input helpers
    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka bulat yang valid.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka yang valid.");
            }
        }
    }
}
