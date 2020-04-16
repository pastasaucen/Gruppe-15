package presentation;

import java.io.IOException;
import java.util.List;

import domain.Cast;
import domain.ITv2Who;
import domain.TV2Who;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DemoController {

    private ITv2Who tv2 = TV2Who.getInstance();
    private int searchCategory = 1;

    @FXML
    Button searchButton;
    @FXML
    Button castButton;
    @FXML
    Button productionButton;
    @FXML
    TextField searchField;
    @FXML
    ListView resultView;

    @FXML
    public void setCastCategory(){
        this.searchCategory = 1;
    }

    @FXML
    public void setProductionCategory(){
        this.searchCategory = 2;
    }

    /**
     * If search category is set to 1: Takes text from search field and devides it into first / last name, then calls
     * the prepareCastSearch method in TV2Who class which returns a list of Cast objects based on the names.
     * If search category is set to 2: Checks whether or not there is a name or an id in the search field.
     */
    @FXML
    public void requestSearch(){
        resultView.setItems(null);
        if(!searchField.getText().equals("")){
            if(searchCategory == 1){
                String[] tokens = searchField.getText().split(" ");
                System.out.println(tokens[0]);
                String firstName = "";
                for(int i = 0; i < tokens.length-2;i++){
                    if(i>0){
                        firstName = firstName + tokens[i] + " ";
                    }
                    firstName = firstName + tokens[i];
                }
                List<Cast> castList = tv2.prepareCastSearchList(firstName,tokens[tokens.length-1]);
                //Dummyvariabel til test!
                Cast dummy = new Cast(69,"Dummy","Nielsen","sackboy@gmail.com");
                Cast mrsDummy = new Cast(69,"Dummylina","Nielsen","sackboy@gmail.com");
                castList.add(dummy);
                castList.add(mrsDummy);
                //
                ObservableList<Cast> oCastList = FXCollections.observableList(castList);
                System.out.println(castList);
                resultView.setItems(oCastList);
            }else{
                //TODO implementer efter aftale
                System.out.println("Function not implemented");
            }
        }
    }

}
