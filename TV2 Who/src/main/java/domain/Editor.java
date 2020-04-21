package domain;

public class Editor extends User implements  IEditor{

    public Editor(String name, String email) {
        super(name, email, UserType.EDITOR);
    }
}
