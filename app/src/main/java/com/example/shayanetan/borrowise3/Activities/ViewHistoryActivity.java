package com.example.shayanetan.borrowise3.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.shayanetan.borrowise3.Fragments.ViewHistoryAbstractFragment;
import com.example.shayanetan.borrowise3.Fragments.ViewHistoryItemFragment;
import com.example.shayanetan.borrowise3.Fragments.ViewHistoryMoneyFragment;
import com.example.shayanetan.borrowise3.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise3.Models.Transaction;
import com.example.shayanetan.borrowise3.R;

public class ViewHistoryActivity extends BaseActivity
        implements ViewHistoryAbstractFragment.OnFragmentInteractionListener,
        ViewHistoryItemFragment.OnFragmentInteractionListener,
        ViewHistoryMoneyFragment.OnFragmentInteractionListener {

    protected DatabaseOpenHelper dbHelper;
    protected int trans_id;

    protected Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        setTitle(R.string.title_activity_view_history);

        Intent intent = getIntent();
        trans_id = intent.getIntExtra(Transaction.COLUMN_ID, 0);

        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());

        transaction = dbHelper.queryTransaction(trans_id);
        /* put trans_id into bundle to pass to fragment */
        Bundle bundle = new Bundle();
        bundle.putInt(Transaction.COLUMN_ID, trans_id);

        if(transaction.getClassification().contentEquals(Transaction.ITEM_TYPE)) {
            ViewHistoryItemFragment itemFragment = new ViewHistoryItemFragment();
            itemFragment.setArguments(bundle);
            itemFragment.setOnFragmentInteractionListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_view_history_container, itemFragment)
                    .commit();
            overridePendingTransition(0, 0);
        } else {
            ViewHistoryMoneyFragment moneyFragment = new ViewHistoryMoneyFragment();
            moneyFragment.setArguments(bundle);
            moneyFragment.setOnFragmentInteractionListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_view_history_container, moneyFragment)
                    .commit();
            overridePendingTransition(0, 0);
        }

    }

    /* this is irrelevant for now but may be used in the future */
    @Override
    public void updateTransaction(int id, int type, int btnType) {

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
