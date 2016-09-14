package com.example.shayanetan.borrowise2.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.Toast;

import com.example.shayanetan.borrowise2.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise2.Adapters.ViewPagerAdapter;
import com.example.shayanetan.borrowise2.Fragments.DeleteDialogFragment;
import com.example.shayanetan.borrowise2.Fragments.HistoryAbstractFragment;
import com.example.shayanetan.borrowise2.Fragments.HistoryBorrowedFragment;
import com.example.shayanetan.borrowise2.Fragments.HistoryLendFragment;
import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.R;
import com.example.shayanetan.borrowise2.Views.SlidingTabLayout;

/*
 * Edited by Stephanie Dy on 7/20/2016 added onResume to close drawer
 */

public class HistoryActivity extends BaseActivity  implements HistoryAbstractFragment.OnFragmentInteractionListener,
        DeleteDialogFragment.OnFragmentInteractionListener {
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SlidingTabLayout slidingTabLayout;

    private static String TITLE_TAB1 = "BORROWED FROM";
    private static String TITLE_TAB2 = "LENT TO";

    private HistoryBorrowedFragment historyBorrowedFragment;
    private HistoryLendFragment historyLendFragment;

    private DatabaseOpenHelper dbHelper;

    public HistoryActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle(R.string.title_activity_history);

        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());

        viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.Hview_pager);

        historyBorrowedFragment = new HistoryBorrowedFragment();
        historyBorrowedFragment.setOnFragmentInteractionListener(this);

        historyLendFragment = new HistoryLendFragment();
        historyLendFragment.setOnFragmentInteractionListener(this);

        viewPagerAdapter.addFragment(historyBorrowedFragment, TITLE_TAB1);
        viewPagerAdapter.addFragment(historyLendFragment, TITLE_TAB2);
        viewPager.setAdapter(viewPagerAdapter);

        slidingTabLayout = (SlidingTabLayout)findViewById(R.id.Hsliding_tab);
        // True if Tabs are same Width
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

    }

    public void onResume() {
        super.onResume();
        historyBorrowedFragment.resetData();
        historyLendFragment.resetData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
//        //menu.findItem(R.id.action_settings).setVisible(false);
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void deleteTransaction(HistoryCursorAdapter adapter, int id, String type, String classification) {
        DeleteDialogFragment deleteDialog = new DeleteDialogFragment();
        deleteDialog.setOnFragmentInteractionListener(this);
        deleteDialog.setHistoryCursorAdapter(adapter);
        deleteDialog.setId(id);
        deleteDialog.setType(type);
        deleteDialog.setClassification(classification);
        deleteDialog.show(getFragmentManager(), "");
    }

    @Override
    public void retrieveTransaction(HistoryCursorAdapter adapter, String type) {

        Cursor cursor = null;
        switch(type){
            case HistoryCursorAdapter.TYPE_BORROWED:
                cursor = dbHelper.querryBorrowTransactionsJoinUser("1,-1");
                break;
            case HistoryCursorAdapter.TYPE_LEND:
                cursor = dbHelper.querryLendTransactionsJoinUser("1,-1");
                break;
        }
        if(adapter != null) {
            adapter.swapCursor(cursor);
        }
    }

    @Override
    public void retrieveTransaction(HistoryCursorAdapter adapter, String type, String filter) {

        Cursor cursor = null;
        switch(type){
            case HistoryCursorAdapter.TYPE_BORROWED:
                cursor = dbHelper.querryBorrowTransactionsJoinUser("1,-1", filter);
                break;
            case HistoryCursorAdapter.TYPE_LEND:
                cursor = dbHelper.querryLendTransactionsJoinUser("1,-1", filter);
                break;
        }

        adapter.swapCursor(cursor);

    }

    @Override
    public void deleteDialog(HistoryCursorAdapter historyCursorAdapter, int id, String type, String classification) {
        dbHelper.deleteTransaction(id, classification);
        Toast.makeText(getApplicationContext(), "Successfully deleted transaction!", Toast.LENGTH_SHORT).show();
        retrieveTransaction(historyCursorAdapter, type);
    }
}
