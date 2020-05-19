package domain;

import domain.persistenceInterfaces.IPersistenceCast;
import domain.persistenceInterfaces.IPersistenceLogIn;
import domain.producer.IProducer;
import domain.producer.Producer;
import persistence.PersistenceHandler;

import java.sql.Date;
import java.util.List;


public class TV2Who implements ITV2WhoUI {

    ProductionCatalog productionCatalog;
    private static TV2Who instance = null;
    private User currentUser;
    private IPersistenceLogIn iPersistenceLogIn = PersistenceHandler.getInstance();
    private IPersistenceCast iPersistenceCast = PersistenceHandler.getInstance();
    private UserCatalog userCatalog = new UserCatalog();

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
     * Attempts to create a user session, if the requested email and password is valid. Prints an error message if the
     * email or password is incorrect.
     * @param email
     * @param password
     */
    @Override
    public boolean createUserSession(String email, String password) {
        //TODO fix commentation
        User newUser = iPersistenceLogIn.logInValidation(email, password);
        //User newUser = new SystemAdministrator("SD","SD");
        //User newUser = null;
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
        return CastCatalog.getInstance().searchForCast(name, currentUser);
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

    /**
     * Creates production but does not save in persistence
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

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void createUser(String name, String email, UserType userType, String pasword) {
        userCatalog.createUser(name, email, userType, pasword);
    }
}
