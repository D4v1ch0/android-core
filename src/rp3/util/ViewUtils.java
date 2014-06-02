package rp3.util;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.content.SimpleGeneralValueAdapter;
import rp3.core.R;
import rp3.data.Message;
import rp3.data.models.GeneralValue;
import rp3.runtime.Session;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public abstract class ViewUtils {

	public static final void setViewText(View rootView, int id, String text) {
		View v = rootView.findViewById(id);
		if (v instanceof TextView) {
			((TextView) v).setText(text);
		} else if (v instanceof Button) {
			((Button) v).setText(text);
		}
	}

	public static final void setViewDefaultDateText(View rootView, int id,
			Date date) {
		setViewText(rootView, id, Format.getDefaultDateFormat(date));
	}

	public static final void setViewDefaultDateText(View rootView, int id,
			Calendar date) {
		setViewText(rootView, id, Format.getDefaultDateFormat(date));
	}

	public static final void setViewDefaultCurrencyText(View rootView, int id,
			double value) {
		setViewText(rootView, id, Format.getDefaultCurrencyFormat(value));
	}

	public static final void setViewDefaultNumberText(View rootView, int id,
			double value) {
		setViewText(rootView, id, Format.getDefaultNumberFormat(value));
	}

	public static final void setTextViewText(View rootView, int id, String text) {
		((TextView) rootView.findViewById(id)).setText(text);
	}

	public static final void setTextViewDefaultDateText(View rootView, int id,
			Date date) {
		((TextView) rootView.findViewById(id)).setText(Format
				.getDefaultDateFormat(date));
	}

	public static final void setTextViewCurrencyText(View rootView, int id,
			double value) {
		((TextView) rootView.findViewById(id)).setText(Format
				.getDefaultCurrencyFormat(value));
	}

	public static final void setTextViewNumberText(View rootView, int id,
			double value) {
		((TextView) rootView.findViewById(id)).setText(Format
				.getDefaultNumberFormat(value));
	}

	public static final void setButtonClickListener(View rootView, int id,
			OnClickListener l) {
		View v = rootView.findViewById(id);
		if (v instanceof Button)
			((Button) v).setOnClickListener(l);
		else if (v instanceof ImageButton)
			((ImageButton) v).setOnClickListener(l);
	}

	public static final void setImageButtonClickListener(View rootView, int id,
			OnClickListener l) {
		((ImageButton) rootView.findViewById(id)).setOnClickListener(l);
	}

	public static final void setViewAdapter(View rootView, int id,
			ListAdapter adapter) {		
		View v = rootView.findViewById(id);
		if(v instanceof ListView)
			((ListView) v).setAdapter(adapter);
		else if(v instanceof GridView)
			((GridView) v).setAdapter(adapter);
	}
		
	public static final void setListViewHeader(View rootView, int id,
			int resHeaderID) {
		View headerView = LayoutInflater.from(Session.getContext()).inflate(
				resHeaderID, null);
		ListView list = ((ListView) rootView.findViewById(id));
		list.addHeaderView(headerView, null, false);
	}

	public static final void setSpinnerAdapter(View rootView, int id,
			SpinnerAdapter adapter) {
		final Spinner sp = (Spinner) rootView.findViewById(id);
		sp.setAdapter(adapter);
		// sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		// {
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int position, long id) {
		// sp.setTag(id);
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// sp.setTag(null);
		// }
		// });
	}

	public static final long getSpinnerSelectedLongID(View rootView, int id) {
		Spinner sp = (Spinner) rootView.findViewById(id);
		return Convert.getLong(sp.getSelectedItemId());
	}

	public static final int getSpinnerSelectedIntID(View rootView, int id) {
		Spinner sp = (Spinner) rootView.findViewById(id);
		return Convert.getInt(sp.getSelectedItemId());
	}

	public static final int getSpinnerSelectedPosition(View rootView, int id) {
		Spinner sp = (Spinner) rootView.findViewById(id);
		return sp.getSelectedItemPosition();
	}	

	public static final String getSpinnerSelectedFieldCursor(View rootView,
			int id, String fieldaName) {
		Spinner sp = (Spinner) rootView.findViewById(id);
		Cursor c = (Cursor) sp.getSelectedItem();
		if (c != null)
			return CursorUtils.getString(c, fieldaName);
		else
			return null;
	}
	
	public static final String getSpinnerGeneralValueSelectedCode(View rootView, int id) {
		Spinner sp = (Spinner) rootView.findViewById(id);
		GeneralValue gv = (GeneralValue)sp.getSelectedItem();
		return gv.getCode();
	}

	public static final void setSpinnerSimpleAdapter(View rootView,
			Context context, int id, String columnName, Cursor c) {
		setSpinnerAdapter(rootView, id, new SimpleCursorAdapter(context,
				R.layout.base_rowlist_simple_spinner, c,
				new String[] { columnName },
				new int[] { R.id.textView_content }, 0));
	}

	public static final void setSpinnerSimpleAdapter(View rootView,
			Context context, int id, List<Object> objects) {
		setSpinnerAdapter(rootView, id, new ArrayAdapter<Object>(context,
				R.layout.base_rowlist_simple_spinner, R.id.textView_content,
				objects));
	}

	public static final void setSpinnerSimpleAdapter(View rootView,
			Context context, int id, Object[] objects) {
		setSpinnerAdapter(rootView, id, new ArrayAdapter<Object>(context,
				R.layout.base_rowlist_simple_spinner, R.id.textView_content,
				objects));
	}

	public static final void setSpinnerSimpleAdapter(View rootView,
			Context context, int id, int arrayResourceID) {
		setSpinnerAdapter(rootView, id, new ArrayAdapter<Object>(context,
				R.layout.base_rowlist_simple_spinner, R.id.textView_content,
				context.getResources().getStringArray(arrayResourceID)));
	}

	public static final SpinnerAdapter getSpinnerAdapter(View rootView, int id) {
		Spinner sp = (Spinner) rootView.findViewById(id);
		return sp.getAdapter();
	}

	public static final void setSpinnerSelectionByPosition(View rootView,
			int id, int position) {
		Spinner sp = (Spinner) rootView.findViewById(id);
		sp.setSelection(position);
	}

	public static final void setSpinnerSelectionByFieldCursor(View rootView,
			int id, String fieldaName, Object selectedValue) {
		Spinner sp = (Spinner) rootView.findViewById(id);
		SpinnerAdapter adapter = sp.getAdapter();

		if (adapter instanceof SimpleCursorAdapter) {
			Cursor c = ((SimpleCursorAdapter) adapter).getCursor();

			if (c.moveToFirst()) {
				do {

					String currentValue = CursorUtils.getString(c, fieldaName);

					if (String.valueOf(selectedValue).equals(currentValue)) {
						sp.setSelection(c.getPosition());
						break;
					}

				} while (c.moveToNext());
			}
		}
	}

	public static final void setSpinnerSelectionById(View rootView, int id,
			long itemId) {
		Spinner sp = (Spinner) rootView.findViewById(id);
		SpinnerAdapter adapter = sp.getAdapter();
				
		for (int i = 0; i < adapter.getCount(); i++) {
			long tempId = adapter.getItemId(i);
			if (tempId == itemId) {
				sp.setSelection(i);
				break;
			}
		}
	}

	public static final void setSpinnerGeneralValueSelection(View rootView, int id, String code){
		Spinner sp = (Spinner) rootView.findViewById(id);
		SimpleGeneralValueAdapter adapter = (SimpleGeneralValueAdapter)sp.getAdapter();

		for (int i = 0; i < adapter.getCount(); i++) {
			String tempId = adapter.getCode(i);
			if (tempId.equals(code)) {
				sp.setSelection(i);
				break;
			}
		}
	}
		
	public static final void setViewOnItemClickListener(View rootView, int id,
			AdapterView.OnItemClickListener l) {
		View v = rootView.findViewById(id);
		if(v instanceof ListView)
			((ListView) v).setOnItemClickListener(l);
		else if(v instanceof GridView)
			((GridView)v).setOnItemClickListener(l);
		else if(v instanceof Spinner)
			((Spinner)v).setOnItemClickListener(l);
	}
	
	public static final void setViewOnItemSelectedListener(View rootView,
			int id, AdapterView.OnItemSelectedListener l) {
		View v = rootView.findViewById(id);
		if(v instanceof ListView)
			((ListView) v).setOnItemSelectedListener(l);
		else if(v instanceof GridView)
			((GridView)v).setOnItemSelectedListener(l);
		else if(v instanceof Spinner)
			((Spinner)v).setOnItemSelectedListener(l);
	}	

	public static final void setListViewChoiceMode(View rootView, int id,
			int choiceMode) {
		((ListView) rootView.findViewById(id)).setChoiceMode(choiceMode);
	}

	public static void setDatePicker(View rootView, int id, Date value) {
		setDatePicker(rootView, id, value, null);
	}

	public static void setDatePicker(View rootView, int id, Calendar value) {
		setDatePicker(rootView, id, value, null);
	}

	public static void setDatePicker(View rootView, int id, Date value,
			OnDateChangedListener l) {
		Calendar cal = Calendar.getInstance();
		if (value != null)
			cal.setTime(value);

		setDatePicker(rootView, id, cal, l);
	}

	public static void setDatePicker(View rootView, int id, Calendar value,
			OnDateChangedListener l) {
		DatePicker dp = (DatePicker) rootView.findViewById(id);
		dp.init(value.get(Calendar.YEAR), value.get(Calendar.MONTH),
				value.get(Calendar.DAY_OF_MONTH), l);
	}

	public static Date getDatePickerDate(View rootView, int id) {
		Calendar cal = getDatePickerCalendar(rootView, id);
		return cal.getTime();
	}

	public static Calendar getDatePickerCalendar(View rootView, int id) {
		DatePicker dp = (DatePicker) rootView.findViewById(id);

		int year = dp.getYear();
		int month = dp.getMonth();
		int day = dp.getDayOfMonth();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);

		return cal;
	}

	public static void setDatePickerAsYearMonth(View rootView, int id){
		DatePicker datePicker = (DatePicker)rootView.findViewById(id);
		setDatePickerAsYearMonth(datePicker);
	}
	
	public static void setDatePickerAsYearMonth(DatePicker datePicker){
		try {
		    Field f[] = datePicker.getClass().getDeclaredFields();
		    for (Field field : f) {
		        if (field.getName().equals("mDayPicker")) {
		            field.setAccessible(true);
		            Object dayPicker = new Object();
		            dayPicker = field.get(datePicker);
		            ((View) dayPicker).setVisibility(View.GONE);
		        }
		    }
		} catch (Exception e) {		    
		}
	}
	
	public static void setDatePickerDialogAsYearMonth(DatePickerDialog dp) {		
		try {

			Field[] datePickerDialogFields = dp.getClass().getDeclaredFields();
			for (Field datePickerDialogField : datePickerDialogFields) {
				if (datePickerDialogField.getName().equals("mDatePicker")) {
					datePickerDialogField.setAccessible(true);
					DatePicker datePicker = (DatePicker) datePickerDialogField
							.get(dp);
					Field datePickerFields[] = datePickerDialogField.getType()
							.getDeclaredFields();
					for (Field datePickerField : datePickerFields) {
						if ("mDayPicker".equals(datePickerField.getName())
								|| "mDaySpinner".equals(datePickerField
										.getName())) {
							datePickerField.setAccessible(true);							
							Object dayPicker = new Object();
							dayPicker = datePickerField.get(datePicker);
							((View) dayPicker).setVisibility(View.GONE);
						}
					}
				}

			}
		} catch (Exception ex) {
		}		
	}

	public static final String getTextViewString(View rootView, int id) {
		return ((TextView) rootView.findViewById(id)).getText().toString();
	}

	public static final double getTextViewDouble(View rootView, int id) {
		return Convert.getDouble(getTextViewString(rootView, id));
	}

	public static final int getTextViewInt(View rootView, int id) {
		return Convert.getInt(getTextViewString(rootView, id));
	}

	public static final void setViewVisibility(View rootView, int id,
			int visibility) {
		rootView.findViewById(id).setVisibility(visibility);
	}

	public static final int getViewVisibility(View rootView, int id) {
		return rootView.findViewById(id).getVisibility();
	}

	public static final void setViewError(View rootView, int id, String text) {
		setViewError(rootView, id, new Message(text));
	}

	public static final void setViewError(View rootView, int id, Message m) {
		View v = rootView.findViewById(id);
		if (v instanceof TextView) {
			((TextView) v).setError(m.getText());
		}
	}

	public static final void setViewPagerAdapter(View rootView, int id,
			PagerAdapter adapter) {
		ViewPager pager = (ViewPager) rootView.findViewById(id);
		pager.setAdapter(adapter);
	}

	public static final void setImageViewBitmapFromInternalStorageAsync(
			View rootView, int id, String fileName) {
		ImageView imageView = (ImageView) rootView.findViewById(id);
		FileUtils.setBitmapFromInternalStorageAsync(imageView, fileName);
	}

	public static final void setImageViewBitmapFromInternalStorageAsync(
			View rootView, int id, String fileName, int reqWidth, int reqHeight) {
		ImageView imageView = (ImageView) rootView.findViewById(id);
		FileUtils.setBitmapFromInternalStorageAsync(imageView, fileName,
				reqWidth, reqHeight);
	}

	public static final void setImageViewBitmapFromResourcesAsync(
			View rootView, int id, int resID) {
		ImageView imageView = (ImageView) rootView.findViewById(id);
		FileUtils.setBitmapFromResourcesAsync(imageView, resID);
	}

	public static final void setImageViewBitmapFromResourcesAsync(
			View rootView, int id, int resID, int reqWidth, int reqHeight) {
		ImageView imageView = (ImageView) rootView.findViewById(id);
		FileUtils.setBitmapFromResourcesAsync(imageView, resID, reqWidth,
				reqHeight);
	}
}
