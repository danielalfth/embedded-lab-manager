import java.util.ArrayList;
import java.util.List;

/**
 * Praktikan. Melihat kelas yang sudah dijadwalkan Assistant (dan disetujui Admin),
 * memilih kelas untuk absen, dan melihat riwayat kehadiran.
 */
public class Student extends User {

    public Student(String id, String name, String password) {
        super(id, name, password);
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public List<RoomBooking> viewAvailableClasses(Room room) {
        return room.getApprovedBookings();
    }

    public boolean checkIn(RoomBooking classSession) {
        return classSession.addAttendee(this);
    }

    public List<RoomBooking> viewAttendanceHistory(Room room) {
        List<RoomBooking> result = new ArrayList<>();
        for (RoomBooking b : room.getApprovedBookings()) {
            if (b.hasAttended(this)) {
                result.add(b);
            }
        }
        return result;
    }
}
