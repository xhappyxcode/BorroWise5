package com.example.shayanetan.borrowise3.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shayanetan.borrowise3.Activities.ViewTransactionActivity;
import com.example.shayanetan.borrowise3.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise3.Models.Transaction;
import com.example.shayanetan.borrowise3.R;

/*
 *  Edited by Stephanie Dy on 7/16/2016
 *                         on 7/29/2016 replaced onClickListener to transactionCursorAdapter
 */

public class ViewBorrowedFragment extends Fragment {

    public static String VIEW_TYPE = "borrowed_viewtype";
    private String filterType;

    private RecyclerView recyclerView;
    private TransactionsCursorAdapter transactionsCursorAdapter;

    private OnFragmentInteractionListener mListener;

    private Button btn_TBorrowed_all, btn_TBorrowed_item, btn_TBorrowed_money;

    public ViewBorrowedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        filterType = "All";
        transactionsCursorAdapter = new TransactionsCursorAdapter(getActivity().getBaseContext(), null);

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_view_borrowed, container, false);

        //initiate adapter and set recycler view adapter
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerview_transaction_borrowed);

//        transactionsCursorAdapter.setmOnClickListener(new TransactionsCursorAdapter.OnButtonClickListener() {
//            @Override
//            public void onButtonClick(int id, int type, int btnType) {
//                mListener.updateTransaction(id, type, btnType, transactionsCursorAdapter, VIEW_TYPE, filterType);
//                //    mListener.retrieveTransaction(transactionsCursorAdapter,VIEW_TYPE);
//            }
//        });

        /* replaced original onClickListener for adapter */
        transactionsCursorAdapter.setmOnClickListener(new TransactionsCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, int type) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ViewTransactionActivity.class);
                intent.putExtra(Transaction.COLUMN_ID, id);
                intent.putExtra(Transaction.COLUMN_TYPE, Transaction.BORROWED_ACTION);

                startActivity(intent);
            }
        });
        /*      fix the layout size of the recyclerview */
        recyclerView.setHasFixedSize(true);
        /*      due to revision, GridLayoutManager changed into LinearLayoutManager */
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(transactionsCursorAdapter);


        if(filterType.equalsIgnoreCase("All"))
            mListener.retrieveTransaction(transactionsCursorAdapter,VIEW_TYPE);
        else
            mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE, filterType);

        /*********************************************************************/
        btn_TBorrowed_all = (Button) layout.findViewById(R.id.btn_TBorrowed_all);
        btn_TBorrowed_item = (Button) layout.findViewById(R.id.btn_TBorrowed_item);
        btn_TBorrowed_money = (Button) layout.findViewById(R.id.btn_TBorrowed_money);

        //this is just to show the user that all is selected as default
        btn_TBorrowed_all.setBackgroundResource(R.color.accentBlueColor);

        btn_TBorrowed_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = "All";
                mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE);

                btn_TBorrowed_all.setBackgroundResource(R.color.accentBlueColor);
                btn_TBorrowed_item.setBackgroundResource(R.color.text_primaryColor);
                btn_TBorrowed_money.setBackgroundResource(R.color.text_primaryColor);
            }
        });

        btn_TBorrowed_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = Transaction.ITEM_TYPE;
                mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE, Transaction.ITEM_TYPE);

                btn_TBorrowed_item.setBackgroundResource(R.color.accentBlueColor);
                btn_TBorrowed_all.setBackgroundResource(R.color.text_primaryColor);
                btn_TBorrowed_money.setBackgroundResource(R.color.text_primaryColor);
            }
        });

        btn_TBorrowed_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = Transaction.MONEY_TYPE;
                mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE, Transaction.MONEY_TYPE);

                btn_TBorrowed_money.setBackgroundResource(R.color.accentBlueColor);
                btn_TBorrowed_item.setBackgroundResource(R.color.text_primaryColor);
                btn_TBorrowed_all.setBackgroundResource(R.color.text_primaryColor);
            }
        });

        return layout;
    }

    public void resetData() {
        mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE);

//        btn_TBorrowed_all.setBackgroundResource(R.color.accentBlueColor);
//        btn_TBorrowed_item.setBackgroundResource(R.color.text_primaryColor);
//        btn_TBorrowed_money.setBackgroundResource(R.color.text_primaryColor);
    }

    public void onAttach() {
        mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener{
//        public void updateTransaction(int id, int type, int btnType,TransactionsCursorAdapter adapter, String viewType, String filterType);
        public void retrieveTransaction(TransactionsCursorAdapter adapter, String viewType);
        public void retrieveTransaction(TransactionsCursorAdapter adapter, String viewType, String filterType);
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }


}
