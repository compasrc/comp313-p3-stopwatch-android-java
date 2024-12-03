package edu.luc.etl.cs313.android.simplestopwatch.model.clock;

/**
 * A listener for onTick events coming from the internal clock model.
 *
 * @author laufer
 */
public interface TickListener {
    /**
     * Listener method for the behavior that must be implemented when a tick occurs.
     * */
    void onTick();
}
