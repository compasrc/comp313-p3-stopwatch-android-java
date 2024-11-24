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
    public IncrementingState(final StopwatchSMStateView sm) {this.sm = sm;}

    private int ticker = Constants.TICK_WAIT;

    /**
     * The view of the state machine that the IncrementingState object has.
     * */
    private final StopwatchSMStateView sm;

    /**
     * Implementation of onButton() from the Listener interface for IncrementingState.
     * When the button is pressed, either more time is added or
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
     */
    @Override
    public void onTick() {
        ticker--;
        if (ticker == 0) {
            sm.actionAlarm();
            sm.toRunningState();
        } else {
            sm.toIncrementingState();
        }
    }

    @Override
    public void updateView() { sm.updateUIRuntime(); }

    @Override
    public int getId() {
        return R.string.INCREMENTING;
    }
}
