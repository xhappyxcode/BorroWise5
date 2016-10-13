package com.example.shayanetan.borrowise3.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shayanetan.borrowise3.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise3.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise3.Fragments.AmountDialogFragment;
import com.example.shayanetan.borrowise3.Fragments.DeleteDialogFragment;
import com.example.shayanetan.borrowise3.Fragments.PaymentErrorDialogFragment;
import com.example.shayanetan.borrowise3.Fragments.RatingDialogFragment;
import com.example.shayanetan.borrowise3.Fragments.ViewTransactionAbstractFragment;
import com.example.shayanetan.borrowise3.Fragments.ViewTransactionItemFragment;
import com.example.shayanetan.borrowise3.Fragments.ViewTransactionMoneyFragment;
import com.example.shayanetan.borrowise3.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise3.Models.ItemTransaction;
import com.example.shayanetan.borrowise3.Models.MoneyTransaction;
import com.example.shayanetan.borrowise3.Models.PaymentHistory;
import com.example.shayanetan.borrowise3.Models.Transaction;
import com.example.shayanetan.borrowise3.R;

public class ViewTransactionActivity extends AppCompatActivity implements
        AmountDialogFragment.OnFragmentInteractionListener,
        RatingDialogFragment.OnFragmentInteractionListener,
        ViewTransactionAbstractFragment.OnFragmentInteractionListener,
        DeleteDialogFragment.OnFragmentInteractionListener{

    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView toolbar_icon;

    protected DatabaseOpenHelper dbHelper;
    protected int trans_id;

    protected Transaction transaction;
    private int TempID, currType, currBtn;
    private ViewTransactionItemFragment itemFragment;
    private ViewTransactionMoneyFragment moneyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //setTitle(R.string.title_activity_view_transaction);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_icon = (ImageView) findViewById(R.id.img_toolbar_icon);

        Intent intent = getIntent();
        trans_id = intent.getIntExtra(Transaction.COLUMN_ID, 0);

        if(intent.getIntExtra(Transaction.TRANSACTION_ITEM_TYPE, 0) == TransactionsCursorAdapter.TYPE_ITEM) {
            toolbar_title.setText(intent.getStringExtra(Transaction.ITEM_NAME));
            toolbar_icon.setImageResource(R.drawable.ic_item);
        }
        else {
            toolbar_title.setText("PHP"+intent.getStringExtra(Transaction.ITEM_NAME));
            toolbar_icon.setImageResource(R.drawable.ic_money);
        }



        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());


        transaction = dbHelper.queryTransaction(trans_id);

        /* put trans_id into bundle to pass to fragment */
        Bundle bundle = new Bundle();
        bundle.putInt(Transaction.COLUMN_ID, trans_id);


        if(transaction.getClassification().contentEquals(Transaction.ITEM_TYPE)) {
            itemFragment = new ViewTransactionItemFragment();
            itemFragment.setArguments(bundle);
            itemFragment.setOnFragmentInteractionListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_view_transaction_container, itemFragment)
                    .commit();
        } else {
            moneyFragment = new ViewTransactionMoneyFragment();
            moneyFragment.setArguments(bundle);
            moneyFragment.setOnFragmentInteractionListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_view_transaction_container, moneyFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("ViewTransactionActivity", "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_view_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_delete:
               // Toast.makeText(ViewTransactionActivity.this, "Delete is selected", Toast.LENGTH_SHORT).show();
                Log.d("DELEtE TRANSACITON", "Deleting transaction....");
                DeleteDialogFragment dialogFragment = new DeleteDialogFragment();
                dialogFragment.setOnFragmentInteractionListener(this);
                dialogFragment.show(getFragmentManager(), "");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void updateAmount(TransactionsCursorAdapter adapter, String viewType, String filterType, double partialAmt) {

        MoneyTransaction m = (MoneyTransaction) dbHelper.queryTransaction(trans_id);
        double computedAmount = m.getAmountDeficit() - partialAmt;
        m.setAmountDeficit(computedAmount);
        m.setReturnDate(System.currentTimeMillis());
        m.setStatus(0);

        //  Toast.makeText(this, "Deficit " + m.getAmountDeficit(), Toast.LENGTH_LONG).show();

        if(m.getAmountDeficit() < 0){
            PaymentErrorDialogFragment errorDialog = new PaymentErrorDialogFragment();
            errorDialog.setTransactionsCursorAdapter(adapter);
            errorDialog.setViewType(viewType);
            errorDialog.setFilterType(filterType);
            errorDialog.show(getFragmentManager(), "");
        } else  {
            addPaymentHistory(m, partialAmt);
            if(m.getAmountDeficit() == 0) {
                RatingDialogFragment df = new RatingDialogFragment();
                df.setOnFragmentInteractionListener(this);
                df.setTransactionsCursorAdapter(adapter);
                df.setViewType(viewType);
                df.setFilterType(filterType);
                df.show(getFragmentManager(), "");

                MoneyTransaction m2 = (MoneyTransaction) dbHelper.queryTransaction(trans_id);
                m2.setAmountDeficit(m.getAmountDeficit() - partialAmt);
                m2.setReturnDate(System.currentTimeMillis());
                m2.setStatus(1);
                Log.v("BEFORE TEMPID: ", "" + trans_id);
                trans_id = dbHelper.updateTransaction(m2);
                Log.v("AFTER TEMPID: ", "" + trans_id);
            }
        }


    }

    public long addPaymentHistory(MoneyTransaction m, double partialAmt) {
        trans_id = dbHelper.updateTransaction(m);
        PaymentHistory payment = new PaymentHistory();
        payment.setTransaction_id(trans_id);
        payment.setDate(System.currentTimeMillis());
        payment.setPayment(partialAmt);
        long temp = dbHelper.insertPaymentHistory(payment);
        Toast.makeText(getApplicationContext(),"Successfully added payment",Toast.LENGTH_SHORT).show();

        moneyFragment.updateData();
        return temp;
    }

    @Override
    public void updateRating(TransactionsCursorAdapter adapter, String viewType, String filterType, double rating) {
        int transID = 0;
        switch (currType) {
            case TransactionsCursorAdapter.TYPE_MONEY:
                Log.v("rating TRANS ID!!!!!! ",""+TempID);
                //     Toast.makeText(getBaseContext(), "rating TRANS ID!!!!!! " +TempID, Toast.LENGTH_LONG).show();
                if(currBtn == TransactionsCursorAdapter.BTN_TYPE_RETURN) {
                    MoneyTransaction m = (MoneyTransaction) dbHelper.queryTransaction(TempID);
                    m.setAmountDeficit(m.getAmountDeficit());
                    m.setReturnDate(System.currentTimeMillis());
                    m.setStatus(1);
                    m.setRate(rating);
                    transID = dbHelper.updateTransaction(m);
                }else{
                    //Toast.makeText(this, "PARTIALS NOT YET SUPPORTED", Toast.LENGTH_SHORT).show();
                    MoneyTransaction m = (MoneyTransaction) dbHelper.queryTransaction(TempID);
                    m.setRate(rating);
                    transID = dbHelper.updateTransaction(m);
                }
                break;
            case TransactionsCursorAdapter.TYPE_ITEM:
                currType = TransactionsCursorAdapter.TYPE_ITEM;
                if(currBtn == TransactionsCursorAdapter.BTN_TYPE_RETURN) {

                    ItemTransaction i = (ItemTransaction) dbHelper.queryTransaction(TempID);
                    i.setReturnDate(System.currentTimeMillis());
                    i.setStatus(1);
                    i.setRate(rating);
                    transID = dbHelper.updateTransaction(i);
                }else{
                    ItemTransaction i = (ItemTransaction) dbHelper.queryTransaction(TempID);
                    i.setStatus(-1);
                    i.setRate(rating);
                    i.setReturnDate(System.currentTimeMillis());
                    transID = dbHelper.updateTransaction(i);
                }
                break;
        }
        Transaction transaction = dbHelper.queryTransaction(transID);
        dbHelper.updateUserRating(transaction.getUserID());
//        if(filterType.equalsIgnoreCase("All"))
//            retrieveTransaction(adapter, viewType);
//        else
//            retrieveTransaction(adapter, viewType, filterType);
        finish();
    }

    @Override
    public void updateTransaction(int id, int type, int btnType) {
        this.currBtn = btnType;
        switch (type) {
            case TransactionsCursorAdapter.TYPE_MONEY:
                currType = TransactionsCursorAdapter.TYPE_MONEY;
                break;
            case TransactionsCursorAdapter.TYPE_ITEM:
                currType = TransactionsCursorAdapter.TYPE_ITEM;
        }
        this.TempID = id;
        Log.v("update TRANS ID!!!!!! ", "" + id);
        //  Toast.makeText(getBaseContext(), "update TRANS ID!!!!!! " +id, Toast.LENGTH_LONG).show();

        if (currBtn == TransactionsCursorAdapter.BTN_TYPE_PARTIAL) {

            AmountDialogFragment df = new AmountDialogFragment();
            df.setOnFragmentInteractionListener(this);
            df.show(getFragmentManager(), "");

        } else {
            RatingDialogFragment df = new RatingDialogFragment();
            df.setOnFragmentInteractionListener(this);
            df.show(getFragmentManager(), "");

        }

    }

    @Override
    public void deleteDialog(HistoryCursorAdapter historyCursorAdapter, int id, String type, String classification) {
        dbHelper.deleteTransaction(transaction.getId(),transaction.getClassification());
        finish();
    }
}
