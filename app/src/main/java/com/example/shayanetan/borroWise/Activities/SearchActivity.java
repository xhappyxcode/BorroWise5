package com.example.shayanetan.borrowise.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shayanetan.borrowise.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise.Adapters.TransactionHistoryCursorAdapter;
import com.example.shayanetan.borrowise.Fragments.DeleteDialogFragment;
import com.example.shayanetan.borrowise.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise.Models.Transaction;

import edu.mobapde.borroWise.R;

public class SearchActivity extends BaseActivity
        implements DeleteDialogFragment.OnFragmentInteractionListener {

    DatabaseOpenHelper dbHelper;
    EditText et_search;
    ImageView img_search;
    RecyclerView recyclerView;
    TransactionHistoryCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setToolbar_title(R.string.title_activity_search);

        et_search = (EditText) findViewById(R.id.et_search);
        img_search = (ImageView) findViewById(R.id.img_search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_search);

        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());

        adapter = new TransactionHistoryCursorAdapter(getBaseContext(), null);
        adapter.setOnHistoryLongClickListener(new TransactionHistoryCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, String type, String classification, String item_name) {
//              Log.v("SearchActivity", "HistoryLongClick");
                deleteTransaction(id, type, classification);
            }
        });

        adapter.setOnHistoryClickListener(new TransactionHistoryCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, String type, String classification, String item_name) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), ViewHistoryActivity.class);
                intent.putExtra(Transaction.COLUMN_ID, id);
                intent.putExtra(Transaction.TRANSACTION_ITEM_TYPE, classification);
                intent.putExtra(Transaction.ITEM_NAME, item_name);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        adapter.setOnTransactionClickListener(new TransactionHistoryCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, String type, String classification, String item_name) {
//                Log.v("SearchActvity", "TransactionClick");
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), ViewTransactionActivity.class);
                intent.putExtra(Transaction.COLUMN_ID, id);
                intent.putExtra(Transaction.TRANSACTION_ITEM_TYPE, classification);
                intent.putExtra(Transaction.ITEM_NAME, item_name);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = et_search.getText().toString();
                if(search != null) {
                    Cursor cursor = dbHelper.queryByKeyword(search);
                    adapter.swapCursor(cursor);
                }
            }
        });
    }

    public void retrieveTransaction() {
        String search = et_search.getText().toString();
        if(search != null) {
            Cursor cursor = dbHelper.queryByKeyword(search);
            adapter.swapCursor(cursor);
        }
    }

    public void deleteTransaction(int id, String type, String classification) {
        DeleteDialogFragment deleteDialog = new DeleteDialogFragment();
        deleteDialog.setOnFragmentInteractionListener(this);
//        deleteDialog.setHistoryCursorAdapter(adapter);
        deleteDialog.setId(id);
        deleteDialog.setType(type);
        deleteDialog.setClassification(classification);
        deleteDialog.show(getFragmentManager(), "");
    }

    @Override
    public void deleteDialog(HistoryCursorAdapter adapter, int id, String type, String classification) {
        dbHelper.deleteTransaction(id, classification);
        Toast.makeText(getApplicationContext(), "Successfully deleted transaction!", Toast.LENGTH_SHORT).show();
        retrieveTransaction();
    }
}
