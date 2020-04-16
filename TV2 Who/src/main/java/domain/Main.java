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

        Production production = new Production(1, "Batman", new Date(System.currentTimeMillis()));
        ProductionsHandler productionsHandler = new ProductionsHandler();
        System.out.println(productionsHandler.getProductions("2"));

    }
}
