package com.example.shayanetan.borrowise2.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.R;

/**
 * Created by Joy on 7/25/2016.
 */
public class AddTransactionDialogFragment extends DialogFragment {
    View v;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        public void addTransaction(int transactionType);
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener) {
        this.mListener=mListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        v = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.blank_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.add_transaction_dialog)
                .setTitle("Add Transaction")
                .setView(v)
                .setPositiveButton(R.string.dialog_money, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO: go to AddTransactionActivity and use AddMoneyFragment
                        mListener.addTransaction(TransactionsCursorAdapter.TYPE_MONEY);
                    }
                })
                .setNegativeButton(R.string.dialog_item, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO: go to AddTransactionActivity and use AddItemFragment
                        mListener.addTransaction(TransactionsCursorAdapter.TYPE_ITEM);
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog addDialog = builder.create();
        addDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btn_item = (Button) ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                Button btn_money = (Button) ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);

                btn_money.setBackgroundColor(Color.GREEN);
                btn_money.setTextColor(Color.WHITE);
                btn_item.setBackgroundColor(Color.BLUE);
                btn_item.setTextColor(Color.WHITE);
            }
        });
        return addDialog;
    }


    public void showDialog()
    {
        this.show(getFragmentManager(), "");
    }

}
