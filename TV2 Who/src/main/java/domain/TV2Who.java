package domain;

import domain.persistenceInterfaces.IPersistenceLogIn;
import persistence.PersistenceHandler;

import java.util.List;


public class TV2Who implements ITV2WhoUI {

    ProductionCatalog productionCatalog;
    private static TV2Who instance = null;
    private User currentUser;
    private IPersistenceLogIn iPersistenceLogIn = PersistenceHandler.getInstance();

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

    public void findProduction(String nameOrId) { }

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
     * Attempts to create a user session, if the requested email and password is valid. Prints an error message if the
     * email or password is incorrect.
     * @param email
     * @param password
     */
    @Override
    public void createUserSession(String email, String password) {
        User newUser = iPersistenceLogIn.logInValidation(email, password);
        if (newUser == null) {
            System.out.println("Invalid username or password");
            return;
        }

        currentUser = newUser;
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
