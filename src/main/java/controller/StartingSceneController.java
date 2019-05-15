package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import operations.SceneOperations;

import java.io.IOException;

public class StartingSceneController {
    public ComboBox starting_functions_combobox;
    public Button starting_start_button;
    public Text starting_choose_text;
    public Text starting_error_text;
    public Text starting_authors_text;

    private static String function;
    private SceneOperations SO = new SceneOperations();


    public void saveFunction(ActionEvent event) {
        String chosenFunction = (String) starting_functions_combobox.getValue();

        if (chosenFunction.equals("Ackley function"))
            function = "ackley";
        else if (chosenFunction.equals("Booth function"))
            function = "booth";
        else if (chosenFunction.equals("Three-hump camel function"))
            function = "camel";


    }

    public void startApplication(ActionEvent event) {
        if (function != null) {
            try {
                SO.changeSceneToPSO(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            starting_error_text.setVisible(true);
            starting_error_text.setText("Please, choose a function!");
        }
    }

    public void initialize() {
        setFunctionCombobox();

    }

    private void setFunctionCombobox() {
        starting_functions_combobox.getItems().addAll(
                "Ackley function",
                "Booth function",
                "Three-hump camel function"
        );


    }

}
