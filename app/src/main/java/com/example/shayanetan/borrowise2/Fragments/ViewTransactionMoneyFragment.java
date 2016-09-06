package com.example.shayanetan.borrowise2.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shayanetan.borrowise2.Adapters.CursorRecyclerViewAdapter;
import com.example.shayanetan.borrowise2.Adapters.PaymentCursorAdapter;
import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.Models.MoneyTransaction;
import com.example.shayanetan.borrowise2.Models.PaymentHistory;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.R;

import org.w3c.dom.Text;

public class ViewTransactionMoneyFragment extends ViewTransactionAbstractFragment {

    private ImageView btn_add_payment;
    private TextView tv_amount, tv_total;
    private MoneyTransaction moneyTransaction;

    private RecyclerView recyclerView;
    private PaymentCursorAdapter paymentCursorAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_view_transaction_money, container, false);
        tv_person_name = (TextView) layout.findViewById(R.id.tv_view_AMPersonName);
        tv_startDate = (TextView) layout.findViewById(R.id.tv_view_money_startDate);
        tv_endDate = (TextView) layout.findViewById(R.id.tv_view_money_endDate);
        tv_type = (TextView) layout.findViewById(R.id.view_money_type);

        tv_amount = (TextView) layout.findViewById(R.id.tv_view_AMAmount);
        btn_add_payment = (ImageView) layout.findViewById(R.id.btn_add_payment_history);
        tv_total = (TextView) layout.findViewById(R.id.tv_view_total);

        paymentCursorAdapter = new PaymentCursorAdapter(getActivity().getBaseContext(), null);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview_payment_history);

        init();

        moneyTransaction = (MoneyTransaction) dbHelper.queryTransaction(trans_id);
        tv_amount.setText(String.valueOf(moneyTransaction.getAmountDeficit()));
        tv_total.setText(String.valueOf(moneyTransaction.getTotalAmountDue()));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(paymentCursorAdapter);
        retreivePayments();

        btn_add_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add payment history
                mListener.updateTransaction(trans_id, TransactionsCursorAdapter.TYPE_MONEY, TransactionsCursorAdapter.BTN_TYPE_PARTIAL);
            }
        });
        return layout;
    }

    public void updateData() {
        moneyTransaction = (MoneyTransaction) dbHelper.queryTransaction(trans_id);
        tv_amount.setText(String.valueOf(moneyTransaction.getAmountDeficit()));
        retreivePayments();
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void retreivePayments() {
        Cursor cursor = null;
        cursor = (Cursor) dbHelper.querryPaymentHistory(trans_id);
        if(cursor != null)
            paymentCursorAdapter.swapCursor(cursor);
    }

}
