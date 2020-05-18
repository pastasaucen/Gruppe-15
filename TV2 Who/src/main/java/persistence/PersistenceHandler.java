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
    public List<Cast> getCastMembers(String searchString, User currentUser) {
        List<Cast> castList = new ArrayList<>();

        try {
            PreparedStatement getCastMembersStmt = connection.prepareStatement(
                    "SELECT id, first_name, last_name, email, bio " +
                            "FROM cast_members " +
                            "WHERE first_name LIKE ? OR last_name LIKE ? OR email = ?");

            getCastMembersStmt.setString(1, '%'+searchString+'%');
            getCastMembersStmt.setString(2, '%'+searchString+'%');
            getCastMembersStmt.setString(3, searchString);

            ResultSet castMemberRs = getCastMembersStmt.executeQuery();

            while (castMemberRs.next()){
                Cast castMember = new Cast(
                        castMemberRs.getInt("id"),
                        castMemberRs.getString("first_name"),
                        castMemberRs.getString("last_name"),
                        castMemberRs.getString("email"),
                        castMemberRs.getString("bio")
                );
                getRoles(castMember, currentUser);

                castList.add(castMember);

            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return castList;
    }

    /**
     * This method is used to get the Roles of the cast members that has been searched for.
     * @param castMember
     */
    private void getRoles(Cast castMember, User currentUser){
        try {
            PreparedStatement getRolesStmt;

            // If the user is null, then it acts like an RDuser. They have the same rights using this method.
            if (currentUser == null) {
                currentUser = new RDUser("", "");
            }

            // Uses a different query for
            switch (currentUser.getUserType()) {
                case SYSTEMADMINISTRATOR:
                case EDITOR:
                    //System Admin and Editor query
                    getRolesStmt = connection.prepareStatement(
                            "SELECT roles.id AS id, role_name, name, release_date FROM roles, cast_to_roles, productions " +
                                    "WHERE roles.id = role_id " +
                                    "AND productions.id = production_id " +
                                    "AND cast_id = ?");
                    break;
                case PRODUCER:
                    //Producer query
                    getRolesStmt = connection.prepareStatement(
                            "SELECT roles.id AS id, role_name, name, release_date FROM roles, cast_to_roles, productions " +
                                    "WHERE roles.id = role_id " +
                                    "AND productions.id = production_id " +
                                    "AND cast_id = ? AND (state = 'ACCEPTED' OR associated_producer = ?)");
                    getRolesStmt.setString(2, currentUser.getEmail());
                    break;
                default:
                    // Not logged in or as an RDuser
                    getRolesStmt = connection.prepareStatement(
                            "SELECT roles.id AS id, role_name, name, release_date FROM roles, cast_to_roles, productions " +
                                    "WHERE roles.id = role_id " +
                                    "AND productions.id = production_id " +
                                    "AND cast_id = ? AND state = 'ACCEPTED'");

            }

            getRolesStmt.setInt(1, castMember.getId());

            ResultSet rolesRs = getRolesStmt.executeQuery();

            while (rolesRs.next()) {
                Production production = new Production(rolesRs.getString("name"),
                        rolesRs.getDate("release_date"));

                castMember.addRole(rolesRs.getInt("id"), rolesRs.getString("role_name"), production);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Existing roles are not updated using this method.
     * @param castMembers the cast member to be saved.
     */
    @Override
    public void saveCastMembers(List<Cast> castMembers) {
        List<Cast> newMembers = new ArrayList<>();
        List<Cast> existingMembers = new ArrayList<>();

        // Sorts the list of cast members into categories, existing and new members.
        for (int i = 0; i < castMembers.size(); i++) {
            if (castMembers.get(i).getId() == -1) {
                newMembers.add(castMembers.get(i));
            } else {
                existingMembers.add(castMembers.get(i));
            }
        }

        try {
            //Existing cast members gets updated in the database.
            PreparedStatement updateCastMemberStmt = connection.prepareStatement(
                    "UPDATE cast_members " +
                    "SET first_name = ?, last_name = ?, email = ?, bio = ? " +
                    "WHERE id = ?");

            PreparedStatement insertRoleStmt = connection.prepareStatement(
                    "INSERT INTO roles (role_name, production_id) " +
                        "VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);

            PreparedStatement insertCtoRStmt = connection.prepareStatement(
                    "INSERT INTO cast_to_roles (cast_id, role_id) " +
                        "VALUES (?,?)");

            for (int i = 0; i < existingMembers.size(); i++) {
                updateCastMemberStmt.setString(1, existingMembers.get(i).getFirstName());
                updateCastMemberStmt.setString(2, existingMembers.get(i).getLastName());
                updateCastMemberStmt.setString(3, existingMembers.get(i).getEmail());
                updateCastMemberStmt.setString(4, existingMembers.get(i).getBio());
                updateCastMemberStmt.setInt(5, existingMembers.get(i).getId());

                updateCastMemberStmt.execute();

                List<Role> roles = existingMembers.get(i).getRoles();

                for (int j = 0; j < roles.size(); j++) {
                    if (roles.get(j).getId() == -1) {
                        insertRoleStmt.setString(1, roles.get(j).getRoleName());
                        insertRoleStmt.setInt(2, roles.get(j).getProduction().getId());
                        insertRoleStmt.execute();
                        ResultSet key = insertRoleStmt.getGeneratedKeys();
                        key.next();

                        insertCtoRStmt.setInt(1, existingMembers.get(i).getId());
                        insertCtoRStmt.setInt(2, key.getInt(1));
                        insertCtoRStmt.execute();

                        roles.get(j).setId(key.getInt(1));
                    }
                }
            }
            //Inserting new cast members into the database.
            PreparedStatement insertMemberStmt = connection.prepareStatement(
                    "INSERT INTO cast_members (first_name, last_name, email, bio) " +
                        "VALUES (?,?,?,?) ", Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < newMembers.size(); i++) {
                insertMemberStmt.setString(1, newMembers.get(i).getFirstName());
                insertMemberStmt.setString(2, newMembers.get(i).getLastName());
                insertMemberStmt.setString(3, newMembers.get(i).getEmail());
                insertMemberStmt.setString(4, newMembers.get(i).getBio());
                insertMemberStmt.execute();

                ResultSet generatedKeys = insertMemberStmt.getGeneratedKeys();
                generatedKeys.next();

                newMembers.get(i).setId(generatedKeys.getInt(1));

                List<Role> roles = newMembers.get(i).getRoles();

                for (int j = 0; j < roles.size(); j++) {
                    if (roles.get(j).getId() == -1) {
                        insertRoleStmt.setString(1, roles.get(j).getRoleName());
                        insertRoleStmt.setInt(2, roles.get(j).getProduction().getId());
                        insertRoleStmt.execute();
                        ResultSet key = insertRoleStmt.getGeneratedKeys();
                        key.next();

                        insertCtoRStmt.setInt(1, newMembers.get(i).getId());
                        insertCtoRStmt.setInt(2, key.getInt(1));
                        insertCtoRStmt.execute();

                        roles.get(j).setId(key.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


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
    public List<Production> getProductions(String searchString, User currentUser) {

        List<Production> productions = new ArrayList<>();

        try {
            PreparedStatement getProductionsStmt;

            // If the user is null, then it acts like an RDuser. They have the same rights using this method.
            if (currentUser == null) {
                currentUser = new RDUser("", "");
            }

            // Uses a different query for
            switch (currentUser.getUserType()) {
                case SYSTEMADMINISTRATOR:
                case EDITOR:
                    //System Admin and Editor query
                    getProductionsStmt = connection.prepareStatement(
                            "SELECT productions.id AS id, productions.name AS name, release_date, state, tv_code, associated_producer " +
                                    "FROM productions " +
                                    "WHERE tv_code = ? OR productions.name LIKE ?");
                    break;
                case PRODUCER:
                    //Producer query
                    getProductionsStmt = connection.prepareStatement(
                            "SELECT productions.id AS id, productions.name AS name, release_date, state, tv_code, associated_producer " +
                                    "FROM productions " +
                                    "WHERE (tv_code = ? OR productions.name LIKE ?) " +
                                        "AND (state = 'ACCEPTED' OR associated_producer = ?)");
                    getProductionsStmt.setString(3, currentUser.getEmail());
                    break;
                default:
                    // Not logged in or as an RDuser
                    getProductionsStmt = connection.prepareStatement(
                            "SELECT productions.id AS id, productions.name AS name, release_date, state, tv_code, associated_producer " +
                                    "FROM productions " +
                                    "WHERE (tv_code = ? OR productions.name LIKE ?) " +
                                        "AND state = 'ACCEPTED'");

            }

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
                        castMembersRs.getString("email"),
                        "" // empty bio
                );

                getRolesForProduction(castMember, production);

                castList.add(castMember);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return castList;
    }

    private void getRolesForProduction(Cast castMember, Production production) {

        try {
            PreparedStatement getRolesStmt = connection.prepareStatement(
                    "SELECT roles.id AS id, role_name FROM roles, cast_to_roles " +
                            "WHERE roles.id = role_id AND cast_id = ?" +
                            "AND production_id = ?");

            getRolesStmt.setInt(1, castMember.getId());
            getRolesStmt.setInt(2, production.getId());

            ResultSet rolesRs = getRolesStmt.executeQuery();
            while (rolesRs.next()) {
                castMember.addRole(rolesRs.getInt("id"), rolesRs.getString("role_name"), production);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

                saveCastMembers(production.getCastList());

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

                saveCastMembers(production.getCastList());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void createUser(User newUser, String password) {

        try {
            PreparedStatement insertUserStmt = connection.prepareStatement(
                    "INSERT INTO users (name, email, password, user_type) VALUES (?, ?, md5(?), ?);");

            insertUserStmt.setString(1, newUser.getName());
            insertUserStmt.setString(2, newUser.getEmail());
            insertUserStmt.setString(3, password);
            insertUserStmt.setString(4, newUser.getUserType().toString());

            insertUserStmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
