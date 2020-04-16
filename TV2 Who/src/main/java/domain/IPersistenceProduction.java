package domain;

import java.util.List;

public interface IPersistenceProduction {

    /**
     * Retrieves a list of relevant productions from a search string.
     * @param searchString The name of the production or its ID.
     * @return a list of relevant productions.
     */
    List<Production> getProductions(String searchString);

    /**
     * Saves the given list of productions in the database.
     * @param production
     */
    void saveProduction(Production production);

}
