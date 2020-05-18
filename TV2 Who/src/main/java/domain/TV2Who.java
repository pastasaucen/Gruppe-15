package domain;

import domain.persistenceInterfaces.IPersistenceCast;
import domain.persistenceInterfaces.IPersistenceLogIn;
import persistence.PersistenceHandler;
import java.sql.Date;
import java.util.List;


public class TV2Who implements ITV2WhoUI {

    ProductionCatalog productionCatalog;
    private static TV2Who instance = null;
    private User currentUser;
    private IPersistenceLogIn iPersistenceLogIn = PersistenceHandler.getInstance();
    private IPersistenceCast iPersistenceCast = PersistenceHandler.getInstance();

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
     * Attempts to create a user session, if the requested email and password is valid. Prints an error message if the
     * email or password is incorrect.
     * @param email
     * @param password
     */
    @Override
    public boolean createUserSession(String email, String password) {
        User newUser = iPersistenceLogIn.logInValidation(email, password);
        if (newUser == null) {
            System.out.println("Invalid email or password");
            return false;
        }

        currentUser = newUser;
        return true;
    }

    /**
     * Prepares a list of cast members for use in the presentation layer
     *
     * @param name
     * @return List(Cast)
     */
    @Override
    public List<Cast> prepareCastSearchList(String name) {
        return CastCatalog.getInstance().searchForCast(name);
        // TODO: Is this correct?
    }

    /**
     * Creates production but does not save in persistanse
     * @param name
     * @param releaseDate
     * @return
     */
    @Override
    public Production createProduction(String name, Date releaseDate) {
        Production production = new Production(name, releaseDate);
        return production;
    }

    @Override
    public Cast createCast(String firstName, String lastName, String email, String bio) {
        return new Cast(-1,firstName,lastName,email,bio);
    }

    @Override
    public void saveProduction(Production production) {
        productionCatalog.addProduction(production);
    }

    @Override
    public void saveCastMembers(List<Cast> castList){
        iPersistenceCast.saveCastMembers(castList);
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
