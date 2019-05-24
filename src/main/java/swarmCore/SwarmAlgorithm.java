package swarmCore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SwarmAlgorithm {

    private int numOfParticles, epochs;
    private double inertia, cognitiveComponent, socialComponent;
    private Vector bestPosition;
    private double bestSolution;
    private Particle.FunctionType function;

    private List<Vector> bestPositions = new ArrayList<>();
    private List<Double> bestSolutions = new ArrayList<>();
    private List<Double> oldSolutions = new ArrayList<>();
    private List<String> algorithmTextLogs = new ArrayList<>();

    private static final double DEFAULT_INERTIA = 0.729844;
    private static final double DEFAULT_COGNITIVE = 1.496180;
    private static final double DEFAULT_SOCIAL = 1.496180;

    private int beginRange, endRange;
    private static final int DEFAULT_BEGIN_RANGE = -100;
    private static final int DEFAULT_END_RANGE = 101;

    public SwarmAlgorithm(Particle.FunctionType function, int particles, int epochs, double inertia, double cognitive, double social, int beginRange, int endRange) {
        this.numOfParticles = particles;
        this.epochs = epochs;
        this.inertia = inertia;
        this.cognitiveComponent = cognitive;
        this.socialComponent = social;
        this.function = function;
        double infinity = Double.POSITIVE_INFINITY;
        bestPosition = new Vector(infinity, infinity, infinity);
        bestSolution = Double.POSITIVE_INFINITY;
        this.beginRange = beginRange;
        this.endRange = endRange;
    }

    public void run() {
        Particle[] particles = initialize();
        String s;
        double oldSolution = bestSolution;

        for (int i = 0; i < epochs; i++) {
            oldSolutions.add(oldSolution);
            bestPositions.add(bestPosition);
            bestSolutions.add(bestSolution);

            if (bestSolution < oldSolution) {
                s = "Global Best Evaluation (Epoch " + (i) + "):\t" + bestSolution;
                algorithmTextLogs.add(s);
                oldSolution = bestSolution;
            } else {
                s = "Global Best Evaluation (Epoch " + (i) + "):\t" + bestSolution;
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
    }
    private Particle[] initialize() {
        Particle[] particles = new Particle[numOfParticles];
        for (int i = 0; i < numOfParticles; i++) {
            Particle particle = new Particle(function, beginRange, endRange);
            particles[i] = particle;
            updateGlobalBest(particle);
        }
        return particles;
    }


    private void updateGlobalBest(Particle particle) {
        if (particle.getBestSolution() < bestSolution) {
            bestPosition = particle.getBestPosition();
            bestSolution = particle.getBestSolution();
        }
    }

    private void updateVelocity(Particle particle) {
        Vector oldVelocity = particle.getVelocity();
        Vector particleBestPosition = particle.getBestPosition();
        Vector globalBestPosition = bestPosition.clone();
        Vector position = particle.getPosition();

        Random random = new Random();
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();

        Vector newVelocity = oldVelocity.clone();
        newVelocity.multipleCoordinates(inertia);

        particleBestPosition.subtractCoordinates(position);
        particleBestPosition.multipleCoordinates(cognitiveComponent);
        particleBestPosition.multipleCoordinates(r1);
        newVelocity.addCoordinates(particleBestPosition);

        globalBestPosition.subtractCoordinates(position);
        globalBestPosition.multipleCoordinates(socialComponent);
        globalBestPosition.multipleCoordinates(r2);
        newVelocity.addCoordinates(globalBestPosition);

        particle.setVelocity(newVelocity);
    }


    public List<Vector> getBestPositions() {
        return bestPositions;
    }

    public List<Double> getBestSolutions() {
        return bestSolutions;
    }

    public List<Double> getOldSolutions() {
        return oldSolutions;
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
