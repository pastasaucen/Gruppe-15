package domain;

public class RDUser extends User implements IRDUser{

    public RDUser(String name, String email, UserType userType) {
        super(name, email, userType);
    }
}
