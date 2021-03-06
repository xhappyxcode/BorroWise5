package com.example.shayanetan.borrowise.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.shayanetan.borrowise.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise.Models.ItemTransaction;
import com.example.shayanetan.borrowise.Models.MoneyTransaction;
import com.example.shayanetan.borrowise.Models.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.mobapde.borroWise.R;

/**
 * Created by ShayaneTan on 4/12/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private final static int NOTIF_ID = 101;
    private final static int MA_PENDING_INTENT = 10;
    private final static int SA_PENDING_INTENT = 11;
    private static int COUNT = 0;

    private DatabaseOpenHelper dbHelper;

    @Override
    public void onReceive(Context context, Intent intent2) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        Intent intent = new Intent();
        intent.setClass(context, ViewTransactionListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,MA_PENDING_INTENT, intent, PendingIntent.FLAG_UPDATE_CURRENT);//flag_update_current depends on the extras

        Intent secondact_intent= new Intent();
        secondact_intent.setClass(context,ViewTransactionListActivity.class);
        PendingIntent second_pendingIntent = PendingIntent.getActivity(context,SA_PENDING_INTENT, secondact_intent, PendingIntent.FLAG_UPDATE_CURRENT);//flag_update_current depends on the extras

        //create a notification
        NotificationCompat.Builder notif_builder = null;

        dbHelper = DatabaseOpenHelper.getInstance(context);
        int transactionId = intent2.getExtras().getInt(Transaction.COLUMN_ID);
        String tranType = intent2.getExtras().getString(Transaction.COLUMN_CLASSIFICATION);

        if(tranType.equalsIgnoreCase(Transaction.ITEM_TYPE)){
            ItemTransaction it = (ItemTransaction) dbHelper.queryTransaction(transactionId);
            String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(it.getDueDate()));
            notif_builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notif)
                        .setContentTitle("BorroWise pending item")
                        .setContentText("Hurry! The item " + it.getName() + " will already be due on " + dateString)
                        .setContentIntent(pendingIntent)
                        .setTicker("BorroWise")
                        .setNumber(COUNT)
                        .setAutoCancel(true);
        }else if(tranType.equalsIgnoreCase(Transaction.MONEY_TYPE)){
            MoneyTransaction mt = (MoneyTransaction) dbHelper.queryTransaction(transactionId);
            String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(mt.getDueDate()));
            notif_builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_notif)
                    .setContentTitle("BorroWise pending amount due")
                    .setContentText("Hurry! Your debt of PHP " + mt.getTotalAmountDue() + " will already be due on " + dateString)
                    .setContentIntent(pendingIntent)
                    .setTicker("BorroWise")
                    .setNumber(COUNT)
                    .setAutoCancel(true);
        }

        if(notif_builder != null) {
            //submit notification to the notification manager
            NotificationManager notif_manager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
            notif_manager.notify(NOTIF_ID, notif_builder.build());
            COUNT++;
        }
    }
}
