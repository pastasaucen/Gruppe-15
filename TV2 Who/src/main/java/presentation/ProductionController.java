package presentation;

import domain.Cast;
import domain.Production;
import domain.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductionController extends BorderPane {
    @FXML
    ListView<String> searchedProductionsList; //List of productions that matches the search
    @FXML
    BorderPane productionBorderPane, centerBorderPane;//boarderpane for production.fxml and borderpain for the borderpain
    @FXML
    Text header, center; //used for centerBorderPane

    String headerText, centerText; //used for Text header and center

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
        searchedProductionsList.getSelectionModel().getSelectedItem();

        clickingOnproductionList(productions); //makes it possible to click on the results

        //sets header and listview on centerBorderPain
        clearProductionListBorderPane();
        headerText = "Der er " + productions.size() +  " produktion(er) der matcher din søgning: '" + searchWord + "'";
        setHeader();

        productionBorderPane.setCenter(searchedProductionsList);
    }

    /**
     * Clears productionBorderPane
     */
    private void clearProductionListBorderPane(){
        productionBorderPane.setLeft(null);
        productionBorderPane.setTop(null);
        productionBorderPane.setCenter(null);
        productionBorderPane.setBottom(null);
        productionBorderPane.setCenter(null);
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

}
