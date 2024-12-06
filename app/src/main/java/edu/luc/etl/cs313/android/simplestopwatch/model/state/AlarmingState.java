package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

/**
 * Class definition for the AlarmingState class (state design pattern)
 */
class AlarmingState implements StopwatchState {

    /**
     * Constructor method for the AlarmingState - sets the view of this state
     * of its surrounding state machine.
     *
     * @param sm The state machine view to pass to the state.
     */
    public AlarmingState(final StopwatchSMStateView sm) { this.sm = sm; }

    /**
     * The view of the state machine that the StopwatchState object has.
     * */
    private final StopwatchSMStateView sm;

    /**
     * Implementation of onButton() from the StopwatchUIListener interface for AlarmingState.
     * When the button is pressed:
     * 1. Stop the alarm sound.
     * 2. Reset stopwatch to initial state (Stopped).
     */
    @Override
    public void onButton() {
        sm.actionStop();
        sm.actionInit();
    }

    /**
     * Implementation of onTick() from the TickListener interface for AlarmingState.
     * 1. Sound the alarm.
     * 2. Continue in the Alarming State (until the button is pressed).
     */
    @Override
    public void onTick() {
        sm.actionAlarm();
        sm.toAlarmingState();
    }

    @Override
    public void updateView() { sm.updateUIRuntime(); }

    @Override
    public int getId() {
        return R.string.ALARMING;
    }
}
