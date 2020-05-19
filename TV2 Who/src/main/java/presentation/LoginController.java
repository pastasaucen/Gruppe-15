package presentation;

import domain.TV2Who;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class LoginController extends BorderPane {


    @FXML
    Text warningText, emailText, codewordText, headerText;
    @FXML
    TextField emailField, codewordField;
    @FXML
    BorderPane borderPane;

    private TV2Who tv2Who = TV2Who.getInstance();

    FrameController frameController = null;

    public LoginController(){
        FXMLLoader loginFxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));

        loginFxmlLoader.setRoot(this);
        loginFxmlLoader.setController(this);

        try {
            loginFxmlLoader.load();
        } catch (
                IOException e) {
            System.out.println("Failed to load login.fxml");
        }

        frameController = FrameController.getInstance();
    }




    public void login(ActionEvent e){
        boolean exists = tv2Who.createUserSession(emailField.getText(), codewordField.getText());

        if(exists == false){
            warningText.setText("Email eller kodeord forkert \nprøv igen");
        } else{
            warningText.setText("");
            emailField.clear();
            codewordField.clear();
            frameController.loggedInFrame();
        }

    }




}
