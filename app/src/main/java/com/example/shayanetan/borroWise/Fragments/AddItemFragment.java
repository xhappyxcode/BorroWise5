package com.example.shayanetan.borrowise.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shayanetan.borrowise.Models.ItemTransaction;
import com.example.shayanetan.borrowise.Models.Transaction;
import java.io.ByteArrayOutputStream;

import edu.mobapde.borroWise.R;

/*
 * Edited by Stephanie Dy on 7/20/2016 removed button addContact
 *                        on 7/27/2016 removed img_btn_switch, onFragmentSwitch()
 */

public class AddItemFragment extends AddAbstractFragment {

    private final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText et_AIItemName;
    private ImageView img_camera;
    private View card_camera;
    private String filePath="";

    public AddItemFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_add_item, container, false);


        img_camera = (ImageView) layout.findViewById(R.id.img_camera);
        et_AIItemName = (EditText) layout.findViewById(R.id.et_AIItemName);
        atv_person_name = (AutoCompleteTextView) layout.findViewById(R.id.atv_AIPersonName);

        card_camera = (View) layout.findViewById(R.id.card_camera);

        layout_startDate = (View) layout.findViewById(R.id.layout_item_startDate);
        layout_endDate = (View) layout.findViewById(R.id.layout_item_endDate);
        layout_notif_settings = (View) layout.findViewById(R.id.layout_item_notif_settings);

        tv_notif_time = (TextView) layout.findViewById(R.id.tv_item_notif_time);
        tv_notif_days_before = (TextView) layout.findViewById(R.id.tv_item_notif_days_before);

        tv_endDate = (TextView) layout.findViewById(R.id.tv_item_endDate);
        tv_startDate = (TextView) layout.findViewById(R.id.tv_item_startDate);

        btn_borrowed = (Button) layout.findViewById(R.id.btn_AIBorrow);
        btn_lent = (Button) layout.findViewById(R.id.btn_AILend);

        init(); // method found in abstract class

        card_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
                myContext.overridePendingTransition(0, 0);
            }
        });

        btn_borrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String item = et_AIItemName.getText().toString();

                long startDate = parseDateToMillis(tv_startDate.getText().toString());
                long dueDate = parseDateToMillis(tv_endDate.getText().toString());
                if (mListener != null && !item.isEmpty() && !selected_name.isEmpty()) {
                    if(dueDate >= startDate) {
                        int id = mListener.onAddNewUser(selected_name, selected_contact_number);

                        ItemTransaction it = new ItemTransaction(Transaction.ITEM_TYPE, id, Transaction.BORROWED_ACTION, 0,
                                parseDateToMillis(tv_startDate.getText().toString()),
                                parseDateToMillis(tv_endDate.getText().toString()),
                                0, 0.0,
                                tv_notif_time.getText().toString(), //alarmTime
                                Integer.parseInt(tv_notif_days_before.getText().toString()), //daysLeft
                                item, "", filePath);
                        mListener.onAddTransactions(it);

                        printAddAcknowledgement(et_AIItemName.getText().toString(), "borrowed");
                        clearAllFields();
                    } else {
                        Toast.makeText(getActivity().getBaseContext(), "Due date must be after start date!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    printRejectDialog();
                }


            }
        });

        btn_lent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String item = et_AIItemName.getText().toString();

                if (mListener != null && !item.isEmpty() && !selected_name.isEmpty()) {
                    long startDate = parseDateToMillis(tv_startDate.getText().toString());
                    long dueDate = parseDateToMillis(tv_endDate.getText().toString());
                    int id = mListener.onAddNewUser(selected_name, selected_contact_number);
                    if(dueDate >= startDate) {
                        ItemTransaction it = new ItemTransaction(Transaction.ITEM_TYPE, id, Transaction.LEND_ACTION, 0,
                                parseDateToMillis(tv_startDate.getText().toString()),
                                parseDateToMillis(tv_endDate.getText().toString()),
                                0, 0.0,
                                tv_notif_time.getText().toString(), //alarmTime
                                Integer.parseInt(tv_notif_days_before.getText().toString()), //daysLeft
                                item, "", filePath);

                       // Toast.makeText(getActivity().getBaseContext(), "tv Notif Time: "+tv_notif_time.getText().toString(), Toast.LENGTH_LONG).show();

                        mListener.onAddTransactions(it);
                        printAddAcknowledgement(et_AIItemName.getText().toString(), "lent");
                        clearAllFields();
                    } else {
                        Toast.makeText(getActivity().getBaseContext(), "Due date must be after start date!", Toast.LENGTH_LONG).show();
                    }

                }else{
                    printRejectDialog();
                }
            }
        });

        return layout;
    }

    public void printAddAcknowledgement(String entry_name, String type){
        if(type.equalsIgnoreCase("lent")) {

            LayoutInflater factory = LayoutInflater.from(getActivity());
            final View view = factory.inflate(R.layout.add_transaction_confirmation, null);
            TextView tv_confirmation = (TextView) view.findViewById(R.id.tv_confirmation);
            tv_confirmation.setText(entry_name + " has been successfully " + type + " to " + selected_name + " !");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setView(view)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    });
            Dialog dialog = builder.create();
            dialog.show();

        }else {

            LayoutInflater factory = LayoutInflater.from(getActivity());
            final View view = factory.inflate(R.layout.add_transaction_confirmation, null);
            TextView tv_confirmation = (TextView) view.findViewById(R.id.tv_confirmation);
            tv_confirmation.setText(entry_name + " has been successfully " + type + " from " + selected_name + " !");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setView(view)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    });
            Dialog dialog = builder.create();
            dialog.show();

        }
    }

    public void clearAllFields(){
        et_AIItemName.setText("");
        atv_person_name.setText("");
        img_camera.setImageResource(R.drawable.ic_camera);
        img_camera.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setDateToCurrent();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);


        // CALL THIS
        //Bitmap bp = (Bitmap) data.getExtras().get("data");
        //iv.setImageBitmap(bp);
        if(requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);
            //knop.setVisibility(Button.VISIBLE); METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getActivity().getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            //File finalFile = new File(getRealPathFromURI(tempUri));

            filePath = getRealPathFromURI(tempUri);

            img_camera.setImageBitmap(photo);
            //  img_camera.set;
            img_camera.setScaleType(ImageView.ScaleType.CENTER_CROP);

//            System.out.println("CAMERA SAVED FILEPATH: " + getRealPathFromURI(tempUri));
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

//    @Override
//    protected void onFragmentSwitch() {
//        img_btn_switch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AddMoneyFragment fragment = new AddMoneyFragment();
//                FragmentManager fm = myContext.getSupportFragmentManager();
//                transaction = fm.beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment);
//                transaction.commit();
//            }
//        });
//    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
