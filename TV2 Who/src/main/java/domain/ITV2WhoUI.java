package domain;

import java.sql.Date;
import java.util.List;

public interface ITV2WhoUI {

    public List<Cast> prepareCastSearchList(String firstName,String lastName);

    public List<Production> prepareProductionSearchList(String nameOrId);

    public static TV2Who getInstance(){
        return TV2Who.getInstance();
    }

    public Production createProduction(String name, Date releaseDate);

    public void saveProduction(Production production);
}
