package rp3.util;

/**
 * Created by magno_000 on 22/04/2016.
 */
public class NumberUtils {
    public static double Round(double value, int precision)
    {
        double factor = Math.pow(10,precision);
        value = value * factor;
        value = Math.round(value);
        value = value / factor;
        return value;
    }
}
