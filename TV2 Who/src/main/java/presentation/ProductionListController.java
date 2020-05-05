package presentation;

import domain.Production;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;

public class ProductionListController extends BorderPane {
    @FXML
    ListView<Production> searchedProductionsList;

    public void ProductionListController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productionList.fxml"));
        fxmlLoader.setRoot(this);

        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void productionList(List<Production> productions){

        searchedProductionsList.setFixedCellSize(productions.size());

        for( int i = 0; i < productions.size(); i++){
            searchedProductionsList.getItems().set(i,productions.get(i));
        }

    }

}
