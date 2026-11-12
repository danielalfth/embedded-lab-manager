/**
 * Abstract class induk semua komponen lab.
 */
public abstract class Component {
    private String code;
    private String name;
    private int quantity;

    protected Component(String code, String name, int quantity) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode komponen tidak boleh kosong.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama komponen tidak boleh kosong.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Jumlah tidak boleh negatif.");
        }
        this.code = code;
        this.name = name;
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int q) {
        if (q < 0) {
            throw new IllegalArgumentException("Jumlah tidak boleh negatif.");
        }
        this.quantity = q;
    }

    // Diisi oleh class turunan (polymorphism)
    public abstract String getDetail();

    protected String getBaseInfo() {
        return "[" + code + "] " + name + " (jumlah: " + quantity + ")";
    }
}
