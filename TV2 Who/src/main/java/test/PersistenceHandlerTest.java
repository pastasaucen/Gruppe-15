package test;

import domain.User;
import domain.UserType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import persistence.PersistenceHandler;

import java.sql.SQLOutput;

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
        User user = persistenceHandler.logInValidation("w.bolding@outlook.com", "1x2c3v4b");
        Assert.assertEquals(UserType.SYSTEMADMINISTRATOR, user.getUserType());
        System.out.println("The user was found: " + user.toString());

        // Should not find a user
        System.out.println("Should receive a null pointer value");
        User nullUser = persistenceHandler.logInValidation("null", "null");
        Assert.assertNull(nullUser);
        System.out.println("Found this: " + null);
    }

    @Test
    public void getProductions() {
    }

    @Test
    public void saveProduction() {
    }

    @Test
    public void createUser() {
    }
}