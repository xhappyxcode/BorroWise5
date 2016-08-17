package com.example.shayanetan.borrowise2.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.Models.ItemTransaction;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ViewTransactionItemFragment extends ViewTransactionAbstractFragment {

    private TextView tv_itemName;
    private ImageView img_camera;
    private Button btn_lost, btn_returned;

    private ItemTransaction itemTransaction;

    public ViewTransactionItemFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_view_transaction_item, container, false);
        img_camera = (ImageView) layout.findViewById(R.id.img_view_item);
        tv_itemName = (TextView) layout.findViewById(R.id.tv_viewItemName);
        tv_person_name = (TextView) layout.findViewById(R.id.tv_viewPersonName);

        tv_endDate = (TextView) layout.findViewById(R.id.tv_view_item_endDate);
        tv_startDate = (TextView) layout.findViewById(R.id.tv_view_item_startDate);

        tv_type = (TextView) layout.findViewById(R.id.view_item_type);

        btn_lost = (Button) layout.findViewById(R.id.btn_lost);
        btn_returned = (Button) layout.findViewById(R.id.btn_returned);


        init(); // method found in abstact class

        itemTransaction = (ItemTransaction) dbHelper.queryTransaction(trans_id);

        tv_itemName.setText(itemTransaction.getName());
        File imgFile = new  File(itemTransaction.getPhotoPath());
        if(imgFile.exists()){
            // ItemTransaction.bmpOptions.inSampleSize = 8;
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            img_camera.setImageBitmap(myBitmap);
            img_camera.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        btn_lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: change status of transaction to lost
                mListener.updateTransaction(trans_id, TransactionsCursorAdapter.TYPE_ITEM, TransactionsCursorAdapter.BTN_TYPE_LOST);

            }
        });

        btn_returned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: change status of transaction to lost
                mListener.updateTransaction(trans_id, TransactionsCursorAdapter.TYPE_ITEM, TransactionsCursorAdapter.BTN_TYPE_RETURN);
            }
        });

        return layout;
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}

