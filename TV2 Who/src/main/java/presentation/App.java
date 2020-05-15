package presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {


    private static Parent root;

  @Override
  public void start(Stage stage){
      try {
          root = FXMLLoader.load(getClass().getResource("frame.fxml"));
          stage.setTitle("TV2 WHO");

          Image stageLogo = new Image("/presentation/pictures/stageLogo.png");
          stage.getIcons().add(stageLogo);
          stage.setResizable(false);

          Scene scene = new Scene(root);
          stage.setScene(scene);
          stage.show();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

    public static void main(String[] args) {
        launch();
    }



}