package presentation;

import domain.Production;
import domain.persistenceInterfaces.IPersistenceProduction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FrameController implements Initializable {
    @FXML
    BorderPane mainBorderPane;
    @FXML
    ImageView searchImage;
    @FXML
    TextField searchTextField;
    @FXML
    RadioButton productionRadioButton, actorRadioButton;

    IPersistenceProduction persistenceProduction;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WelcomeController welcomeController = new WelcomeController();
        mainBorderPane.setCenter(welcomeController);
    }

    public void search(ActionEvent e){
        if(productionRadioButton.isPressed() && !searchTextField.getText().equals("")){

            //List<Production> productionList = persistenceProduction.getProductions(searchTextField.getText());
            //Tester
            List<Production> productionList = new ArrayList<>();
            productionList.add(new Production(1, "name", new Date()));

            ProductionListController productionListController = new ProductionListController();
            productionListController.productionList(productionList); //Den her skal fikses
            mainBorderPane.setCenter(productionListController);

        }
    }

}
