package Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import Adapters.TransactionsCursorAdapter;
import Adapters.ViewPagerAdapter;
import Fragments.AddTransactionDialogFragment;
import Fragments.ViewBorrowedFragment;
import Fragments.ViewLentFragment;
import Models.DatabaseOpenHelper;
import Models.Transaction;
import com.example.shayanetan.borrowise2.R;
import Views.SlidingTabLayout;

/*
 * Edited by Stephanie Dy on 2/27/2016 added btn_addTransaction and onClickListener
 *                                           implemented AddTransactionDialogFragment
 */
public class ViewTransactionListActivity extends BaseActivity
        implements ViewBorrowedFragment.OnFragmentInteractionListener,
        ViewLentFragment.OnFragmentInteractionListener,
        AddTransactionDialogFragment.OnFragmentInteractionListener {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SlidingTabLayout slidingTabLayout;

    private static String TITLE_TAB1 = "BORROWED FROM";
    private static String TITLE_TAB2 = "LENT TO";

    private ViewBorrowedFragment borrowFragment;
    private ViewLentFragment lentFragment;
    private DatabaseOpenHelper dbHelper;

    private FloatingActionButton btn_addTransaction;


    // private TransactionsCursorAdapter transactionsCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction_list);
        setTitle(R.string.title_activity_view_transaction);

        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());
        //transactionsCursorAdapter = new TransactionsCursorAdapter(getBaseContext(),null);

        viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.view_pager);

        borrowFragment = new ViewBorrowedFragment();
        borrowFragment.setOnFragmentInteractionListener(this);
        //borrowFragment.setTransactionsCursorAdapter(transactionsCursorAdapter);

        lentFragment = new ViewLentFragment();
        lentFragment.setOnFragmentInteractionListener(this);
        // lentFragment.setTransactionsCursorAdapter(transactionsCursorAdapter);

        viewPagerAdapter.addFragment(borrowFragment, TITLE_TAB1);
        viewPagerAdapter.addFragment(lentFragment, TITLE_TAB2);

        // viewPagerAdapter.addFragment(new TransactionLentFragment(), TITLE_TAB2);
        viewPager.setAdapter(viewPagerAdapter);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab);
        // True if Tabs are same Width
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

        btn_addTransaction = (FloatingActionButton) findViewById(R.id.fab_add_transaction);
        btn_addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();

            }
        });
    }

    public void onResume() {
        super.onResume();
        updateLists();
    }

    private void updateLists() {
        borrowFragment.resetData();
        lentFragment.resetData();
    }

    public void showAddDialog() {
        AddTransactionDialogFragment dialog = new AddTransactionDialogFragment();
        dialog.setOnFragmentInteractionListener(this);
        dialog.show(getFragmentManager(), "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_transaction_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addTransaction(int transactionType) {
        Intent intent = new Intent(getBaseContext(), AddTransactionActivity.class);
        intent.putExtra(Transaction.COLUMN_TYPE, transactionType);
        startActivity(intent);
    }

    @Override
    public void retrieveTransaction(TransactionsCursorAdapter adapter, String viewType) {
        Cursor cursor = null;

        Log.v("PASOKKKKK", "HALU");
        if(viewType.equalsIgnoreCase(ViewLentFragment.VIEW_TYPE)) {
            cursor= dbHelper.querryLendTransactionsJoinUser("0");
        }
        else if(viewType.equalsIgnoreCase(ViewBorrowedFragment.VIEW_TYPE)){
            cursor = dbHelper.querryBorrowTransactionsJoinUser("0");
        }

        if(adapter != null) {
            adapter.swapCursor(cursor);
        }
    }

    @Override
    public void retrieveTransaction(TransactionsCursorAdapter adapter, String viewType, String filterType) {
        Cursor cursor = null;

        if(viewType.equalsIgnoreCase(ViewLentFragment.VIEW_TYPE)){
            cursor = dbHelper.querryLendTransactionsJoinUser("0", filterType);
        }
        else if(viewType.equalsIgnoreCase(ViewBorrowedFragment.VIEW_TYPE )) {
            cursor = dbHelper.querryBorrowTransactionsJoinUser("0", filterType);
        }
        adapter.swapCursor(cursor);
    }
}


