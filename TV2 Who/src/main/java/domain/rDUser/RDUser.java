package domain.rDUser;

import domain.User;
import domain.UserType;

public class RDUser extends User implements IRDUser{

    public RDUser(String name, String email) {
        super(name, email, UserType.RDUSER);
    }
}
