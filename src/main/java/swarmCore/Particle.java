package swarmCore;

import functions.AckleyFunction;
import functions.BoothFunction;
import functions.ThreeHumpCamelFunction;

import java.util.Random;

/**
 * Represents a particle from the Particle Swarm Optimization algorithm.
 */
public class Particle {

    private Vector position;        // Current position.
    private Vector velocity;
    private Vector bestPosition;    // Personal best solution.
    private double bestSolution;        // Personal best value.
    private FunctionType function;  // The evaluation function to use.

    /**
     * Construct a Particle with a random starting position.
     *
     * @param beginRange the minimum xyz values of the position (inclusive)
     * @param endRange   the maximum xyz values of the position (exclusive)
     */
    Particle(FunctionType function, int beginRange, int endRange) {
        if (beginRange >= endRange) {
            throw new IllegalArgumentException("Begin range must be less than end range.");
        }
        this.function = function;
        position = new Vector();
        velocity = new Vector();
        setRandomPosition(beginRange, endRange);
        bestPosition = velocity.clone();
        bestSolution = eval();
    }

    /**
     * The evaluation of the current position.
     *
     * @return the evaluation
     */
    private double eval() {

        if (function == FunctionType.Ackleys) {
            return AckleyFunction.ackleyFunction(position.getX(), position.getY());
        } else if (function == FunctionType.Booths) {
            return BoothFunction.boothFunction(position.getX(), position.getY());
        } else {
            return ThreeHumpCamelFunction.threeHumpCamelFunction(position.getX(), position.getY());
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

    /**
     * Update the personal best if the current evaluation is better.
     */
    void updatePersonalBest() {
        double eval = eval();
        if (eval < bestSolution) {
            bestPosition = position.clone();
            bestSolution = eval;
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

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setBestPosition(Vector bestPosition) {
        this.bestPosition = bestPosition;
    }

    public void setBestSolution(double bestSolution) {
        this.bestSolution = bestSolution;
    }

    public FunctionType getFunction() {
        return function;
    }

    public void setFunction(FunctionType function) {
        this.function = function;
    }
}
