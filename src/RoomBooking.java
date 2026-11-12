import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Satu pengajuan peminjaman ruangan. Diajukan Assistant dan divalidasi Admin.
 * Setelah APPROVED, booking menjadi jadwal kelas yang bisa dipilih Student untuk absen.
 * participantCount berfungsi sebagai kuota kelas. Status memakai enum ValidationStatus.
 */
public class RoomBooking {
    private Assistant assistant;
    private Room room;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int participantCount;
    private String purpose;
    private ValidationStatus status;
    private List<Student> attendees;

    public RoomBooking(Assistant assistant, Room room, LocalDate date, LocalTime startTime,
                       LocalTime endTime, int participantCount, String purpose) {
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("Jam selesai harus setelah jam mulai.");
        }
        if (participantCount <= 0) {
            throw new IllegalArgumentException("Jumlah peserta harus lebih dari 0.");
        }
        this.assistant = assistant;
        this.room = room;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participantCount = participantCount;
        this.purpose = purpose;
        this.status = ValidationStatus.PENDING;
        this.attendees = new ArrayList<>();
    }

    public boolean validate(User validator) {
        if (!validator.getRole().equals("Admin")) {
            return false;
        }
        if (status != ValidationStatus.PENDING) {
            return false;
        }
        if (room.isAvailable(date, startTime, endTime) && isCapacityOk()) {
            setStatus(ValidationStatus.APPROVED);
        } else {
            setStatus(ValidationStatus.REJECTED);
        }
        return status == ValidationStatus.APPROVED;
    }

    public ValidationStatus getStatus() {
        return status;
    }

    // Absensi Student. Hanya untuk jadwal APPROVED, belum pernah absen, dan kuota belum penuh.
    public boolean addAttendee(Student s) {
        if (status != ValidationStatus.APPROVED || hasAttended(s)
                || attendees.size() >= participantCount) {
            return false;
        }
        attendees.add(s);
        return true;
    }

    public boolean hasAttended(Student s) {
        for (Student a : attendees) {
            if (a.getId().equals(s.getId())) {
                return true;
            }
        }
        return false;
    }

    public List<Student> getAttendees() {
        return new ArrayList<>(attendees);
    }

    public String getPurpose() {
        return purpose;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean overlaps(LocalDate date, LocalTime start, LocalTime end) {
        return this.date.equals(date) && this.startTime.isBefore(end) && start.isBefore(this.endTime);
    }

    private boolean isCapacityOk() {
        return participantCount <= room.getCapacity();
    }

    private void setStatus(ValidationStatus s) {
        this.status = s;
    }

    @Override
    public String toString() {
        return assistant.getName() + " | " + room.getName() + " | " + date + " "
                + startTime + "-" + endTime + " | " + participantCount + " peserta | "
                + purpose + " | " + status + " | hadir " + attendees.size() + "/" + participantCount;
    }
}
