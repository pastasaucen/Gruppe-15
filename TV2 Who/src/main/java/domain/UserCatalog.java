package domain;

import domain.editor.Editor;
import domain.persistenceInterfaces.IPersistenceUser;
import domain.producer.Producer;
import domain.rDUser.RDUser;
import persistence.PersistenceHandler;

public class UserCatalog {

    private static UserCatalog instance;

    private IPersistenceUser persistenceUser = PersistenceHandler.getInstance();

    public static UserCatalog getInstance(){
        if( instance == null){
            instance = new UserCatalog();
        }
        return instance;
    }


    /**
     * Created a user depending on the type and sends it to the database.
     * @param name
     * @param email
     * @param userType Enum
     * @param password password is deleted after sending to Database
     */
    public void createUser(String name, String email, UserType userType, String password){
        switch(userType){
            case SYSTEMADMINISTRATOR:
                persistenceUser.createUser(new SystemAdministrator(name, email), password);
                break;
            case PRODUCER:
                persistenceUser.createUser(new Producer(name, email), password);
                break;
            case RDUSER:
                persistenceUser.createUser(new RDUser(name, email), password);
                break;
            case EDITOR:
                persistenceUser.createUser(new Editor(name, email), password);
        }
    }
}
