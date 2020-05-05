package presentation;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class FrameController {
    @FXML
    BorderPane mainBorderPane;

    @FXML
    public void onBtn1Click(){
        Scene1 scene1 = new Scene1();
        mainBorderPane.setCenter(scene1);

    }
}
