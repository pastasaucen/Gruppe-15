package test;

import domain.Cast;
import domain.Production;
import domain.State;
import domain.producer.Producer;

import java.sql.Date;

import static org.junit.Assert.*;

public class ProductionTest {

    Producer producer;
    Production production;
    Cast cast;


    @org.junit.Before
    public void setUp() throws Exception {
        producer = new Producer("Jeppe", "123@gmail.com");


        producer.createProduction("Shrek", new Date(1));

        cast = new Cast(-1, "Jens", "Vejmand", "mail@gmail.com", "");

        producer.getProduction().addCastMember(cast);

        //production = new Production(1, "Shrek",
        //new java.sql.Date(1), State.PENDING, "123@gmail.com");


    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void addRole() {
        producer.getProduction().addRole("CameraMan", cast);
        System.out.println(producer.getProduction().getCastList().toString());
    }
}