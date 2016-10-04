package com.example.shayanetan.borrowise3.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shayanetan.borrowise3.Models.PaymentHistory;
import com.example.shayanetan.borrowise3.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Joy on 8/8/2016.
 */
public class PaymentCursorAdapter extends CursorRecyclerViewAdapter<PaymentCursorAdapter.PaymentViewHolder> {


    public PaymentCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(final PaymentViewHolder viewHolder, Cursor cursor) {
        Double amount = cursor.getDouble(cursor.getColumnIndex(PaymentHistory.COLUMN_PAYMENT));
        long date = cursor.getLong(cursor.getColumnIndex(PaymentHistory.COLUMN_DATE));

        viewHolder.tv_amount.setText(String.format("%.2f", amount));
        viewHolder.tv_date.setText(parseMillisToDate(date));
        viewHolder.payment_container.setTag(cursor.getInt(cursor.getColumnIndex(PaymentHistory.COLUMN_ID)));

    }

    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_payment, parent, false); //same size as parent but not binded to the parent
        return new PaymentViewHolder(v);
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tv_amount, tv_date;
        View payment_container;

        public PaymentViewHolder(View itemView) {
            super(itemView);

            payment_container = (View) itemView.findViewById(R.id.payment_container);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_paymentAmt);
            tv_date = (TextView) itemView.findViewById(R.id.tv_paymentDate);
        }

    }

    public String parseMillisToDate(long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        Date resultdate = new Date(millis);
        return sdf.format(resultdate);
    }


}
