/**
 * Admin. Menambah komponen ke ruang lab, memvalidasi RoomBooking, dan menampilkan laporan.
 */
public class Admin extends User {

    public Admin(String id, String name, String password) {
        super(id, name, password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    public void addComponentToRoom(Room room, Component c) {
        room.addComponent(c);
    }

    public boolean validateBooking(RoomBooking booking) {
        return booking.validate(this);
    }

    public String viewReport(Room room) {
        return room.generateReport();
    }
}
