package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
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
    public Text pso_current_best_evaluation_text;
    public Text pso_global_best_evaluation_text;
    public Text pso_x_value_text;
    public Text pso_y_value_text;
    public Text pso_fields_error_text;

    private enum FunctionType {
        ACKLEY, BOOTH, CAMEL
    }

    private enum RadioChosen {
        DEFAULT, OWN
    }

    private FunctionType currentFunctionType;
    private RadioChosen interiaBox;
    private RadioChosen cognitiveBox;
    private RadioChosen socialBox;

    private double inertiaValue;
    private double cognitiveComponentValue;
    private double socialComponentValue;
    private int beginRange;
    private int endRange;

    private int applicationDelay;


    public void saveSettings(ActionEvent event) {
    }

    public void changeFunction(ActionEvent event) {
    }

    public void startPSOApplication(ActionEvent event) {
    }

    public void initialize() {
        setFunctionName();
    }

    private void setFunctionName() {
        String functionName = StartingSceneController.getFunction() + " function";
        pso_function_name_text.setText(functionName);
        checkFunctionType(functionName);
    }

    private void checkFunctionType(String functionName) {
        if (functionName.equals("Ackley"))
            currentFunctionType = FunctionType.ACKLEY;
        else if (functionName.equals("Booth"))
            currentFunctionType = FunctionType.BOOTH;
        else if (functionName.equals("Camel"))
            currentFunctionType = FunctionType.CAMEL;
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
            endRange = SwarmAlgorithm.getDefaultBeginRange();
        }
    }

}
