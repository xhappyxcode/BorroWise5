package com.example.shayanetan.borrowise3.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shayanetan.borrowise3.Adapters.PaymentCursorAdapter;
import com.example.shayanetan.borrowise3.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise3.Models.MoneyTransaction;
import com.example.shayanetan.borrowise3.R;

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

        tv_notif_days = (TextView) layout.findViewById(R.id.tv_mnotif_days_before);
        tv_notif_time = (TextView) layout.findViewById(R.id.tv_mnotif_time);

        paymentCursorAdapter = new PaymentCursorAdapter(getActivity().getBaseContext(), null);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview_payment_history);

        init();

        moneyTransaction = (MoneyTransaction) dbHelper.queryTransaction(trans_id);
        tv_amount.setText(String.format("%.2f", moneyTransaction.getAmountDeficit()));
        tv_total.setText(String.format("%.2f", moneyTransaction.getTotalAmountDue()));

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
