package presentation;

import domain.*;
import domain.producer.IProducer;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class ProductionController extends BorderPane {
    @FXML
    ListView<String> searchedProductionsList = null, //List of productions that matches the search used to make listView in the code
            searchedCastList = null,
            castView = null;
    @FXML
    BorderPane productionBorderPane, //Border pane in the scene
            centerBorderPane;//boarderpane used for top in productionBorderPane for layout

    @FXML
    Text header, center; //used for centerBorderPane

    @FXML
    Button assignCastButton, assignRoleButton;

    String headerText, centerText; //used for Text header and center
    List<Cast> castList; //Used for searchedCastList
    ArrayList<String> roleString; //List for update rollList

    ITV2WhoUI tv2Who = TV2Who.getInstance();
    FrameController frameController = null;
    CastController castController = null;
    private Production currentProduction;


    /**
     * makes subscene that can be loaded by frame
     */
    public ProductionController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("production.fxml"));
        fxmlLoader.setRoot(this);

        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setUp(){
        frameController = FrameController.getInstance();
        castController = frameController.getCastController();
    }


    /**
     * Used for when search on productions in frame
     * Makes listView for searched productions and sets it in the center of centerBorderPain and sets the text for top
     * of that(makes a header)
     *
     * @param searchWord
     * @param productions
     */
    public void productionList(String searchWord, List<Production> productions) {
        //makes listview
        ArrayList<String> stringList = new ArrayList<>();
        for (Production production : productions) {
            stringList.add(production.getName() + "\n" + production.getReleaseDate());
        }

        ObservableList<String> list = FXCollections.observableArrayList(stringList);
        searchedProductionsList = new ListView<String>(list);

        searchedProductionsList.getSelectionModel().getSelectedItem(); //Makes it possible to click on one

        clickingOnproductionList(productions); //Action when click on one

        //sets header and listview on centerBorderPain
        clearProductionBorderPane();//clears the borderPane
        headerText = "Der er " + productions.size() + " produktion(er) der matcher din søgning: '" + searchWord + "'";
        setHeader();

        VBox vBox = new VBox();
        vBox.getChildren().addAll(searchedProductionsList);
        vBox.setPadding(new Insets(0,0,100,100));
        productionBorderPane.setCenter(vBox);
    }

    /**
     * Clears productionBorderPane
     */
    private void clearProductionBorderPane() {
        productionBorderPane.setTop(null);
        productionBorderPane.setCenter(null);
        productionBorderPane.setBottom(null);
        productionBorderPane.setLeft(null);
        productionBorderPane.setRight(null);
    }

    /**
     * Makes a header in centerBorderPane
     */
    private void setHeader() {
        header = new Text(headerText);
        header.setTextAlignment(TextAlignment.CENTER);
        header.setFont(Font.font(25));
        productionBorderPane.setTop(header);
        productionBorderPane.setAlignment(header, Pos.CENTER);
    }

    /**
     * Makes a header in centerBorderPane with text
     *
     * @param headerText
     */
    private void setHeader(String headerText) {
        this.headerText = headerText;
        setHeader();
    }

    /**
     * Makes a text and sets it in center of centerBorderPane
     */
    public void setCenter() {
        center = new Text(centerText);
        center.setTextAlignment(TextAlignment.CENTER);
        center.setFont(Font.font(15));
        productionBorderPane.setCenter(center);
    }

    /**
     * Makes a text and sets it in center of centerBorderPane with a text
     *
     * @param centerText
     */
    public void setCenter(String centerText) {
        this.centerText = centerText;
        setCenter();
    }

    /**
     * Scene when no productions found
     */
    public void productionNotFound() {
        clearProductionBorderPane();
        setHeader("PRODUKTIONER");
        setCenter("INGEN PRODUKTIONER FUNDET");
    }

    /**
     * When clicking on a production scene gets changed to production information with listview of roles
     *
     * @param productions
     */
    public void clickingOnproductionList(List<Production> productions) {
        //Action when clicked on something
        searchedProductionsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //Gets the clicked item
                Production production = productions.get(searchedProductionsList.getSelectionModel().getSelectedIndex());
                productionProfile(production);
        }
        });
    }

    /**
     * Profile for production so can be used elsewhere also
     * @param production
     */
    public void productionProfile(Production production){
        try {
            Production prodUsing = production;
            currentProduction = prodUsing;

            if(prodUsing.getAssociatedProducerEmail() == null){
                prodUsing.setAssociatedProducerEmail("INGEN PRODUCER");
            }

            setHeader(prodUsing.getName());
            //Sets the left part for information about release date
            Label label = new Label("RELEASE DATE :");
            label.setFont(Font.font(15));
            Label label2 = new Label( prodUsing.getReleaseDate().toString());
            label2.setFont(Font.font(12));
            Label tvCodeTitel = new Label("TV KODE");
            tvCodeTitel.setFont(Font.font(15));
            Label tvCodeInfo = new Label ( prodUsing.getTvCode());
            tvCodeInfo.setFont(Font.font(12));


            VBox vbox = new VBox(10);
            vbox.setPrefWidth(120);
            vbox.setAlignment(Pos.TOP_LEFT);
            vbox.getChildren().addAll(label, label2, tvCodeTitel, tvCodeInfo);


            //Makes listview with all the roles in the production
            ArrayList<String> castList = new ArrayList<>();
            for (Cast cast : prodUsing.getCastList()) {
                String roles = "";
                ArrayList<String> roleListString = new ArrayList<>();

                for (Role r : cast.getRoles()) {
                    if (r.getProduction().equals(prodUsing)) {
                        roleListString.add(r.getRoleName());
                    }
                }
                for (int i = 0; i < roleListString.size(); i++) {
                    if (roleListString.size() == i - 1) {
                        roles = roles + roleListString.get(i);
                    } else {
                        roles = roles + roleListString + ", ";
                    }
                }
                castList.add(cast.getFirstName() + " " + cast.getLastName() + "\n" + roles);
            }
            ObservableList<String> list = FXCollections.observableArrayList(castList);

            castView = new ListView<>(list);
            clickingOnCastList(prodUsing.getCastList());

            castView.setMinWidth(500);
            HBox hbox = new HBox();
            hbox.getChildren().addAll(vbox,castView);
            hbox.setPadding(new Insets(0,0,0,100));
            hbox.setSpacing(20);
            clearProductionBorderPane();
            productionBorderPane.setCenter(hbox);

            //productionBorderPane.setCenter(castView);
        } catch (IndexOutOfBoundsException e) {

        }

    }

    /**
     * For when clicking on castView in clickOnProduction
     * @param casts
     */
    private void clickingOnCastList(List<Cast> casts) {
        //Action when clicked on something
        castView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Cast castUsing = casts.get(castView.getSelectionModel().getSelectedIndex());
                    createProfile(castUsing);
                } catch (IndexOutOfBoundsException e) {

                }
            }
        });
    }

    /**
     * Sets up cast profile
     * //TODO SEE if can use CAST in Framecontroller
     * @param cast
     */
    public void createProfile(Cast cast) {
        frameController.centerCast();
        castController.createProfile(cast);
    }


    /**
     * CALL THIS METHOD FOR WHEN WANTING TO ADD PRODUCTION
     * <p>
     * Start scene for creating production, adds roles and ends at the start scene again
     */
    public void createProduction() {
        clearProductionBorderPane();
        setHeader("Opret produktion");

        Text productionNameText = new Text("Produktions navn");
        TextField productionNameField = new TextField();
        productionNameField.setPromptText("Produktions navn");
        productionNameField.setPrefWidth(300);
        Text productionDayText = new Text("Dag");
        TextField productionDayField = new TextField();
        productionDayField.setPromptText("DD");
        productionDayField.setPrefWidth(50);
        Text productionMonthText = new Text("Måned");
        TextField productionMonthField = new TextField();
        productionMonthField.setPromptText("MM");
        productionMonthField.setPrefWidth(50);
        Text productionYearText = new Text("Year");
        TextField productionYearField = new TextField();
        productionYearField.setPromptText("YYYY");
        productionYearField.setPrefWidth(50);

        Text warningText = new Text();
        warningText.setFill(Color.RED);

        GridPane releaseDate = new GridPane();
        releaseDate.setVgap(10);
        releaseDate.setHgap(50);
        releaseDate.add(productionDayText, 0, 0);
        releaseDate.add(productionMonthText, 1, 0);
        releaseDate.add(productionYearText, 2, 0);
        releaseDate.add(productionDayField, 0, 1);
        releaseDate.add(productionMonthField, 1, 1);
        releaseDate.add(productionYearField, 2, 1);


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 0, 0, 150));
        grid.add(productionNameText, 2, 2);
        grid.add(productionNameField, 2, 3);
        grid.add(releaseDate, 2, 6);

        Button createProductionButton = new Button("Opret Produktion");
        grid.add(createProductionButton, 2, 11);
        grid.add(warningText, 2, 12);
        productionBorderPane.setCenter(grid);

        //Action for createProductionButton
        createProductionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Boolean dateUsable = true, nameUsable = true;
                Date date = null;
                Integer day, month, year;

                try {
                    day = Integer.valueOf(productionDayField.getText());
                    month = Integer.parseInt(productionMonthField.getText());
                    year = Integer.parseInt(productionYearField.getText());
                    date = new Date(year, month, day);
                } catch (NumberFormatException e) {
                    dateUsable = false;
                }

                if (productionNameField.getText().isEmpty()) {
                    nameUsable = false;
                }

                if (productionDayField.getText().length() > 2 || productionMonthField.getText().length() > 2
                        || productionYearField.getText().length() > 4) {
                    dateUsable = false;
                }

                if (nameUsable.equals(false)) {
                    warningText.setText("UDFYLD PRODUKTIONS NAVN");
                    return;
                } else {
                    warningText.setText("");
                }

                if (dateUsable.equals(false)) {
                    warningText.setText("UDFYLD OPRETTELSES DATO KORREKT");
                    return;
                } else {
                    warningText.setText("");
                }

                List<Production> list = tv2Who.prepareProductionSearchList(productionNameField.getText());

                ArrayList<Production> exists = new ArrayList<>();
                for (Production production : list) {
                    if (productionNameField.getText().equalsIgnoreCase(production.getName()) && date.equals(production.getReleaseDate())) {
                        exists.add(production);
                    }
                }

                if (!exists.isEmpty()) {
                    Stage stage = new Stage();
                    stage.setResizable(false);
                    BorderPane borderPane = new BorderPane();
                    borderPane.setPrefSize(500, 500);
                    Scene scene = new Scene(borderPane);

                    Text text = new Text("Der findes allerede en eller flere produktioner med dette navn og udgivelsesdato\n" +
                            "ønsker du stadig at oprette en produktion?");
                    borderPane.setCenter(text);

                    HBox hbox = new HBox();
                    Button yes = new Button("Ja");
                    Button no = new Button("Nej");
                    hbox.getChildren().addAll(yes, no);
                    hbox.setAlignment(Pos.TOP_CENTER);
                    hbox.setPrefHeight(150);
                    hbox.setSpacing(100);
                    borderPane.setBottom(hbox);

                    Date finalDate = date;
                    yes.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            stage.close();
                            addCastScene(productionNameField.getText(), finalDate);
                        }
                    });

                    no.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            stage.close();
                        }
                    });

                    stage.setAlwaysOnTop(true);
                    stage.initModality(Modality.APPLICATION_MODAL);

                    stage.setScene(scene);
                    stage.show();

                } else {
                    addCastScene(productionNameField.getText(), date);
                }
            }

        });

    }

    /**
     * creates a production but doesn't save at first. changes scene to where can add cast and roles .
     * When finished saves production in database
     * todo tilføje opretter email
     *
     * @param pName
     * @param pReleaseDate
     */
    private void addCastScene(String pName, Date pReleaseDate) {
        IProducer producer = (IProducer) tv2Who.getCurrentUser();

        producer.createProduction(pName, pReleaseDate);
        Production production = producer.getProduction();

        roleString = new ArrayList<>();

        List<String> roleListString = new ArrayList<>();

        clearProductionBorderPane();
        setHeader("TILFØJ MEDVIRKENDE");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 0, 0, 100));

        HBox hBox = new HBox();
        TextField searchCast = new TextField();
        searchCast.setPromptText("Søg efter medvirkende");
        searchCast.setPrefWidth(250);
        Button search = new Button("Søg");
        hBox.getChildren().addAll(searchCast, search);

        grid.add(hBox, 2, 2);
        productionBorderPane.setCenter(grid);

        Text castText = new Text("Valgte medspiller");
        grid.add(castText, 2, 4);
        String stringCastNotChoosen = "ROLLENAVN IKKE DEFINERET";
        Text castChoosenText = new Text(stringCastNotChoosen);
        grid.add(castChoosenText, 2, 5);

        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                castList = tv2Who.prepareCastSearchList(searchCast.getText());

                ArrayList<String> string = new ArrayList<>();
                for (Cast cast : castList) {
                    string.add(cast.getFirstName() + cast.getLastName() + "\n" + cast.getEmail());
                }

                ObservableList<String> list = FXCollections.observableArrayList(string);
                searchedCastList = new ListView<>(list);

                searchedCastList.getSelectionModel().getSelectedItem();
                choosingActorforRole(castList, castChoosenText);

                grid.add(searchedCastList, 2, 3);

            }
        });

        TextField roleName = new TextField();
        roleName.setPromptText("Ny rolle navn");
        roleName.setPrefWidth(300);
        Button addRole = new Button("Tilføj rolle");
        HBox hBoxRolle = new HBox();
        hBoxRolle.getChildren().addAll(roleName, addRole);
        grid.add(hBoxRolle, 3, 2);

        Text warning = new Text();
        warning.setFill(Color.RED);
        grid.add(warning, 3, 4);


        addRole.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                warning.setText("");

                if (castChoosenText.getText().equalsIgnoreCase(stringCastNotChoosen)) {
                    warning.setText("vælg en medspiller");
                } else if (roleName.getText().isEmpty()) {
                    Stage stage = new Stage();
                    stage.setResizable(false);
                    BorderPane borderPane = new BorderPane();
                    borderPane.setPrefSize(500, 500);
                    Scene scene = new Scene(borderPane);

                    Text text = new Text("Der er ikke tilføjet en rolle til medspilleren \n" +
                            "Vil du stadig tilføje medspilleren?");
                    borderPane.setCenter(text);

                    HBox hbox = new HBox();
                    Button yes = new Button("Ja");
                    Button no = new Button("Nej");
                    hbox.getChildren().addAll(yes, no);
                    hbox.setAlignment(Pos.TOP_CENTER);
                    hbox.setPrefHeight(150);
                    hbox.setSpacing(100);
                    borderPane.setBottom(hbox);
                    stage.setAlwaysOnTop(true);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.show();

                    yes.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            stage.close();
                            Cast castUsing = castList.get(searchedCastList.getSelectionModel().getSelectedIndex());
                            producer.addCastMember(castUsing, production);
                            updateRoleList(grid, production, roleName.getText(), castChoosenText.getText());
                        }
                    });
                    no.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            stage.close();
                        }
                    });
                } else {
                    Cast castUsing = castList.get(searchedCastList.getSelectionModel().getSelectedIndex());
                    producer.addCastMember(castUsing, production);
                    updateRoleList(grid, production, roleName.getText(), castChoosenText.getText());
                }
            }
        });

        Button finished = new Button("OPRET");
        grid.add(finished, 3, 5);

        finished.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                createProduction();

                producer.submitProduction();

                Stage stage = new Stage();
                stage.setResizable(false);
                BorderPane borderPane = new BorderPane();
                borderPane.setPrefSize(300, 300);
                Scene scene = new Scene(borderPane);

                Text text = new Text("PRODUKTIONEN ER OPRETTET");
                borderPane.setCenter(text);

                HBox hbox = new HBox();
                Button ok = new Button("OK");
                hbox.getChildren().addAll(ok);
                hbox.setAlignment(Pos.TOP_CENTER);
                hbox.setPrefHeight(150);
                hbox.setSpacing(100);
                borderPane.setBottom(hbox);
                stage.setAlwaysOnTop(true);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.show();

                ok.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        stage.close();
                    }
                });
            }
        });

    }

    /**
     * call this for updating roll list in addCastScene
     *
     * @param grid
     * @param production
     */
    private void updateRoleList(GridPane grid, Production production, String roleName, String castText) {

        String roleCounts = roleName;
        if (roleCounts.equalsIgnoreCase("")) {
            roleCounts = "ROLLENAVN IKKE DEFINERET";
        }
        roleString.add(castText + "\n" + roleCounts);


        ObservableList<String> list = FXCollections.observableArrayList(roleString);
        ListView<String> roleList = new ListView<>(list);
        Text productionsRoleText = new Text(production.getName() + " roles");
        VBox roleVBox = new VBox();
        roleVBox.getChildren().addAll(productionsRoleText, roleList);
        grid.add(roleVBox, 3, 3);

    }

    /**
     * click action for searchedCastList
     * Prints error when choose somthing that's not an item but ignore that
     *
     * @param casts
     * @param text
     */
    private void choosingActorforRole(List<Cast> casts, Text text) {
        //Action when clicked on something
        searchedCastList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Cast castUsing = casts.get(searchedCastList.getSelectionModel().getSelectedIndex());
                    text.setText(castUsing.getFirstName() + " " + castUsing.getLastName() + "\n" + castUsing.getEmail());
                } catch (IndexOutOfBoundsException e) {

                }

            }
        });
    }

    //TODO: virker ikke! Afhænger af metode som skal laves i persistence handleren..
    public void chooseProductionScene() {
        clearProductionBorderPane();

        //Scene elements setup
        VBox vertical1 = new VBox();
        vertical1.setPadding(new Insets(5, 5, 5, 100));
        ListView<Production> productionListView = new ListView<>();
        setHeader("Vælg produktion");

        IProducer producer = (IProducer) tv2Who.getCurrentUser();
        ObservableList<Production> observProductionList = FXCollections.observableArrayList();
        observProductionList.setAll(tv2Who.prepareProductionSearchList("b"));
        productionListView.setItems(observProductionList);

        productionListView.setOnMouseClicked((MouseEvent) -> {
            productionProfile(productionListView.getSelectionModel().getSelectedItem());
            frameController.setMyProductionsChosen();
        });

        vertical1.getChildren().add(productionListView);
        productionBorderPane.setCenter(vertical1);
    }

    /**
     * This method changes the ProductionPane to the administrative GUI scene in which admins can assign roles to
     * cast members for the production from which the scene is accessed.
     */
    public void assignCastScene() {
        clearProductionBorderPane();

        //Setup elements of the scene
        VBox vertical1 = new VBox();
        VBox vertical2 = new VBox();
        HBox horizontal = new HBox();
        horizontal.getChildren().addAll(vertical1, vertical2);
        horizontal.setPadding(new Insets(5, 5, 5, 100));
        vertical1.setPadding(new Insets(20, 20, 20, 20));
        vertical1.setSpacing(10);
        vertical2.setPadding(new Insets(20, 20, 20, 20));
        vertical2.setSpacing(10);
        productionBorderPane.setCenter(horizontal);
        setHeader("Tilknyt medvirkende til produktionen '" + currentProduction.getName() + "'");

        //Left side setup
        //Search list
        Label overheadSearchLabel = new Label("");
        ObservableList<Cast> observSearchCastList = FXCollections.observableArrayList();
        ListView searchListView = new ListView(observSearchCastList);
        searchListView.setPrefWidth(300);
        TextField castSearchField = new TextField();
        castSearchField.setPrefWidth(280);
        castSearchField.setPromptText("Indtast navn på medvirkende her");
        Button searchButton = new Button();
        searchButton.setText("Søg");
        HBox searchFieldBox = new HBox();
        searchFieldBox.getChildren().addAll(castSearchField, searchButton);

        //Choosen cast
        ObservableList<Cast> observChoosenCast = FXCollections.observableArrayList();
        ListView choosenCastView = new ListView(observChoosenCast);
        choosenCastView.setPrefHeight(320);
        TextField roleNameField = new TextField();
        roleNameField.setPromptText("Indtast rollenavn her");
        Button addToListButton = new Button();
        addToListButton.setText("Tilføj til liste");
        vertical1.getChildren().addAll(searchFieldBox, searchListView, choosenCastView, roleNameField, addToListButton);

        //Right side setup
        ObservableList<Cast> observAddedCastList = FXCollections.observableArrayList();
        ListView addedCastView = new ListView(observAddedCastList);
        addedCastView.setPrefWidth(250);
        Button commitButton = new Button();
        commitButton.setText("Tilføj valgte til produktion");
        vertical2.getChildren().addAll(addedCastView, commitButton);

        //Element functionality
        //Search Button functionality
        searchButton.setOnAction((event) -> {
            observSearchCastList.clear();
            observSearchCastList.setAll(tv2Who.prepareCastSearchList(castSearchField.getText()));
        });

        //Search List functionality
        searchListView.setOnMouseClicked((MouseEvent) -> {
            try {
                observChoosenCast.clear();
                int index = searchListView.getSelectionModel().getSelectedIndex();
                observChoosenCast.add(observSearchCastList.get(index));
            } catch (Exception e) {
                System.out.println("Empty element clicked");
            }
        });

        //Add to list button functionlaity
        addToListButton.setOnAction((event) -> {
            if (roleNameField.getText().equals("")) {
                observAddedCastList.add(observChoosenCast.get(0));
            } else {
                observChoosenCast.get(0).addRole(-1, roleNameField.getText(), currentProduction);
                observAddedCastList.add(observChoosenCast.get(0));
                observChoosenCast.clear();
                roleNameField.clear();
            }
        });

        //Removal of element in the added cast ListView
        addedCastView.setOnMouseClicked(MouseEvent -> {
            try {
                int index = addedCastView.getSelectionModel().getSelectedIndex();
                observAddedCastList.remove(index);
            } catch (Exception e) {
                System.out.println("Empty element clicked");
            }
        });

        //Commit button sends elements of "observAddedCastList" to database
        commitButton.setOnAction((event) -> {
            IProducer producer = (IProducer) tv2Who.getCurrentUser();
            for (Cast currCast : observAddedCastList) {
                System.out.println(currCast.getFirstName() + " added to production " + currentProduction.getName());
                producer.addCastMember(currCast, currentProduction);
            }
            observAddedCastList.clear();
        });
    }

    public void assignRoleScene() {
        clearProductionBorderPane();

        //Scene elements setup
        setHeader("Tildel roller for produktionen " + currentProduction.getName());
        VBox vertical1 = new VBox();
        vertical1.setPadding(new Insets(5, 80, 5, 100));
        vertical1.setMaxWidth(750);
        productionBorderPane.setCenter(vertical1);

        //List of cast members currently associated with the current production
        ObservableList<Cast> observCastList = FXCollections.observableArrayList(currentProduction.getCastList());
        ListView<Cast> castListView = new ListView<>(observCastList);

        //When a cast member has been chosen from the cast list, the object will appear here
        ObservableList<Cast> choosenCastMember = FXCollections.observableArrayList();
        ListView<Cast> choosenCastView = new ListView<>(choosenCastMember);
        choosenCastView.setPrefHeight(180);

        TextField roleNameField = new TextField();
        roleNameField.setPromptText("Indtast rollenavn her");

        Button commitButton = new Button();
        commitButton.setText("Tildel rolle til medvirkende");

        //Add to Vbox
        vertical1.getChildren().addAll(castListView,choosenCastView,roleNameField,commitButton);

        castListView.setOnMouseClicked((MouseEvent) -> {
            int index = castListView.getSelectionModel().getSelectedIndex();
            choosenCastMember.clear();
            choosenCastMember.add(castListView.getSelectionModel().getSelectedItem());
        });

        choosenCastView.setOnMouseClicked((MouseEvent) -> {
            choosenCastMember.clear();
        });

        commitButton.setOnAction((event) -> {
            if (choosenCastMember.size() == 0 || roleNameField.getText().equals("")){
                System.out.println("No elements in list or role field empty");
            }else{
                IProducer producer = (IProducer)tv2Who.getCurrentUser();
                producer.addRole(roleNameField.getText(),choosenCastMember.get(0),currentProduction);
                System.out.println(choosenCastMember);
                System.out.println(currentProduction);
                choosenCastMember.clear();
                roleNameField.clear();
            }
        });
    }

}
