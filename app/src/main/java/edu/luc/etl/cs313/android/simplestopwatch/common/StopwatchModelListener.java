package edu.luc.etl.cs313.android.simplestopwatch.common;

/**
 * A listener for UI update events.
 * This interface is typically implemented by the adapter, with the
 * events coming from the model.
 *
 * @author laufer
 */
public interface StopwatchModelListener {
    /**
     * Listener method for detecting changes in timeValue.
     *
     * @param timeValue The time value the listener listens for.
     */
    void onTimeUpdate(int timeValue);

    /**
     * Listener method for detecting changes in state.
     *
     * @param stateId The state the listener listens for.
     * */
    void onStateUpdate(int stateId);

    /**
     * Listener method for playing an alarm sound.
     */
    void soundAlarm(final int notification_sound);

    int getUserRuntime();
}
