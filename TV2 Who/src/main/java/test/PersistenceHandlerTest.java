package test;

import domain.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import persistence.PersistenceHandler;

import java.sql.Date;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PersistenceHandlerTest {

    PersistenceHandler persistenceHandler;

    @Before
    public void setUp() throws Exception {
        persistenceHandler = PersistenceHandler.getInstance();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getCastMembers() {
        String searchString = "Lone";
        List<Cast> castList = persistenceHandler.getCastMembers(searchString);
        System.out.println(castList);
    }

    @Test
    public void saveCastMembers() {
        List<Cast> castList = new ArrayList<>();
        Cast cast = new Cast(-1,
                "Jeppe",
                "Stenstrup",
                "js@mail.dk",
                "Mit navn er Jeppe og jeg er sej");
        castList.add(cast);
        cast.addRole(-1,"Chihuahua", new Production(2,"test", new Date(System.currentTimeMillis())));
        persistenceHandler.saveCastMembers(castList);
        System.out.println(castList);
        Assert.assertNotEquals(-1, cast.getId());

        Cast cast1 = new Cast(cast.getId(),
                "Christian",
                "Bodeval",
                "cb@mail.dk",
                "Mit navn var altså Christian, ikke Jeppe");
        cast1.addRole(-1,"Bulldog", new Production(2,"test", new Date(System.currentTimeMillis())));
        castList.clear();
        castList.add(cast1);
        persistenceHandler.saveCastMembers(castList);
        List<Cast> alteredCast = persistenceHandler.getCastMembers(cast1.getEmail());
        System.out.println(alteredCast);
        Assert.assertEquals("Christian", alteredCast.get(0).getFirstName());


    }

    @Test
    public void logInValidation() {
        // Should find a user
        System.out.println("Should find a user");
        User user = persistenceHandler.logInValidation("andreas@edal.dk", "12345");
        Assert.assertEquals("andreas@edal.dk", user.getEmail());
        Assert.assertEquals(UserType.SYSTEMADMINISTRATOR, user.getUserType());
        System.out.println("The user was found: " + user.toString());

        // Should not find a user
        System.out.println("Should receive a null pointer value");
        User nullUser = persistenceHandler.logInValidation("null", "null");
        Assert.assertNull(nullUser);
    }

    @Test
    public void getProductions() {
        // Tests the method by using the name
        String searchStringName = "Badehotellet";
        List<Production> productionListName = persistenceHandler.getProductions(searchStringName);
        System.out.println("From the name:");
        System.out.println(productionListName);
        Assert.assertEquals("Badehotellet", productionListName.get(0).getName());

        // Tests the method by using the TV-code
        String searchStringTVCode = "0001";
        List<Production> productionListTVCode = persistenceHandler.getProductions(searchStringTVCode);
        System.out.println("From the TV-code:");
        System.out.println(productionListTVCode);
        Assert.assertEquals("Batman", productionListTVCode.get(0).getName());

    }

    @Test
    public void saveProduction() {
        // Creates a new
        String name = String.valueOf((int) (Math.random()*1000));
        Production newProduction = new Production(-1,
                name,
                new Date(System.currentTimeMillis()),
                State.ACCEPTED,
                "NULL",
                "andreas@edal.dk"
            );

        persistenceHandler.saveProduction(newProduction);
        Assert.assertNotEquals(-1, newProduction.getId());
        System.out.println(newProduction);

        String newEmail = "w.bolding@outlook.com";
        String newName = "I'VE CHANGED MY NAME! " + newProduction.getName();
        Production alteredProduction = new Production(newProduction.getId(),
                newName,
                new Date(2001-1900, 0,1),
                State.DECLINED,
                "LLUN",
                newEmail
        );
        persistenceHandler.saveProduction(alteredProduction);

        List<Production> productions = persistenceHandler.getProductions(newName);
        Assert.assertEquals(newEmail, productions.get(0).getAssociatedProducerEmail());
        System.out.println(productions);
    }

    @Test
    public void createUser() {
        
    }
}