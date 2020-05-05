package persistence;

import domain.*;
import domain.editor.Editor;
import domain.persistenceInterfaces.IPersistenceCast;
import domain.persistenceInterfaces.IPersistenceLogIn;
import domain.persistenceInterfaces.IPersistenceProduction;
import domain.persistenceInterfaces.IPersistenceUser;
import domain.producer.Producer;
import domain.rDUser.RDUser;

import java.sql.*;
import java.util.List;

public class PersistenceHandler implements IPersistenceLogIn, IPersistenceUser, IPersistenceProduction, IPersistenceCast {

    private String url = "balarama.db.elephantsql.com";
    private int port = 5432;
    private String databaseName = "amjtpqun";
    private String username = "amjtpqun";
    private String password = "3CZn-HZbGNJkMsFNKPwPVE9NTwyhkvmi";
    static Connection connection = null;

    private static PersistenceHandler instance;

    public static PersistenceHandler getInstance() {
        if (instance == null) {
            instance = new PersistenceHandler();
        }

        return instance;
    }

    private PersistenceHandler() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            connection = DriverManager.getConnection("jdbc:postgresql://"+url+':'+port+'/'+databaseName, username, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cast> getCastMembers(String searchString) {
        return null;
    }

    @Override
    public void saveCastMembers(List<Cast> castMembers) {

    }

    /**
     * Retrieves a user from the database from the given email and password.
     * @param email
     * @param password
     * @return returns the user with the given email and password. Returns null if no user was found.
     */
    @Override
    public User logInValidation(String email, String password) {

        try {
            PreparedStatement getUserStmt = connection.prepareStatement(
                    "SELECT * FROM users WHERE email = ? AND password = ?;");

            getUserStmt.setString(1, email);          // Specifying the email and password
            getUserStmt.setString(2, password);

            ResultSet usersResultSet = getUserStmt.executeQuery();  // The query gets executed

            // Checks whether a user was found in the result set
            if (usersResultSet.next()) {
                // Name of the user
                String name = usersResultSet.getString("name");

                // Gets the user type of the user
                UserType userType = UserType.valueOf(usersResultSet.getString("user_type"));

                switch (userType.toString()) {
                    case "SYSTEMADMINISTRATOR":
                        return new SystemAdministrator(name, email);
                    case "PRODUCER":
                        return new Producer(name, email);
                    case "RDUSER":
                        return new RDUser(name, email);
                    case "EDITOR":
                        return new Editor(name, email);
                    default:
                        System.out.println("The user type \"" + userType.toString() + "\" does not match any user " +
                                "types in the system...");
                        return null;
                }
            } else {
                // Invalid login
                System.out.println("Invalid email or password...");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException ex) {
            System.out.println("The user type was not recognized...");
        }

        return null;
    }

    @Override
    public List<Production> getProductions(String searchString) {
        return null;
    }

    @Override
    public void saveProduction(Production production) {

    }

    @Override
    public void createUser(User newUser, String password) {

    }
}
