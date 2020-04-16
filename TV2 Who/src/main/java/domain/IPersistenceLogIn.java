package domain;

public interface IPersistenceLogIn {

    /**
     * Returns the correct user if a user exists in the persistence layer with the given email and password.
     * @param email
     * @param password
     * @return a user instans that represents the current user logged in to the user.
     */
    User logInValidation(String email, String password);

}
