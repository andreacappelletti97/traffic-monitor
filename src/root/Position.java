import java.io.Serializable;

public class Position implements Serializable {
    private int xPosition;
    private int yPosition;

    public Position(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public void printPosition() {
        System.out.println("(" + getxPosition() + ", " + getyPosition() + ")");
    }
    public int getxPosition() {
        return xPosition;
    }
    public int getyPosition() {
        return yPosition;
    }

    @Override
    public boolean equals(Object checkPosition) {
        if (checkPosition == null) return false;

        Position objPos = (Position) checkPosition;
        return objPos.getxPosition() == getxPosition() && objPos.getyPosition() == getyPosition();
    }
}
