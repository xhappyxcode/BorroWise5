package com.example.shayanetan.borrowise2.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.shayanetan.borrowise2.Activities.ViewTransactionActivity;
import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.R;

/*
 *  Edited by Stephanie Dy on 7/16/2016
 *                         on 7/29/2016
 */

public class ViewLentFragment extends Fragment {

    public static String VIEW_TYPE = "lent_viewtype";

    private RecyclerView recyclerView;
    private String filterType;

    private Spinner filter;
    private TransactionsCursorAdapter transactionsCursorAdapter;

    private OnFragmentInteractionListener mListener;

    private Button btn_TLent_all, btn_TLent_item, btn_TLent_money;

    public ViewLentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        filterType = "All";
        transactionsCursorAdapter = new TransactionsCursorAdapter(getActivity().getBaseContext(), null);
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_view_lent, container, false);

        //initiate adapter and set recycler view adapter
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerview_transaction_lent);

//        transactionsCursorAdapter.setmOnClickListener(new TransactionsCursorAdapter.OnButtonClickListener() {
//            @Override
//            public void onButtonClick(int id, int type, int btnType) {
//                mListener.updateTransaction(id, type, btnType, transactionsCursorAdapter, VIEW_TYPE, filterType);
//              //  mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE);
//            }
//        });
        /* replaced the original OnClickListener of adapter */
        transactionsCursorAdapter.setmOnClickListener(new TransactionsCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, int type) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ViewTransactionActivity.class);
                intent.putExtra(Transaction.COLUMN_ID, id);
                intent.putExtra(Transaction.COLUMN_TYPE, Transaction.LEND_ACTION);
                startActivity(intent);
            }
        });
/*      due to revision, it needs only 1 column */
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(transactionsCursorAdapter);

        if(filterType.equalsIgnoreCase("All"))
            mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE);
        else
            mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE, filterType);


        btn_TLent_all = (Button) layout.findViewById(R.id.btn_TLent_all);
        btn_TLent_item = (Button) layout.findViewById(R.id.btn_TLent_item);
        btn_TLent_money = (Button) layout.findViewById(R.id.btn_TLent_money);

        btn_TLent_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = "All";
                mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE);

                btn_TLent_all.setBackgroundResource(R.color.accentBlueColor);
                btn_TLent_money.setBackgroundResource(R.color.text_primaryColor);
                btn_TLent_item.setBackgroundResource(R.color.text_primaryColor);
            }
        });

        btn_TLent_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = Transaction.ITEM_TYPE;
                mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE, Transaction.ITEM_TYPE);

                btn_TLent_item.setBackgroundResource(R.color.accentBlueColor);
                btn_TLent_all.setBackgroundResource(R.color.text_primaryColor);
                btn_TLent_money.setBackgroundResource(R.color.text_primaryColor);

            }
        });

        btn_TLent_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = Transaction.MONEY_TYPE;
                mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE, Transaction.MONEY_TYPE);

                btn_TLent_money.setBackgroundResource(R.color.accentBlueColor);
                btn_TLent_item.setBackgroundResource(R.color.text_primaryColor);
                btn_TLent_all.setBackgroundResource(R.color.text_primaryColor);
            }
        });

        return layout;
    }

    public void resetData() {
        mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE);
//        btn_TLent_all.setBackgroundResource(R.color.accentBlueColor);
//        btn_TLent_money.setBackgroundResource(R.color.text_primaryColor);
//        btn_TLent_item.setBackgroundResource(R.color.text_primaryColor);
    }

    public void onAttach() {
        mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void setTransactionsCursorAdapter(TransactionsCursorAdapter transactionsCursorAdapter){
        this.transactionsCursorAdapter = transactionsCursorAdapter;
    }
    public interface OnFragmentInteractionListener{

//        public void updateTransaction(int id, int type, int btnType, TransactionsCursorAdapter adapter, String viewType, String filterType);
        public void retrieveTransaction(TransactionsCursorAdapter adapter, String viewType);
        public void retrieveTransaction(TransactionsCursorAdapter adapter, String viewType, String filterType);
    }
    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

}
