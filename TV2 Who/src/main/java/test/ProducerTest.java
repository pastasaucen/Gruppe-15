package test;

import domain.Production;
import domain.User;
import domain.producer.IProducer;
import domain.producer.Producer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import persistence.PersistenceHandler;

import java.util.List;

import static org.junit.Assert.*;

public class ProducerTest {

    User user = new Producer("Andreas", "andreas@edal.dk");
    IProducer producer = (IProducer) user;
    PersistenceHandler persistenceHandler = PersistenceHandler.getInstance();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addRole() {
        List<Production> badehotellet = persistenceHandler.getProductions("Badehotellet", user);

        producer.addRole("Test!!!", badehotellet.get(0).getCastList().get(0), badehotellet.get(0));
    }
}