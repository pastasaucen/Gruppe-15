package test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import persistence.TVCodeGenerator;

import static org.junit.Assert.*;

public class TVCodeGeneratorTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void incrementTVCode() {
        String start = "0000";
        System.out.println(start);

        for (int i = 0; i < 1296; i++) {
            start = TVCodeGenerator.incrementTVCode(start);
            System.out.println(start);
        }

        Assert.assertEquals("0100", start);
    }
}