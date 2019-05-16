package swarmCore;

import controller.PSOSceneController;

import java.util.Random;

public class SwarmAlgorithm {

    private int numOfParticles, epochs;
    private double inertia, cognitiveComponent, socialComponent;
    private Vector bestPosition;
    private double bestEval;
    private Particle.FunctionType function; // The function to search.
    private int applicationDelay;
    private String algorithmTextLog = "";
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
     * @param particles        the number of particles to create
     * @param epochs           the number of generations
     * @param inertia          the particles resistance to change
     * @param cognitive        the cognitive component or introversion of the particle
     * @param social           the social component or extroversion of the particle
     * @param applicationDelay the speed of the swarm algorithm
     */
    public SwarmAlgorithm(Particle.FunctionType function, int particles, int epochs, double inertia, double cognitive, double social, int applicationDelay, int beginRange, int endRange) {
        this.numOfParticles = particles;
        this.epochs = epochs;
        this.inertia = inertia;
        this.cognitiveComponent = cognitive;
        this.socialComponent = social;
        this.function = function;
        this.applicationDelay = applicationDelay;
        double infinity = Double.POSITIVE_INFINITY;
        bestPosition = new Vector(infinity, infinity, infinity);
        bestEval = Double.POSITIVE_INFINITY;
        this.beginRange = beginRange;
        this.endRange = endRange;
    }

    /**
     * Execute the algorithm.
     */
    public void run() throws InterruptedException {
        Particle[] particles = initialize();
        String s = "";

        double oldEval = bestEval;
        System.out.println("--------------------------EXECUTING-------------------------");
        System.out.println("Global Best Evaluation (Epoch " + 0 + "):\t" + bestEval);

        for (int i = 0; i < epochs; i++) {
            Thread.sleep(applicationDelay);
            if (bestEval < oldEval) {
                s = "Global Best Evaluation (Epoch " + (i) + "):" + bestEval + "\n";
                oldEval = bestEval;
            } else if (i != 0)
                s = "Global Best Evaluation (Epoch " + (i) + "):" + bestEval + "\n";

            for (Particle p : particles) {
                p.updatePersonalBest();
                updateGlobalBest(p);
            }

            for (Particle p : particles) {
                updateVelocity(p);
                p.updatePosition();
            }

            setViewFields(s, oldEval);
        }

        System.out.println("---------------------------RESULT---------------------------");
        System.out.println("x = " + bestPosition.getX());
        System.out.println("y = " + bestPosition.getY());

        System.out.println("Final Best Evaluation: " + bestEval);
        System.out.println("---------------------------COMPLETE-------------------------");

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

    private void setViewFields(String s, double oldEval) {
        algorithmTextLog += s;

        PSOSceneController.pso_current_best_evaluation_text.setVisible(true);
        PSOSceneController.pso_global_best_evaluation_text.setVisible(true);
        PSOSceneController.pso_x_value_text.setVisible(true);
        PSOSceneController.pso_y_value_text.setVisible(true);

        PSOSceneController.pso_current_best_evaluation_text.setText(Double.valueOf(oldEval).toString());
        PSOSceneController.pso_global_best_evaluation_text.setText(Double.valueOf(bestEval).toString());
        PSOSceneController.pso_x_value_text.setText(Double.valueOf(bestPosition.getX()).toString());
        PSOSceneController.pso_y_value_text.setText(Double.valueOf(bestPosition.getY()).toString());
        PSOSceneController.pso_swarm_text_log_textarea.setText(algorithmTextLog);

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

    public int getNumOfParticles() {
        return numOfParticles;
    }

    public void setNumOfParticles(int numOfParticles) {
        this.numOfParticles = numOfParticles;
    }

    public int getEpochs() {
        return epochs;
    }

    public void setEpochs(int epochs) {
        this.epochs = epochs;
    }

    public double getInertia() {
        return inertia;
    }

    public void setInertia(double inertia) {
        this.inertia = inertia;
    }

    public double getCognitiveComponent() {
        return cognitiveComponent;
    }

    public void setCognitiveComponent(double cognitiveComponent) {
        this.cognitiveComponent = cognitiveComponent;
    }

    public double getSocialComponent() {
        return socialComponent;
    }

    public void setSocialComponent(double socialComponent) {
        this.socialComponent = socialComponent;
    }

    public Vector getBestPosition() {
        return bestPosition;
    }

    public void setBestPosition(Vector bestPosition) {
        this.bestPosition = bestPosition;
    }

    public double getBestEval() {
        return bestEval;
    }

    public void setBestEval(double bestEval) {
        this.bestEval = bestEval;
    }

    public Particle.FunctionType getFunction() {
        return function;
    }

    public void setFunction(Particle.FunctionType function) {
        this.function = function;
    }

    public int getBeginRange() {
        return beginRange;
    }

    public void setBeginRange(int beginRange) {
        this.beginRange = beginRange;
    }

    public int getEndRange() {
        return endRange;
    }

    public void setEndRange(int endRange) {
        this.endRange = endRange;
    }

    public static int getDefaultBeginRange() {
        return DEFAULT_BEGIN_RANGE;
    }

    public static int getDefaultEndRange() {
        return DEFAULT_END_RANGE;
    }
}
