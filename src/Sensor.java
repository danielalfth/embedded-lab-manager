/**
 * Komponen sensor dengan rentang ukur tertentu.
 */
public class Sensor extends Component {
    private String measureType;
    private double minRange;
    private double maxRange;

    public Sensor(String code, String name, int quantity, String measureType,
                  double minRange, double maxRange) {
        super(code, name, quantity);
        if (minRange >= maxRange) {
            throw new IllegalArgumentException("Rentang minimum harus lebih kecil dari maksimum.");
        }
        this.measureType = measureType;
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    public String getMeasureType() {
        return measureType;
    }

    @Override
    public String getDetail() {
        return getBaseInfo() + " | Sensor " + measureType + " | rentang " + minRange
                + " sampai " + maxRange;
    }
}
