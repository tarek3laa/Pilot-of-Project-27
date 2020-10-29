package sample.subsystem;

public class Command {
    private int id;
    private String name;
    private boolean isBinary;
    private boolean haveFraction;
    private double low, high;


    public Command(int id, String name, boolean isBinary, boolean haveFraction, double low, double high) {
        this.id = id;
        this.name = name;
        this.isBinary = isBinary;
        this.haveFraction = haveFraction;
        this.low = low;
        this.high = high;

    }

    public int getId() {
        return id;
    }



    public String getName() {
        return name;
    }

    public boolean isBinary() {
        return isBinary;
    }

    public boolean isHaveFraction() {
        return haveFraction;
    }

    public double getLow() {
        return low;
    }

    public double getHigh() {
        return high;
    }
}
