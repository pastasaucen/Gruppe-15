package domain;

import domain.persistenceInterfaces.IPersistenceProduction;
import domain.producer.Producer;
import persistence.PersistenceHandler;

import java.util.List;

public class ProductionCatalog {

    private static ProductionCatalog instance;
    private IPersistenceProduction persistenceProduction = PersistenceHandler.getInstance();

    private ProductionCatalog() {
    }

    public static ProductionCatalog getInstance() {
        if (instance == null) {
            instance = new ProductionCatalog();
        }
        return instance;
    }

    /**
     * Saves a production to the catalogue.
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
    public List<Production> getProduction(String searchString, User currentUser) {
        // Searches for the productions in the persistence layer
        return persistenceProduction.getProductions(searchString, currentUser);
    }

    public List<Production> getProductions(Producer producer) {
        return persistenceProduction.getProductions(producer);
    }

    public Production getProduction(int id) {
        return persistenceProduction.getProduction(id);
    }
}
