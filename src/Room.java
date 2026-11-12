import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Satu ruang lab beserta komponen dan booking yang sudah disetujui.
 */
public class Room {
    private String code;
    private String name;
    private int capacity;
    private List<Component> components;
    private List<RoomBooking> bookings;

    public Room(String code, String name, int capacity) {
        this.code = code;
        this.name = name;
        setCapacity(capacity);
        this.components = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int c) {
        if (c <= 0) {
            throw new IllegalArgumentException("Kapasitas harus lebih dari 0.");
        }
        this.capacity = c;
    }

    public void addComponent(Component c) {
        components.add(c);
    }

    public List<Component> getComponents() {
        return new ArrayList<>(components);
    }

    public boolean isAvailable(LocalDate date, LocalTime start, LocalTime end) {
        for (RoomBooking b : bookings) {
            if (b.getStatus() == ValidationStatus.APPROVED && b.overlaps(date, start, end)) {
                return false;
            }
        }
        return true;
    }

    public List<RoomBooking> getApprovedBookings() {
        List<RoomBooking> result = new ArrayList<>();
        for (RoomBooking b : bookings) {
            if (b.getStatus() == ValidationStatus.APPROVED) {
                result.add(b);
            }
        }
        return result;
    }

    public List<RoomBooking> getBookings() {
        return new ArrayList<>(bookings);
    }

    public void addBooking(RoomBooking b) {
        bookings.add(b);
    }

    public List<RoomBooking> getPendingBookings() {
        List<RoomBooking> result = new ArrayList<>();
        for (RoomBooking b : bookings) {
            if (b.getStatus() == ValidationStatus.PENDING) {
                result.add(b);
            }
        }
        return result;
    }

    public String generateReport() {
        int approved = 0, rejected = 0, pending = 0;
        for (RoomBooking b : bookings) {
            switch (b.getStatus()) {
                case APPROVED: approved++; break;
                case REJECTED: rejected++; break;
                default: pending++; break;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("========== LAPORAN EMBEDLAB MANAGER ==========\n");
        sb.append("Ruang: ").append(name).append(" (").append(code)
                .append("), kapasitas ").append(capacity).append("\n\n");

        sb.append("Rekap peminjaman ruangan (total ").append(bookings.size()).append(")\n");
        sb.append("  APPROVED : ").append(approved).append("\n");
        sb.append("  REJECTED : ").append(rejected).append("\n");
        sb.append("  PENDING  : ").append(pending).append("\n");
        int totalAttendance = 0;
        for (RoomBooking b : bookings) {
            totalAttendance += b.getAttendees().size();
        }
        sb.append("  Total absensi tercatat : ").append(totalAttendance).append("\n\n");

        sb.append("Komponen di ruang ini (").append(components.size()).append(" jenis)\n");
        for (Component c : components) {
            sb.append("  - ").append(c.getCode()).append(" ").append(c.getName())
                    .append(" (jumlah: ").append(c.getQuantity()).append(")\n");
        }
        sb.append("==============================================");
        return sb.toString();
    }
}
