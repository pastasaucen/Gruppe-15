package presentation;


import domain.*;
import domain.producer.IProducer;
import domain.producer.Producer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class FrameController extends BorderPane {
    @FXML
    BorderPane mainBorderPane;
    @FXML
    TextField searchTextField;
    @FXML
    RadioButton productionRadioButton;
    @FXML
    RadioButton castRadioButton;
    @FXML
    HBox frameHBox;
    @FXML
    Label productionLabel;
    @FXML
    Label castLabel;

    VBox optionsVBox = new VBox();
    Label userLabel, createProduction, createUser;

    private ProductionController productionController = new ProductionController();
    private CastController castController = new CastController();
    private WelcomeController welcomeController = new WelcomeController();
    private ITV2WhoUI tv2Who = TV2Who.getInstance();
    private LoginController loginController = null;
    private UserController userController = new UserController();

    private static FrameController instance = null;
    private User currentUser = null;


    private FrameController() {
        FXMLLoader frameFxmlLoader = new FXMLLoader(getClass().getResource("frame.fxml"));

        frameFxmlLoader.setRoot(this);
        frameFxmlLoader.setController(this);

        try {
            frameFxmlLoader.load();
        } catch (IOException e) {
            System.out.println("Failed to load frame.fxml");
        }
    }

    public static FrameController getInstance(){
        if (instance == null){
            instance = new FrameController();
            instance.setUp();
        }
        return instance;
    }


    public void setUp(){
        WelcomeController welcomeController = new WelcomeController();
        loginController = new LoginController();
        mainBorderPane.setCenter(welcomeController);

        //Sets radiobuttons together for searching
        ToggleGroup searchParameters = new ToggleGroup();
        productionRadioButton.setToggleGroup(searchParameters);
        castRadioButton.setToggleGroup(searchParameters);


    }

    /**
     * Sets center borderpain to welcome.fxml
     */
    public void centerLogin(){
        welcomeController.setPrefHeight(450);
        mainBorderPane.setCenter(loginController);
    }

    public void centerLoginMouse(javafx.scene.input.MouseEvent mouseEvent){ centerLogin();}

    /**
     * Sets center borderpain to welcome.fxml
     */
    public void centerWelcome(){
        welcomeController.setPrefHeight(450);
        mainBorderPane.setCenter(null);
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
            List<Production> productionList = tv2Who.prepareProductionSearchList(searchWord);

            centerProduction();

            productionController.productionList(searchWord, productionList); //Den her skal fikses
        } else if(productionRadioButton.isSelected() && searchWord.equals("")){
            productionScene();
        } else if(castRadioButton.isSelected() && !searchWord.equals("")){
            centerCast();
            //TODO: Næste linje virker først når IPersistence også virker!
            castController.showCastList(searchWord,tv2Who.prepareCastSearchList(searchWord));

        } else if(castRadioButton.isSelected() && searchWord.equals("")){
            centerCast();
        } else {
            centerWelcome();
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
        castRadioButton.setSelected(true);
        castController.setPrefHeight(450);
        mainBorderPane.setCenter(castController);
        castController.noCastFoundMessage();
    }

    /**
     * Used for GUI when no productions found
     * @param mouseEvent
     */
    public void productionScene(javafx.scene.input.MouseEvent mouseEvent){
        productionRadioButton.setSelected(true);
        productionController.productionNotFound();
        centerProduction();
    }

    /**
     * Sets center no productions found
     */
    //TODO: Hvad gør denne metode?
    private void productionScene(){
        productionRadioButton.setSelected(true);
        productionController.productionNotFound();
        centerProduction();
    }

    /**
     * This should be deleted when createProduction has been tested
     * @param mouseEvent
     */
    public void createProductionScene(javafx.scene.input.MouseEvent mouseEvent){
        centerProduction();
        productionController.createProduction();
    }

    public void loggedInFrame(){
    centerWelcome();
    currentUser = tv2Who.getCurrentUser();

    VBox menuVBox = new VBox();
    menuVBox.prefWidth(100);
    menuVBox.setAlignment(Pos.TOP_LEFT);
    menuVBox.setPadding(new Insets(10, 20,20,20));
    menuVBox.setStyle("-fx-background-color: #9b9b9b");

    Text emailText = new Text("EMAIL");
    emailText.setFont(Font.font(15));
    Text userEmailText = new Text(tv2Who.getCurrentUser().getEmail());
    Text typeText = new Text("BRUGER TYPE");
    typeText.setFont(Font.font(15));
    Text userTypeText = new Text(tv2Who.getCurrentUser().getUserType().toString());
    optionsVBox = new VBox();

    menuVBox.getChildren().addAll(emailText, userEmailText, typeText, userTypeText, optionsVBox);
    mainBorderPane.setLeft(menuVBox);

    userLabel = new Label("BRUGER");
    userLabel.setTextFill(Color.WHITE);
    userLabel.setAlignment(Pos.CENTER);
    userLabel.setPrefWidth(200);
    userLabel.setPrefHeight(60);
    frameHBox.getChildren().add(userLabel);

    loggedin();

    }

    public void loggedin(){
        switch (currentUser.getUserType()){
            case SYSTEMADMINISTRATOR:
                loggedinProduction();
                loggedinCast();
                loggedinUser();
                break;
            case PRODUCER:
                break;
            case EDITOR:
                break;
            case RDUSER:
        }
    }

    private void loggedinProduction(){

        productionLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                createProduction = new Label("OPRET PRODUKTION");
                optionsVBox.getChildren().clear();
                createOptionLabel(createProduction);
                optionsVBox.getChildren().add(createProduction);
                createProduction.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        centerProduction();
                        productionController.createProduction();
                    }
                });
            }
        });
    }

    private void loggedinCast(){

        castLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                optionsVBox.getChildren().clear();
            }
        });
    }

    private void loggedinUser(){

        userLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                createUser = new Label("CREATE USER");
                optionsVBox.getChildren().clear();
                createOptionLabel(createUser);
                optionsVBox.getChildren().add(createUser);

                createUser.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        centerUser();
                        userController.createUser();
                    }
                });

            }
        });

    }

    private void createOptionLabel(Label label){
        label.setPrefHeight(100);
        label.setAlignment(Pos.CENTER);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 15");

    }

    public void centerUser(){
        userController.setPrefHeight(450);
        mainBorderPane.setCenter(userController);
    }





    public void createCastScene(){
        mainBorderPane.setCenter(castController);
        castController.createCastScene();
    }
}
