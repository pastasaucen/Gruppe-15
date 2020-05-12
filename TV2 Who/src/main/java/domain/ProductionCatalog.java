package domain;

import domain.persistenceInterfaces.IPersistenceProduction;
import persistence.PersistenceHandler;

import java.util.ArrayList;
import java.util.List;

public class ProductionCatalog {

    private static ProductionCatalog instance;
    private IPersistenceProduction persistenceProduction = PersistenceHandler.getInstance();

    //  Gets overridden every time to search in the persistence layer
    private List<Production> productions;

    private ProductionCatalog() {
        productions = new ArrayList<>();
    }

    public static ProductionCatalog getInstance() {
        if (instance == null) {
            instance = new ProductionCatalog();
        }
        return instance;
    }

    /**
     * Adds a production to the catalogue
     * @param newProduction
     */
    public void addProduction(Production newProduction) {
        // Saves the production using the persistence layer
        persistenceProduction.saveProduction(newProduction);
    }

    /**
     * Saves temporarily found productions and sets text in the search field.
     *
     * @param searchString Searches for the input. Can either be id or name
     * @return
     */
    public List<Production> getProduction(String searchString) {
        // Searches for the productions in the persistence layer
        productions = persistenceProduction.getProductions(searchString);

        // Checks whether no result were found
        if (productions == null) {
            // TODO Send a message to the presentation layer that no results were found
            return null;
        } else {
            return productions;     // Returns the found productions
        }
    }
}
