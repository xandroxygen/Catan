package testing;

import shared.model.Hex;
import org.junit.Test;


import static org.junit.Assert.assertTrue;

/**
 * Testing class for testing tests.
 */
public class TestTest {

    @Test
    public void shouldPass() {
        assertTrue(true);
    }

    @Test
    public void shouldFail() {
       // assertTrue(false);
    }

    @Test
    public void importsClassAndShouldPass() {

        assertTrue(true);
    }
}
