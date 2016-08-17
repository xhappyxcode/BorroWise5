package com.example.shayanetan.borrowise2.Activities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.Fragments.AmountDialogFragment;
import com.example.shayanetan.borrowise2.Fragments.DeleteDialogFragment;
import com.example.shayanetan.borrowise2.Fragments.RatingDialogFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewBorrowedFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewLentFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewTransactionAbstractFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewTransactionItemFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewTransactionMoneyFragment;
import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.Models.ItemTransaction;
import com.example.shayanetan.borrowise2.Models.MoneyTransaction;
import com.example.shayanetan.borrowise2.Models.PaymentHistory;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.R;

/*
 *
 */

public class ViewTransactionActivity extends BaseActivity
        implements DeleteDialogFragment.OnFragmentInteractionListener,
        AmountDialogFragment.OnFragmentInteractionListener,
        RatingDialogFragment.OnFragmentInteractionListener,
        ViewTransactionAbstractFragment.OnFragmentInteractionListener {

    ImageView btn_edit, btn_delete;
    protected DatabaseOpenHelper dbHelper;
    protected int trans_id;

    protected Transaction transaction;
    private int TempID, currType, currBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);
        Intent intent = getIntent();
        trans_id = intent.getIntExtra(Transaction.COLUMN_ID, 0);

        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());

//        btn_edit = findViewById(R.id.btn_edit);
//        btn_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO: allow edit of some things
//            }
//        });
//
//        btn_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DeleteDialogFragment dialogFragment = new DeleteDialogFragment();
//                dialogFragment.showDialog();
//
//            }
//        });

        transaction = dbHelper.queryTransaction(trans_id);
        /* put trans_id into bundle to pass to fragment */
        Bundle bundle = new Bundle();
        bundle.putInt(Transaction.COLUMN_ID, trans_id);

        if(transaction.getClassification().contentEquals(Transaction.ITEM_TYPE)) {
//            ItemTransaction itemTransaction = (ItemTransaction) dbHelper.queryTransaction(trans_id);
            ViewTransactionItemFragment itemFragment = new ViewTransactionItemFragment();
            itemFragment.setArguments(bundle);
            itemFragment.setOnFragmentInteractionListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_view_transaction_container, itemFragment)
                    .commit();
        } else {
            ViewTransactionMoneyFragment moneyFragment = new ViewTransactionMoneyFragment();
            moneyFragment.setArguments(bundle);
            moneyFragment.setOnFragmentInteractionListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_view_transaction_container, moneyFragment)
                    .commit();
        }

    }


    @Override
    public void deleteDialog() {
        // delete transaction
        Toast toast = new Toast(getBaseContext());
        toast.setText("Transaction successfully deleted");
        toast.setDuration(Toast.LENGTH_SHORT);
        finish();
    }

    @Override
    public void updateAmount(TransactionsCursorAdapter adapter, String viewType, String filterType, double partialAmt) {


        MoneyTransaction m = (MoneyTransaction) dbHelper.queryTransaction(trans_id);
        double computedAmount = m.getAmountDeficit() - partialAmt;
        m.setAmountDeficit(computedAmount);
        m.setReturnDate(System.currentTimeMillis());
        m.setStatus(0);

        //  Toast.makeText(this, "Deficit " + m.getAmountDeficit(), Toast.LENGTH_LONG).show();

        if(m.getAmountDeficit() < 0){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getBaseContext());
            alertDialogBuilder.setTitle("PAYMENT ERROR");
            alertDialogBuilder.setMessage("The payment is greater than the balance needed to be paid!")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismissDialog(which);
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else if(m.getAmountDeficit() == 0) {
            RatingDialogFragment df = new RatingDialogFragment();
            df.setOnFragmentInteractionListener(this);
            df.setTransactionsCursorAdapter(adapter);
            df.setViewType(viewType);
            df.setFilterType(filterType);
            df.show(getFragmentManager(), "");

            MoneyTransaction m2 = (MoneyTransaction) dbHelper.queryTransaction(trans_id);
            m2.setAmountDeficit(m.getAmountDeficit() - partialAmt);
            m2.setReturnDate(System.currentTimeMillis());
            m2.setStatus(1);
            Log.v("BEFORE TEMPID: ", ""+trans_id);
            trans_id = dbHelper.updateTransaction(m2);
            Log.v("AFTER TEMPID: ",""+trans_id);
            finish();
        } else {
            trans_id = dbHelper.updateTransaction(m);
            PaymentHistory payment = new PaymentHistory();
            payment.setTransaction_id(trans_id);
            payment.setDate(System.currentTimeMillis());
            payment.setPayment(partialAmt);
            long temp = dbHelper.insertPaymentHistory(payment);
        }

//        if(filterType.equalsIgnoreCase("All"))
//            retrieveTransaction(adapter, viewType);
//        else
//            retrieveTransaction(adapter, viewType, filterType);
        Toast toast = new Toast(getBaseContext());
        toast.setText("Successfully added payment");
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }



    @Override
    public void updateRating(TransactionsCursorAdapter adapter, String viewType, String filterType, double rating) {
        int transID = 0;
        switch (currType) {
            case TransactionsCursorAdapter.TYPE_MONEY:
                Log.v("rating TRANS ID!!!!!! ",""+TempID);
                //     Toast.makeText(getBaseContext(), "rating TRANS ID!!!!!! " +TempID, Toast.LENGTH_LONG).show();
                if(currBtn == TransactionsCursorAdapter.BTN_TYPE_RETURN) {
                    MoneyTransaction m = (MoneyTransaction) dbHelper.queryTransaction(TempID);
                    m.setAmountDeficit(m.getAmountDeficit());
                    m.setReturnDate(System.currentTimeMillis());
                    m.setStatus(1);
                    m.setRate(rating);
                    transID = dbHelper.updateTransaction(m);
                }else{
                    //Toast.makeText(this, "PARTIALS NOT YET SUPPORTED", Toast.LENGTH_SHORT).show();
                    MoneyTransaction m = (MoneyTransaction) dbHelper.queryTransaction(TempID);
                    m.setRate(rating);
                    transID = dbHelper.updateTransaction(m);
                }
                break;
            case TransactionsCursorAdapter.TYPE_ITEM:
                currType = TransactionsCursorAdapter.TYPE_ITEM;
                if(currBtn == TransactionsCursorAdapter.BTN_TYPE_RETURN) {

                    ItemTransaction i = (ItemTransaction) dbHelper.queryTransaction(TempID);
                    i.setReturnDate(System.currentTimeMillis());
                    i.setStatus(1);
                    i.setRate(rating);
                    transID = dbHelper.updateTransaction(i);
                }else{
                    ItemTransaction i = (ItemTransaction) dbHelper.queryTransaction(TempID);
                    i.setStatus(-1);
                    i.setRate(rating);
                    i.setReturnDate(System.currentTimeMillis());
                    transID = dbHelper.updateTransaction(i);
                }
                break;
        }
        Transaction transaction = dbHelper.queryTransaction(transID);
        dbHelper.updateUserRating(transaction.getUserID());
//        if(filterType.equalsIgnoreCase("All"))
//            retrieveTransaction(adapter, viewType);
//        else
//            retrieveTransaction(adapter, viewType, filterType);
        finish();
    }

    @Override
    public void updateTransaction(int id, int type, int btnType) {
        this.currBtn = btnType;
        switch (type) {
            case TransactionsCursorAdapter.TYPE_MONEY:
                currType = TransactionsCursorAdapter.TYPE_MONEY;
                break;
            case TransactionsCursorAdapter.TYPE_ITEM:
                currType = TransactionsCursorAdapter.TYPE_ITEM;
        }
        this.TempID = id;
        Log.v("update TRANS ID!!!!!! ", "" + id);
        //  Toast.makeText(getBaseContext(), "update TRANS ID!!!!!! " +id, Toast.LENGTH_LONG).show();

        if (currBtn == TransactionsCursorAdapter.BTN_TYPE_PARTIAL) {

            AmountDialogFragment df = new AmountDialogFragment();
            df.setOnFragmentInteractionListener(this);
            df.show(getFragmentManager(), "");
        } else {
            RatingDialogFragment df = new RatingDialogFragment();
            df.setOnFragmentInteractionListener(this);
            df.show(getFragmentManager(), "");

        }

    }

}
