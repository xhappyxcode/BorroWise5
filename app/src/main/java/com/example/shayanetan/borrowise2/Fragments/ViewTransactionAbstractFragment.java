package com.example.shayanetan.borrowise2.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.Models.User;
import com.example.shayanetan.borrowise2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Joy on 7/22/2016.
 */
public abstract class ViewTransactionAbstractFragment extends Fragment {

    protected FragmentActivity myContext;
    protected TextView tv_startDate, tv_endDate, tv_person_name, tv_type;
    protected Transaction transaction;
    protected int trans_id;
    protected DatabaseOpenHelper dbHelper;

    protected OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null) {
            trans_id = bundle.getInt(Transaction.COLUMN_ID, 0);
        }
    }

    @Override
    public  abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    public interface OnFragmentInteractionListener{
        //TODO: Update argument type and name
//        public void onAddTransactions(Transaction t);
        public void updateTransaction(int id, int type, int btnType);
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){this.mListener = mListener; }

    public void init() {

        myContext = (FragmentActivity) getActivity();
        dbHelper = new DatabaseOpenHelper(myContext);
        transaction = dbHelper.queryTransaction(trans_id);

        if(transaction.getType().contentEquals(Transaction.BORROWED_ACTION)){
            tv_type.setText(R.string.borrowed_from);
        } else {
            tv_type.setText(R.string.lent_to);
        }
        /* insert person name here */
        User user = dbHelper.queryUser(transaction.getUserID());
        tv_person_name.setText(user.getName());

        tv_startDate.setText(parseMillisToDate(transaction.getStartDate()));
        tv_endDate.setText(parseMillisToDate(transaction.getDueDate()));
        /* set notification settings here */

    }

    public long parseDateToMillis(String toParse){
        long millis=0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy"); // I assume d-M, you may refer to M-d for month-day instead.
            Date date = formatter.parse(toParse); // You will need try/catch around this
            millis = date.getTime();

        } catch (ParseException e){
            e.printStackTrace();
        }
        return millis;
    }

    public String parseMillisToDate(long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        Date resultdate = new Date(millis);
        return sdf.format(resultdate);
    }

}
