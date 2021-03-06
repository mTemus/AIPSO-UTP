package swarmCore;

import controller.PSOSceneController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SwarmAlgorithm {

    private int numOfParticles, epochs, filterPrecision;
    private double inertia, cognitiveComponent, socialComponent, optimum;
    private Vector bestPosition;
    private double bestSolution;
    private Particle.FunctionType function;
    private static DecimalFormat solutionsPattern = new DecimalFormat("#0.0000000000000000000000");


    private List<Vector> bestPositions = new ArrayList<>();
    private List<Double> bestSolutions = new ArrayList<>();
    private List<Double> oldSolutions = new ArrayList<>();
    private List<String> algorithmTextLogs = new ArrayList<>();
    private List<List<Particle>> swarmParticles = new ArrayList<>();

    private static final double DEFAULT_INERTIA = 0.729844;
    private static final double DEFAULT_COGNITIVE = 1.496180;
    private static final double DEFAULT_SOCIAL = 1.496180;

    private int beginRange, endRange;


    public SwarmAlgorithm(Particle.FunctionType function, int particles, int epochs, double inertia, double cognitive, double social, int beginRange, int endRange, double optimum, int filterPrecision) {
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
        this.optimum = optimum;
        this.filterPrecision = filterPrecision;
    }

    public void run() {
        List<Particle> particles = initialize();
        String s;
        double oldSolution = bestSolution;
        DecimalFormat finalSolution = setDecimalFormat();
        exportViewPattern(finalSolution);

        for (int i = 0; i < epochs; i++) {
            swarmParticles.add(particles);
            oldSolutions.add(oldSolution);
            bestPositions.add(bestPosition);
            bestSolutions.add(bestSolution);

            if (bestSolution < oldSolution) {
                s = "Global best solution (Epoch " + (i) + "):\t" + solutionsPattern.format(bestSolution);
                algorithmTextLogs.add(s);
                oldSolution = bestSolution;
            } else {
                s = "Global best solution (Epoch " + (i) + "):\t" + solutionsPattern.format(bestSolution);
                algorithmTextLogs.add(s);
            }

            if (filterPrecision == 0) {
                if (bestSolution == optimum) {
                    break;
                }
            } else {
                if (finalSolution.format(bestSolution).equals(finalSolution.format(optimum))) {
                    break;
                }
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

    private List<Particle> initialize() {
        List<Particle> particles = new ArrayList<>();
        for (int i = 0; i < numOfParticles; i++) {
            Particle particle = new Particle(function, beginRange, endRange, optimum);
            particles.add(particle);
            updateGlobalBest(particle);
        }
        return particles;
    }

    private void exportViewPattern(DecimalFormat viewPattern) {
        PSOSceneController PSOSC = new PSOSceneController();
        PSOSC.setViewPattern(viewPattern);
    }

    private DecimalFormat setDecimalFormat() {
        String pattern = "#0.";
        if (filterPrecision == 0) {
            String stringDouble = Double.toString(Double.MAX_VALUE);

            for (int i = 0; i < stringDouble.length(); i++) {
                pattern += "0";
            }
        } else {
            for (int i = 0; i < filterPrecision; i++) {
                pattern += "0";
            }
        }
        return new DecimalFormat(pattern);
    }

    private void updateGlobalBest(Particle particle) {
            if (particle.getBestSolution() < bestSolution && particle.getBestSolution() >= optimum) {
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

    public List<List<Particle>> getSwarmParticles() {
        return swarmParticles;
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

}
