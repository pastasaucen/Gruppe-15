package domain;

import persistence.ProductionsHandler;

import java.sql.Date;

public class Main {

    public static void main(String[] args) {

        // Test "opret produktion"
        /*
        Producer producer = new Producer("Jan","blah@blah.dk");

        producer.createProduction("Badehotellet", new Date(System.currentTimeMillis()));
        producer.addCastMember("Bob Bobsen");
        producer.addCastMember("Inger Bobsen");
        producer.addRole("Batman", new Cast(-1, "Bob", "Bobsen", "bob@bobsen.dk"));

        producer.submitProduction();

         */


        Production production = new Production("Olsen Banden", new Date(System.currentTimeMillis()));
        Cast castMember1 = new Cast(2,"Inger", "Bobsen", "ing@bob.dk");
        production.addCastMember(castMember1);
        production.addRole("WonderWoman", castMember1);
        production.addRole("Peter Plys", castMember1);
        ProductionsHandler productionsHandler = new ProductionsHandler();
        productionsHandler.saveProduction(production);
        //System.out.println(productionsHandler.getProductions("Bade"));

    }
}
