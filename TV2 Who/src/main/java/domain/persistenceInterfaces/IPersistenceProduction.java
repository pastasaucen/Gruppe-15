package domain.persistenceInterfaces;

import domain.Production;

import java.util.List;

public interface IPersistenceProduction {

    /**
     * Retrieves a list of relevant productions from a search string.
     * @param searchString The name of the production or its ID.
     * @return a list of relevant productions.
     */
    List<Production> getProductions(String searchString);

    /**
     * Saves the production in the persistence layer.
     * @param production
     */
    void saveProduction(Production production);

}
