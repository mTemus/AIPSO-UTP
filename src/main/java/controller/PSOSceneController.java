package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import swarmCore.Particle;
import swarmCore.SwarmAlgorithm;

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
    public RadioButton delay_0_radio;
    public RadioButton delay_1500_radio;
    public Button pso_change_function_button;
    public Button pso_start_button;
    public Text pso_current_best_evaluation_text = new Text();
    public Text pso_global_best_evaluation_text = new Text();
    public Text pso_x_value_text;
    public Text pso_y_value_text = new Text();
    public Text pso_fields_error_text = new Text();

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


    public void saveSettings(ActionEvent event) throws InterruptedException {
        checkRadioboxes();
        setParticlesAmount();
        setEpochsAmount();

        if (!fieldsFilledProperly())
            pso_fields_error_text.setVisible(true);
        else {
            pso_fields_error_text.setVisible(false);
            dataCollectedProperly = true;
        }

        Thread t = new Thread(()->{
            for (int i = 0; i < 100; i++){
                Text text = new Text();
                text.setText(Integer.valueOf(i).toString());
                setPso_x_value_text(text);
                System.out.println(i);
            }
        });
        t.start();
    }

    public void changeFunction(ActionEvent event) {
    }

    public void startPSOApplication(ActionEvent event) throws InterruptedException {
        if (dataCollectedProperly) {
            SwarmAlgorithm swarm = new SwarmAlgorithm(function, particlesAmount, epochsAmount,
                    inertiaValue, cognitiveComponentValue, socialComponentValue,
                    applicationDelay, beginRange, endRange);

            swarm.run();
        }
    }

    public void initialize() {
        setFunctionName();
    }

    private void setFunctionName() {
        String functionName = StartingSceneController.getFunction();
        pso_function_name_text.setText(functionName + " function");
        setFunction(functionName);

    }

    private void setFunction(String functionName) {
        if (functionName.equals("Ackley"))
            function = Particle.FunctionType.Ackleys;
        else if (functionName.equals("Booth"))
            function = Particle.FunctionType.Booths;
        else if (functionName.equals("Camel"))
            function = Particle.FunctionType.ThreeHumpCamel;
        else
            System.out.println("Check function type error.");
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

        if (delay_0_radio.isSelected())
            applicationDelay = 0;
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

    public Text getPso_current_best_evaluation_text() {
        return pso_current_best_evaluation_text;
    }

    public void setPso_current_best_evaluation_text(Text pso_current_best_evaluation_text) {
        this.pso_current_best_evaluation_text = pso_current_best_evaluation_text;
    }

    public Text getPso_global_best_evaluation_text() {
        return pso_global_best_evaluation_text;
    }

    public void setPso_global_best_evaluation_text(Text pso_global_best_evaluation_text) {
        this.pso_global_best_evaluation_text = pso_global_best_evaluation_text;
    }

    public Text getPso_x_value_text() {
        return pso_x_value_text;
    }

    public void setPso_x_value_text(Text pso_x_value_text) {
        this.pso_x_value_text = pso_x_value_text;
    }

    public Text getPso_y_value_text() {
        return pso_y_value_text;
    }

    public void setPso_y_value_text(Text pso_y_value_text) {
        this.pso_y_value_text = pso_y_value_text;
    }

    public TextArea getPso_swarm_text_log_textarea() {
        return pso_swarm_text_log_textarea;
    }

    public void setPso_swarm_text_log_textarea(TextArea pso_swarm_text_log_textarea) {
        this.pso_swarm_text_log_textarea = pso_swarm_text_log_textarea;
    }
}
