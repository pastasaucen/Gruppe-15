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

    public Production createProduction(String name, Date releaseDate);

    public User getCurrentUser();

    public void saveProduction(Production production);
}
