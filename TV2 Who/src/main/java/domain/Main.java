package domain;

import persistence.ProductionsHandler;

import java.sql.Date;

public class Main {

    public static void main(String[] args) {

        /*
        // Test "opret produktion"
        Producer producer = new Producer("Jan","blah@blah.dk", UserType.PRODUCER);

        producer.createProduction("Badehotellet", new Date(System.currentTimeMillis()));
        producer.addCastMember("Bob Bobsen");
        producer.addCastMember("Inger Bobsen");
        producer.addRole("Batman", new Cast(-1, "Bob", "Bobsen", "bob@bobsen.dk"));

        producer.submitProduction();
         */


        Production production = new Production("Fast and Furious 7", new Date(System.currentTimeMillis()));
        Cast castMember2 = new Cast(3,"Lone", "Borgersen", "lobo@mmmi.sdu.dk");
        production.addCastMember(castMember2);
        production.addRole("Bil 2", castMember2);
        production.setAssociatedProducerEmail("hej@farvel.dk");
        ProductionsHandler productionsHandler = new ProductionsHandler();
        //productionsHandler.saveProduction(production);
        //System.out.println(productionsHandler.getProductions("a"));
    }
}
