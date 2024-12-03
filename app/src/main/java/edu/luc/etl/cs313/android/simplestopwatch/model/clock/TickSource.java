package edu.luc.etl.cs313.android.simplestopwatch.model.clock;

/**
 * A source of onTick events for the stopwatch.
 * This interface is typically implemented by the model.
 *
 * @author laufer
 */
public interface TickSource {
    /**
     * Sets the source for the events that occurs for the stopwatch.
     *
     * @param listener The listener for the onTick events in the stopwatch model.
     * */
    void setTickListener(TickListener listener);
}
