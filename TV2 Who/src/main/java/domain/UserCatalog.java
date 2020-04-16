package domain;

public class UserCatalog {

    private static UserCatalog instance;

    private IPersistenceUser persistenceUser;

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
     * @param password pasword is deleted after sending to Database
     */
    public void createUser(String name, String email, UserType userType, String password){

        switch(userType){
            case SYSTEMADMINISTRATOR:
                persistenceUser.createUser(new SystemAdministrator(name, email,userType), password);
                break;
            case PRODUCER:
                persistenceUser.createUser(new Producer(name, email, userType), password);
                break;
            case RDUSER:
                persistenceUser.createUser(new RDUser(name, email, userType), password);
            case EDITOR:
                persistenceUser.createUser(new Editor(name, email, userType), password);
        }
        password = null; // Deleting password for security

    }
}