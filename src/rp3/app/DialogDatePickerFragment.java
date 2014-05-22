package rp3.app;

import java.util.Calendar;
import java.util.Date;

import rp3.util.ViewUtils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

@SuppressLint("ValidFragment")
public class DialogDatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	@SuppressLint("ValidFragment")
	public DialogDatePickerFragment(int id, DialogDatePickerChangeListener l, boolean asYearMonth){		
		// Use the current date as the default date in the picker
		this.id = id;
		this.listener = l;
		this.asYearMonth = asYearMonth;
		Calendar c = Calendar.getInstance();
		init(c);
	}
	
	public DialogDatePickerFragment(int id, Calendar c, DialogDatePickerChangeListener l, boolean asYearMonth){
		this.id = id;
		this.listener = l;
		this.asYearMonth = asYearMonth;		
		init(c);
	}
	
	public DialogDatePickerFragment(int id, Date d, DialogDatePickerChangeListener l, boolean asYearMonth){
		this.id = id;
		this.listener = l;
		this.asYearMonth = asYearMonth;
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		init(c);		
	}
	
	public void init(Calendar c){
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
	}
	
	private DialogDatePickerChangeListener listener;
	private int year = 0;
	private int month = 0;
	private int day = 0;
	private int id;
	private boolean asYearMonth;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {		
		// Create a new instance of DatePickerDialog and return it
		DatePickerDialog dp = new DatePickerDialog(getActivity(), this, year, month, day);
		if(asYearMonth){
			ViewUtils.setDatePickerDialogAsYearMonth(dp);
			dp.getDatePicker().setCalendarViewShown(false);
		}		
		return dp;
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);				
		listener.onDailogDatePickerChange(id, c);
	}
}