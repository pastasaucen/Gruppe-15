package presentation;


import domain.*;
import javafx.event.ActionEvent;
import domain.producer.IProducer;
import domain.producer.Producer;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FrameController extends BorderPane{
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
    @FXML
    ImageView loginImage, frameImage, searchImage;

    VBox optionsVBox = new VBox(); //Menu when logget in
    Label userLabel, createProduction, createUser, createCast, myProductions, assignRole, assignCast; //Labels for logget in

    private ProductionController productionController = null;
    private CastController castController = null;
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

    public CastController getCastController(){
        return castController;
    }

    public ProductionController getProductionController(){
        return productionController;
    }

    /**
     * Used instead of initialize
     */
    public void setUp(){
        WelcomeController welcomeController = new WelcomeController();
        productionController = new ProductionController();
        loginController = new LoginController();
        castController = new CastController();
        castController.setUp();
        productionController.setUp();
        mainBorderPane.setCenter(welcomeController);

        //Sets radiobuttons together for searching
        ToggleGroup searchParameters = new ToggleGroup();
        productionRadioButton.setToggleGroup(searchParameters);
        castRadioButton.setToggleGroup(searchParameters);
    }

    /**
     * Sets center borderpain to login.fxml
     */
    public void centerLogin(){
        welcomeController.setPrefHeight(450);
        mainBorderPane.setCenter(loginController);
    }

    /**
     * Sets center borderpane to login.fxml
     * @param mouseEvent
     */
    public void centerLoginMouse(javafx.scene.input.MouseEvent mouseEvent){
        centerLogin();
    }

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
     * Changes the scene depending on what gets searched
     * @param actionEvent
     */
    public void search(Event actionEvent) {
        String searchWord = searchTextField.getText();
        if(productionRadioButton.isSelected() && !searchWord.equals("")){

            List<Production> productionList = tv2Who.prepareProductionSearchList(searchWord);

            centerProduction();

            productionController.productionList(searchWord, productionList);
        } else if(productionRadioButton.isSelected() && searchWord.equals("")){
            productionScene();
        } else if(castRadioButton.isSelected() && !searchWord.equals("")){
            centerCast();
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
    public void centerProduction(){
        productionController.setPrefHeight(450);
        mainBorderPane.setCenter(productionController);
    }

    /**
     * center to cast
     */
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
        productionScene();
    }

    /**
     * Sets center the scene to a "no productions found" scene.
     */
    private void productionScene(){
        productionRadioButton.setSelected(true);
        productionController.productionNotFound();
        centerProduction();
    }

    /**
     *
     * @param mouseEvent
     */
    public void createProductionScene(javafx.scene.input.MouseEvent mouseEvent){
        centerProduction();
        productionController.createProduction();
    }

    /**
     *Sets setting when logget in --> Frame, menu and logout
     */
    public void loggedInFrame(){
    centerWelcome();
    currentUser = tv2Who.getCurrentUser();

    VBox menuVBox = new VBox();
    menuVBox.setMinWidth(200);
    menuVBox.setAlignment(Pos.TOP_LEFT);
    menuVBox.setPadding(new Insets(10, 20,20,20));
    menuVBox.setStyle("-fx-background-color: #9b9b9b");

    Text emailText = new Text("EMAIL");
    emailText.setFont(Font.font(15));
    Text userEmailText = new Text(tv2Who.getCurrentUser().getEmail());
    Text typeText = new Text("BRUGER TYPE");
    typeText.setFont(Font.font(15));
    Text userTypeText = new Text(currentUser.getUserType().toString());
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

    /**
     * Action when logget in depending on userType
     * can logOut when loggetin
     */
    public void loggedin(){
        loginImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                logOut();
            }
        });

        frameImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                centerWelcome();
                optionsVBox.getChildren().clear();
            }
        });

        searchImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                optionsVBox.getChildren().clear();
                search(mouseEvent);
            }
        });

        switch (currentUser.getUserType()){
            case SYSTEMADMINISTRATOR:
                loggedinProduction();
                loggedinCast();
                loggedinUser();
                break;
            case PRODUCER:
                loggedinProduction();
                loggedinCast();
                notLoggedInUser();
                break;
            case EDITOR:
                break;
            case RDUSER:
        }
    }

    /**
     * Sets action for logOutButton and returns everything to original after pushed
     */
    public void logOut(){
        mainBorderPane.setCenter(null);
        Button logOut = new Button("LOG UD");
        mainBorderPane.setCenter(logOut);

        logOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tv2Who.logOut();
                mainBorderPane.setLeft(null);
                userLabel = null;
                frameHBox.getChildren().remove(2);
                centerWelcome();

                productionLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        productionScene();
                    }
                });

                castLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        centerCastMouse(mouseEvent);
                    }
                });

                loginImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        centerLoginMouse(mouseEvent);
                    }
                });

                frameImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        centerWelcomeMouse(mouseEvent);
                    }
                });

                searchImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        search(mouseEvent);
                    }
                });

            }
        });
    }

    /**
     * sets functionality for when logged in and have acces for productions
     */
    private void loggedinProduction(){

        productionLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                productionScene();
                optionsVBox.getChildren().clear();
                createProduction = new Label("OPRET PRODUKTION");
                createOptionLabel(createProduction);

                myProductions = new Label("MINE PRODUKTIONER");
                createOptionLabel(myProductions);

                optionsVBox.getChildren().addAll(createProduction, myProductions);

                createProduction.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        centerProduction();
                        productionController.createProduction();

                    }
                });

                myProductions.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        productionScene();
                       productionController.chooseProductionScene();
                       optionsVBox.getChildren().clear();
                       optionsVBox.getChildren().addAll(createProduction, myProductions);

                    }
                });


            }
        });
    }

    /**
     * sets functionality for when logged in and have acces for cast
     */
    private void loggedinCast(){
        castLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                centerCast();
                optionsVBox.getChildren().clear();
                createCast = new Label("OPRET MEDVIRKENDE");
                createOptionLabel(createCast);
                optionsVBox.getChildren().add(createCast);

                createCast.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        createCastScene();
                    }
                });
            }
        });
    }

    /**
     * sets functionality for when logged in and have acces for user
     */
    private void loggedinUser(){

        userLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                userController.userStart();
                centerUser();
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

    public void notLoggedInUser(){
        frameHBox.getChildren().remove(2);
    }

    public void setMyProductionsChosen(){
        assignCast = new Label("TILDEL MEDVIRKENDE");
        createOptionLabelUnder(assignCast);
        assignRole = new Label("TILDEL ROLLE");
        createOptionLabelUnder(assignRole);

        VBox myProductionsVBox = new VBox();
        myProductionsVBox.getChildren().addAll(myProductions, assignCast,assignRole);
        optionsVBox.getChildren().clear();
        optionsVBox.getChildren().addAll(createProduction, myProductionsVBox);

        assignCast.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                productionScene();
                productionController.assignCastScene();
            }
        });

        assignRole.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                productionScene();
                productionController.assignRoleScene();
            }
        });

    }

    /**
     * setup for labels in menu
     * @param label
     */
    private void createOptionLabel(Label label){
        label.setPadding(new Insets(20,0,0,0));
        label.setAlignment(Pos.CENTER);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 15");

    }

    private void createOptionLabelUnder(Label label){
        createOptionLabel(label);
        label.setStyle("-fx-font-size: 10; -fx-text-fill: #5b0b59");
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
