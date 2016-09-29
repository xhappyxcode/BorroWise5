package com.example.shayanetan.borrowise2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.shayanetan.borrowise2.Activities.ViewHistoryActivity;
import com.example.shayanetan.borrowise2.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise2.Models.Transaction;

/**
 * Created by ShayaneTan on 3/16/2016.
 */
public abstract class HistoryAbstractFragment extends Fragment {

    protected OnFragmentInteractionListener mListener;
    protected RecyclerView recyclerView;
    protected HistoryCursorAdapter historyCursorAdapter;
    protected Spinner filter;

    @Override
    public  abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void init(){

        historyCursorAdapter = new HistoryCursorAdapter(getActivity().getBaseContext(), null);

        historyCursorAdapter.setmOnLongClickListener(new HistoryCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, String type, String classification) {
                mListener.deleteTransaction(historyCursorAdapter, id, type, classification);
            }
        });

        historyCursorAdapter.setmOnClickListener(new HistoryCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, String type, String classification) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ViewHistoryActivity.class);
                intent.putExtra(Transaction.COLUMN_ID, id);
                intent.putExtra(Transaction.COLUMN_TYPE, type);

                startActivity(intent);
            }
        });
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void deleteTransaction(HistoryCursorAdapter adapter, int id, String type, String classification);
        public void retrieveTransaction(HistoryCursorAdapter adapter, String type);
        public void retrieveTransaction(HistoryCursorAdapter adapter, String type, String filter);
    }
}
