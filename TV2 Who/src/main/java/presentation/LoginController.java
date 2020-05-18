package presentation;

import domain.TV2Who;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class LoginController extends BorderPane {

    @FXML
    Button loginButton;
    @FXML
    Text warningText, emailText, codewordText, headerText;
    @FXML
    TextField emailField, codewordField;
    @FXML
    BorderPane borderPane;

    private TV2Who tv2Who = TV2Who.getInstance();

    public LoginController(){
        FXMLLoader roleFxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));

        roleFxmlLoader.setRoot(this);
        roleFxmlLoader.setController(this);

        try {
            roleFxmlLoader.load();
        } catch (
                IOException e) {
            System.out.println("Failed to load cast.fxml");
        }
    }

    public void checkLogin(ActionEvent e){
        boolean exists = tv2Who.createUserSession(emailField.getText(), codewordField.getText());

        if(!exists){
            warningText.setText("Email eller kodeord forkert \nprøv igen");
        } else{
            warningText.setText("");
            clearBorderPane();

            Text text = new Text(" du er nu logget ind som \n" + tv2Who.getCurrentUser().getUserType());
            borderPane.setCenter(text);
        }

    }

    private void clearBorderPane(){
        headerText.setText("");
        borderPane.setCenter(null);
    }



}
