package domain;

import java.util.List;

public interface ITv2Who {

    public List<Cast> prepareCastSearchList(String firstName,String lastName);

    public List<Production> prepareProductionSearchList(String nameOrId);

    public static TV2Who getInstance(){
        return TV2Who.getInstance();
    }
}
