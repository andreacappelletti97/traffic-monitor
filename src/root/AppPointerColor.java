import java.io.Serializable;

public class AppPointerColor implements Serializable {
    private double r;
    private double g;
    private double b;

    public AppPointerColor(double r, double g, double b) {
        if (isInputValid(r) && isInputValid(g) && isInputValid(b)) {
            this.r = r;
            this.g = g;
            this.b = b;
        } else {
            setDefaultColor();
        }
    }
    public AppPointerColor() {
        setDefaultColor();
    }

    public double getR() {
        return r;
    }
    public double getG() {
        return g;
    }
    public double getB() {
        return b;
    }

    private boolean isInputValid(double input) {
        return input >= 0 && input <= 1;
    }
    private void setDefaultColor() {
        // Yellow default color
        this.r = 1;
        this.g = 1;
        this.b = 0;
    }
}
