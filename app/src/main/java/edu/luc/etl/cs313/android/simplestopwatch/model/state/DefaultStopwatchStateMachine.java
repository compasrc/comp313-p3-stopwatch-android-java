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

    /**
     * Constructor method for the state machine. Requires the passive time model and active clock model.
     *
     * @param timeModel The passive time model.
     * @param clockModel The active clock model.
     * */
    public DefaultStopwatchStateMachine(final TimeModel timeModel, final ClockModel clockModel) {
        this.timeModel = timeModel;
        this.clockModel = clockModel;
    }

    /**
     * The passive model for time to be used by the state machine.
     * */
    private final TimeModel timeModel;

    /**
     * The active model for the clock to be used by the state machine.
     * */
    private final ClockModel clockModel;

    /**
     * The internal state of this adapter component. Required for the State pattern.
     */
    private StopwatchState state;

    /**
     * Sets the current state of the state machine.
     *
     * @param state The state to set for the state machine.
     * */
    protected void setState(final StopwatchState state) {
        this.state = state;
        listener.onStateUpdate(state.getId());
    }

    /**
     * The listener which updates the state machine of changes to the UI.
     * */
    private StopwatchModelListener listener;

    /**
     * Sets the listener which updates the state machine of changes to the UI.
     *
     * @param listener The listener to set for the state machine.
     * */
    @Override
    public void setModelListener(final StopwatchModelListener listener) {
        this.listener = listener;
    }

    // forward event uiUpdateListener methods to the current state
    // these must be synchronized because events can come from the
    // UI thread or the timer thread

    /***
     * The method which is forwarded to the current state to describe the behavior when the button in the UI is pressed.
     */
    @Override public synchronized void onButton() { state.onButton(); }

    /**
     * The method which is forwarded to the current state to describe the behavior when a tick passes.
     * */
    @Override public synchronized void onTick()      { state.onTick(); }

    /**
     * Updates the UI with the runtime from the time model.
     * */
    @Override public void updateUIRuntime() { listener.onTimeUpdate(timeModel.getRuntime()); }

    // model interactions
    /**
     * Returns the runtime stored in the time model.
     * */
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

    @Override
    public int getEnteredTime() {
        return listener.getUserRuntime();
    }

    @Override
    public void enterTime(int runtime) {
        timeModel.setRuntime(runtime);
    }
}
