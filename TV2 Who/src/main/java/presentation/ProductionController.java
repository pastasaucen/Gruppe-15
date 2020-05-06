package presentation;

import domain.Cast;
import domain.Production;
import domain.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductionController extends BorderPane {
    @FXML
    ListView<String> searchedProductionsList;
    @FXML
    BorderPane productionListBorderPane, centerBorderPane;
    @FXML
    Text header, center;

    String headerText, centerText;

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

    public void productionList(String searchWord, List<Production> productions){
        ArrayList<String> stringList = new ArrayList<>();
        for (Production production: productions){
            stringList.add(production.getName() + "\n" + production.getReleaseDate());
        }

        ObservableList<String> list = FXCollections.observableArrayList(stringList);
        searchedProductionsList = new ListView<String>(list);
        searchedProductionsList.getSelectionModel().getSelectedItem();

        trying(productions);


        headerText = "Der er " + productions.size() +  " produktion(er) der matcher din søgning: '" + searchWord + "'";
        setHeader();

        productionListBorderPane.setCenter(searchedProductionsList);


    }
    private void setHeader(){
        header = new Text(headerText);
        header.setTextAlignment(TextAlignment.CENTER);
        header.setFont(Font.font(30));
        centerBorderPane.setCenter(header);
        centerBorderPane.setPrefHeight(50);
        productionListBorderPane.setTop(centerBorderPane);
    }
    private void setHeader(String headerText){
        this.headerText = headerText;
        setHeader();
    }

    public void setCenter() {
        center = new Text(centerText);
        center.setTextAlignment(TextAlignment.CENTER);
        center.setFont(Font.font(15));
        productionListBorderPane.setCenter(center);
    }

    public void setCenter(String centerText) {
        this.centerText = centerText;
        setCenter();
    }

    public void productionNotFound(){
        setHeader("PRODUKTIONER");
        setCenter("INGEN PRODUKTIONER FUNDET");
    }

    public void trying(List<Production> productions){
        searchedProductionsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Production prodUsing = productions.get(searchedProductionsList.getSelectionModel().getSelectedIndex());
                setHeader(prodUsing.getName());
                String space = "       ";
                productionListBorderPane.setLeft(new Text( "  RELEASE DATE :\n  " + prodUsing.getReleaseDate().toString() + space));

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
                productionListBorderPane.setCenter(castView);

            }
        });
    }

}
