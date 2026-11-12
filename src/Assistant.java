import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Assistant. Membuat jadwal kelas (pengajuan peminjaman ruangan ke Admin) dan melihat komponen lab.
 */
public class Assistant extends User {

    public Assistant(String id, String name, String password) {
        super(id, name, password);
    }

    @Override
    public String getRole() {
        return "Assistant";
    }

    public RoomBooking requestBooking(Room room, LocalDate date, LocalTime start,
                                      LocalTime end, int participants, String purpose) {
        RoomBooking booking = new RoomBooking(this, room, date, start, end,
                participants, purpose);
        room.addBooking(booking);
        return booking;
    }

    // Jadwal (pengajuan) milik Assistant ini beserta status dan jumlah kehadirannya
    public List<RoomBooking> viewMySchedules(Room room) {
        List<RoomBooking> result = new ArrayList<>();
        for (RoomBooking b : room.getBookings()) {
            if (b.getAssistant().getId().equals(getId())) {
                result.add(b);
            }
        }
        return result;
    }

    /**
     * Memanggil getDetail() pada tiap komponen.
     * Method yang dijalankan berbeda tergantung jenis komponen (polymorphism).
     */
    public List<String> viewComponents(Room room) {
        List<String> results = new ArrayList<>();
        for (Component c : room.getComponents()) {
            results.add(c.getDetail());
        }
        return results;
    }
}
