package functions;

public class ThreeHumpCamelFunction {

    public static double threeHumpCamelFunction(double x, double y, double optimum) {
        double p1 = 2 * x * x;
        double p2 = 1.05 * Math.pow(x, 4);
        double p3 = Math.pow(x, 6) / 6;
        return p1 - p2 + p3 + x * y + y * y + optimum;
    }
}
