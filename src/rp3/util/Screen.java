package rp3.util;

import rp3.runtime.Session;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

public class Screen {
	
	public static final int ORIENTATION_PORTRAIT = 1;
	public static final int ORIENTATION_LANDSCAPE = 2;
	
	public static int getOrientation(){
		return Session.getContext().getResources().getConfiguration().orientation;
	}
	
	public static int getRequestOrientationFromCurrentScreen(){
		switch (getOrientation()) {
		case android.content.res.Configuration.ORIENTATION_PORTRAIT:
			return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		case android.content.res.Configuration.ORIENTATION_LANDSCAPE:
			return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			}
		return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	}

	public static boolean isMinNormalLayoutSize(Context c) {
		int screenSize = getScreenLayoutSize(c);
		return screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL
				|| screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE
				|| screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	public static boolean isMinLargeLayoutSize(Context c) {
		int screenSize = getScreenLayoutSize(c);
		return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE
				|| screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	public static boolean isSmallLayoutSize(Context c) {
		int screenSize = getScreenLayoutSize(c);
		return screenSize == Configuration.SCREENLAYOUT_SIZE_SMALL;
	}

	public static boolean isNormalLayoutSize(Context c) {
		int screenSize = getScreenLayoutSize(c);
		return screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL;
	}

	public static boolean isLargeLayoutSize(Context c) {
		int screenSize = getScreenLayoutSize(c);
		return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isXLargeLayoutSize(Context c) {
		int screenSize = getScreenLayoutSize(c);
		return screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	public static int getScreenLayoutSize(Context c) {
		int screenSize = c.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		return screenSize;
	}

	public static boolean isLowDensity(Context c) {
		int density = getDensity(c);
		return density == DisplayMetrics.DENSITY_LOW;
	}

	public static boolean isMediumDensity(Context c) {
		int density = getDensity(c);
		return density == DisplayMetrics.DENSITY_MEDIUM;
	}

	public static boolean isHighDensity(Context c) {
		int density = getDensity(c);
		return density == DisplayMetrics.DENSITY_HIGH;
	}

	public static boolean isXHighDensity(Context c) {
		int density = getDensity(c);
		return density == DisplayMetrics.DENSITY_XHIGH;
	}

	@SuppressLint("InlinedApi")
	public static boolean isXXHighDensity(Context c) {
		int density = getDensity(c);
		return density == DisplayMetrics.DENSITY_XXHIGH;
	}

	@SuppressLint("InlinedApi")
	public static boolean isXXXHighDensity(Context c) {
		int density = getDensity(c);
		return density == DisplayMetrics.DENSITY_XXXHIGH;
	}

	public static boolean isDefaultDensity(Context c) {
		int density = getDensity(c);
		return density == DisplayMetrics.DENSITY_DEFAULT;
	}

	@SuppressLint("NewApi")
	public static int getDensity(Context c) {
		int density = c.getResources().getConfiguration().densityDpi;
		return density;
	}

}
