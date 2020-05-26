package domain;

import java.sql.Date;
import java.util.List;

public interface ITV2WhoUI {

    List<Cast> prepareCastSearchList(String name);

    List<Production> prepareProductionSearchList(String nameOrId);

    boolean createUserSession(String email, String password);

    static TV2Who getInstance(){
        return TV2Who.getInstance();
    }

    Cast getCastMember(int id);

    Production getProduction(int id);

    User getCurrentUser();

    void logOut();
}
