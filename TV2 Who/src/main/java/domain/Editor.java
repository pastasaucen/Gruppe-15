package domain;

public class Editor extends User implements  IEditor{
    public Editor(String name, String email, UserType userType) {
        super(name, email, userType);
    }
}
