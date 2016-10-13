package com.example.shayanetan.borrowise.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shayanetan.borrowise.Adapters.HistoryCursorAdapter;

import edu.mobapde.borroWise.R;

public class ViewUserBorrowedFragment extends Fragment {

    public static String VIEW_TYPE = "borrower_viewtype";

    private RecyclerView recyclerView;
    private HistoryCursorAdapter historyCursorAdapter;

    private OnFragmentInteractionListener mListener;

    public ViewUserBorrowedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        historyCursorAdapter = new HistoryCursorAdapter(getActivity().getBaseContext(),null);
        historyCursorAdapter.setmOnClickListener(new HistoryCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, String type, String classification, String item_name) {

            }
        });

        historyCursorAdapter.setmOnLongClickListener(new HistoryCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, String type, String classification, String item_name) {

            }
        });

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_users_borrowed, container, false);

        //initiate adapter and set recycler view adapter
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerview_users_borrowed);

//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(historyCursorAdapter);
        mListener.retrieveTransaction(historyCursorAdapter, VIEW_TYPE);

        return layout;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener{
        public void deleteTransaction(HistoryCursorAdapter adapter, int id, String type, String classification);
        public void retrieveTransaction(HistoryCursorAdapter adapter, String viewType);
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

}
