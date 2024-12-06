package edu.luc.etl.cs313.android.simplestopwatch.test.model;

import static edu.luc.etl.cs313.android.simplestopwatch.common.Constants.SEC_PER_HOUR;
import static edu.luc.etl.cs313.android.simplestopwatch.common.Constants.SEC_PER_MIN;
import static edu.luc.etl.cs313.android.simplestopwatch.common.Constants.SEC_PER_TICK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

/**
 * Testcase superclass for the time model abstraction.
 * This is a simple unit test of an object without dependencies.
 *
 * @author laufer
 * @see http://xunitpatterns.com/Testcase%20Superclass.html
 */
public abstract class AbstractTimeModelTest {

    private TimeModel model;

    /**
     * Setter for dependency injection. Usually invoked by concrete testcase
     * subclass.
     *
     * @param model
     */
    protected void setModel(final TimeModel model) {
        this.model = model;
    }

    /**
     * Verifies that initial state runtime is zero.
     *
     * @author Chris & Michael
     */
    @Test
    public void testInitialState() {
        final var state = model.getRuntime();
        assertEquals(0, state);
    }

    /**
     * Verifies that runtime is decremented correctly.
     *
     * @author Chris & Michael
     */
    @Test
    public void testDecrementRuntimeOne() {
        final var rt = model.getRuntime();
        model.decRuntime();
        assertEquals((rt - SEC_PER_TICK), model.getRuntime());
    }

    /**
     * Verifies that runtime is incremented correctly.
     *
     * @author Emil & Ryan
     */
    @Test
    public void testIncrementRuntimeOne() {
        final var rt = model.getRuntime();
        model.incRuntime();
        assertEquals((rt + SEC_PER_TICK), model.getRuntime());
    }
}
