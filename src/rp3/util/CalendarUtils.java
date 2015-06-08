package rp3.util;

import java.util.Calendar;

/**
 * Created by magno_000 on 28/05/2015.
 */
public class CalendarUtils {

    public static long DayDiff(Calendar c, Calendar e)
    {
        long time = c.getTimeInMillis() - e.getTimeInMillis();
        long dias = time /( 1000 * 60 * 60 * 24);
        return dias;
    }

    public static long DayDiffTruncate(Calendar c, Calendar e)
    {
        long time = c.get(Calendar.DAY_OF_YEAR) - e.get(Calendar.DAY_OF_YEAR);
        return time;
    }
}
