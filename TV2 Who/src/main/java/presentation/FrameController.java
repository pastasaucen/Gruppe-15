package presentation;

import domain.Cast;
import domain.Production;
import domain.persistenceInterfaces.IPersistenceProduction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
    @FXML
    Label productionLabel, roleLabel;

    IPersistenceProduction persistenceProduction;
    ProductionController productionController = new ProductionController();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WelcomeController welcomeController = new WelcomeController();
        mainBorderPane.setCenter(welcomeController);

        ToggleGroup searchParameters = new ToggleGroup();
        productionRadioButton.setToggleGroup(searchParameters);
        actorRadioButton.setToggleGroup(searchParameters);

    }

    public void search(javafx.scene.input.MouseEvent mouseEvent) {
        String searchWord = searchTextField.getText();
        if(productionRadioButton.isSelected() && !searchWord.equals("")){
//Todo næste linje fjerne kommentar
            //næste linje fjerne kommentar
            //List<Production> productionList = persistenceProduction.getProductions(searchWord);
//TODO Herfra til næste to do skal slettes
            List<Production> productionList = new ArrayList<>();
            Production name = new Production(1, "name", new Date(2014,02,11));
            productionList.add(name);
            productionList.add(new Production(1, "second", new Date(2014,02,11)));
            Cast cast1 = new Cast(1, "1 firstname", "1 lastname", "1email");
            cast1.addRole("role", name);
            name.addCastMember(cast1);
            name.addCastMember(new Cast(2, "kj", "jn", "jk"));
            //Todo Herfra til todo før denne slettes

            mainBorderPane.setCenter(productionController);
            productionController.productionList(searchWord, productionList); //Den her skal fikses
        } else if(productionRadioButton.isSelected() && searchWord.equals("")){
            productionController.productionNotFound();
            mainBorderPane.setCenter(productionController);
        }

    }

    public void productionScene(javafx.scene.input.MouseEvent mouseEvent){
        productionController.productionNotFound();
        mainBorderPane.setCenter(productionController);
    }
}
