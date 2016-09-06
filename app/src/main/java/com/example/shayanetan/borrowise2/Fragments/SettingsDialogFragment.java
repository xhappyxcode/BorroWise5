package com.example.shayanetan.borrowise2.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.R;

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

    public interface OnFragmentInteractionListener{
        public void updateNotification(long alarmTime, int daysLeft);
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public void setTransactionsCursorAdapter(TransactionsCursorAdapter transactionsCursorAdapter) {
        this.transactionsCursorAdapter = transactionsCursorAdapter;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
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

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView time = (TextView) v.findViewById(R.id.tv_notif_time);
                Spinner day = (Spinner) v.findViewById(R.id.spnr_NOTIF_days);

                String t = time.getText().toString();
                String d = day.getSelectedItem().toString();

                tv_time.setText(t);
                tv_day.setText(d);

                long alarmTime = Long.parseLong(t);
                int daysLeft = Integer.parseInt(d);

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
}
