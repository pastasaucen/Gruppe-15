package persistence;

import domain.persistenceInterfaces.IPersistenceLogIn;
import domain.User;

public class LoginHandler implements IPersistenceLogIn {

    @Override
    public User logInValidation(String email, String password) {
        return null;
    }
}
