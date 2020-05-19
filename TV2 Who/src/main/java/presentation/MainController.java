package presentation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    BorderPane borderPane;


    FrameController frameController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        frameController = FrameController.getInstance();
        borderPane.setCenter(frameController);
    }



}
