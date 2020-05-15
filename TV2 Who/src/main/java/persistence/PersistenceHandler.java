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
import java.util.ArrayList;
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
                    "SELECT * FROM users WHERE email = ? AND password = md5(?);");

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

        List<Production> productions = new ArrayList<>();

        try {
            PreparedStatement getProductionsStmt = connection.prepareStatement(
                    "SELECT productions.id AS id, productions.name AS name, release_date, state, tv_code, associated_producer " +
                    "FROM productions " +
                    "WHERE tv_code = ? OR productions.name LIKE ?");

            getProductionsStmt.setString(1, searchString);
            getProductionsStmt.setString(2, '%'+searchString+'%');

            ResultSet productionsRs = getProductionsStmt.executeQuery();

            while (productionsRs.next()) {
                Production production = new Production(
                        productionsRs.getInt("id"),
                        productionsRs.getString("name"),
                        productionsRs.getDate("release_date"),
                        State.valueOf(productionsRs.getString("state")),
                        productionsRs.getString("tv_code"),
                        productionsRs.getString("associated_producer")
                );

                // Adds the cast members to the production
                List<Cast> castList = getCastMembers(production);

                for (int i = 0; i < castList.size(); i++) {
                    production.addCastMember(castList.get(i));
                }

                productions.add(production);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productions;
    }

    /**
     * Returns a list of cast members from the given production.
     * @param production the production to find cast members for.
     * @return
     */
    private List<Cast> getCastMembers(Production production) {
        List<Cast> castList = new ArrayList<>();

        try {
            PreparedStatement getCastMembersStmt = connection.prepareStatement(
                    "SELECT cast_members.id, cast_members.first_name, cast_members.last_name, cast_members.email " +
                            "FROM cast_members, production_to_cast, productions " +
                            "WHERE cast_members.id = cast_id AND productions.id = production_id " +
                                "AND productions.id = ?");

            getCastMembersStmt.setInt(1, production.getId());

            ResultSet castMembersRs = getCastMembersStmt.executeQuery();

            while (castMembersRs.next()) {
                Cast castMember = new Cast(
                        castMembersRs.getInt("id"),
                        castMembersRs.getString("first_name"),
                        castMembersRs.getString("last_name"),
                        castMembersRs.getString("email")
                );

                List<Role> roles = getRolesForProduction(castMember, production);

                castList.add(castMember);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return castList;
    }

    private List<Role> getRolesForProduction(Cast castMember, Production production) {
        List<Role> roles = new ArrayList<>();

        try {
            PreparedStatement getRolesStmt = connection.prepareStatement(
                    "SELECT role_name FROM roles, cast_to_roles " +
                            "WHERE roles.id = role_id AND cast_id = ?" +
                            "AND production_id = ?");

            getRolesStmt.setInt(1, castMember.getId());
            getRolesStmt.setInt(2, production.getId());

            ResultSet rolesRs = getRolesStmt.executeQuery();
            while (rolesRs.next()) {
                castMember.addRole(rolesRs.getString("role_name"), production);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    @Override
    public void saveProduction(Production production) {

        try {
            // A production with the an if -1 have not been inserted into a database before.
            if (production.getId() == -1) {
                // Insert
                PreparedStatement insertProductionStmt = connection.prepareStatement(
                        "INSERT INTO productions (name, release_date, state, tv_code, associated_producer) " +
                                "VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

                insertProductionStmt.setString(1, production.getName());
                insertProductionStmt.setDate(2, production.getReleaseDate());
                insertProductionStmt.setString(3, production.getState().toString());
                insertProductionStmt.setString(4, production.getTvCode());
                insertProductionStmt.setString(5, production.getAssociatedProducerEmail());

                insertProductionStmt.execute();
                ResultSet generatedKeys = insertProductionStmt.getGeneratedKeys();
                generatedKeys.next();
                int productionId = generatedKeys.getInt(1);
                production.setId(productionId);

                saveCastMembers(production.getCast());

            } else {
                // Update
                PreparedStatement updateProductionStmt = connection.prepareStatement(
                        "UPDATE productions " +
                                "SET name = ?, release_date = ?, state = ?, tv_code = ?, associated_producer = ? " +
                                "WHERE id = ?");

                updateProductionStmt.setString(1, production.getName());
                updateProductionStmt.setDate(2, production.getReleaseDate());
                updateProductionStmt.setString(3, production.getState().toString());
                updateProductionStmt.setString(4, production.getTvCode());
                updateProductionStmt.setString(5, production.getAssociatedProducerEmail());
                updateProductionStmt.setInt(6, production.getId());

                updateProductionStmt.execute();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void createUser(User newUser, String password) {

    }
}
