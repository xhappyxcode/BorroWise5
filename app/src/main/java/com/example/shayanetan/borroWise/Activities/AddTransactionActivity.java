package com.example.shayanetan.borrowise.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.shayanetan.borrowise.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise.Fragments.AddAbstractFragment;
import com.example.shayanetan.borrowise.Fragments.AddItemFragment;
import com.example.shayanetan.borrowise.Fragments.AddMoneyFragment;
import com.example.shayanetan.borrowise.Fragments.SettingsDialogFragment;
import com.example.shayanetan.borrowise.Fragments.TimePickerFragment;
import com.example.shayanetan.borrowise.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise.Models.ItemTransaction;
import com.example.shayanetan.borrowise.Models.MoneyTransaction;
import com.example.shayanetan.borrowise.Models.Transaction;
import com.example.shayanetan.borrowise.Models.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.mobapde.borroWise.R;

/*
 * Edited by Stephanie Dy on 7/20/2016 added onResume to close drawer
 *                        on 7/27/2016 redirected this activity from AddTransactionDialogFragment
 *                                     receives the transactionType from dialog and chooses the fragment to add to activity
 */

public class AddTransactionActivity extends BaseActivity implements
        AddAbstractFragment.OnFragmentInteractionListener,
        SettingsDialogFragment.OnFragmentInteractionListener,
        TimePickerFragment.OnFragmentInteractionListener{

    private Toolbar toolbar;
    private TextView toolbar_title;

    private DatabaseOpenHelper dbHelper;

    final static String SP_KEY_BORROW_TIME = "BORROWTIME";
    final static String SP_KEY_BORROW_DAYS = "BORROWDAYS";
    final static String SP_KEY_LEND_TIME = "LENDTIME";
    final static String SP_KEY_LEND_DAYS = "LENDDAYS";

    private AddItemFragment itemFragment;
    private AddMoneyFragment moneyFragment;

    static SettingsDialogFragment settingsDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        setToolbar_title(R.string.title_activity_add_transaction);

        Intent intent = getIntent();
        int transactionType = intent.getIntExtra(Transaction.COLUMN_TYPE, TransactionsCursorAdapter.TYPE_ITEM);



        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());

        itemFragment = null;
        moneyFragment = null;

        if(transactionType==TransactionsCursorAdapter.TYPE_ITEM) {
            itemFragment = new AddItemFragment();
            itemFragment.setOnFragmentInteractionListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, itemFragment)
                    .commit();
        } else {
            moneyFragment = new AddMoneyFragment();
            moneyFragment.setOnFragmentInteractionListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, moneyFragment)
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public int onAddNewUser(String name, String contact_info) {
        int id = dbHelper.checkUserIfExists(name, contact_info);
        if(id == -1){
            id = (int) dbHelper.insertUser(new User(name, contact_info, 0));
        }else{
            System.out.println("USER doesnt EXISTS!");
        }
        return id;
    }

    @Override
    public void onAddTransactions(Transaction t) {
       long itemId =  dbHelper.insertTransaction(t);
        Log.v("NEW TRANS ID!!!!!! ", "" + itemId);
        Log.v("START DATE: ", ""+t.getStartDate());
        Log.v("DUE DATE: ", ""+t.getDueDate());
     //   Toast.makeText(getBaseContext(), "NEW TRANS ID!!!!!! " +itemId, Toast.LENGTH_LONG).show();
        setItemAlarm((int) itemId, t, t.getDueDate(), t.getClassification(), t.getType());
    }

    @Override
    public void onSettingsDialog() {
        settingsDialogFragment = new SettingsDialogFragment();
        settingsDialogFragment.setOnFragmentInteractionListener(this);
        settingsDialogFragment.show(getFragmentManager(), "Settings");
    }

    public void setItemAlarm(int item_id, Transaction t, long end, String classification, String type){

        String dateString = new SimpleDateFormat("MMM dd,yyyy").format(new Date(t.getDueDate()));

        if(type.equalsIgnoreCase(Transaction.BORROWED_ACTION)){
            //Create an intent to broadcast
            Log.v("TYPE", "TYPEEE: "+type);
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), AlarmReceiver.class);//even receivers receive intents
            intent.putExtra(Transaction.COLUMN_ID, item_id);
            intent.putExtra(Transaction.COLUMN_CLASSIFICATION, classification);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), item_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);


            Calendar alarm = Calendar.getInstance();
            alarm.setTimeInMillis(end);
            Log.v("BEFORE ALARM:", "" + alarm.getTimeInMillis());
            if(t.getAlarmTime() != null) {
                DateFormat sdf = new SimpleDateFormat("HH:mm");
                Date date = null;
                try {
                    date = sdf.parse(t.getAlarmTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                alarm.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                Log.v("cHour: ", calendar.get(Calendar.HOUR_OF_DAY) + "");
                alarm.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
                Log.v("cMinute: ", calendar.get(Calendar.MINUTE) + "");
                alarm.set(Calendar.SECOND, 0);
                Log.v("AFTER ALARM:", ""+alarm.getTimeInMillis());
            }else{
                alarm.set(Calendar.HOUR_OF_DAY, 10);
                alarm.set(Calendar.MINUTE,0);
                alarm.set(Calendar.SECOND, 0);
            }

            String dateToday = new SimpleDateFormat("MM/dd/yyyy").format(new Date(Calendar.getInstance().getTimeInMillis()));
            String dueDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date(end));


          if(!dateToday.equalsIgnoreCase(dueDate)) {
              Log.v("CHECKKK", "CALENDAR: " + dateToday + " DUE: " + dueDate);
              if(t.getDaysLeft() > -1) {
                  int duration = t.getDaysLeft();
                  Log.v("inside if"," duration: "+duration);
                  for(int i=duration; i>0; i--){
                      alarm.add(Calendar.DAY_OF_MONTH, -i);
                  }
              }else {
                  Log.v("inside if"," duration: else");
                  alarm.add(Calendar.DAY_OF_MONTH, -1);
              }
          }

            AlarmManager alarmManager = (AlarmManager)getSystemService(Service.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pendingIntent);


        }else if(type.equalsIgnoreCase(Transaction.LEND_ACTION)){
            User u = null;

            String message = "";

            if(classification.equalsIgnoreCase(Transaction.ITEM_TYPE)) {
                ItemTransaction it = (ItemTransaction) dbHelper.queryTransaction(item_id);
                u = dbHelper.queryUser(it.getUserID());

                if(t.getDaysLeft() <= 0) {
                    message = "[BorroWise Reminder] \n"
                            + "Hi " + u.getName()
                            + "! Please be reminded to return the borrowed item "
                            + it.getName() + " today!";
                }else{
                    message = "[BorroWise Reminder] \n"
                            + "Hi " + u.getName()
                            + "! Please be reminded to return the borrowed item "
                            + it.getName() + " on "+dateString+"!";
                }

            }else{
                MoneyTransaction mt = (MoneyTransaction) dbHelper.queryTransaction(item_id);
                u = dbHelper.queryUser(mt.getUserID());

                if(t.getDaysLeft() <= 0){
                    message = "[BorroWise Reminder] \n"
                            + "Hi " + u.getName()
                            + "! Please be reminded to return the borrowed money PHP "
                            + mt.getAmountDeficit()+ " today!";
                }else{
                    message = "[BorroWise Reminder] \n"
                            + "Hi " + u.getName()
                            + "! Please be reminded to return the borrowed money PHP "
                            + mt.getAmountDeficit()+ " on "+dateString+"!";
                }

            }

            Intent intent = new Intent(getBaseContext(), SMSReceiver.class);
            intent.putExtra(SMSReceiver.NUMBER,u.getContactInfo());
            intent.putExtra(SMSReceiver.MESSAGE, message);
            intent.putExtra(Transaction.COLUMN_ID, item_id);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), item_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

            Calendar smsAlarm = Calendar.getInstance();
            smsAlarm.setTimeInMillis(end);

            if(t.getAlarmTime() != null) {
                DateFormat sdf = new SimpleDateFormat("HH:mm");
                Date date = null;
                try {
                    date = sdf.parse(t.getAlarmTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                smsAlarm.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                smsAlarm.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
                smsAlarm.set(Calendar.SECOND, 0);
            }else{
                smsAlarm.set(Calendar.HOUR_OF_DAY, 10);
                smsAlarm.set(Calendar.MINUTE,0);
                smsAlarm.set(Calendar.SECOND, 0);
            }


            String dateToday = new SimpleDateFormat("MM/dd/yyyy").format(new Date(Calendar.getInstance().getTimeInMillis()));
            String dueDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date(end));


            if(!dateToday.equalsIgnoreCase(dueDate)) {
              //  Log.v("In lend: CHECKKK", "CALENDAR: " + dateToday + " DUE: " + dueDate);
                if(t.getDaysLeft() > -1) {
                    int duration = t.getDaysLeft();
                    for(int i=duration; i>0; i--){
                        smsAlarm.add(Calendar.DAY_OF_MONTH, -i);
                    }
                }else {
                    smsAlarm.add(Calendar.DAY_OF_MONTH, -1);
                }
            }

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, smsAlarm.getTimeInMillis(), pendingIntent);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void updateNotification(String alarmTime, int daysLeft) {
        if(itemFragment != null) {
            itemFragment.setNotificationValue(alarmTime, daysLeft);
        }else if(moneyFragment != null){
            moneyFragment.setNotificationValue(alarmTime, daysLeft);
        } else{
            Log.v("item and moneyFragment", "NULL");
        }
        Log.v("alarmTime", alarmTime);
        Log.v("daysLeft", String.valueOf(daysLeft));
    }

    @Override
    public void onTimeDialog() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setOnFragmentInteractionListener(this);
        if(timePickerFragment.getmListener() == null)
            Log.v("getmListener", "is null");
        timePickerFragment.show(getSupportFragmentManager(), "TimePicker");
    }

    @Override
    public void getTime(String time) {
        settingsDialogFragment.setResultTime(time);
    }
}
