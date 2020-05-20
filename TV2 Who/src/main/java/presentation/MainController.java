package presentation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * This class is used to allow instances of framecontroller.
 * main.fxml is loaded in App and this then loads fram.fxml
 */

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
