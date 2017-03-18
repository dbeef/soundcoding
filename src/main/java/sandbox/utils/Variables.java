package sandbox.utils;

/**
 * Created by dbeef on 18.03.17.
 */
public class Variables {
    public static final int PAUSE_FREQUENCY = 3000;
    public static final int ZERO_FREQUENCY = 2500;
    public static final int ONE_FREQUENCY = 2000;
    public static final int START_END_FREQUENCY = 3500;
    //120 [ms] makes almost 100% chance of correctly understanding, but takes 4800 [ms]
    //to broadcast 7 bits.
    public static final int MESSAGE_INTERVAL = 85;
}
