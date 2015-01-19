package rp3.widget;

import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Calendar;

/**
 * You have to make a clone of the file DigitalClock.java to use in your application, modify in the following manner:-
 *      private final static String m12 = "h:mm aa";
 *      private final static String m24 = "k:mm";
 */

public class DigitalClock extends TextView {

    Calendar mCalendar;
    private final static String m12 = "h:mm aa";
    private final static String m24 = "k:mm";

    private Runnable mTicker;
    private Handler mHandler;

    private boolean mTickerStopped = false;

    String mFormat;

    public DigitalClock(Context context) {
        super(context);
        initClock(context);
    }

    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {
        Resources r = context.getResources();
        mFormat = m24;
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
                public void run() {
                    if (mTickerStopped) return;
                    if(mCalendar == null)
                    	mCalendar = Calendar.getInstance();
                    setText(DateFormat.format(mFormat, mCalendar));
                    invalidate();
                    long now = SystemClock.uptimeMillis();
                    long next = now + (1000 - now % 1000);
                    mHandler.postAtTime(mTicker, next);
                }
            };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }
    
    public void setCalendar(Calendar c)
    {
    	mCalendar = c;
    }
    
    public void set24HourFormat()
    {
    	mFormat = m24;
    }
    public void set12HourFormat()
    {
    	mFormat = m24;
    }

}
