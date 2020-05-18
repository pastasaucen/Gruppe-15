package domain.persistenceInterfaces;

import domain.Production;
import domain.User;

import java.util.List;

public interface IPersistenceProduction {

    /**
     * Retrieves a list of relevant productions from a search string.
     * @param searchString The name of the production or its TV-code.
     * @return a list of relevant productions.
     */
    List<Production> getProductions(String searchString, User currentUser);

    /**
     * Saves the production in the persistence layer.
     * @param production
     */
    void saveProduction(Production production);

}
