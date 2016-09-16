package Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Models.MoneyTransaction;
import Models.Transaction;
import com.example.shayanetan.borrowise2.R;
/*
 * Edited by Stephanie Dy on 7/20/2016 Removed addContact button
 *                        on 7/27/2016 removed img_button_switch, onFragmentSwitch()
 */

public class AddMoneyFragment extends AddAbstractFragment {

    private FragmentTransaction transaction;
    private EditText et_AMAmount;

    public AddMoneyFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_add_money, container, false);

//        img_btn_switch = (FloatingActionButton) layout.findViewById(R.id.btn_MoneyToItem);
        btn_borrowed = (Button) layout.findViewById(R.id.btn_AMBorrow);
        btn_lent = (Button) layout.findViewById(R.id.btn_AMLend);
//        btn_addContact = (ImageView) layout.findViewById(R.id.btn_addContact);
        et_AMAmount = (EditText) layout.findViewById(R.id.et_AMAmount);
        atv_person_name = (AutoCompleteTextView) layout.findViewById(R.id.atv_AMPersonName);
        tv_notif_time = (TextView) layout.findViewById(R.id.tv_money_notif_time);
        tv_notif_days_before = (TextView) layout.findViewById(R.id.tv_money_notif_days_before);

        layout_startDate = (View) layout.findViewById(R.id.layout_money_startDate);
        layout_endDate = (View) layout.findViewById(R.id.layout_money_endDate);

        tv_endDate = (TextView) layout.findViewById(R.id.tv_money_endDate);
        tv_startDate = (TextView) layout.findViewById(R.id.tv_money_startDate);

        init(); //method can be found in abstract class
//      Removed addContact button to prevent misunderstandings
//        btn_addContact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_INSERT,
//                        ContactsContract.Contacts.CONTENT_URI);
//                startActivity(intent);
//            }
//        });

        btn_borrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String money = et_AMAmount.getText().toString();

                if(!money.isEmpty() && !selected_name.isEmpty()){
                    long startDate = parseDateToMillis(tv_startDate.getText().toString());
                    long dueDate = parseDateToMillis(tv_endDate.getText().toString());
                    if(dueDate >= startDate) {
                        int id = mListener.onAddNewUser(selected_name, selected_contact_number);
                        double amount =  Double.parseDouble(money);
                        MoneyTransaction m = new MoneyTransaction(Transaction.MONEY_TYPE, id, Transaction.BORROWED_ACTION, 0,
                                parseDateToMillis(tv_startDate.getText().toString()),
                                parseDateToMillis(tv_endDate.getText().toString()),
                                0,0.0,
//                            Long.parseLong(tv_notif_time.getText().toString()), //alarmTime
//                            Integer.parseInt(tv_notif_days_before.getText().toString()), //daysLeft
                                amount, amount);
                        mListener.onAddTransactions(m);
                        printAddAcknowledgement(et_AMAmount.getText().toString(), "borrowed");

                        clearAllFields();
                    } else {
                        Toast.makeText(getActivity().getBaseContext(), "Due date must be after start date!", Toast.LENGTH_LONG).show();
                    }
                }else
                    printRejectDialog();

            }
        });

        btn_lent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String money = et_AMAmount.getText().toString();

                if(!money.isEmpty() && !selected_name.isEmpty()){
                    long startDate = parseDateToMillis(tv_startDate.getText().toString());
                    long dueDate = parseDateToMillis(tv_endDate.getText().toString());
                    if(dueDate >= startDate) {
                        int id = mListener.onAddNewUser(selected_name, selected_contact_number);

                        double amount =  Double.parseDouble(money);
                        MoneyTransaction m = new MoneyTransaction(Transaction.MONEY_TYPE, id, Transaction.LEND_ACTION, 0,
                                parseDateToMillis(tv_startDate.getText().toString()),
                                parseDateToMillis(tv_endDate.getText().toString()),
                                0,0.0, amount,amount);

                        mListener.onAddTransactions(m);
                        printAddAcknowledgement(et_AMAmount.getText().toString(), "lent");

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
            tv_confirmation.setText("PHP " + entry_name + " has been successfully " + type + " to " + selected_name + "!");

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
            tv_confirmation.setText("PHP " + entry_name + " has been successfully " + type + " from " + selected_name + "!");

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
        et_AMAmount.setText("");
        atv_person_name.setText("");
        setDateToCurrent();
    }

//    @Override
//    protected void onFragmentSwitch() {
//        img_btn_switch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AddItemFragment fragment = new AddItemFragment();
//                FragmentManager fm = myContext.getSupportFragmentManager();
//                transaction = fm.beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment);
//                transaction.commit();
//            }
//        });
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



}
