package presentation;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Scene1 extends AnchorPane {

    public Scene1(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("scene1.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
