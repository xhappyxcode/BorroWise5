package com.example.shayanetan.borrowise3.Models;

/**
 * Created by Joy on 7/25/2016.
 */
public class Notification {
    public static final String TABLE_NAME="notifications";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_DUE_DATE="due_date";
    public static final String COLUMN_NOTIF_DAYS="notif_days";
    public static final String COLUMN_NOTIF_TIME="notif_time";
    public static final String COLUMN_TRANSACTION_ID="transaction_id";

    private int id;
    private long dueDate;
    private int notifDays;
    private int notifTime;
    private int transactionId;

    public Notification() {

    }

    public Notification(int id, long dueDate, int notifDays, int notifTime, int transactionId) {
        this.id=id;
        this.dueDate=dueDate;
        this.notifDays=notifDays;
        this.notifTime=notifTime;
        this.transactionId=transactionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public int getNotifDays() {
        return notifDays;
    }

    public void setNotifDays(int notifDays) {
        this.notifDays = notifDays;
    }

    public int getNotifTime() {
        return notifTime;
    }

    public void setNotifTime(int notifTime) {
        this.notifTime = notifTime;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
