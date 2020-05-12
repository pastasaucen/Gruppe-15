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

  /*  @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }*/


  @Override
  public void start(Stage stage){
      try {
          root = FXMLLoader.load(getClass().getResource("frame.fxml"));
          stage.setTitle("TV2 WHO");
          Image stageLogo = new Image("/presentation/pictures/stageLogo.png");
          stage.getIcons().add(stageLogo);
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