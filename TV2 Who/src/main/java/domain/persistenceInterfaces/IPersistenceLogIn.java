package domain.persistenceInterfaces;

import domain.User;

public interface IPersistenceLogIn {

    /**
     * Returns the correct user if a user exists in the persistence layer with the given email and password.
     * @param email
     * @param password
     * @return a user instance that represents the current user logged in to the user.
     */
    User logInValidation(String email, String password);

}
