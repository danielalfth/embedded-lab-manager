/**
 * Abstract class induk semua pengguna.
 * Password bersifat private dan tidak punya getter, sehingga tidak bisa dibaca dari luar.
 */
public abstract class User {
    private String id;
    private String name;
    private String password;

    protected User(String id, String name, String password) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID tidak boleh kosong.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong.");
        }
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public boolean login(String inputPassword) {
        return checkPassword(inputPassword);
    }

    public void logout() {
        System.out.println("Logout berhasil. Sampai jumpa, " + name + ".");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong.");
        }
        this.name = name;
    }

    // Diisi oleh class turunan (polymorphism)
    public abstract String getRole();

    private boolean checkPassword(String input) {
        return password.equals(input);
    }
}
