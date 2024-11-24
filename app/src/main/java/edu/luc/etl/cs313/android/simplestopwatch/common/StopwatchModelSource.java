package edu.luc.etl.cs313.android.simplestopwatch.common;

/**
 * A source of UI update events for the stopwatch.
 * This interface is typically implemented by the model.
 *
 * @author laufer
 */
public interface StopwatchModelSource {
    /**
     * The method by which we can set the listener for the UI.
     *
     * @param listener The listener we set for the stopwatch.
     * */
    void setModelListener(StopwatchModelListener listener);
}
