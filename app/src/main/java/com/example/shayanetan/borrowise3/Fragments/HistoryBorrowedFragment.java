package com.example.shayanetan.borrowise3.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shayanetan.borrowise3.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise3.Models.Transaction;
import com.example.shayanetan.borrowise3.R;

public class HistoryBorrowedFragment extends HistoryAbstractFragment {

    public Button btn_HBorrowed_all, btn_HBorrowed_item, btn_HBorrowed_money;
    private String filterType;


    public HistoryBorrowedFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        init();
        filterType = "All";

        View layout = inflater.inflate(R.layout.fragment_history_borrowed, container, false);
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerview_history_borrowed);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(historyCursorAdapter);
        mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED);

        if(filterType.equalsIgnoreCase("All"))
            mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED);
        else
            mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED, filterType);

        btn_HBorrowed_all = (Button) layout.findViewById(R.id.btn_HBorrowed_all);
        btn_HBorrowed_item = (Button) layout.findViewById(R.id.btn_HBorrowed_item);
        btn_HBorrowed_money = (Button) layout.findViewById(R.id.btn_HBorrowed_money);

        btn_HBorrowed_all.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_hover));
        btn_HBorrowed_all.setTextColor(Color.WHITE);

        btn_HBorrowed_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = "All";
                mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED);

                btn_HBorrowed_all.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_hover));
                btn_HBorrowed_all.setTextColor(Color.WHITE);
                btn_HBorrowed_item.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_default));
                btn_HBorrowed_item.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryColor));
                btn_HBorrowed_money.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_default));
                btn_HBorrowed_money.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryColor));
            }
        });

        btn_HBorrowed_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = Transaction.ITEM_TYPE;
                mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED, Transaction.ITEM_TYPE);

                btn_HBorrowed_item.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_hover));
                btn_HBorrowed_item.setTextColor(Color.WHITE);
                btn_HBorrowed_all.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_default));
                btn_HBorrowed_all.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryColor));
                btn_HBorrowed_money.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_default));
                btn_HBorrowed_money.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryColor));
            }
        });

        btn_HBorrowed_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = Transaction.MONEY_TYPE;
                mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED, Transaction.MONEY_TYPE);
                btn_HBorrowed_money.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_hover));
                btn_HBorrowed_money.setTextColor(Color.WHITE);
                btn_HBorrowed_all.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_default));
                btn_HBorrowed_all.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryColor));
                btn_HBorrowed_item.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_button_default));
                btn_HBorrowed_item.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryColor));
            }
        });

        return layout;
    }

    public void resetData() {
        mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED);
    }


}
