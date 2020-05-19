package test;

import domain.Cast;
import domain.Production;
import domain.State;
import domain.TV2Who;
import domain.producer.Producer;

import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ProductionTest {

    Producer producer;
    Production production;
    Cast cast;


    @org.junit.Before
    public void setUp() throws Exception {
        producer = new Producer("Jeppe", "123@gmail.com");

        //producer.createProduction("Shrek", new Date(1));


        cast = new Cast(-1, "Jens", "Vejmand", "mail@gmail.com", "");
        //producer.addRole("Camera Man", cast);

        List<Production> l = TV2Who.getInstance().prepareProductionSearchList("0000");

        Cast lone = l.get(0).getCastList().get(0);

        producer.addRole("Riddler", lone, l.get(0));

        //production = new Production(1, "Shrek",
        //new java.sql.Date(1), State.PENDING, "123@gmail.com");


    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void addRole() {
        //producer.getProduction().addRole("CameraMan", cast);
        //System.out.println(producer.getProduction().getCastList().toString());
    }
}