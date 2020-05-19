package presentation;

import domain.Cast;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
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
    ListView<String> searchList; //The ListView showing the found elements of a search
    @FXML
    BorderPane castBorderPane; //The main BorderPane of the scene, in which the elements are placed

    @FXML
    String headerText;
    @FXML
    Text header, centerText; //The header of the castBorderPane and a text field to fill the center of the Pane if needed

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
        searchList = new ListView<>(viewList);

        searchList.getSelectionModel().getSelectedItem();
        clickOnCastList(castList);

        clearCastPane();
        //Sets header accordingly to the length of castList and the search word
        setCastHeader(
                "Der er " + castList.size() + " produktionsmedvirkende der matcher din søgning " + "'" + searchWord + "'");

        castBorderPane.setCenter(searchList);

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
     * @param castList
     */
    public void clickOnCastList(List<Cast> castList) {

        searchList.setOnMouseClicked(MouseEvent -> {
            int index = searchList.getSelectionModel().getSelectedIndex();
            createProfile(castList.get(index));
        });
    }

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
        horizontal.setPadding(new Insets(5, 5, 5, 5));
        vertical1.setPadding(new Insets(10, 20, 20, 20));
        vertical2.setPadding(new Insets(20, 20, 20, 20));

        //Profile left side setup
        Label castNameLabel = new Label(firstName + " " + lastName);
        castNameLabel.setFont(Font.font(25));
        castNameLabel.setAlignment(Pos.CENTER);
        //castNameLabel.setPrefHeight(400);
        TextArea profileText = new TextArea(cast.getBio());
        profileText.setPrefHeight(400);
        profileText.setWrapText(true);
        profileText.setEditable(false);
        vertical1.getChildren().addAll(castNameLabel, profileText);


        //Profile right side setup
        ListView<String> roleView = new ListView<>();
        roleView.setPrefHeight(500);
        roleView.setPrefWidth(350);
        Label roleLabel = new Label("Medvirker i: ");
        roleLabel.setFont(Font.font(14));
        vertical2.getChildren().addAll(roleLabel, roleView);

        castBorderPane.setCenter(horizontal);
    }

}
