package com.example.shayanetan.borrowise3.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.shayanetan.borrowise3.Adapters.PaymentCursorAdapter;
import com.example.shayanetan.borrowise3.Models.MoneyTransaction;
import com.example.shayanetan.borrowise3.R;

public class ViewHistoryMoneyFragment extends ViewHistoryAbstractFragment {

    private TextView tv_amount, tv_total;

    private MoneyTransaction moneyTransaction;
    private RecyclerView recyclerView;
    private PaymentCursorAdapter paymentCursorAdapter;

    private OnFragmentInteractionListener mListener;

    public ViewHistoryMoneyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_view_history_money, container, false);

        tv_person_name = (TextView) layout.findViewById(R.id.tv_Hview_AMPersonName);
        tv_startDate = (TextView) layout.findViewById(R.id.tv_Hview_money_startDate);
        tv_endDate = (TextView) layout.findViewById(R.id.tv_Hview_money_endDate);
        tv_retDate = (TextView) layout.findViewById(R.id.tv_Hview_money_retDate);
        tv_status = (TextView) layout.findViewById(R.id.tv_Hview_money_status);
        tv_type = (TextView) layout.findViewById(R.id.Hview_money_type);
        rb_rating = (RatingBar) layout.findViewById(R.id.rb_Hview_rating_money);

        tv_amount = (TextView) layout.findViewById(R.id.tv_Hview_AMAmount);
        paymentCursorAdapter = new PaymentCursorAdapter(getActivity().getBaseContext(), null);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview_Hpayment_history);

        init();

        moneyTransaction = (MoneyTransaction) dbHelper.queryTransaction(trans_id);
        tv_amount.setText(String.format("%.2f", moneyTransaction.getTotalAmountDue()));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(paymentCursorAdapter);
        retreivePayments();

        return layout;
    }

    private void retreivePayments() {
        Cursor cursor = null;
        cursor = (Cursor) dbHelper.querryPaymentHistory(trans_id);
        if(cursor != null)
            paymentCursorAdapter.swapCursor(cursor);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
