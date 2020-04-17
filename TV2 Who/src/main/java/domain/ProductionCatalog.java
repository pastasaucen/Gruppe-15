package domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Date;

public class ProductionCatalog {

    private static ProductionCatalog instance;
    private static IPersistenceProduction persistenceProduction;

    private List<Production> productions = new ArrayList<>();


    private ProductionCatalog() {
        //Testværdier
        productions.add(new Production(69,"Gudfaderen",new Date(System.currentTimeMillis())));
    }

    public static ProductionCatalog getInstance() {
        if (instance == null) {
            instance = new ProductionCatalog();
        }
        return instance;
    }

    public void addProduction(Production newProduction) {
        // TODO Will be changed when the persistence layer is created.
        productions.add(newProduction);
        System.out.println(productions);
    }

    /**
     * Saves temporarily found productions and sets text in the search field.
     *
     * @param //Searches for the input. Can either be id or name
     * @return
     */
    public List<Production> getProduction(String nameOrId) {
        // TODO Will be changed when the presentation layer is created.


        productions = persistenceProduction.getProductions(nameOrId);


        if (productions == null) {
            // Skriv at produktionen ikke eksistere, når gui virker
            return null;
        } else {
            //Print alle produktioner
            for (Production p : productions) {
                System.out.println(p.toString());
            }
            return productions;
        }
    }

    /**
     * Fetches a list of Productions who's ID or Name matches a production in the persistence layer
     * @param nameOrId
     * @return
     */
    public List<Production> searchForProduction(String nameOrId) {
        List<Production> relevantProductions = new ArrayList<>();
        for (Production production: productions) {
            if(nameOrId.matches("[0-9]+")){
                productions.add(production);
            }else if (production.getName().contains(nameOrId)){
                productions.add(production);
            }
        }
        return productions;
    }

}
