package domain;

public interface IPersistenceUser {

    /**
     * Saves the new user in the persistence layer from the given user instance and the given password.
     * The password is given separately for security reasons.
     * @param newUser the user that needs to be saved.
     * @param password the users password.
     */
    void createUser(User newUser, String password);
}
