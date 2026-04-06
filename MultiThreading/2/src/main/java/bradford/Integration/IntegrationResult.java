package bradford.Integration;

public class IntegrationResult {
    private String name;
    private double value;
    private long duration;

    public IntegrationResult(String name, double value, long duration) {
        this.name = name;
        this.value = value;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public long getDuration() {
        return duration;
    }

    public String toString() {
        return "Method: "+name + "\nValue: "+value+"\nDuration: "+duration;
    }
}
