package domain;

public class Editor extends User implements  IEditor{

    private UserType userType = UserType.EDITOR;

    public Editor(String name, String email) {
        super(name, email);
    }
}
