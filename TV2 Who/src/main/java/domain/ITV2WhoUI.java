package domain;

import java.sql.Date;
import java.util.List;

public interface ITV2WhoUI {

    public List<Cast> prepareCastSearchList(String name);

    public List<Production> prepareProductionSearchList(String nameOrId);

    public boolean createUserSession(String email, String password);

    public static TV2Who getInstance(){
        return TV2Who.getInstance();
    }

    Cast getCastMember(int id);

    Production getProduction(int id);

    public User getCurrentUser();

    void logOut();
}
