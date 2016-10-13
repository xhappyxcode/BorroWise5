package com.example.shayanetan.borrowise.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shayanetan.borrowise.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise.Models.Transaction;

import edu.mobapde.borroWise.R;

public class HistoryLendFragment extends HistoryAbstractFragment {

    public Button btn_HLend_all, btn_HLend_item, btn_HLend_money;
    private String filterType;


    public HistoryLendFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        init();
        filterType = "All";

        View layout = inflater.inflate(R.layout.fragment_history_lend, container, false);
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerview_history_lent);

//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(historyCursorAdapter);
        mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_LEND);

        if(filterType.equalsIgnoreCase("All"))
            mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_LEND);
        else
            mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_LEND, filterType);

        btn_HLend_all = (Button) layout.findViewById(R.id.btn_HLend_all);
        btn_HLend_item = (Button) layout.findViewById(R.id.btn_HLend_item);
        btn_HLend_money = (Button) layout.findViewById(R.id.btn_HLend_money);

        btn_HLend_all.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_hover));
        btn_HLend_all.setTextColor(Color.WHITE);

        btn_HLend_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = "All";
                mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_LEND);

                btn_HLend_all.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_hover));
                btn_HLend_all.setTextColor(Color.WHITE);
                btn_HLend_item.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_default));
                btn_HLend_item.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryColor));
                btn_HLend_money.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_default));
                btn_HLend_money.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryColor));
            }
        });

        btn_HLend_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = Transaction.ITEM_TYPE;
                mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_LEND, Transaction.ITEM_TYPE);

                btn_HLend_item.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_hover));
                btn_HLend_item.setTextColor(Color.WHITE);
                btn_HLend_all.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_default));
                btn_HLend_all.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryColor));
                btn_HLend_money.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_default));
                btn_HLend_money.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryColor));
            }
        });

        btn_HLend_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = Transaction.MONEY_TYPE;
                mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_LEND, Transaction.MONEY_TYPE);

                btn_HLend_money.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_hover));
                btn_HLend_money.setTextColor(Color.WHITE);
                btn_HLend_all.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_default));
                btn_HLend_all.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryColor));
                btn_HLend_item.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_default));
                btn_HLend_item.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryColor));

            }
        });

        return layout;
    }

    public void resetData() {
        mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_LEND);
    }

}
