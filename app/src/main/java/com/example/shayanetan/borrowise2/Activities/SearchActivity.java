package com.example.shayanetan.borrowise2.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.shayanetan.borrowise2.Adapters.TransactionHistoryCursorAdapter;
import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.R;

public class SearchActivity extends BaseActivity {

    DatabaseOpenHelper dbHelper;
    EditText et_search;
    ImageView img_search;
    RecyclerView recyclerView;
    TransactionHistoryCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        et_search = (EditText) findViewById(R.id.et_search);
        img_search = (ImageView) findViewById(R.id.img_search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_search);

        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());

        adapter = new TransactionHistoryCursorAdapter(getBaseContext(), null);
        adapter.setOnHistoryLongClickListener(new TransactionHistoryCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, String type, String classification) {
                dbHelper.deleteTransaction(id, classification);
            }
        });

        adapter.setOnHistoryClickListener(new TransactionHistoryCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, String type, String classification) {
                dbHelper.queryTransaction(id);
            }
        });

        adapter.setOnTransactionClickListener(new TransactionHistoryCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, String type, String classification) {
                dbHelper.queryTransaction(id);
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


}