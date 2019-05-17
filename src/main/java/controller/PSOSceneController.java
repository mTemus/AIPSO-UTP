package controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import swarmCore.Particle;
import swarmCore.SwarmAlgorithm;
import swarmCore.Vector;

import java.util.ArrayList;
import java.util.List;

public class PSOSceneController {
    public Text starting_authors_text;
    public Text pso_function_name_text;
    public ProgressBar pso_swarm_progressbar;
    public Button pso_save_settings_button;
    public TextArea pso_swarm_text_log_textarea;
    public RadioButton field_range_50_radio;
    public ToggleGroup field_range;
    public RadioButton field_range_100_radio;
    public RadioButton field_range_150_radio;
    public RadioButton field_range_200_radio;
    public RadioButton inertia_default_radio;
    public ToggleGroup inertia;
    public RadioButton inertia_own_radio;
    public TextField pso_own_inertia_textfield;
    public RadioButton cognitive_default_radio;
    public ToggleGroup cognitive;
    public RadioButton cognitive_own_radio;
    public TextField pso_own_cognitive_textfield;
    public RadioButton social_default_radio;
    public ToggleGroup social;
    public RadioButton social_own_radio;
    public TextField pso_own_social_textfield;
    public TextField pso_particles_amount_textfield;
    public TextField pso_number_of_epochs_textfield;
    public RadioButton delay_1000_radio;
    public ToggleGroup delay;
    public RadioButton delay_500_radio;
    public RadioButton delay_200_radio;
    public RadioButton delay_25_radio;
    public RadioButton delay_1500_radio;
    public Button pso_change_function_button;
    public Button pso_start_button;
    public Text pso_current_best_evaluation_text;
    public Text pso_global_best_evaluation_text;
    public Text pso_x_value_text;
    public Text pso_y_value_text;
    public Text pso_fields_error_text;
    public Text pso_current_epoch_number_text;

    private Particle.FunctionType function;
    private double inertiaValue;
    private double cognitiveComponentValue;
    private double socialComponentValue;
    private int beginRange;
    private int endRange;
    private int particlesAmount;
    private int epochsAmount;
    private int applicationDelay;
    private boolean dataCollectedProperly = false;
    private DoubleProperty algorithmProgress = new SimpleDoubleProperty(0);

    private List<Vector> bestPositions = new ArrayList<>();
    private List<Double> bestEvaluations = new ArrayList<>();
    private List<Double> oldEvaluations = new ArrayList<>();
    private List<String> algorithmTextLogs = new ArrayList<>();

    private boolean running = false;

    private SwarmAlgorithm swarm;

    public void saveSettings(ActionEvent event) {
        if (!running) {
            checkRadioboxes();
            setParticlesAmount();
            setEpochsAmount();

            if (!fieldsFilledProperly())
                pso_fields_error_text.setVisible(true);
            else {
                pso_fields_error_text.setVisible(false);
                dataCollectedProperly = true;
            }
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
                        beginRange, endRange);
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

    public void initialize() {
        setFunctionName();
        pso_swarm_progressbar.progressProperty().bind(getAlgorithmProgress());
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

    private void checkRadioboxes() {
        if (inertia_own_radio.isSelected())
            inertiaValue = Double.parseDouble(pso_own_inertia_textfield.getText());
        else
            inertiaValue = SwarmAlgorithm.getDefaultInertia();

        if (cognitive_own_radio.isSelected())
            cognitiveComponentValue = Double.parseDouble(pso_own_cognitive_textfield.getText());
        else
            cognitiveComponentValue = SwarmAlgorithm.getDefaultCognitive();

        if (social_own_radio.isSelected())
            socialComponentValue = Double.parseDouble(pso_own_social_textfield.getText());
        else
            socialComponentValue = SwarmAlgorithm.getDefaultSocial();

        if (field_range_50_radio.isSelected()) {
            beginRange = 0;
            endRange = 51;
        } else if (field_range_100_radio.isSelected()) {
            beginRange = -50;
            endRange = 51;
        } else if (field_range_150_radio.isSelected()) {
            beginRange = -50;
            endRange = 101;
        } else if (field_range_200_radio.isSelected()) {
            beginRange = SwarmAlgorithm.getDefaultBeginRange();
            endRange = SwarmAlgorithm.getDefaultEndRange();
        }

        if (delay_25_radio.isSelected())
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
        bestEvaluations = swarm.getBestEvals();
        oldEvaluations = swarm.getOldEvals();
        algorithmTextLogs = swarm.getAlgorithmTextLogs();
    }

//    private void lookArrays() {
//
//        System.out.println("Best positions size: " + bestPositions.size());
//        System.out.println("Best evals size: " + bestEvaluations.size());
//        System.out.println("Old evals size: " + oldEvaluations.size());
//        System.out.println("Text log size: " + algorithmTextLogs.size());
//        System.out.println("---------------------------------------------------");
//
//        for (Vector v : bestPositions) {
//            System.out.println("Best position: " + v.toString());
//        }
//        System.out.println("---------------------------------------------------");
//
//        for (Double d : bestEvaluations) {
//            System.out.println("Best eval: " + d);
//        }
//        System.out.println("---------------------------------------------------");
//        for (Double d : oldEvaluations) {
//            System.out.println("Old eval: " + d);
//        }
//        System.out.println("---------------------------------------------------");
//
//        for (String s : algorithmTextLogs) {
//            System.out.println(s);
//        }
//    }

    private void startApplication() throws InterruptedException {
        getArrays();
        setViewFields();
    }

    private void setViewFields() throws InterruptedException {
        String s = "";
        for (int i = 0; i < epochsAmount; i++) {
            Thread.sleep(applicationDelay);
            setAlgorithmProgress(increaseProgress(i));

            pso_current_best_evaluation_text.setText(bestEvaluations.get(i).toString());
            pso_global_best_evaluation_text.setText(oldEvaluations.get(i).toString());
            pso_x_value_text.setText(Double.valueOf(bestPositions.get(i).getX()).toString());
            pso_y_value_text.setText(Double.valueOf(bestPositions.get(i).getY()).toString());
            s += algorithmTextLogs.get(i) + "\n";
            pso_swarm_text_log_textarea.setText(s);
            pso_current_epoch_number_text.setText(Integer.valueOf(i).toString());

            if (bestEvaluations.get(i) == 0) {
                pso_global_best_evaluation_text.setText(bestEvaluations.get(i).toString());
                setAlgorithmProgress(1d);
                running = false;
                break;
            } else if (i == epochsAmount)
                pso_global_best_evaluation_text.setText(bestEvaluations.get(i).toString());
        }

        pso_current_best_evaluation_text.setText("");
        running = false;
    }

    private double increaseProgress(int i) {
        double double_i = (double) i;
        double double_epochs = (double) epochsAmount;

        if (i == epochsAmount - 1)
            return 1;

        return double_i / double_epochs;
    }

    private DoubleProperty getAlgorithmProgress() {
        return algorithmProgress;
    }

    private void setAlgorithmProgress(double algorithmProgress) {
        this.algorithmProgress.set(algorithmProgress);
    }
}
