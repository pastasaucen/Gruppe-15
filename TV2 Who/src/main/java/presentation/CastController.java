package presentation;

import domain.*;
import domain.producer.IProducer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CastController extends BorderPane {
    @FXML
    ListView<String> searchView; //The ListView showing the found elements of a search
    @FXML
    ListView<String> roleView;
    @FXML
    BorderPane castBorderPane; //The main BorderPane of the scene, in which the elements are placed
    @FXML
    String headerText;
    @FXML
    Text header, centerText; //The header of the castBorderPane and a text field to fill the center of the Pane if needed

    ITV2WhoUI tv2Who = TV2Who.getInstance();
    IProducer producer;
    FrameController frameController = null;
    ProductionController productionController = null;

    public CastController() {
        FXMLLoader castFxmlLoader = new FXMLLoader(getClass().getResource("cast.fxml"));
        castFxmlLoader.setRoot(this);
        castFxmlLoader.setController(this);

        try {
            castFxmlLoader.load();
        } catch (IOException e) {
            System.out.println("Failed to load cast.fxml");
        }
        setCastHeader("MEDVIRKENDE");


    }

    public void setUp(){
        frameController = FrameController.getInstance();
        productionController = frameController.getProductionController();
    }

    /**
     * Clears all elements of the cast view BorderPane
     */
    public void clearCastPane() {
        castBorderPane.setCenter(null);
        castBorderPane.setTop(null);
        castBorderPane.setBottom(null);
        castBorderPane.setLeft(null);
        castBorderPane.setRight(null);
    }

    /**
     * Changes the header of the cast view BorderPane
     *
     * @param text
     */
    public void setCastHeader(String text) {
        header = new Text();
        header.setText(text);
        header.setTextAlignment(TextAlignment.CENTER);
        header.setFont(Font.font(30));
        castBorderPane.setTop(header);
        castBorderPane.setAlignment(header, Pos.CENTER);
    }

    /**
     * Used to display the castList (argument) as an observable list on the cast view BorderPane.
     * The searchWord string is used to display what has currently been searched.
     * This method does not handle the search logic itself - this is handled through the ITV2WhoUI interface.
     *
     * @param searchWord
     * @param castList
     */
    public void showCastList(String searchWord, List<Cast> castList) {
        //Creates list of names from the castList
        ArrayList<String> nameList = new ArrayList<>();
        for (Cast current : castList) {
            String nameString = current.getFirstName() + " " + current.getLastName();
            nameList.add(nameString);
        }

        //Creating an Observable List and parses the list of relevant names as argument
        ObservableList<String> viewList = FXCollections.observableArrayList(nameList);
        searchView = new ListView<>(viewList);

        searchView.getSelectionModel().getSelectedItem();
        clickOnCastList(castList);

        clearCastPane();
        //Sets header accordingly to the length of castList and the search word
        setCastHeader(
                "Der er " + castList.size() + " produktionsmedvirkende der matcher din søgning " + "'" + searchWord + "'");

        VBox vBox = new VBox();
        vBox.getChildren().add(searchView);
        vBox.setPadding(new Insets(0,0,0,100));
        castBorderPane.setCenter(vBox);
    }

    /**
     * Used as a visual reprepresentation if a search gives no results or the search field has been left empty
     */
    public void noCastFoundMessage() {
        clearCastPane();
        this.setCastHeader("MEDVIRKENDE");
        centerText = new Text("INGEN MEDVIRKENDE FUNDET");
        centerText.setFont(Font.font(20));
        castBorderPane.setCenter(centerText);
        castBorderPane.setAlignment(centerText, Pos.CENTER);
    }

    /**
     * This method handles the interactivity that enables a user to press a given element of the list of (cast object) search results.
     * When an element is pressed, the pane will be cleared and a profile of the cast member will be setup and displayed
     *
     * @param castList
     */
    public void clickOnCastList(List<Cast> castList) {
        searchView.setOnMouseClicked(MouseEvent -> {
            try {
                int index = searchView.getSelectionModel().getSelectedIndex();
                createProfile(castList.get(index));
            } catch (IndexOutOfBoundsException e){

            }
        });
    }

    public void clickOnRoleList(List<Role> roleList) {

        roleView.setOnMouseClicked(MouseEvent -> {
            int index = roleView.getSelectionModel().getSelectedIndex();
            try {
                createProductionProfile(roleList.get(index).getProduction());
            } catch (IndexOutOfBoundsException e) {
                System.out.println("No item was selected...");
            }
        });
    }

    /**
     * This method clears the CastPane and inserts the elements that comprises the profile of the cast member argument
     * @param cast
     */
    public void createProfile(Cast cast) {
        clearCastPane();
        String lastName = cast.getLastName();
        String firstName = cast.getFirstName();
        //String bio = castList.get(index).getBio();

        //The setup elements of a cast profile
        VBox vertical1 = new VBox();
        VBox vertical2 = new VBox();
        HBox horizontal = new HBox();
        horizontal.getChildren().addAll(vertical1, vertical2);
        horizontal.setPadding(new Insets(5, 100, 5, 100));
        vertical1.setPadding(new Insets(20, 20, 20, 20));
        vertical2.setPadding(new Insets(20, 20, 20, 20));

        //Profile left side setup
        Label castNameLabel = new Label(firstName + " " + lastName);
        castNameLabel.setFont(Font.font(25));
        castNameLabel.setAlignment(Pos.CENTER);
        //castNameLabel.setPrefHeight(400);
        TextArea profileText = new TextArea(cast.getBio());
        profileText.setPrefHeight(400);
        profileText.setPrefWidth(300);
        profileText.setWrapText(true);
        profileText.setEditable(false);
        vertical1.getChildren().addAll(castNameLabel, profileText);


        //Profile right side setup
        roleView = new ListView<>();
        roleView.setPrefHeight(500);
        roleView.setPrefWidth(350);
        Label roleLabel = new Label("Medvirker i: ");
        roleLabel.setFont(Font.font(14));
        vertical2.getChildren().addAll(roleLabel, roleView);

        //Role view setup
        ObservableList roleViewList = FXCollections.observableArrayList(cast.getRoles());
        roleView.setItems(roleViewList);

        roleView.getSelectionModel().getSelectedItem();
        clickOnRoleList(cast.getRoles());


        castBorderPane.setCenter(horizontal);
    }

    //TODO: Denne metode er ikke endelig. Der skal i virkeligheden skiftes scene
    public void createProductionProfile(Production prodUsing) {
        frameController.centerProduction();
        productionController.productionProfile(prodUsing);

    }

    /**
     * This method changes the CastPane to the administrative GUI scene in which admins can create new Cast objects and
     * send these objects to the database.
     */
    public void createCastScene() {
        clearCastPane();

        //CastPane setup
        setCastHeader("Opret ny medvirkende");
        //The setup elements of a cast profile
        VBox vertical1 = new VBox();
        VBox vertical2 = new VBox();
        HBox horizontal = new HBox();
        horizontal.getChildren().addAll(vertical1, vertical2);
        horizontal.setPadding(new Insets(5, 5, 5, 100));
        vertical1.setPadding(new Insets(20, 20, 20, 20));
        vertical1.setSpacing(10);
        vertical2.setPadding(new Insets(20, 20, 20, 20));
        vertical2.setSpacing(10);
        castBorderPane.setCenter(horizontal);
        Button addToListButton = new Button();
        addToListButton.setStyle("-fx-cursor: hand");
        addToListButton.setText("Tilføj til liste");

        //Left VBox setup: Elements for creating cast
        Double labelWidth = 80.0;
        Double fieldWidth = 290.0;
        //First Name box
        HBox firstNameBox = new HBox();
        Label firstNameLabel = new Label("*Fornavn: ");
        firstNameLabel.setPrefWidth(labelWidth);
        TextField firstNameField = new TextField();
        firstNameField.setPrefWidth(fieldWidth);
        firstNameField.setPromptText("Indtast fornavn her");
        firstNameBox.getChildren().addAll(firstNameLabel, firstNameField);
        vertical1.getChildren().add(firstNameBox);

        //Last Name box
        HBox lastNameBox = new HBox();
        Label lastNameLabel = new Label("*Efternavn: ");
        lastNameLabel.setPrefWidth(labelWidth);
        TextField lastNameField = new TextField();
        lastNameField.setPrefWidth(fieldWidth);
        lastNameField.setPromptText("Indtast efternavn her");
        lastNameBox.getChildren().addAll(lastNameLabel, lastNameField);
        vertical1.getChildren().add(lastNameBox);

        //Email box
        HBox emailBox = new HBox();
        Label emailLabel = new Label("*Email: ");
        emailLabel.setPrefWidth(labelWidth);
        TextField emailField = new TextField();
        emailField.setPrefWidth(fieldWidth);
        emailField.setPromptText("Indtast email adresse her");
        emailBox.getChildren().addAll(emailLabel, emailField);
        vertical1.getChildren().add(emailBox);

        //Biography box
        Label bioLabel = new Label("Biografi: ");
        bioLabel.setPrefWidth(labelWidth);
        TextArea bioArea = new TextArea();
        bioArea.setPrefWidth(360);
        bioArea.setWrapText(true);
        bioArea.setPromptText("Indtast biografi her");
        vertical1.getChildren().addAll(bioLabel, bioArea);

        //Add to list button
        vertical1.getChildren().add(addToListButton);
        addToListButton.setPrefWidth(360);

        //Right vbox setup
        ObservableList<Cast> observCastList = FXCollections.observableArrayList();
        ListView tempCastView = new ListView(observCastList);
        tempCastView.setPrefWidth(250);
        Label tempCastLabel = new Label("Nye medvirkende: ");
        Button commitButton = new Button();
        commitButton.setStyle("-fx-cursor: hand");
        commitButton.setPrefWidth(250);
        commitButton.setText("Tilføj nye medvirkende til database");
        vertical2.getChildren().addAll(tempCastLabel, tempCastView, commitButton);

        //Button setup
        addToListButton.setOnAction((event) -> {
            if (firstNameField.getText().equals("") || lastNameField.getText().equals("") || emailField.getText().equals("")) {
                addToListButton.setText("Udfyld venligst fornavn, efternavn og email");
                addToListButton.setStyle("-fx-text-fill: #ff0000");
            } else if (!emailField.getText().contains("@") || !emailField.getText().contains(".")) {
                addToListButton.setText("Email ugyldig");
                addToListButton.setStyle("-fx-text-fill: #ff0000");
            } else {
                addToListButton.setText("Tilføj til liste");
                addToListButton.setStyle("-fx-text-fill: #000000");
                try {
                    Cast tempCast = new Cast(-1, firstNameField.getText(), lastNameField.getText(), emailField.getText(), bioArea.getText());
                    observCastList.add(tempCast);
                    firstNameField.clear();
                    lastNameField.clear();
                    emailField.clear();
                    bioArea.clear();
                } catch (Exception e) {
                    System.out.println("Failed to create Cast object");
                }
            }
        });

        //Commit Button checks elements from
        commitButton.setOnAction((event) -> {

            producer = (IProducer) tv2Who.getCurrentUser();

            for (Cast curCast : observCastList) {
                List<Cast> tempList = tv2Who.prepareCastSearchList(curCast.getEmail());
                if (tempList.size() > 0) {
                    System.out.println("DEBUG: Alert Dialogue should appear here..");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("Emailadressen er allerede i databasen");
                    alert.setContentText("Emailadressen " + curCast.getEmail() + "\ner allerede fundet i systemet på " +
                            "personen: " + "\n" + tempList.get(0).getFirstName() + " " + tempList.get(0).getLastName());
                    ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    alert.showAndWait();
                } else {
                    producer.createCastMember(curCast.getFirstName(),
                            curCast.getLastName(),
                            curCast.getEmail(),
                            curCast.getBio());
                }
            }
            observCastList.clear();
        });

        //Removal of element in the create cast ListView
        tempCastView.setOnMouseClicked(MouseEvent -> {
            try {
                int index = tempCastView.getSelectionModel().getSelectedIndex();
                observCastList.remove(index);
            }catch (Exception e){
                System.out.println("Empty element clicked");
            }
            });
        }
    }
