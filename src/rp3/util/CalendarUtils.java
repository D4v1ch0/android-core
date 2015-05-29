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
}
