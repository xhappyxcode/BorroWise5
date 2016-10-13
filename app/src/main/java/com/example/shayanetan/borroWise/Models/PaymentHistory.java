package com.example.shayanetan.borrowise.Models;

/**
 * Created by Joy on 7/25/2016.
 */
public class PaymentHistory {
    public static final String TABLE_NAME="payment_history";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_DATE="date";
    public static final String COLUMN_PAYMENT="payment";
    public static final String COLUMN_TRANSACTION_ID="transaction_id";

    private int id; // this id is not dependent on the transaction_id
    private long date;
    private double payment;
    private int transaction_id;

    public PaymentHistory(int id, long date, double payment, int transaction_id) {
        this.id=id;
        this.date=date;
        this.payment=payment;
        this.transaction_id=transaction_id;
    }

    public PaymentHistory() {

    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }
}
