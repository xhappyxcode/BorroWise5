package Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.R;

/**
 * Created by Joy on 8/19/2016.
 */
public class PaymentErrorDialogFragment extends DialogFragment {
    View v;

    private TransactionsCursorAdapter transactionsCursorAdapter;
    private String viewType;
    private String filterType;

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public void setTransactionsCursorAdapter(TransactionsCursorAdapter transactionsCursorAdapter) {
        this.transactionsCursorAdapter = transactionsCursorAdapter;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        v = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.blank_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.payment_error_dialog)
                .setTitle("PAYMENT ERROR")
                .setView(v)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    public void showDialog()
    {
        this.show(getFragmentManager(), "");
    }

}
