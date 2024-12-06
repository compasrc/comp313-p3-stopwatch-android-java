package edu.luc.etl.cs313.android.simplestopwatch.test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import android.view.OrientationEventListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.TickListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.state.StopwatchStateMachine;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

/**
 * Testcase superclass for the stopwatch state machine model. Unit-tests the state
 * machine in fast-forward mode by directly triggering successive tick events
 * without the presence of a pseudo-real-time clock. Uses a single unified mock
 * object for all dependencies of the state machine model.
 *
 * @author laufer
 * @see http://xunitpatterns.com/Testcase%20Superclass.html
 */
public abstract class AbstractStopwatchStateMachineTest {

    private StopwatchStateMachine model;

    private UnifiedMockDependency dependency;

    @Before
    public void setUp() throws Exception {
        dependency = new UnifiedMockDependency();
    }

    @After
    public void tearDown() {
        dependency = null;
    }

    /**
     * Setter for dependency injection. Usually invoked by concrete testcase
     * subclass.
     *
     * @param model The model used for dependency injection.
     */
    protected void setModel(final StopwatchStateMachine model) {
        this.model = model;
        if (model == null)
            return;
        this.model.setModelListener(dependency);
        this.model.actionInit();
    }

    protected UnifiedMockDependency getDependency() {
        return dependency;
    }

    /**
     * Verifies that we're initially in the stopped state (and told the listener
     * about it).
     */
    @Test
    public void testPreconditions() {
        assertEquals(R.string.STOPPED, dependency.getState());
    }

    /**
     * Verifies that when runtime reaches 99 in the incrementing state,
     * the state machine transitions into the running state,
     * and then after 5 ticks the runtime is 94.
     *
     * @author Emil and Ryan
     * */
    @Test
    public void testDecrementStartAt99() {
        assertEquals(R.string.STOPPED, dependency.getState());
        for (int i = 0; i < 99; i++) {
            model.onButton();
        }
        assertEquals(R.string.RUNNING, dependency.getState());
        onTickRepeat(5);
        assertTimeEquals(94);
    }


    /**
     * Verifies that when timer reaches 0 during Running state,
     * the state machine transitions to Alarming.
     *
     * @author Chris & Michael
     */
    @Test
    public void testRunningToAlarming() {

        model.onButton();
        onTickRepeat(3);    // enter Running
        onTickRepeat(2);    // enter Alarming
        assertEquals(R.string.ALARMING, dependency.getState());
    }

    /**
     * Verifies that timer stops when Alarming.
     *
     * @author Chris & Michael
     */
    @Test
    public void testTimerInAlarming() {

        model.onButton();
        onTickRepeat(3);    // enter Running
        onTickRepeat(2);    // enter Alarming
        assertTimeEquals(0);
        onTickRepeat(5);
        assertTimeEquals(0);
        onTickRepeat(99);
        assertTimeEquals(0);
    }

    /**
     * Verifies that when button is pressed during Alarming,
     * state machine transitions to Stopped.
     *
     * @author Chris & Michael
     */
    @Test
    public void testAlarmingToStopped() {

        model.onButton();
        onTickRepeat(3);    // enter Running
        onTickRepeat(2);    // enter Alarming
        model.onButton();
        assertEquals(R.string.STOPPED, dependency.getState());
    }

    /**
     * Verifies that when button is pressed during Running,
     * state machine transitions to Stopped.
     *
     * @author Chris & Michael
     */
    @Test
    public void testRunningToStopped() {

        for (int i=0; i<5; i++){
            model.onButton();
        }
        onTickRepeat(3);    //enter Running
        model.onButton();
        assertTimeEquals(0);
        assertEquals(R.string.STOPPED, dependency.getState());

        for (int i=0; i<99; i++){
            model.onButton();
        }
        model.onButton();
        assertTimeEquals(0);
        assertEquals(R.string.STOPPED, dependency.getState());
    }

    /**
     * Verifies the following:
     * 1. The state machine starts in the stopped state.
     * 2. Upon pressing the button, the state machine transitions to the incrementing state and increments runtime by 1.
     * 3. After 5 ticks have passed (3 seconds + 1 second to allow runtime to decrement), the state is alarming.
     * 4. When in the alarming state, after the button is pressed the runtime is set back to 0 and the state machine is in the stopped state.
     * 5. After pressing the button, ensures the same tests in steps 2-4 pass in the second cycle of the state machine.
     *
     * @author Emil and Ryan
     * */
    @Test
    public void testEdgeCaseOne() {
        assertEquals(R.string.STOPPED, dependency.getState());
        model.onButton();
        assertEquals(R.string.INCREMENTING, dependency.getState());
        assertTimeEquals(1);
        onTickRepeat(5);
        assertEquals(R.string.ALARMING, dependency.getState());
        model.onButton();
        assertEquals(R.string.STOPPED, dependency.getState());
        assertTimeEquals(0);
        model.onButton();
        assertTimeEquals(1);
        onTickRepeat(5);
        assertEquals(R.string.ALARMING, dependency.getState());
        model.onButton();
        assertEquals(R.string.STOPPED, dependency.getState());
    }

    /**
     * 1. Timer is set to 7.
     * 2. Waits 3 seconds.
     * 3. Confirms timer is still at 7.
     * 4. Waits 2 more seconds.
     * 5. Confirms timer is now at 5.
     *
     * @author Emil and Ryan
     * */
    @Test
    public void testDecrementStartAtValue() {
        assertEquals(R.string.STOPPED, dependency.getState());
        for(int i = 0; i < 7; i++) {
            model.onButton();
            System.out.println(model.getRuntime());
        }
        assertEquals(R.string.INCREMENTING, dependency.getState());
        assertTimeEquals(7);
        onTickRepeat(3);
        assertEquals(R.string.RUNNING, dependency.getState());
        assertTimeEquals(7);
        onTickRepeat(2);
        assertTimeEquals(5);
    }

    /**
     * Verifies time starts at 0 and when button is pressed 7 times, runtime is 7.
     *
     * @author Ryan and Emil
     * */
    @Test
    public void testActivityScenarioInc() {
        assertEquals(R.string.STOPPED, dependency.getState());
        assertTimeEquals(0);
        for(int i = 0; i < 7; i++) {
            model.onButton();
        }
        assertTimeEquals(7);
    }

    /**
     * Sends the given number of tick events to the model.
     *
     *  @param n the number of tick events
     */
    protected void onTickRepeat(final int n) {
        for (var i = 0; i < n; i++)
            model.onTick();
    }

    /**
     * Checks whether the model has invoked the expected time-keeping
     * methods on the mock object.
     */
    protected void assertTimeEquals(final int t) {
        assertEquals(t, dependency.getTime());
    }
}

/**
 * Manually implemented mock object that unifies the three dependencies of the
 * stopwatch state machine model. The three dependencies correspond to the three
 * interfaces this mock object implements.
 *
 * @author laufer
 */
class UnifiedMockDependency implements TimeModel, ClockModel, StopwatchModelListener {

    private int timeValue = -1, stateId = -1;

    private int runningTime = 0;

    private boolean started = false;

    public int getTime() {
        return timeValue;
    }

    public int getState() {
        return stateId;
    }

    public boolean isStarted() {
        return started;
    }

    @Override
    public void onTimeUpdate(final int timeValue) {
        this.timeValue = timeValue;
    }

    @Override
    public void onStateUpdate(final int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void soundAlarm(int notification_sound) { }

    @Override
    public void setTickListener(TickListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public void resetRuntime() { runningTime = 0; }

    @Override
    public void incRuntime() {
        runningTime++;
    }

    @Override
    public void decRuntime() { runningTime--; }

    @Override
    public int getRuntime() {
        return runningTime;
    }
}

