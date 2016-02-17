package rp3.util;

/**
 * Created by magno_000 on 09/07/2015.
 */
public class StringUtils {
    public static String getStringCapSentence(String text)
    {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static String centerStringInLine(String text, int spaces){
        if(text.length() >= spaces)
            return text + '\n';
        else {
            int whiteSpaces = spaces - text.length();
            if (whiteSpaces % 2 == 1) {
                whiteSpaces = whiteSpaces - 1;
            }
            int spacesToCenter = whiteSpaces / 2;
            for (int i = 1; i <= spacesToCenter; i++)
                text = " " + text + " ";
            return text + '\n';
        }
    }

    public static String rightStringInSpace(String text, int spaces)
    {
        if(text.length() >= spaces)
            return text;
        else
        {
            int whitespaces = spaces - text.length();
            for(int i = 1; i <= whitespaces; i++)
                text = " " + text;
            return text;
        }
    }

    public static String leftStringInSpace(String text, int spaces)
    {
        if(text.length() >= spaces)
            return text;
        else
        {
            int whitespaces = spaces - text.length();
            for(int i = 1; i <= whitespaces; i++)
                text = text + " ";
            return text;
        }
    }
}
