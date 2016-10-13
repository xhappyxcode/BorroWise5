package com.example.shayanetan.borrowise.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shayanetan.borrowise.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise.Fragments.DeleteDialogFragment;
import com.example.shayanetan.borrowise.Fragments.ViewHistoryAbstractFragment;
import com.example.shayanetan.borrowise.Fragments.ViewHistoryItemFragment;
import com.example.shayanetan.borrowise.Fragments.ViewHistoryMoneyFragment;
import com.example.shayanetan.borrowise.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise.Models.Transaction;

import edu.mobapde.borroWise.R;

public class ViewHistoryActivity extends AppCompatActivity implements
        ViewHistoryAbstractFragment.OnFragmentInteractionListener,
        ViewHistoryItemFragment.OnFragmentInteractionListener,
        ViewHistoryMoneyFragment.OnFragmentInteractionListener,
        DeleteDialogFragment.OnFragmentInteractionListener{

    protected DatabaseOpenHelper dbHelper;
    protected int trans_id;

    protected Transaction transaction;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView toolbar_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_icon = (ImageView) findViewById(R.id.img_toolbar_icon);

        Intent intent = getIntent();
        trans_id = intent.getIntExtra(Transaction.COLUMN_ID, 0);
        Log.v("INTENT TAG", intent.getExtras().toString());
        Log.v("NULL",intent.getStringExtra(Transaction.TRANSACTION_ITEM_TYPE));
        if(intent.getStringExtra(Transaction.TRANSACTION_ITEM_TYPE).equals(Transaction.ITEM_TYPE)) {
            toolbar_title.setText(intent.getStringExtra(Transaction.ITEM_NAME));
            toolbar_icon.setImageResource(R.drawable.ic_item);
        }
        else {
            toolbar_title.setText("PHP "+intent.getStringExtra(Transaction.ITEM_NAME));
            toolbar_icon.setImageResource(R.drawable.ic_money);
        }

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

    @Override
    public void deleteDialog(HistoryCursorAdapter historyCursorAdapter, int id, String type, String classification) {
        dbHelper.deleteTransaction(transaction.getId(),transaction.getClassification());
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("ViewHistoryActivity", "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_history, menu);
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
}
