package swarmCore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SwarmAlgorithm {

    private int numOfParticles, epochs;
    private double inertia, cognitiveComponent, socialComponent;
    private Vector bestPosition;
    private double bestEval;
    private Particle.FunctionType function; // The function to search.

    private List<Vector> bestPositions = new ArrayList<>();
    private List<Double> bestEvals = new ArrayList<>();
    private List<Double> oldEvals = new ArrayList<>();
    private List<String> algorithmTextLogs = new ArrayList<>();

    private static final double DEFAULT_INERTIA = 0.729844;
    private static final double DEFAULT_COGNITIVE = 1.496180; // Cognitive component.
    private static final double DEFAULT_SOCIAL = 1.496180; // Social component.

    /**
     * When Particles are created they are given a random position.
     * The random position is selected from a specified range.
     * If the begin range is 0 and the end range is 10 then the
     * value will be between 0 (inclusive) and 10 (exclusive).
     */
    private int beginRange, endRange;
    private static final int DEFAULT_BEGIN_RANGE = -100;
    private static final int DEFAULT_END_RANGE = 101;

    /**
     * Construct the Swarm with custom values.
     *
     * @param particles the number of particles to create
     * @param epochs    the number of generations
     * @param inertia   the particles resistance to change
     * @param cognitive the cognitive component or introversion of the particle
     * @param social    the social component or extroversion of the particle
     */
    public SwarmAlgorithm(Particle.FunctionType function, int particles, int epochs, double inertia, double cognitive, double social, int beginRange, int endRange) {
        this.numOfParticles = particles;
        this.epochs = epochs;
        this.inertia = inertia;
        this.cognitiveComponent = cognitive;
        this.socialComponent = social;
        this.function = function;
        double infinity = Double.POSITIVE_INFINITY;
        bestPosition = new Vector(infinity, infinity, infinity);
        bestEval = Double.POSITIVE_INFINITY;
        this.beginRange = beginRange;
        this.endRange = endRange;
    }

    /**
     * Execute the algorithm.
     */
    public void run() {
        Particle[] particles = initialize();
        String s;
        double oldEval = bestEval;

        for (int i = 0; i < epochs; i++) {
            oldEvals.add(oldEval);
            bestPositions.add(bestPosition);
            bestEvals.add(bestEval);

            if (bestEval < oldEval) {
                s = "Global Best Evaluation (Epoch " + (i) + "):\t" + bestEval;
                algorithmTextLogs.add(s);
                oldEval = bestEval;
            } else {
                s = "Global Best Evaluation (Epoch " + (i) + "):\t" + bestEval;
                algorithmTextLogs.add(s);
            }

            for (Particle p : particles) {
                p.updatePersonalBest();
                updateGlobalBest(p);
            }

            for (Particle p : particles) {
                updateVelocity(p);
                p.updatePosition();
            }
        }

//        System.out.println("---------------------------RESULT---------------------------");
//        System.out.println("x = " + bestPosition.getX());
//        System.out.println("y = " + bestPosition.getY());
//
//        System.out.println("Final Best Evaluation: " + bestEval);
//        System.out.println("---------------------------COMPLETE-------------------------");

    }

    /**
     * Create a setCoordinates of particles, each with random starting positions.
     *
     * @return an array of particles
     */
    private Particle[] initialize() {
        Particle[] particles = new Particle[numOfParticles];
        for (int i = 0; i < numOfParticles; i++) {
            Particle particle = new Particle(function, beginRange, endRange);
            particles[i] = particle;
            updateGlobalBest(particle);
        }
        return particles;
    }

    /**
     * Update the global best solution if a the specified particle has
     * a better solution
     *
     * @param particle the particle to analyze
     */
    private void updateGlobalBest(Particle particle) {
        if (particle.getBestEval() < bestEval) {
            bestPosition = particle.getBestPosition();
            bestEval = particle.getBestEval();
        }
    }

    /**
     * Update the velocity of a particle using the velocity update formula
     *
     * @param particle the particle to update
     */
    private void updateVelocity(Particle particle) {
        Vector oldVelocity = particle.getVelocity();
        Vector particleBestPosition = particle.getBestPosition();
        Vector globalBestPosition = bestPosition.clone();
        Vector position = particle.getPosition();

        Random random = new Random();
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();

        // The first product of the formula.
        Vector newVelocity = oldVelocity.clone();
        newVelocity.multipleCoordinates(inertia);

        // The second product of the formula.
        particleBestPosition.subtractCoordinates(position);
        particleBestPosition.multipleCoordinates(cognitiveComponent);
        particleBestPosition.multipleCoordinates(r1);
        newVelocity.addCoordinates(particleBestPosition);

        // The third product of the formula.
        globalBestPosition.subtractCoordinates(position);
        globalBestPosition.multipleCoordinates(socialComponent);
        globalBestPosition.multipleCoordinates(r2);
        newVelocity.addCoordinates(globalBestPosition);

        particle.setVelocity(newVelocity);
    }


    public List<Vector> getBestPositions() {
        return bestPositions;
    }

    public List<Double> getBestEvals() {
        return bestEvals;
    }

    public List<Double> getOldEvals() {
        return oldEvals;
    }

    public List<String> getAlgorithmTextLogs() {
        return algorithmTextLogs;
    }

    public static double getDefaultInertia() {
        return DEFAULT_INERTIA;
    }

    public static double getDefaultCognitive() {
        return DEFAULT_COGNITIVE;
    }

    public static double getDefaultSocial() {
        return DEFAULT_SOCIAL;
    }

    public static int getDefaultBeginRange() {
        return DEFAULT_BEGIN_RANGE;
    }

    public static int getDefaultEndRange() {
        return DEFAULT_END_RANGE;
    }


}
