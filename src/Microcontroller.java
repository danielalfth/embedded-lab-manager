/**
 * Komponen berupa board mikrokontroler.
 */
public class Microcontroller extends Component {
    private String chipType;
    private double operatingVoltage;

    public Microcontroller(String code, String name, int quantity, String chipType,
                           double operatingVoltage) {
        super(code, name, quantity);
        if (operatingVoltage <= 0) {
            throw new IllegalArgumentException("Tegangan kerja harus lebih dari 0.");
        }
        this.chipType = chipType;
        this.operatingVoltage = operatingVoltage;
    }

    public String getChipType() {
        return chipType;
    }

    public double getOperatingVoltage() {
        return operatingVoltage;
    }

    @Override
    public String getDetail() {
        return getBaseInfo() + " | Microcontroller " + chipType + " | tegangan kerja "
                + operatingVoltage + "V";
    }
}
