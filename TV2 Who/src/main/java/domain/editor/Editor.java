package domain.editor;

import domain.User;
import domain.UserType;

public class Editor extends User implements  IEditor{
    //TODO Fjernes
    public Editor(String name, String email) {
        super(name, email, UserType.EDITOR);
    }
}
