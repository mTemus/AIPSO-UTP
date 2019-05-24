package controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import swarmCore.Particle;
import swarmCore.SwarmAlgorithm;
import swarmCore.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PSOSceneController {
    public Text starting_authors_text;
    public Text pso_function_name_text;
    public ProgressBar pso_swarm_progressbar;
    public Button pso_save_settings_button;
    public TextArea pso_swarm_text_log_textarea;

    public TextField pso_inertia_textfield;
    public TextField pso_cognitive_textfield;
    public TextField pso_social_textfield;
    public TextField pso_particles_amount_textfield;
    public TextField pso_number_of_epochs_textfield;
    public ToggleGroup delay;
    public RadioButton delay_500_radio;
    public RadioButton delay_200_radio;
    public RadioButton delay_50_radio;
    public RadioButton delay_1500_radio;
    public RadioButton delay_1000_radio;
    public Button pso_change_function_button;
    public Button pso_start_button;
    public Text pso_current_best_solution_text;
    public Text pso_global_best_solution_text;
    public Text pso_x_value_text;
    public Text pso_y_value_text;
    public Text pso_fields_error_text;
    public Text pso_current_epoch_number_text;
    public Text interia_property_text;
    public Text cognitive_property_text;
    public Text social_property_text;
    public Text optimum_property_text;
    public Text precision_property_text;
    public TextField pso_optimum_textfield;
    public TextField pso_precision_textfield;
    public AnchorPane pso_main_pane;

    private Particle.FunctionType function;
    private double inertiaValue;
    private double cognitiveComponentValue;
    private double socialComponentValue;
    private double swarmOptimum = 0.0;
    private int beginRange;
    private int endRange;
    private int particlesAmount;
    private int epochsAmount;
    private int applicationDelay;
    private int filterPrecision = 0;
    private boolean dataCollectedProperly = false;
    private static DecimalFormat viewPattern;
    private static DecimalFormat coordinatesPattern = new DecimalFormat("#0.0000000000000000000000");
    private DoubleProperty algorithmProgress = new SimpleDoubleProperty(0);

    private Circle cc;
    private List<Vector> bestPositions = new ArrayList<>();
    private List<Double> bestSolutions = new ArrayList<>();
    private List<Double> oldSolutions = new ArrayList<>();
    private List<String> algorithmTextLogs = new ArrayList<>();
    private List<List<Particle>> swarmParticles = new ArrayList<>();
    private List<Circle> particleObjects = new ArrayList<>();

    private boolean running = false;

    private SwarmAlgorithm swarm;

    public void saveSettings(ActionEvent event) {
        if (!running) {
            checkRadioboxes();
            setParticlesAmount();
            setEpochsAmount();
            setFilterPrecision();
            setSwarmSettings();
            setFieldSize();

            setPropertyText();

            if (!fieldsFilledProperly())
                pso_fields_error_text.setVisible(true);
            else {
                pso_fields_error_text.setVisible(false);
                dataCollectedProperly = true;
            }

            createParticlesToDraw();
        }
    }

    public void changeFunction(ActionEvent event) {
    }

    public void startPSOApplication(ActionEvent event) {
        if (!running) {
            running = true;
            if (dataCollectedProperly) {
                swarm = new SwarmAlgorithm(function,
                        particlesAmount, epochsAmount,
                        inertiaValue, cognitiveComponentValue,
                        socialComponentValue,
                        beginRange, endRange,
                        swarmOptimum, filterPrecision);
                swarm.run();

                Thread swarmThread = new Thread(() -> {
                    try {
                        startApplication();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                swarmThread.start();
            }
        }
    }

    public void resetPropertiesToDefault(ActionEvent event) {
        resetPropertyValues();
        resetPropertyText();
        resetPropertyTextfield();
    }

    public void increasePrecision(ActionEvent event) {
        if (filterPrecision < 20)
            filterPrecision++;
        pso_precision_textfield.setText(Integer.toString(filterPrecision));
    }

    public void decreasePrecision(ActionEvent event) {
        if (filterPrecision > 0)
            filterPrecision--;
        pso_precision_textfield.setText(Integer.toString(filterPrecision));
    }


    public void initialize() {
        setFunctionName();
        pso_swarm_progressbar.progressProperty().bind(getAlgorithmProgress());
//        cc = createCircle();
    }

    private void resetPropertyValues() {
        inertiaValue = SwarmAlgorithm.getDefaultInertia();
        cognitiveComponentValue = SwarmAlgorithm.getDefaultCognitive();
        socialComponentValue = SwarmAlgorithm.getDefaultSocial();
        swarmOptimum = 0.0;
        filterPrecision = 0;
    }

    private void setPropertyText() {
        interia_property_text.setText(Double.toString(inertiaValue));
        cognitive_property_text.setText(Double.toString(cognitiveComponentValue));
        social_property_text.setText(Double.toString(socialComponentValue));
        optimum_property_text.setText(Double.toString(swarmOptimum));
        precision_property_text.setText(Integer.toString(filterPrecision));
    }

    private void resetPropertyText() {
        interia_property_text.setText("0.729844 (default)");
        cognitive_property_text.setText("1.496180 (default)");
        social_property_text.setText("1.496180 (default)");
        optimum_property_text.setText("0.0 (default)");
        precision_property_text.setText("0 (default)");
    }

    private void resetPropertyTextfield() {
        pso_inertia_textfield.setText(Double.toString(inertiaValue));
        pso_cognitive_textfield.setText(Double.toString(cognitiveComponentValue));
        pso_social_textfield.setText(Double.toString(socialComponentValue));
        pso_optimum_textfield.setText(Double.toString(swarmOptimum));
        pso_precision_textfield.setText(Integer.toString(filterPrecision));
    }

    private void setFunctionName() {
        String functionName = StartingSceneController.getFunction();
        pso_function_name_text.setText(functionName + " function");
        setFunction(functionName);
    }

    private void setFunction(String functionName) {
        switch (functionName) {
            case "Ackley":
                function = Particle.FunctionType.Ackleys;
                break;
            case "Booth":
                function = Particle.FunctionType.Booths;
                break;
            case "Camel":
                function = Particle.FunctionType.ThreeHumpCamel;
                break;
            default:
                System.out.println("Check function type error.");
                break;
        }
    }

    private void setFieldSize() {
        beginRange = -275;
        endRange = 276;
    }

    private void checkRadioboxes() {
        if (delay_50_radio.isSelected())
            applicationDelay = 50;
        else if (delay_200_radio.isSelected())
            applicationDelay = 200;
        else if (delay_500_radio.isSelected())
            applicationDelay = 500;
        else if (delay_1000_radio.isSelected())
            applicationDelay = 1000;
        else if (delay_1500_radio.isSelected())
            applicationDelay = 1500;
    }

    private boolean fieldsFilledProperly() {
        return inertiaValue != 0 && cognitiveComponentValue != 0 && socialComponentValue != 0 && particlesAmount != 0 && epochsAmount != 0;
    }

    private void setSwarmSettings() {
        inertiaValue = Double.parseDouble(pso_inertia_textfield.getText());
        cognitiveComponentValue = Double.parseDouble(pso_cognitive_textfield.getText());
        socialComponentValue = Double.parseDouble(pso_social_textfield.getText());
        swarmOptimum = Double.parseDouble(pso_optimum_textfield.getText());
    }

    private void setFilterPrecision() {
        filterPrecision = Integer.parseInt(pso_precision_textfield.getText());
    }

    private void setParticlesAmount() {
        String particles = pso_particles_amount_textfield.getText();
        if (!particles.equals(""))
            particlesAmount = Integer.parseInt(particles);
    }

    private void setEpochsAmount() {
        String epochs = pso_number_of_epochs_textfield.getText();
        if (!epochs.equals(""))
            epochsAmount = Integer.parseInt(epochs);
    }

    private void getArrays() {
        bestPositions = swarm.getBestPositions();
        bestSolutions = swarm.getBestSolutions();
        oldSolutions = swarm.getOldSolutions();
        algorithmTextLogs = swarm.getAlgorithmTextLogs();
        swarmParticles = swarm.getSwarmParticles();
    }

    private void startApplication() throws InterruptedException {
        getArrays();
        setViewFields();
    }

    private void setViewFields() throws InterruptedException {
        String s = "";

        for (int i = 0; i < bestSolutions.size(); i++) {
            Thread.sleep(applicationDelay);
            setAlgorithmProgress(increaseProgress(i));

            for (Particle p : swarmParticles.get(i)) {
                for (Circle c : particleObjects) {
                    if (p.getBestSolution() == bestSolutions.get(i))
                        c.setFill(Color.RED);
                    else
                        c.setFill(Color.BLUE);

                    double coordinateX = 285.5 + (p.getPosition().getX() * 1000000000000d);
                    double coordinateY = 438 + (p.getPosition().getY() * 1000000000000d);

                    testCircle(c, coordinateX, coordinateY);
                }
            }

            pso_current_best_solution_text.setText(viewPattern.format(bestSolutions.get(i)));
            pso_global_best_solution_text.setText(viewPattern.format(oldSolutions.get(i)));
            pso_x_value_text.setText(String.valueOf(coordinatesPattern.format(bestPositions.get(i).getX())));
            pso_y_value_text.setText(String.valueOf(coordinatesPattern.format(bestPositions.get(i).getY())));
            s += algorithmTextLogs.get(i) + "\n";
            pso_swarm_text_log_textarea.setText(s);
            pso_current_epoch_number_text.setText(Integer.valueOf(i).toString());

//            testCircle(cc, 100 + bestPositions.get(i).getX(), 100 + bestPositions.get(i).getY());

            if (i == bestPositions.size() - 1)
                pso_global_best_solution_text.setText(viewPattern.format(bestSolutions.get(i)));
        }

        pso_current_best_solution_text.setText("");
        running = false;
    }

//    private Circle createCircle() {
//        List<Circle> circles = new ArrayList<>();
//        Circle c = new Circle();
//
//        c.setFill(Color.GREEN);
//        c.setStroke(Color.BLACK);
//        c.setVisible(true);
//        c.setRadius(5);
//        circles.add(c);
//        pso_main_pane.getChildren().add(circles.get(0));
//
//        return c;
//    }
//
    private void testCircle(Circle c, double x, double y) {
        c.setLayoutY(y);
        c.setLayoutX(x);
    }


    private double increaseProgress(int i) {
        double double_i = (double) i;
        double double_size = (double) bestSolutions.size();

        if (i == bestSolutions.size() - 1)
            return 1;

        return double_i / double_size;
    }

    private void createParticlesToDraw() {
        for (int i = 0; i < particlesAmount; i++) {
            Circle c = new Circle();
            c.setRadius(5);
            c.setStroke(Color.BLACK);
            c.setFill(Color.BLUE);
            c.setVisible(true);
//            c.setLayoutX(601);
//            c.setLayoutY(290);
            particleObjects.add(c);
            pso_main_pane.getChildren().add(particleObjects.get(i));
        }
    }

    private DoubleProperty getAlgorithmProgress() {
        return algorithmProgress;
    }

    private void setAlgorithmProgress(double algorithmProgress) {
        this.algorithmProgress.set(algorithmProgress);
    }

    public void setViewPattern(DecimalFormat viewPattern) {
        PSOSceneController.viewPattern = viewPattern;
    }


}
