package rp3.util;

/**
 * Created by magno_000 on 09/07/2015.
 */
public class StringUtils {
    public static String getStringCapSentence(String text)
    {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
