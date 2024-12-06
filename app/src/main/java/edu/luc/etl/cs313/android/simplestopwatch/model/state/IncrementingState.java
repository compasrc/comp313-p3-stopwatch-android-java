package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.common.Constants;

/**
 * Class definition for the IncrementingState class (state design pattern).
 */
class IncrementingState implements StopwatchState {

    /**
     * Constructor method for the IncrementingState - sets the view of this state
     * of its surrounding state machine.
     *
     * @param sm The state machine view to pass to the state.
     */
    public IncrementingState(final StopwatchSMStateView sm) { this.sm = sm; }

    private int ticker = Constants.TICK_WAIT;

    /**
     * The view of the state machine that the IncrementingState object has.
     * */
    private final StopwatchSMStateView sm;

    /**
     * Implementation of onButton() from the Listener interface for IncrementingState.
     * When the button is pressed:
     * 1. Increment the timer.
     * 2. IF 99 seconds is reached, sound the alarm and switch to Running state.
     * 3. ELSE reset the ticker to it's initial value of 3 seconds and switch to Incrementing state.
     */
    @Override
    public void onButton() {
        sm.actionInc();
        if (sm.getRuntime() == Constants.SEC_MAX) {
            sm.actionAlarm();
            sm.toRunningState();
        } else {
            ticker = Constants.TICK_WAIT;
            sm.toIncrementingState();
        }
    }

    /**
     * Implementation of onTick() from the Listener interface for IncrementingState.
     * 1. On each tick, decrement the ticker value by one.
     * 2. IF ticker value reaches 0, sound the alarm and switch to Running state.
     * 3. ELSE switch to Incrementing state.
     */
    @Override
    public void onTick() {
        ticker--;
        if (ticker == 0) {
            ticker = Constants.TICK_WAIT;
            sm.actionAlarm();
            sm.toRunningState();
        } else {
            sm.toIncrementingState();
        }
    }

    /**
     * Updates the runtime to the UI.
     * */
    @Override
    public void updateView() { sm.updateUIRuntime(); }

    /**
     * Returns this state.
     * */
    @Override
    public int getId() {
        return R.string.INCREMENTING;
    }
}
