package swarmCore;

public class Vector {

    private double x, y, z;
    private double limit = Double.MAX_VALUE;

    Vector() {
        this(0, 0, 0);
    }

    Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    void setCoordinates(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    void addCoordinates(Vector v) {
        x += v.x;
        y += v.y;
        z += v.z;
        limit();
    }

    void subtractCoordinates(Vector v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        limit();
    }

    void multipleCoordinates(double s) {
        x *= s;
        y *= s;
        z *= s;
        limit();
    }

    private double mag() {
        return Math.sqrt(x * x + y * y);
    }

    private void limit() {
        double m = mag();
        if (m > limit) {
            double ratio = m / limit;
            x /= ratio;
            y /= ratio;
        }
    }

    public Vector clone() {
        return new Vector(x, y, z);
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}

