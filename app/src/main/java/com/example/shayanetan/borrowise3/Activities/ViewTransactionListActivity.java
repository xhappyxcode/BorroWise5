package com.example.shayanetan.borrowise3.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.shayanetan.borrowise3.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise3.Adapters.ViewPagerAdapter;
import com.example.shayanetan.borrowise3.Fragments.AddTransactionDialogFragment;
import com.example.shayanetan.borrowise3.Fragments.ViewBorrowedFragment;
import com.example.shayanetan.borrowise3.Fragments.ViewLentFragment;
import com.example.shayanetan.borrowise3.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise3.Models.Transaction;
import com.example.shayanetan.borrowise3.Views.SlidingTabLayout;
import com.example.shayanetan.borrowise3.R;

/*
 * Edited by Stephanie Dy on 2/27/2016 added btn_addTransaction and onClickListener
 *                                           implemented AddTransactionDialogFragment
 */
public class ViewTransactionListActivity extends BaseActivity
        implements ViewBorrowedFragment.OnFragmentInteractionListener,
        ViewLentFragment.OnFragmentInteractionListener,
        AddTransactionDialogFragment.OnFragmentInteractionListener{

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SlidingTabLayout slidingTabLayout;

    private static String TITLE_TAB1 = "BORROWED FROM";
    private static String TITLE_TAB2 = "LENT TO";

    private ViewBorrowedFragment borrowFragment;
    private ViewLentFragment lentFragment;
    private DatabaseOpenHelper dbHelper;

    private FloatingActionButton btn_addTransaction, btn_addItem, btn_addMoney;
    private boolean isAddBtnOpen;
    private Animation btnOpen, btnClose, rotateForward, rotateBackward;

    // private TransactionsCursorAdapter transactionsCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction_list);
        setTitle(R.string.title_activity_transaction);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

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
            public void onClick(View view) {
                animate();
            }
        });
        /*btn_addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();

            }
        });*/

        btn_addItem = (FloatingActionButton) findViewById(R.id.fab_add_item);
        btn_addMoney = (FloatingActionButton) findViewById(R.id.fab_add_money);

        btnOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.open_floating_action_button);
        btnClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.close_floating_action_button);

        rotateForward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
    }
/*
    public void setListView(boolean enable){
        PackageManager pm = getPackageManager();
        int enableFlag;
        if(enable == false){
            Log.i("setListView : enable", String.valueOf(enable));
            enableFlag = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        } else{
            Log.i("setListView : enable", String.valueOf(enable));
            enableFlag = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
        }
        pm.setComponentEnabledSetting(new ComponentName(this, this.getClass()),
                enableFlag, PackageManager.DONT_KILL_APP);
    }*/

    public void animate(){
        if(isAddBtnOpen){ //if the add button is showing the item/money
            //setListView(false); //make the list view screen not clickable
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            closeFAB();
            isAddBtnOpen = false;
        } else{ //if the add button is not showing the item/money, show the options
            //setListView(true);
            openFAB();
            isAddBtnOpen = true;
            btn_addItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeFAB();
                    addTransaction(TransactionsCursorAdapter.TYPE_ITEM);
                }
            });
            btn_addMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeFAB();
                    addTransaction(TransactionsCursorAdapter.TYPE_MONEY);
                }
            });
        }
    }

    public void closeFAB(){
        btn_addTransaction.startAnimation(rotateBackward); //rotate back to +
        btn_addItem.startAnimation(btnClose); //close item option
        btn_addMoney.startAnimation(btnClose); //close money option
        btn_addItem.setClickable(false);
        btn_addMoney.setClickable(false);
    }

    public void openFAB(){
        btn_addTransaction.startAnimation(rotateForward); //rotate to x
        btn_addItem.startAnimation(btnOpen); //open item option
        btn_addMoney.startAnimation(btnOpen); //open money option
        btn_addItem.setClickable(true);
        btn_addMoney.setClickable(true);
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

        overridePendingTransition(0, 0);
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


