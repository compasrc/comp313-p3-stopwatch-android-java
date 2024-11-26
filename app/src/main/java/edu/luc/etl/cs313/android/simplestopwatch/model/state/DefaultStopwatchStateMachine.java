package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.common.Constants;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

/**
 * An implementation of the state machine for the stopwatch.
 *
 * @author laufer
 */
public class DefaultStopwatchStateMachine implements StopwatchStateMachine {

    public DefaultStopwatchStateMachine(final TimeModel timeModel, final ClockModel clockModel) {
        this.timeModel = timeModel;
        this.clockModel = clockModel;
    }

    private final TimeModel timeModel;

    private final ClockModel clockModel;

    /**
     * The internal state of this adapter component. Required for the State pattern.
     */
    private StopwatchState state;

    protected void setState(final StopwatchState state) {
        this.state = state;
        listener.onStateUpdate(state.getId());
    }

    private StopwatchModelListener listener;

    @Override
    public void setModelListener(final StopwatchModelListener listener) {
        this.listener = listener;
    }

    // forward event uiUpdateListener methods to the current state
    // these must be synchronized because events can come from the
    // UI thread or the timer thread
    @Override public synchronized void onButton() { state.onButton(); }
    @Override public synchronized void onTick()      { state.onTick(); }

    @Override public void updateUIRuntime() { listener.onTimeUpdate(timeModel.getRuntime()); }

    // model interactions
    @Override public int getRuntime() { return timeModel.getRuntime();}

    // known states
    private final StopwatchState STOPPED     = new StoppedState(this);
    private final StopwatchState RUNNING     = new RunningState(this);
    private final StopwatchState INCREMENTING = new IncrementingState(this);
    private final StopwatchState ALARMING = new AlarmingState(this);

    // transitions
    @Override public void toRunningState()    { setState(RUNNING); }
    @Override public void toStoppedState()    { setState(STOPPED); }
    @Override public void toIncrementingState() { setState(INCREMENTING); }
    @Override public void toAlarmingState() { setState(ALARMING); }

    // actions
    @Override public void actionInit()       { toStoppedState(); actionReset(); }
    @Override public void actionReset()      { timeModel.resetRuntime(); actionUpdateView(); }
    @Override public void actionStart()      { clockModel.start(); }
    @Override public void actionStop()       { clockModel.stop(); }
    @Override public void actionInc()        { timeModel.incRuntime(); actionUpdateView(); }
    @Override public void actionDec()        { timeModel.decRuntime(); actionUpdateView(); }
    @Override public void actionAlarm() { listener.soundAlarm(Constants.DEFAULT_ALARM); }
    @Override public void actionUpdateView() { state.updateView(); }

}
