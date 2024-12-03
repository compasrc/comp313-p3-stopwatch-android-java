package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

/**
 * Class definition for the RunningState class (state design pattern).
 * This class also implements the listener design pattern.
 */
class RunningState implements StopwatchState {

    /**
     * Constructor method for the RunningState - sets the view of this state
     * of its surrounding state machine.
     *
     * @param sm The state machine view to pass to the state.
     */
    public RunningState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    /**
     * The view of the state machine that the RunningState object has.
     */
    private final StopwatchSMStateView sm;

    /**
     * Implementation of onButton() from the StopwatchUIListener interface for RunningState.
     * When the button is pressed:
     * 1. Stop the running the timer.
     * 2. Sound the alarm.
     * 3. Reset stopwatch to initial state (Stopped).
     */
    @Override
    public void onButton() {
        sm.actionStop();
        sm.actionAlarm();
        sm.actionInit();
    }

    /**
     * Implementation of onTick() from the StopwatchUIListener interface for RunningState.
     * When the button is pressed:
     * 1. IF the timer hits 0, go to Alarming state.
     * 2. ELSE decrement the timer and go to the Running state.
     */
    @Override
    public void onTick() {
        if (sm.getRuntime() == 0) {
            sm.toAlarmingState();
        } else {
            sm.actionDec();
            sm.toRunningState();
        }
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
        return R.string.RUNNING;
    }
}
