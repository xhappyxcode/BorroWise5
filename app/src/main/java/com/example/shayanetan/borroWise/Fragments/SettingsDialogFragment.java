package com.example.shayanetan.borrowise.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.shayanetan.borrowise.Adapters.TransactionsCursorAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.mobapde.borroWise.R;

/**
 * Created by Reanna Chelsey Lim on 29 Jul 2016.
 */
public class SettingsDialogFragment extends DialogFragment {

    View v;
    Button btn_save, btn_cancel;
    private TextView tv_time, tv_day;
    private TransactionsCursorAdapter transactionsCursorAdapter;
    private String viewType;
    private String filterType;
    private OnFragmentInteractionListener mListener;
    private TextView time;
    private Spinner day;
    private String t, d;

    public interface OnFragmentInteractionListener{
        public void updateNotification(String alarmTime, int daysLeft);
        public void onTimeDialog();
    }
/*
    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public void setTransactionsCursorAdapter(TransactionsCursorAdapter transactionsCursorAdapter) {
        this.transactionsCursorAdapter = transactionsCursorAdapter;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }*/

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

    public void setTv_time(TextView tv_time){
        this.tv_time = tv_time;
    }

    public void setTv_day(TextView tv_day){
        this.tv_day = tv_day;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        v = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.notification_settings_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Notification Settings")
                .setView(v);

        btn_save = (Button) v.findViewById(R.id.btn_save);
        btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
        time = (TextView) v.findViewById(R.id.tv_btn_notif_time);
        day = (Spinner) v.findViewById(R.id.spnr_notif_days);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.array_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(adapter);

        t = "12:00 PM"; //by default: 12:00 PM
        d = "0"; //by default: 0 days
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onTimeDialog();
            }
        });

        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                d = day.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alarmTime = "";
                int daysLeft = Integer.parseInt(d);


               // return (String) DateFormat.format(delegate,Calendar.getInstance().getTime());

                SimpleDateFormat string_to_date_format = new SimpleDateFormat("HH:mm");
                SimpleDateFormat date_to_string_format = new SimpleDateFormat("HH:mm aaa");
                try {
                    Date date = string_to_date_format.parse(time.getText().toString());
                    alarmTime = date_to_string_format.format(date);
                    time.setText(alarmTime);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                mListener.updateNotification(alarmTime, daysLeft);

                dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return dialogBuilder.create();
    }

    public void showDialog()
    {
        this.show(getFragmentManager(), "");

    }

    public void setResultTime(String resultTime) {
        time.setText(resultTime);
    }
}
