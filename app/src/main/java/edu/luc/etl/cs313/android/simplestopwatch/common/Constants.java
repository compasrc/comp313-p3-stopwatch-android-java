package edu.luc.etl.cs313.android.simplestopwatch.common;

/**
 * Constants for the time calculations used by the stopwatch.
 */
public enum Constants {

    ;

    /**
     * Constant defined for the maximum number of seconds the timer can hold.
     */
    public static final int SEC_MAX = 99;

    /**
     * Constant defined as how many seconds to add to the timer when one clock tick passes.
     */
    public static final int SEC_PER_TICK = 1;

    /**
    * Constant defined as how many seconds in a minute (unused).
    */
    public static final int SEC_PER_MIN = 60;

    /**
     * Constant defined as how many seconds in an hour (unused).
     */
    public static final int SEC_PER_HOUR = 3600;

    /**
     * Constant defined as the time to wait before starting the stopwatch.
     */
    public static final int TICK_WAIT = 3;
}