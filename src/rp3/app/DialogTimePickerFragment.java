package rp3.app;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import rp3.util.ViewUtils;

/**
 * Created by magno_000 on 27/04/2015.
 */
public class DialogTimePickerFragment extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener {

    @SuppressLint("ValidFragment")
    public DialogTimePickerFragment(int id, DialogTimePickerChangeListener l){
        // Use the current date as the default date in the picker
        this.id = id;
        this.listener = l;
    }

    public DialogTimePickerFragment(int id, DialogTimePickerChangeListener l, int interval){
        this.id = id;
        this.listener = l;
        this.interval = interval;
    }

    public DialogTimePickerFragment(int id, DialogTimePickerChangeListener l, int hour, int minute){
        this.id = id;
        this.listener = l;
        this.hour = hour;
        this.minute = minute;
    }

    public DialogTimePickerFragment(int id, DialogTimePickerChangeListener l, int hour, int minute, int interval){
        this.id = id;
        this.listener = l;
        this.hour = hour;
        this.minute = minute;
        this.interval = interval;
    }

    private DialogTimePickerChangeListener listener;
    private int hour = 0;
    private int minute = 0;
    private int interval = 1;
    private int id;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
        TimePickerDialog tp = new TimePickerDialog(getActivity(), this, hour, minute, true);
        if(interval > 1){
            //Ingresar aqui intervalo
        }
        return tp;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i2) {
        listener.onDailogTimePickerChange(id, i, i2);
    }
}
