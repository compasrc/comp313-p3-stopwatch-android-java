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
     * Describes the behavior of RunningState
     * */
    @Override
    public void onButton() {
        sm.actionStop();
        sm.actionInit();
        sm.actionAlarm();
        sm.toStoppedState();
    }

    @Override
    public void onTick() {
        if (sm.getRuntime() == 0) {
            sm.toAlarmingState();
        } else {
            sm.actionDec();
            sm.toRunningState();
        }
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.RUNNING;
    }
}
