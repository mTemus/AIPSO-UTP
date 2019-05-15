package swarmCore;

/**
 * Can represent a position as well as a velocity.
 */
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



    void set(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    void add(Vector v) {
        x += v.x;
        y += v.y;
        z += v.z;
        limit();
    }

    void sub(Vector v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        limit();
    }

    void mul(double s) {
        x *= s;
        y *= s;
        z *= s;
        limit();
    }

    void div(double s) {
        x /= s;
        y /= s;
        z /= s;
        limit();
    }

    void normalize() {
        double m = mag();
        if (m > 0) {
            x /= m;
            y /= m;
            z /= m;
        }
    }

    private double mag() {
        return Math.sqrt(x * x + y * y);
    }

    void limit(double l) {
        limit = l;
        limit();
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

