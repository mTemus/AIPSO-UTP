package swarmCore;

import functions.AckleyFunction;
import functions.BoothFunction;
import functions.ThreeHumpCamelFunction;

import java.util.Random;

public class Particle {

    private Vector position;
    private Vector velocity;
    private Vector bestPosition;
    private double bestSolution;
    private FunctionType function;
    private double optimum;

    Particle(FunctionType function, int beginRange, int endRange, double optimum) {
        if (beginRange >= endRange) {
            throw new IllegalArgumentException("Begin range must be less than end range.");
        }
        this.function = function;
        position = new Vector();
        velocity = new Vector();
        setRandomPosition(beginRange, endRange);
        bestPosition = velocity.clone();
        bestSolution = calculateSolution();
        this.optimum = optimum;
    }

    private double calculateSolution() {
        if (function == FunctionType.Ackleys) {
            return AckleyFunction.ackleyFunction(position.getX(), position.getY(), optimum);
        } else if (function == FunctionType.Booths) {
            return BoothFunction.boothFunction(position.getX(), position.getY(), optimum);
        } else {
            return ThreeHumpCamelFunction.threeHumpCamelFunction(position.getX(), position.getY(), optimum);
        }
    }

    private void setRandomPosition(int beginRange, int endRange) {
        int x = rand(beginRange, endRange);
        int y = rand(beginRange, endRange);
        int z = rand(beginRange, endRange);
        position.setCoordinates(x, y, z);
    }


    private static int rand(int beginRange, int endRange) {
        Random r = new java.util.Random();
        return r.nextInt(endRange - beginRange) + beginRange;
    }

    void updatePersonalBest() {
        double solution = calculateSolution();
        if (solution < bestSolution) {
            bestPosition = position.clone();
            bestSolution = solution;
        }
    }

    public enum FunctionType {
        Ackleys,
        Booths,
        ThreeHumpCamel
    }

    void updatePosition() {
        this.position.addCoordinates(velocity);
    }

    public Vector getPosition() {
        return position.clone();
    }

    public Vector getVelocity() {
        return velocity.clone();
    }

    public Vector getBestPosition() {
        return bestPosition.clone();
    }

    public double getBestSolution() {
        return bestSolution;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity.clone();
    }
}
