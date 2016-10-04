package com.example.shayanetan.borrowise3.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by ShayaneTan on 3/12/2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    //private TextView tv_alarm;

    public TimePickerFragment(){}

    private String resultTime;
    private OnFragmentInteractionListener mListener;


    /*public void setTv_alarm(TextView tv_alarm) {
        this.tv_alarm = tv_alarm;
    }*/

    public interface OnFragmentInteractionListener{
        public void getTime(String time);
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

            String hourString = "";
            String result ="";

            if(hourOfDay == 0)
                hourString = "24";
            else
                hourString = String.valueOf(hourOfDay);

            if(minute>=0 && minute <=9)
                result = hourString +":0"+minute;
            else
                result = hourString+":"+minute;

        //tv_alarm.setText(result);
        //if(mListener != null)
        mListener.getTime(result);
        //else
            //Log.v("mListener", "null");
        resultTime = result;

    }

    public OnFragmentInteractionListener getmListener(){
        return mListener;
    }

    public String getResultTime(){
        return resultTime;
    }

    public void showDialog(){
        this.show(getFragmentManager(), "TimePicker");
    }
}
