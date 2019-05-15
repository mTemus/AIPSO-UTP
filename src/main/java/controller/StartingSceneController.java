package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

public class StartingSceneController {
    public ComboBox starting_functions_combobox;
    public Button starting_start_button;
    public Text starting_choose_text;
    public Text starting_error_text;
    public Text starting_authors_text;

    public void saveFunction(ActionEvent event) {
    }

    public void startApplication(ActionEvent event) {
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
