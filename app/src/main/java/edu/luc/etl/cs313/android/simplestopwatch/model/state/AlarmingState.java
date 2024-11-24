package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class AlarmingState implements StopwatchState {

    public AlarmingState(final StopwatchSMStateView sm) {this.sm = sm;}

    private final StopwatchSMStateView sm;

    @Override
    public void onButton() {
        sm.actionStop();
        sm.actionInit();
    }

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
