package operations;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneOperations {

    public void changeSceneToPSO(ActionEvent event) throws IOException {
        Parent EventViewParent = FXMLLoader.load(getClass().getResource("/PSOScene.fxml"));
        Scene eventScene = new Scene(EventViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(eventScene);
        window.setTitle("PSO Project | Marcin Wójcik & Sebastian Kemnitz");
        window.show();
    }
}
