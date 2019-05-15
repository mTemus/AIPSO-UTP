package functions;

public class AckleyFunction {

    /**
     * Perform Ackley's function.
     * Domain is [5, 5]
     * Minimum is 0 at x = 0 & y = 0.
     *
     * @param x the x component
     * @param y the y component
     * @return the z component
     */
    public static double ackleyFunction(double x, double y) {
        double p1 = -20 * Math.exp(-0.2 * Math.sqrt(0.5 * ((x * x) + (y * y))));
        double p2 = Math.exp(0.5 * (Math.cos(2 * Math.PI * x) + Math.cos(2 * Math.PI * y)));
        return p1 - p2 + Math.E + 20;
    }

}
