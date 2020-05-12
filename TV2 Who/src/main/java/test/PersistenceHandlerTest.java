package test;

import domain.Production;
import domain.State;
import domain.User;
import domain.UserType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import persistence.PersistenceHandler;

import java.sql.Date;
import java.sql.SQLOutput;
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
    }

    @Test
    public void saveCastMembers() {

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
        String searchStringName = "Badehotellet";
        List<Production> productionListName = persistenceHandler.getProductions(searchStringName);
        System.out.println("From the name:");
        System.out.println(productionListName);
        Assert.assertEquals("Badehotellet", productionListName.get(0).getName());

        String searchStringTVCode = "0001";
        List<Production> productionListTVCode = persistenceHandler.getProductions(searchStringTVCode);
        System.out.println("From the TV-code:");
        System.out.println(productionListTVCode);
        Assert.assertEquals("Batman", productionListTVCode.get(0).getName());

    }

    @Test
    public void saveProduction() {
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