package functions;

public class BoothFunction {

    public static double boothFunction(double x, double y, double optimum) {
        double p1 = Math.pow(x + 2 * y - 7, 2);
        double p2 = Math.pow(2 * x + y - 5, 2);
        return p1 + p2 + optimum;
    }
}
