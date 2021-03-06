package com.example.shayanetan.borrowise.Models;

/**
 * Created by kewpe on 10 Mar 2016.
 */
public abstract class Transaction {
    public static final String TABLE_NAME = "transactions";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CLASSIFICATION = "classification"; /* item or money */
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_TYPE = "type"; /* borrowed or lent */
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_DUE_DATE = "due_date";
    public static final String COLUMN_RETURN_DATE = "return_date";
    public static final String COLUMN_RATE = "rate";
    public static final String COLUMN_ALARM_TIME = "alarm_time"; /*added this*/
    public static final String COLUMN_DAYS_LEFT = "days_left"; /*added this*/

    public static final String TRANSACTION_ITEM_TYPE ="item_type";
    public static final String ITEM_NAME = "item_name";
    public static final String ITEM_TYPE = "Item";
    public static final String MONEY_TYPE = "Money";

    public static final String LEND_ACTION = "lend";
    public static final String BORROWED_ACTION = "borrow";


    protected int id;
    protected String classification;
    protected int userID;
    protected String type;
    protected int status;
    protected long startDate;
    protected long dueDate;
    protected long returnDate;
    protected double rate;
    private String alarmTime; /*added this*/
    private int daysLeft; /*added this*/

    public Transaction(){};
    public Transaction(String classification, int userID, String type, int status, long startDate, long dueDate, long returnDate, double rate, String alarmTime, int daysLeft) {
        this.classification = classification;
        this.userID = userID;
        this.type = type;
        this.status = status;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.rate = rate;
        this.setAlarmTime(alarmTime);
        this.setDaysLeft(daysLeft);
    }

    public Transaction(String classification, int userID, String type, int status, long startDate, long dueDate, long returnDate, double rate) {
        this.classification = classification;
        this.userID = userID;
        this.type = type;
        this.status = status;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.rate = rate;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int user_id) {
        this.userID = userID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public long getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(long returnDate) {
        this.returnDate = returnDate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }
}
