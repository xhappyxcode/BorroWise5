package com.example.shayanetan.borrowise2.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.shayanetan.borrowise2.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise2.R;

/**
 * Created by Joy on 7/22/2016.
 */

public class DeleteDialogFragment extends DialogFragment {

    View v;

    private OnFragmentInteractionListener mListener;
    private int id;
    private String type;
    private String classification;
    private HistoryCursorAdapter historyCursorAdapter;

    public void setType(String type) {
        this.type = type;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHistoryCursorAdapter(HistoryCursorAdapter historyCursorAdapter) {
        this.historyCursorAdapter = historyCursorAdapter;
    }

    public interface OnFragmentInteractionListener {
        public void deleteDialog(HistoryCursorAdapter historyCursorAdapter, int id, String type, String classification);
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        v = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.blank_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_dialog)
                .setTitle("Delete Transaction")
                .setView(v)
                .setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: delete transaction, pass the transaction id here
                        mListener.deleteDialog(historyCursorAdapter, id, type, classification);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User cancelled the the deletion of transaction
                        dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button deleteButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                Button cancelButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);

                deleteButton.setBackgroundColor(Color.GREEN);
                deleteButton.setTextColor(Color.WHITE);
                cancelButton.setBackgroundColor(Color.RED);
                cancelButton.setTextColor(Color.WHITE);
            }
        });
        return dialog;
    }


    public void showDialog()
    {
        this.show(getFragmentManager(), "");
    }
}
