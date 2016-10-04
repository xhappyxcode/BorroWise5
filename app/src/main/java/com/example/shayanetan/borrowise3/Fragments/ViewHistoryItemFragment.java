package com.example.shayanetan.borrowise3.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.shayanetan.borrowise3.Models.ItemTransaction;
import com.example.shayanetan.borrowise3.R;

import java.io.File;


public class ViewHistoryItemFragment extends ViewHistoryAbstractFragment {

    private TextView tv_itemName;
    private ImageView img_camera;

    private ItemTransaction itemTransaction;

    private OnFragmentInteractionListener mListener;

    public ViewHistoryItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_view_history_item, container, false);

        img_camera = (ImageView) layout.findViewById(R.id.img_Hview_item);
        tv_itemName = (TextView) layout.findViewById(R.id.tv_HviewItemName);
        tv_type = (TextView) layout.findViewById(R.id.Hview_item_type);
        tv_person_name = (TextView) layout.findViewById(R.id.tv_HviewPersonName);
        tv_startDate = (TextView) layout.findViewById(R.id.tv_Hview_item_startDate);
        tv_endDate = (TextView) layout.findViewById(R.id.tv_Hview_item_endDate);
        tv_retDate = (TextView) layout.findViewById(R.id.tv_Hview_item_retDate);
        tv_status = (TextView) layout.findViewById(R.id.tv_Hview_item_status);
        rb_rating = (RatingBar) layout.findViewById(R.id.rb_Hview_ratingitem);

        init();

        itemTransaction = (ItemTransaction) dbHelper.queryTransaction(trans_id);
        tv_itemName.setText(itemTransaction.getName());
        File imgFile = new  File(itemTransaction.getPhotoPath());
        if(imgFile.exists()){
            // ItemTransaction.bmpOptions.inSampleSize = 8;
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            img_camera.setImageBitmap(myBitmap);
            img_camera.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
