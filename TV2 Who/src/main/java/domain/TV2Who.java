package domain;

import domain.persistenceInterfaces.IPersistenceCast;
import domain.persistenceInterfaces.IPersistenceLogIn;
import domain.producer.IProducer;
import domain.producer.Producer;
import persistence.PersistenceHandler;

import java.sql.Date;
import java.util.List;


public class TV2Who implements ITV2WhoUI {

    private ProductionCatalog productionCatalog;
    private static TV2Who instance = null;
    private User currentUser;
    private IPersistenceLogIn iPersistenceLogIn = PersistenceHandler.getInstance();
    private CastCatalog castCatalog;

    private TV2Who() {
        this.productionCatalog = ProductionCatalog.getInstance();
        this.castCatalog = CastCatalog.getInstance();
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

    @Override
    public Cast getCastMember(int id) {
        return castCatalog.getCastMember(id, currentUser);
    }

    @Override
    public Production getProduction(int id) {
        return productionCatalog.getProduction(id);
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
        return castCatalog.searchForCast(name, currentUser);
    }

    /**
     * Searches for productions through productionCatalog by name or ID.
     *
     * @param searchString
     */
    @Override
    public List<Production> prepareProductionSearchList(String searchString) {
        return productionCatalog.getProduction(searchString, currentUser);
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void logOut() {
        currentUser = null;
    }
}
