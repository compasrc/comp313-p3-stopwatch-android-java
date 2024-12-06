package edu.luc.etl.cs313.android.simplestopwatch.test.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.pm.ActivityInfo;
import edu.luc.etl.cs313.android.simplestopwatch.common.Constants;
import org.junit.Ignore;
import org.junit.Test;

import android.widget.Button;
import android.widget.TextView;
import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.android.StopwatchAdapter;
import org.robolectric.shadows.ShadowMediaPlayer;

import static edu.luc.etl.cs313.android.simplestopwatch.common.Constants.SEC_PER_MIN;

/**
 * Abstract GUI-level test superclass of several essential stopwatch scenarios.
 *
 * @author laufer
 *
 * TODO move this and the other tests to src/test once Android Studio supports
 * non-instrumentation unit tests properly.
 */
public abstract class AbstractStopwatchActivityTest {

    /**
     * Verifies that the activity under test can be launched.
     */
    @Test
    public void testActivityCheckTestCaseSetUpProperly() {
        assertNotNull("activity should be launched successfully", getActivity());
    }

    /**
     * Verifies the following scenario: time is 0.
     *
     * @throws Throwable
     */
    @Test
    public void testActivityScenarioInit() throws Throwable {
        getActivity().runOnUiThread(() -> assertEquals(0, getDisplayedValue()));
    }

    /**
     * Verifies the following scenario: time is 0, press start, wait 5+ seconds, expect time 5.
     *
     * @throws Throwable
     */
    @Test
    public void testActivityScenarioRun() throws Throwable {
        getActivity().runOnUiThread(() -> {
            assertEquals(0, getDisplayedValue());
            for (int i = 0; i < 5; i++) {
                assertTrue(getStartStopButton().performClick());
                runUiThreadTasks();
            }
            assertEquals(5, getDisplayedValue());
        });
        Thread.sleep(3000); // <-- do not run this in the UI thread!
        runUiThreadTasks();
        assertEquals(5, getDisplayedValue());
        Thread.sleep(4000);
        runUiThreadTasks();
        Thread.sleep(1000);
        runUiThreadTasks(); // need to run twice otherwise the code halts when the alarm sounds
        getActivity().runOnUiThread(() -> {
            assertEquals(0, getDisplayedValue());
            assertTrue(getStartStopButton().performClick());
        });
    }

    /**
     * Verifies that when runtime reaches 99 in the incrementing state,
     * the state machine transitions into the running state,
     * and then after 3 seconds the runtime is 94.
     *
     * @author Emil and Ryan
     * */
    @Test
    public void testDecrementStartAt99() throws Throwable {
        assertEquals(0, getDisplayedValue());
        for (int i = 0; i < 99; i++) {
            assertTrue(getStartStopButton().performClick());
            runUiThreadTasks();
        }
        assertEquals(Constants.SEC_MAX, getDisplayedValue());
        Thread.sleep(5000);
        runUiThreadTasks();
        assertEquals(94, getDisplayedValue());
        assertTrue(getStartStopButton().performClick());
    }

    /**
     * Verifies that stopwatch waits 3 seconds before running.
     *
     * @author Emil and Ryan
     * */
    @Test
    public void testRuntimeOut() throws Throwable {
        getActivity().runOnUiThread(() -> {
           for(int i = 0; i < 3; i++) {
               assertTrue(getStartStopButton().performClick());
           }
           assertEquals(3, getDisplayedValue());
        });
        Thread.sleep(3000);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            assertEquals(3, getDisplayedValue());
        });
        Thread.sleep(1000);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            assertEquals(2, getDisplayedValue());
        });
    }

    /**
     * Verifies that decrementing behavior is preserved after screen is rotated from portrait to landscape.
     *
     * @author Emil and Ryan
     * */
    @Test
    public void testDecrementAfterRotation() throws Throwable {
        getActivity().changeOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().runOnUiThread(() -> {
            for(int i = 0; i < 7; i++) {
                assertTrue(getStartStopButton().performClick());
            }
            assertEquals(7, getDisplayedValue());
        });
        getActivity().changeOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Thread.sleep(3000);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            assertEquals(7, getDisplayedValue());
        });
        Thread.sleep(1000);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            assertEquals(6, getDisplayedValue());
        });
    }

    // auxiliary methods for easy access to UI widgets

    protected abstract StopwatchAdapter getActivity();

    protected int tvToInt(final TextView t) {
        return Integer.parseInt(t.getText().toString().trim());
    }

    protected int getDisplayedValue() {
        final TextView ts = getActivity().findViewById(R.id.seconds);
        return tvToInt(ts);
    }

    protected Button getStartStopButton() {
        return getActivity().findViewById(R.id.startStop);
    }

    /**
     * Explicitly runs tasks scheduled to run on the UI thread in case this is required
     * by the testing framework, e.g., Robolectric.
     */
    protected void runUiThreadTasks() { }
}
