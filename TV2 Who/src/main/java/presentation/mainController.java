package presentation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable {
    @FXML
    BorderPane borderPane;
    FrameController frameController = new FrameController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borderPane.setCenter(frameController);

    }
}
