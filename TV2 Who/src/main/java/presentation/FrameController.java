package presentation;

import domain.*;
import domain.Cast;
import domain.ITV2WhoUI;
import domain.Production;
import domain.TV2Who;
import domain.persistenceInterfaces.IPersistenceProduction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

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
    RadioButton productionRadioButton, castRadioButton;
    @FXML
    Label productionLabel, roleLabel;
    @FXML
    HBox frameHBox;

    private ProductionController productionController = new ProductionController();
    private CastController castController = new CastController();
    private WelcomeController welcomeController = new WelcomeController();
    private ITV2WhoUI tv2Who = TV2Who.getInstance();



    public FrameController(){}


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WelcomeController welcomeController = new WelcomeController();
        mainBorderPane.setCenter(welcomeController);

        //Sets radiobuttons together for searching
        ToggleGroup searchParameters = new ToggleGroup();
        productionRadioButton.setToggleGroup(searchParameters);
        castRadioButton.setToggleGroup(searchParameters);

    }


    /**
     * Sets center borderpain to welcome.fxml
     */
    public void centerWelcome(){
        welcomeController.setPrefHeight(450);
        mainBorderPane.setCenter(welcomeController);
    }

    /**
     * Used on tv2 icon on frame to set center to welcome
     * @param mouseEvent
     */
    public void centerWelcomeMouse(javafx.scene.input.MouseEvent mouseEvent){
        centerWelcome();
    }

    public void centerCastMouse(javafx.scene.input.MouseEvent mouseEvent){ centerCast();}

    /**
     * CHanges the scene depending on what gets searched
     * TODO skal kunne reagere på om der søges på skuespillere
     * TODO Funktionalitet for søg efter medvirkedne her
     * @param mouseEvent
     */
    public void search(javafx.scene.input.MouseEvent mouseEvent) {
        String searchWord = searchTextField.getText();
        if(productionRadioButton.isSelected() && !searchWord.equals("")){
//Todo næste linje fjerne kommentar
            //næste linje fjerne kommentar
            //List<Production> productionList = tv2Who.prepareProductionSearchList(searchWord);
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

            centerProduction();

            productionController.productionList(searchWord, productionList); //Den her skal fikses
        } else if(productionRadioButton.isSelected() && searchWord.equals("")){
            productionScene();
        } else if(castRadioButton.isSelected() && !searchWord.equals("")){
            centerCast();
            //TODO: Næste linje virker først når IPersistence også virker!
            castController.showCastList(searchWord,tv2Who.prepareCastSearchList(searchWord,""));

            //TODO: Følgende test slettes senere..
            ArrayList<Cast> testCastList = new ArrayList<>();
            Cast dummy1 = new Cast(80,"Nicolaj","Nielsen","nicoskov993@hotmail.com");
            Cast dummy2 = new Cast(81,"Tesniem","El-Merie","tesPrivat@gmail.com");
            testCastList.add(dummy1);
            testCastList.add(dummy2);
            castController.showCastList(searchWord,testCastList);
        }

    }

    /**
     * Sets center to production.fxml with right size
     */
    private void centerProduction(){
        productionController.setPrefHeight(450);
        mainBorderPane.setCenter(productionController);
    }

    public void centerCast(){
        castController.setPrefHeight(450);
        mainBorderPane.setCenter(castController);
        castController.noCastFoundMessage();
    }

    /**
     * Used for GUI when no productions found
     * @param mouseEvent
     */
    public void productionScene(javafx.scene.input.MouseEvent mouseEvent){
        productionController.productionNotFound();
        centerProduction();
    }

    /**
     * Sets center no productions found
     */
    private void productionScene(){
        productionController.productionNotFound();
        centerProduction();
    }

    /**
     * This should be deleted when createProduction has been tested
     * @param mouseEvent
     */
    public void createProductionScene(javafx.scene.input.MouseEvent mouseEvent){
        productionController.createProduction();
    }
}
