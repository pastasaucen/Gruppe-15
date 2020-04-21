package domain;

public class RDUser extends User implements IRDUser{

    public RDUser(String name, String email) {
        super(name, email, UserType.RDUSER);
    }
}
