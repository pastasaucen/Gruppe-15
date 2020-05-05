package domain;

import java.util.List;

public interface ITV2WhoUI {

    public List<Cast> prepareCastSearchList(String firstName,String lastName);

    public List<Production> prepareProductionSearchList(String nameOrId);

    public void createUserSession(String email, String password);

    public static TV2Who getInstance(){
        return TV2Who.getInstance();
    }
}
