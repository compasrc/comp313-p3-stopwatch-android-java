package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.common.Constants;

/**
 * Class definition for the StoppedState class (state design pattern)
 */
class StoppedState implements StopwatchState {

    /**
     * Constructor method for the StoppedState - sets the view of this state
     * of its surrounding state machine.
     *
     * @param sm The state machine view to pass to the state.
     */
    public StoppedState(final StopwatchSMStateView sm) { this.sm = sm; }

    /**
     * The view of the state machine that the IncrementingState object has.
     * */
    private final StopwatchSMStateView sm;

    /**
     * Implementation of onButton() from the StopwatchUIListener interface for StoppedState.
     * When the button is pressed:
     * 1. Increment the timer.
     * 2. Start running the stopwatch.
     * 3. Switch to Incrementing state.
     */
    @Override
    public void onButton() {
        int enteredTime = sm.getEnteredTime();
        if (enteredTime == Constants.UI_DEFAULT) {
            sm.actionInc();
            sm.actionStart();
            sm.toIncrementingState();
        } else {
            sm.enterTime(enteredTime);
            updateView();
            sm.actionAlarm();
            sm.toRunningState();
            sm.actionStart();
        }
    }

    /**
     * Implementation of onTick() from the TickListener interface for StoppedState.
     * The Stopped state is not to allow ticks, so throw an error if a tick occurs.
     */
    @Override
    public void onTick() {
        throw new UnsupportedOperationException("onTick");
    }

    /**
     * Updates the runtime to the UI.
     * */
    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    /**
     * Returns this state.
     * */
    @Override
    public int getId() {
        return R.string.STOPPED;
    }
}
