package domain;

import domain.persistenceInterfaces.IPersistenceCast;
import persistence.PersistenceHandler;
import domain.producer.Producer;

import java.util.ArrayList;
import java.util.List;

public class CastCatalog {


    //private ITV2WhoUI tv2 = TV2Who.getInstance();

    private IPersistenceCast persistenceCast = PersistenceHandler.getInstance();
    private static CastCatalog instance;

    // Temporary until the persistence layer is implemented. We always want the newest data from the persistence layer.
    private List<Cast> cast = new ArrayList<>();

    private CastCatalog() {
    }

    public static CastCatalog getInstance() {
        if (instance == null) {
            instance = new CastCatalog();
        }
        return instance;
    }

    /**
     * Finds the cast member with the given first name and last name.
     *
     * @param searchString
     * @return a list of cast members with the given names
     */
    public List<Cast> searchForCast(String searchString, User currentUser) {
        return persistenceCast.getCastMembers(searchString, currentUser);
    }

    /**
     * creates a cast member and adds it to the database (for now arraylist)
     * @param firstName
     * @param lastName
     * @param email
     */
    public void createCastMember(String firstName, String lastName, String email, String bio) {
        cast.add(new Cast(-1, firstName, lastName, email, bio));
    }
}