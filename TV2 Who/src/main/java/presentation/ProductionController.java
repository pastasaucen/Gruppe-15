package presentation;

import domain.Cast;
import domain.Production;
import domain.Role;
import domain.persistenceInterfaces.IPersistenceCast;
import domain.persistenceInterfaces.IPersistenceProduction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.converter.DateTimeStringConverter;

import java.io.IOException;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


public class ProductionController extends BorderPane {
    @FXML
    ListView<String> searchedProductionsList, searchedCastList; //List of productions that matches the search
    @FXML
    BorderPane productionBorderPane, centerBorderPane;//boarderpane for production.fxml and borderpain for the top of productionBorderPane
    @FXML
    Text header, center; //used for centerBorderPane

    String headerText, centerText; //used for Text header and center
    IPersistenceProduction persistenceProduction; //TODO Instansiate
    IPersistenceCast persistenceCast; //TODO Instansiate
    List<Cast> castList;

    /**
     * makes subscene that can be loaded by fram
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

    /**
     * Makes listView for searched productions and sets it in the center of centerBorderPain and sets the text for top
     * of that(makes a header)
     * @param searchWord
     * @param productions
     */
    public void productionList(String searchWord, List<Production> productions){
        //makes listview
        ArrayList<String> stringList = new ArrayList<>();
        for (Production production: productions){
            stringList.add(production.getName() + "\n" + production.getReleaseDate());
        }

        ObservableList<String> list = FXCollections.observableArrayList(stringList);
        searchedProductionsList = new ListView<String>(list);
        searchedProductionsList.setStyle("-fx-control-inner-background: white"); //sets bagground color for listview

        searchedProductionsList.getSelectionModel().getSelectedItem(); //Makes it possible to click on one

        clickingOnproductionList(productions); //makes it possible to click on the results

        //sets header and listview on centerBorderPain
        clearProductionBorderPane();
        headerText = "Der er " + productions.size() +  " produktion(er) der matcher din søgning: '" + searchWord + "'";
        setHeader();


        productionBorderPane.setCenter(searchedProductionsList);
    }

    /**
     * Clears productionBorderPane
     */
    private void clearProductionBorderPane(){
        productionBorderPane.setTop(null);
        productionBorderPane.setCenter(null);
        productionBorderPane.setBottom(null);
        productionBorderPane.setLeft(null);
        productionBorderPane.setRight(null);
    }

    /**
     * Makes a header in centerBorderPane
     */
    private void setHeader(){
        header = new Text(headerText);
        header.setTextAlignment(TextAlignment.CENTER);
        header.setFont(Font.font(30));
        centerBorderPane.setCenter(header);
        centerBorderPane.setPrefHeight(50);
        productionBorderPane.setTop(centerBorderPane);
    }

    /**
     * Makes a header in centerBorderPane with text
     * @param headerText
     */
    private void setHeader(String headerText){
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
     * @param centerText
     */
    public void setCenter(String centerText) {
        this.centerText = centerText;
        setCenter();
    }

    /**
     * Scene when no productions found
     */
    public void productionNotFound(){
        clearProductionBorderPane();
        setHeader("PRODUKTIONER");
        setCenter("INGEN PRODUKTIONER FUNDET");
    }

    /**
     * When clicking on a production scene gets changed to production information with listview of roles
     * TODO make roles clickable
     * @param productions
     */
    public void clickingOnproductionList(List<Production> productions){
        //Action when clicked on something
        searchedProductionsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //Gets the clicked item
                Production prodUsing = productions.get(searchedProductionsList.getSelectionModel().getSelectedIndex());
                setHeader(prodUsing.getName());


                //Sets the left part for information about release date
                Label label = new Label("  RELEASE DATE :" );
                label.setFont(Font.font(15));
                Label label2 = new Label("   " + prodUsing.getReleaseDate().toString());
                label2.setFont(Font.font(12));

                VBox vbox = new VBox(10);
                vbox.setPrefWidth(120);
                vbox.setAlignment(Pos.TOP_LEFT);
                vbox.getChildren().addAll(label,label2);


                BorderPane left = new BorderPane();
                Label space = new Label("");
                label.setMinWidth(20);
                left.setLeft(space);
                left.setCenter(vbox);
                productionBorderPane.setLeft(left);


                //Makes listview with all the roles in the production
                ArrayList<String> castList = new ArrayList<>();
                for(Cast cast: prodUsing.getCast()){
                    String roles = "";
                    ArrayList<String> roleListString = new ArrayList<>();

                    for(Role r: cast.getRoles()){
                        if(r.getProduction().equals(prodUsing)){
                            roles = r.getRoleName();
                        }
                    }
                    castList.add(cast.getFirstName() + " " + cast.getLastName() +"\n" + roles );
                }
                ObservableList<String> list = FXCollections.observableArrayList(castList);

                ListView<String> castView = new ListView<>(list);
                productionBorderPane.setCenter(castView);

            }
        });
    }

    /**
     * Creates production
     * TODO tilføje opretter
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
        releaseDate.add(productionDayText,0,0);
        releaseDate.add(productionMonthText,1,0);
        releaseDate.add(productionYearText,2,0);
        releaseDate.add(productionDayField,0,1);
        releaseDate.add(productionMonthField,1,1);
        releaseDate.add(productionYearField,2,1);


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(100, 0, 0, 150));
        grid.add(productionNameText, 2, 2);
        grid.add(productionNameField, 2, 3);
        grid.add(releaseDate,2,6);

        Button createProductionButton = new Button("Opret Produktion");
        grid.add(createProductionButton, 2, 11);
        grid.add(warningText,2,12);



        createProductionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Boolean dateUsable = true, nameUsable = true;
                Date date = null;
                Integer day,month,year;

               try {
                   day = Integer.valueOf(productionDayField.getText());
                   month = Integer.parseInt(productionMonthField.getText());
                   year = Integer.parseInt(productionYearField.getText());
                   date = new Date(year, month, day);
               }catch(NumberFormatException e){
                   dateUsable = false;
               }

                if(productionNameField.getText().isEmpty()){
                    nameUsable = false;
                }

                if(nameUsable.equals(false)){
                    warningText.setText("UDFYLD PRODUKTIONS NAVN");
                    return;
                } else{
                    warningText.setText("");
                }

                if(dateUsable.equals(false)){
                    warningText.setText("UDFYLD OPRETTELSES DATO KORREKT");
                    return;
                } else{
                    warningText.setText("");
                }



                /*
                TODO fjerne kommentar
                List<Production> list = iPersistenceProduction.getProductions(productionNameField.getText());

                 */
                ArrayList<Production> list = new ArrayList<>();
                Production name = new Production(1, "name", new Date(2014,02,11));
                list.add(name);
                list.add(new Production(1, "second", new Date(2014,02,11)));
                Cast cast1 = new Cast(1, "1 firstname", "1 lastname", "1email");
                cast1.addRole("role", name);
                name.addCastMember(cast1);
                name.addCastMember(new Cast(2, "kj", "jn", "jk"));

                ArrayList<Production> exists = new ArrayList<>();
                for(Production production: list){
                    if (productionNameField.getText().equalsIgnoreCase(production.getName()) && date.equals(production.getReleaseDate())){
                        exists.add(production);
                    }
                }

                if (!exists.isEmpty()) {
                    Stage stage = new Stage();
                    stage.setResizable(false);
                    BorderPane borderPane = new BorderPane();
                    borderPane.setPrefSize(500,500);
                    Scene scene = new Scene(borderPane);

                    Text text = new Text("Der findes allerede en eller flere produktioner med dette navn og udgivelsesdato\n" +
                            "ønsker du stadig at oprette en produktion?");
                    borderPane.setCenter(text);


                    HBox hbox = new HBox();
                    Button yes = new Button("Ja");
                    Button no = new Button("Nej");
                    hbox.getChildren().addAll(yes,no);
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

                    stage.setScene(scene);
                    stage.show();

                } else {
                  addCastScene(productionNameField.getText(), date);
                }
            }

        });


        centerBorderPane.setCenter(grid);
    }

    public void addCastScene(String pName, Date pReleaseDate){
        Production production = new Production(pName,pReleaseDate);
        //production.setAssociatedProducerEmail(); //ToDo tilføje skaberen

        clearProductionBorderPane();
        setHeader("TILFØJ MEDVIRKENDE");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 0, 0, 150));

        HBox hBox = new HBox();
        TextField searchCast = new TextField();
        searchCast.setPromptText("Søg efter medvirkende");
        searchCast.setPrefWidth(300);
        Button search = new Button("Søg");
        hBox.getChildren().addAll(searchCast, search);

        grid.add(hBox,2,2);
        productionBorderPane.setCenter(grid);



        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //castList = persistenceCast.getCastMembers(searchCast.getText());
                //TODO Herfra til næste to do skal slettes
                Cast cast1 = new Cast(1, "1 firstname", "1 lastname", "1email");
                castList = new ArrayList<>();
                castList.add(cast1);
                //Todo Herfra til todo før denne slettes

                ArrayList<String> string = new ArrayList<>();
                for(Cast cast: castList){
                    string.add(cast.getFirstName() + cast.getLastName() + "\n" + cast.getEmail());
                }

                ObservableList<String> list = FXCollections.observableArrayList(string);
                searchedCastList = new ListView<>(list);
                searchedCastList.getSelectionModel().getSelectedItem();

                grid.add(searchedCastList,2,3);

            }
        });

        TextField roleName = new TextField();
        roleName.setPromptText("Ny rolle navn");
        roleName.setPrefWidth(300);
        Button addRole = new Button("Tilføj rolle");
        HBox hBoxRolle = new HBox();
        hBoxRolle.getChildren().addAll(roleName, addRole);
        grid.add(hBoxRolle,3, 2);

        updateRoleList(grid, production);

        Text warning = new Text();
        warning.setFill(Color.RED);
        grid.add(warning,3,4);

        addRole.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                warning.setText("");
                if(searchedCastList.getSelectionModel().getSelectedItem().isBlank() || roleName.getText().isEmpty()){
                    if(searchedCastList.getSelectionModel().getSelectedItem().isBlank()){
                        warning.setText("vælg en medspiller");
                    }else{
                        warning.setText("Giv rollen et navn");
                    }
                } else{
                    Cast castUsing = castList.get(searchedCastList.getSelectionModel().getSelectedIndex());
                    castUsing.addRole(roleName.getText(),production);
                    production.addCastMember(castUsing);
                    updateRoleList(grid, production);
                }
            }
        });

        Button finished = new Button("OPRET");
        grid.add(finished, 3, 5);

        finished.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //persistenceProduction.saveProduction(production);
                createProduction();
            }
        });



        //Todo tilføje produktion

    }

    private void updateRoleList(GridPane grid, Production production){
        ArrayList<String> roleString = new ArrayList<>();
        for(Cast cast: production.getCast()){
            String roleCounts = "";
            for(Role role: cast.getRoles()){
                if(role.getProduction().equals(production)){
                    roleCounts = role.getRoleName();
                }
            }
            roleString.add(cast.getFirstName() + " " + cast.getLastName() + "\n" + roleCounts );
        }

        ObservableList<String> list = FXCollections.observableArrayList(roleString);
        ListView<String> roleList = new ListView<>(list);
        Text productionsRoleText = new Text(production.getName() + " roles");
        VBox roleVBox = new VBox();
        roleVBox.getChildren().addAll(productionsRoleText, roleList);
        grid.add(roleVBox,3,3);

    }







}
