package functions;

public class BoothFunction {
    /**
     * Perform Booth's function.
     * Domain is [-10, 10]
     * Minimum is 0 at x = 1 & y = 3.
     *
     * @param x the x component
     * @param y the y component
     * @return the z component
     */
    public static double boothFunction(double x, double y, double optimum) {
        double p1 = Math.pow(x + 2 * y - 7, 2);
        double p2 = Math.pow(2 * x + y - 5, 2);
        return p1 + p2 + optimum;
    }
}
