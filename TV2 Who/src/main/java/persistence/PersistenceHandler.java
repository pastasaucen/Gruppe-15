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
            connection = DriverManager.getConnection("jdbc:postgresql://" + url + ':' + port + '/' + databaseName, username, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the cast members that either has a first name or last name which is similar to the search string or an
     * email which is exactly like the search string.
     *
     * Roles from cast members where the production is DECLINED or PENDING is only showed to system admins or editors.
     * Producers can see roles which are from theirs productions or ACCEPTED. Regular users can only see roles from
     * ACCEPTED productions.
     *
     * @param searchString a first name and/or last name, or email.
     * @param currentUser the current user session.
     * @return a list of relevant cast members
     */
    @Override
    public List<Cast> getCastMembers(String searchString, User currentUser) {
        List<Cast> castList = new ArrayList<>();

        searchString = searchString.toLowerCase();

        try {
            PreparedStatement getCastMembersStmt = connection.prepareStatement(
                    "SELECT id, first_name, last_name, email, bio " +
                            "FROM cast_members " +
                            "WHERE LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ? OR LOWER(email) = ?");

            getCastMembersStmt.setString(1, '%'+searchString+'%');  // first name
            getCastMembersStmt.setString(2, '%'+searchString+'%');  // last name
            getCastMembersStmt.setString(3, searchString);             // email (exact, not like)

            ResultSet castMemberRs = getCastMembersStmt.executeQuery();     // Executes the query

            // Iterates through every result in the result set, which is relevant cast members
            while (castMemberRs.next()) {
                // The cast members is created
                Cast castMember = new Cast(
                        castMemberRs.getInt("id"),
                        castMemberRs.getString("first_name"),
                        castMemberRs.getString("last_name"),
                        castMemberRs.getString("email"),
                        castMemberRs.getString("bio")
                );
                getRoles(castMember, currentUser);  // The roles are assigned

                castList.add(castMember);   // The cast members is added to the list
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return castList;
    }

    /**
     * Assigns relevant roles to the cast member. The user determines whether or not roles from DECLINED or PENDING
     * productions are assigned. For more see the getCastMembers-method above.
     *
     * @param castMember determines which cast member to get roles for.
     * @param currentUser the user from the current user session.
     */
    private void getRoles(Cast castMember, User currentUser){
        try {
            PreparedStatement getRolesStmt;

            /* Determines the range of roles to assign from the given user type */
            // If the user is null, then it acts like an RDuser. They have the same rights using this method.
            if (currentUser == null) {
                currentUser = new RDUser("", "");
            }

            // Uses a different query for different user types
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

            // Iterates through every production
            while (rolesRs.next()) {
                // Make a production for the role to reference to. This production is a light weight version to help performance
                Production production = new Production(rolesRs.getString("name"),
                        rolesRs.getDate("release_date"));

                // Adds the role to the cast member
                castMember.addRole(rolesRs.getInt("id"),
                        rolesRs.getString("role_name"),
                        production);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the cast member. Existing roles are not updated using this method.
     * @param castMembers the cast member to be saved.
     */
    @Override
    public void saveCastMembers(List<Cast> castMembers) {
        List<Cast> newMembers = new ArrayList<>();
        List<Cast> existingMembers = new ArrayList<>();

        // Sorts the list of cast members into categories, existing and new members.
        for (Cast castMember : castMembers) {
            if (castMember.getId() == -1) {     // Cast members with an id of -1 are new
                newMembers.add(castMember);
            } else {
                existingMembers.add(castMember);
            }
        }

        try {
            // Existing cast members gets updated in the database.
            PreparedStatement updateCastMemberStmt = connection.prepareStatement(
                    "UPDATE cast_members " +
                            "SET first_name = ?, last_name = ?, email = ?, bio = ? " +
                            "WHERE id = ?");

            // Inserts a new role
            PreparedStatement insertRoleStmt = connection.prepareStatement(
                    "INSERT INTO roles (role_name, production_id) " +
                            "VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);

            // Inserts a reference to the role and the cast member
            PreparedStatement insertCtoRStmt = connection.prepareStatement(
                    "INSERT INTO cast_to_roles (cast_id, role_id) " +
                            "VALUES (?,?)");

            // Updates every cast member and inserts
            for (Cast curCast : existingMembers) {
                updateCastMemberStmt.setString(1, curCast.getFirstName());
                updateCastMemberStmt.setString(2, curCast.getLastName());
                updateCastMemberStmt.setString(3, curCast.getEmail());
                updateCastMemberStmt.setString(4, curCast.getBio());
                updateCastMemberStmt.setInt(5, curCast.getId());

                updateCastMemberStmt.execute();

                insertRoles(insertRoleStmt, insertCtoRStmt, curCast);   // The roles are inserted as well
            }

            //Inserting new cast members into the database.
            PreparedStatement insertMemberStmt = connection.prepareStatement(
                    "INSERT INTO cast_members (first_name, last_name, email, bio) " +
                            "VALUES (?,?,?,?) ", Statement.RETURN_GENERATED_KEYS);

            // Inserts new cast members into the database
            for (Cast curCast : newMembers) {
                insertMemberStmt.setString(1, curCast.getFirstName());
                insertMemberStmt.setString(2, curCast.getLastName());
                insertMemberStmt.setString(3, curCast.getEmail());
                insertMemberStmt.setString(4, curCast.getBio());
                insertMemberStmt.execute();

                // The new cast members id is saved.
                ResultSet generatedKeys = insertMemberStmt.getGeneratedKeys();
                generatedKeys.next();

                // The id is set on the cast member from the domain layer
                curCast.setId(generatedKeys.getInt(1));

                insertRoles(insertRoleStmt, insertCtoRStmt, curCast);   // The roles are inserted as well
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts the roles with an id of -1 (that indicates they are not present in the database) into the database.
     * @param insertRoleStmt
     * @param insertCtoRStmt
     * @param curCast
     * @throws SQLException An SQL exception
     */
    private void insertRoles(PreparedStatement insertRoleStmt, PreparedStatement insertCtoRStmt, Cast curCast) throws SQLException {
        List<Role> roles = curCast.getRoles();

        // Iterates through every role
        for (Role role : roles) {
            // If the role is new (ie. has an id of -1), then insert it.
            if (role.getId() == -1) {
                insertRoleStmt.setString(1, role.getRoleName());
                insertRoleStmt.setInt(2, role.getProduction().getId());
                insertRoleStmt.execute();
                ResultSet key = insertRoleStmt.getGeneratedKeys();
                key.next();

                insertCtoRStmt.setInt(1, curCast.getId());
                insertCtoRStmt.setInt(2, key.getInt(1));
                insertCtoRStmt.execute();

                role.setId(key.getInt(1));
            }
        }
    }

    /**
     * Retrieves a user from the database from the given email and password.
     * @param email The given email from the user.
     * @param password the given password from the user.
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

                // Returns a new instance of an user of the correct type.
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

    /**
     * Retrieves the relevant productions from either the production name or the TV-code.
     *
     * The currentUser determines whether or not PENDING or DECLINED productions are included. System admins and editors
     * can see every production, producers can see ACCEPTED and their own productions, and regular users and RDusers can
     * see only ACCEPTED productions.
     *
     * @param searchString The name of the production or its TV-code.
     * @param currentUser The current user session.
     * @return a relevant list of productions.
     */
    @Override
    public List<Production> getProductions(String searchString, User currentUser) {

        searchString = searchString.toLowerCase();

        List<Production> productions = new ArrayList<>();

        try {
            PreparedStatement getProductionsStmt;

            // If the user is null, then it acts like an RDuser. They have the same rights using this method.
            if (currentUser == null) {
                currentUser = new RDUser("", "");
            }

            // Uses a different query for different kinds of users...
            switch (currentUser.getUserType()) {
                case SYSTEMADMINISTRATOR:
                case EDITOR:
                    //System Admin and Editor query
                    getProductionsStmt = connection.prepareStatement(
                            "SELECT productions.id AS id, productions.name AS name, release_date, state, tv_code, associated_producer " +
                                    "FROM productions " +
                                    "WHERE tv_code = ? OR LOWER(productions.name) LIKE ?");
                    break;
                case PRODUCER:
                    //Producer query
                    getProductionsStmt = connection.prepareStatement(
                            "SELECT productions.id AS id, productions.name AS name, release_date, state, tv_code, associated_producer " +
                                    "FROM productions " +
                                    "WHERE (tv_code = ? OR LOWER(productions.name) LIKE ?) " +
                                        "AND (state = 'ACCEPTED' OR associated_producer = ?)");
                    getProductionsStmt.setString(3, currentUser.getEmail());
                    break;
                default:
                    // Not logged in or as an RDuser
                    getProductionsStmt = connection.prepareStatement(
                            "SELECT productions.id AS id, productions.name AS name, release_date, state, tv_code, associated_producer " +
                                    "FROM productions " +
                                    "WHERE (tv_code = ? OR LOWER(productions.name) LIKE ?) " +
                                        "AND state = 'ACCEPTED'");

            }

            getProductionsStmt.setString(1, searchString);              // TV-code
            getProductionsStmt.setString(2, '%'+searchString+'%');   // Production name

            ResultSet productionsRs = getProductionsStmt.executeQuery();

            // For every found row, create a production from the cells.
            while (productionsRs.next()) {
                Production production = new Production(
                        productionsRs.getInt("id"),
                        productionsRs.getString("name"),
                        productionsRs.getDate("release_date"),
                        State.valueOf(productionsRs.getString("state")),
                        productionsRs.getString("tv_code"),
                        productionsRs.getString("associated_producer")
                );

                // Gets the cast members from the given production
                List<Cast> castList = getCastMembers(production);

                // Assigns the cast to the new production instance.
                for (Cast cast : castList) {
                    production.addCastMember(cast);
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
     * Note that the cast members will only contain roles from this production.
     *
     * @param production the production to find cast members for.
     * @return a list of cast members from the given production.
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

            // Iterates through every row in the result set
            while (castMembersRs.next()) {
                // Instantiates a new cast member with information from the cells in the row.
                Cast castMember = new Cast(
                        castMembersRs.getInt("id"),
                        castMembersRs.getString("first_name"),
                        castMembersRs.getString("last_name"),
                        castMembersRs.getString("email"),
                        "" // The bio is kept empty, because it isn't relevant, when searching for a production.
                );

                // Assigns the roles to the cast member.
                getRolesForProduction(castMember, production);

                castList.add(castMember);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return castList;
    }

    /**
     * Retrieves the roles only relevant to the given cast member and production.
     * @param castMember The cast member the roles should be assigned to.
     * @param production The production in which the roles should be from.
     */
    private void getRolesForProduction(Cast castMember, Production production) {

        try {
            PreparedStatement getRolesStmt = connection.prepareStatement(
                    "SELECT roles.id AS id, role_name FROM roles, cast_to_roles " +
                            "WHERE roles.id = role_id AND cast_id = ?" +
                            "AND production_id = ?");

            getRolesStmt.setInt(1, castMember.getId());
            getRolesStmt.setInt(2, production.getId());

            ResultSet rolesRs = getRolesStmt.executeQuery();
            // Adds the roles to the cast member.
            while (rolesRs.next()) {
                castMember.addRole(rolesRs.getInt("id"), rolesRs.getString("role_name"), production);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a production to the database. If the production is new (has an id of -1) then it is inserted into the
     * database. If it already exists, then the the database is updated.
     * The cast members and their roles are also saved.
     * @param production The production which needs to be saved.
     */
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
                ResultSet generatedKeys = insertProductionStmt.getGeneratedKeys();  // The primary key (PK) is saved
                generatedKeys.next();
                production.setId(generatedKeys.getInt(1));  // The id is set to the PK

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

                saveCastMembers(production.getCastList());  // The cast members are also saved
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Inserts a new user into the database. The password is hashed using md5.
     * @param newUser the user that needs to be saved.
     * @param password the users password.
     */
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
