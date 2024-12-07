package edu.luc.etl.cs313.android.simplestopwatch.test.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import edu.luc.etl.cs313.android.simplestopwatch.common.Constants;
import org.junit.Ignore;
import org.junit.Test;

import android.widget.Button;
import android.widget.TextView;
import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.android.StopwatchAdapter;
import org.robolectric.shadows.ShadowMediaPlayer;
import edu.luc.etl.cs313.android.simplestopwatch.common.Constants;

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
            for(int i = 0; i < 5; i++) {
                assertTrue(getStartStopButton().performClick());
            }
        });
        runUiThreadTasks();
        Thread.sleep(3000);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            assertEquals(5, getDisplayedValue());
        });
        runUiThreadTasks();
        Thread.sleep(5500);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            assertEquals(0, getDisplayedValue());
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
        getActivity().runOnUiThread(() -> {
            assertEquals(0, getDisplayedValue());
            for(int i = 0; i < 99; i++) {
                assertTrue(getStartStopButton().performClick());
                runUiThreadTasks();
            }
            assertEquals(Constants.SEC_MAX, getDisplayedValue());
        });
        runUiThreadTasks();
        Thread.sleep(5000);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            assertEquals(94, getDisplayedValue());
        });
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
        runUiThreadTasks();
        Thread.sleep(3000);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            assertEquals(3, getDisplayedValue());
        });
        runUiThreadTasks();
        Thread.sleep(5000);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            assertEquals(0, getDisplayedValue());
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
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            for(int i = 0; i < 7; i++) {
                assertTrue(getStartStopButton().performClick());
            }
            assertEquals(7, getDisplayedValue());
        });
        runUiThreadTasks();
        Thread.sleep(3000);
        runUiThreadTasks();
        getActivity().changeOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            assertEquals(7, getDisplayedValue());
        });
        runUiThreadTasks();
        getActivity().changeOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        runUiThreadTasks();
        Thread.sleep(8000);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            assertEquals(0, getDisplayedValue());
        });
    }

    /**
     * Checks the following functionality:
     * 1. Stopwatch goes up to 99 and begins counting down.
     * 2. Stopwatch begins counting down once 99 is reached.
     * 3. Clicking the stopwatch while it is counting down stops the stopwatch.
     *
     * @author Emil and Ryan
     * */
    @Test
    public void testActivityScenarioIncUntilFull() throws Throwable {
        getActivity().runOnUiThread(() -> {
            for(int i = 0; i < 99; i++) {
                assertTrue(getStartStopButton().performClick());
                runUiThreadTasks();
            }
            assertEquals(Constants.SEC_MAX, getDisplayedValue());
        });

        Thread.sleep(2000);
        runUiThreadTasks();

        getActivity().runOnUiThread(() -> {
           assertEquals(Constants.SEC_MAX - 2, getDisplayedValue());
           assertTrue(getStartStopButton().performClick());
           assertEquals(0, getDisplayedValue());
        });
    }

    /**
     * Once time is incremented to 99, verifies that time is conserved after device is rotated.
     *
     * @author Emil and Ryan
     * */
    @Test
    public void testRotationAt99() throws Throwable {
        getActivity().changeOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            assertEquals(0, getDisplayedValue());
            for(int i = 0; i < 99; i++) {
                assertTrue(getStartStopButton().performClick());
                runUiThreadTasks();
            }
            assertEquals(Constants.SEC_MAX, getDisplayedValue());
        });
        runUiThreadTasks();
        getActivity().changeOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> assertEquals(Constants.SEC_MAX, getDisplayedValue()));
    }

    /**
     * Increments time by some amount, rotates device, waits three seconds, and checks that timer is still decrementing correctly.
     *
     * @author Emil and Ryan
     * */
    @Test
    public void testIncAfterRotation() throws Throwable {
        getActivity().changeOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            assertEquals(0, getDisplayedValue());
            for(int i = 0; i < 14; i++) {
                assertTrue(getStartStopButton().performClick());
                runUiThreadTasks();
            }
            assertEquals(14, getDisplayedValue());
        });
        runUiThreadTasks();
        getActivity().changeOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        runUiThreadTasks();
        Thread.sleep(3000);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> assertEquals(14, getDisplayedValue()));
        runUiThreadTasks();
        Thread.sleep(2000);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> assertTrue(getDisplayedValue() < 14));
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
