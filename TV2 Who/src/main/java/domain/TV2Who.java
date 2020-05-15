package domain;

import java.util.List;


public class TV2Who implements ITV2WhoUI {

    ProductionCatalog productionCatalog;
    private static TV2Who instance = null;

    private TV2Who() {
        this.productionCatalog = ProductionCatalog.getInstance();
    }

    /**
     * Used to retrieve the singleton instance of the TV2 Who class
     *
     * @return
     */
    public static TV2Who getInstance() {
        if (instance == null) {
            instance = new TV2Who();
        }
        return instance;
    }

    /**
     * Searches for productions through productionCatalog by name or ID.
     *
     * @param nameOrId
     */
    @Override
    public List<Production> prepareProductionSearchList(String nameOrId) {
        return productionCatalog.getProduction(nameOrId);
    }

    /**
     * Prepares a list of cast members for use in the presentation layer
     *
     * @param firstName
     * @param lastName
     * @return List(Cast)
     */
    @Override
    public List<Cast> prepareCastSearchList(String firstName, String lastName) {
        return CastCatalog.getInstance().searchForCast(firstName, lastName);
    }
}
