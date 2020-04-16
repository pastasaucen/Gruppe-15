package domain;

public class RDUser extends User implements IRDUser{

    private UserType userType = UserType.RDUSER;

    public RDUser(String name, String email) {
        super(name, email);
    }
}
