package com.example.shayanetan.borrowise.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.shayanetan.borrowise.Models.Transaction;
import com.example.shayanetan.borrowise.Models.User;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.mobapde.borroWise.R;

/**
 * Created by Joy on 9/9/2016.
 */
public class TransactionHistoryCursorAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder> {

    public static final int TYPE_ITEM_TRANSACTION = 1;
    public static final int TYPE_MONEY_TRANSACTION = 2;
    public static final int TYPE_ITEM_HISTORY = 3;
    public static final int TYPE_MONEY_HISTORY = 4;

    public  static final String TYPE_BORROWED = "borrowed";
    public  static final String TYPE_LEND= "lend";

    private OnButtonClickListener OnTransactionClickListener;
    private OnButtonClickListener OnHistoryClickListener;
    private OnButtonClickListener OnHistoryLongClickListener;

    private String viewTypeFinal;

    public void setOnTransactionClickListener(OnButtonClickListener onTransactionClickListener) {
        OnTransactionClickListener = onTransactionClickListener;
    }

    public void setOnHistoryClickListener(OnButtonClickListener onHistoryClickListener) {
        OnHistoryClickListener = onHistoryClickListener;
    }

    public void setOnHistoryLongClickListener(OnButtonClickListener onHistoryLongClickListener) {
        OnHistoryLongClickListener = onHistoryLongClickListener;
    }

    public OnButtonClickListener getOnTransactionClickListener() {
        return OnTransactionClickListener;
    }

    public OnButtonClickListener getOnHistoryClickListener() {
        return OnHistoryClickListener;
    }

    public OnButtonClickListener getOnHistoryLongClickListener() {
        return OnHistoryLongClickListener;
    }

    public interface OnButtonClickListener {
        public void onButtonClick(int id, String type, String classification, String item_name);
    }

    @Override
    public int getItemViewType(int position) {
        Cursor itemCursor = super.getCursor();
        itemCursor.moveToPosition(position);

        Log.v("STATUS....", itemCursor.getString(itemCursor.getColumnIndex(Transaction.COLUMN_STATUS)));


        if(itemCursor.getString(itemCursor.getColumnIndex(Transaction.COLUMN_CLASSIFICATION)).equalsIgnoreCase("item") &&
                itemCursor.getString(itemCursor.getColumnIndex(Transaction.COLUMN_STATUS)).equalsIgnoreCase("0"))
            return TYPE_ITEM_TRANSACTION;
        else if(itemCursor.getString(itemCursor.getColumnIndex(Transaction.COLUMN_CLASSIFICATION)).equalsIgnoreCase("money") &&
                itemCursor.getString(itemCursor.getColumnIndex(Transaction.COLUMN_STATUS)).equalsIgnoreCase("0"))
            return TYPE_MONEY_TRANSACTION;
        else if(itemCursor.getString(itemCursor.getColumnIndex(Transaction.COLUMN_CLASSIFICATION)).equalsIgnoreCase("item") &&
                (itemCursor.getString(itemCursor.getColumnIndex(Transaction.COLUMN_STATUS)).equalsIgnoreCase("1") ||
                        itemCursor.getString(itemCursor.getColumnIndex(Transaction.COLUMN_STATUS)).equalsIgnoreCase("-1")) )
            return TYPE_ITEM_HISTORY;
        else
            return TYPE_MONEY_HISTORY;
    }

    public TransactionHistoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, Cursor cursor) {
        /* common attributes if transaction is ongoing or history */
        String name = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME));
        int id = cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_ID));
        String type = cursor.getString(cursor.getColumnIndex(Transaction.COLUMN_TYPE));
        if(type.equalsIgnoreCase("borrow"))
            viewTypeFinal = TYPE_BORROWED;
        else
            viewTypeFinal = TYPE_LEND;
        String transactionAttribute1 = cursor.getString(cursor.getColumnIndex("Attribute1"));
        String transactionAttribute3 = cursor.getString(cursor.getColumnIndex("Attribute3"));

        /* attributes if transaction is ongoing */
        String dueDate = parseMillisToDate(cursor.getLong(cursor.getColumnIndex(Transaction.COLUMN_DUE_DATE)));
        int daysleft = getDaysBetween(new Date(cursor.getLong(cursor.getColumnIndex(Transaction.COLUMN_DUE_DATE))));
        String transactionAttribute2 = cursor.getString(cursor.getColumnIndex("Attribute2"));

        /* attributes if transaction is history */
        String returnDate="";
        double rating = cursor.getDouble(cursor.getColumnIndex(Transaction.COLUMN_RATE));
        int statusInteger = cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_STATUS));
        if( statusInteger == -1 || statusInteger == 0) {
            returnDate = "N/A";
        } else{
            returnDate = parseMillisToDate(cursor.getLong(cursor.getColumnIndex(Transaction.COLUMN_RETURN_DATE)));
        }

        String status = cursor.getString(cursor.getColumnIndex(Transaction.COLUMN_STATUS));
        String statusFinal = "";
        switch (status){
            case "1": statusFinal = "Returned"; break;
            case "-1": statusFinal = "Lost"; break;
            case "0": statusFinal = "Ongoing"; break;
        }

        switch(viewHolder.getItemViewType()) {
            case TYPE_ITEM_TRANSACTION:
                File imgFile = new  File(transactionAttribute3);
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    myBitmap = getRoundedShape(myBitmap);
                    ((ItemTransactionViewHolder)viewHolder).img_item.setImageBitmap(myBitmap);
                    ((ItemTransactionViewHolder)viewHolder).img_item.setScaleType(ImageView.ScaleType.CENTER);
                }
                ((ItemTransactionViewHolder)viewHolder).tv_account_item.setText(name);
                ((ItemTransactionViewHolder)viewHolder).tv_duedate_val.setText(dueDate);
                ((ItemTransactionViewHolder)viewHolder).tv_itemname.setText(transactionAttribute1);
                ((ItemTransactionViewHolder)viewHolder).item_container.setTag(R.id.key_entry_item_name,transactionAttribute1);
                ((ItemTransactionViewHolder)viewHolder).item_container.setTag(cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_ID)));
                ((ItemTransactionViewHolder)viewHolder).item_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ItemTransactionViewHolder vh = (ItemTransactionViewHolder)viewHolder ;
                        String item_name =  vh.item_container.getTag(R.id.key_entry_item_name).toString();

                        OnTransactionClickListener.onButtonClick(Integer.parseInt(v.getTag().toString()), viewTypeFinal, Transaction.ITEM_TYPE, item_name);
                    }
                });


                if(daysleft < 0) {
                    /* if overdue, change label to overdue and text color to red*/
                    int daysOverdue = daysleft * -1;

                    String message = "";

                    if(daysOverdue > 1)
                        message = daysOverdue+" days overdue";
                    else
                        message = daysOverdue+" day overdue";
                    ((ItemTransactionViewHolder)viewHolder).img_item_day.setColorFilter(Color.RED);
                    ((ItemTransactionViewHolder)viewHolder).tv_daysleft_val.setTextColor(Color.RED);
                    ((ItemTransactionViewHolder)viewHolder).tv_daysleft_val.setText(message);
                } else {

                    String message ="";
                    if(daysleft > 1)
                        message = daysleft+" days left";
                    else
                        message =  daysleft +" day left";

                    ((ItemTransactionViewHolder)viewHolder).tv_daysleft_val.setText(message);
                }
                break;
            case TYPE_MONEY_TRANSACTION:
                ((MoneyTransactionViewHolder)viewHolder).money_container.setTag(R.id.key_entry_id, id);
                ((MoneyTransactionViewHolder)viewHolder).tv_account_money.setText(name);
                ((MoneyTransactionViewHolder)viewHolder).tv_duedate_val.setText(dueDate);
                ((MoneyTransactionViewHolder)viewHolder).tv_amount.setText(transactionAttribute2);
                ((MoneyTransactionViewHolder)viewHolder).money_container.setTag(R.id.key_entry_item_name, transactionAttribute2);
                ((MoneyTransactionViewHolder)viewHolder).money_container.setTag(cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_ID)));
                ((MoneyTransactionViewHolder)viewHolder).money_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MoneyTransactionViewHolder vh = (MoneyTransactionViewHolder)viewHolder ;
                        String item_name =  vh.money_container.getTag(R.id.key_entry_item_name).toString();
                        OnTransactionClickListener.onButtonClick(Integer.parseInt(v.getTag().toString()), viewTypeFinal, Transaction.MONEY_TYPE, item_name);
                    }
                });
                if(daysleft < 0) {
                    int daysOverdue = daysleft * -1;

                    String message = "";

                    if(daysOverdue == 1 || daysOverdue == 0)
                        message = daysOverdue +" day overdue";
                    else
                        message = daysOverdue +" days overdue";

                    ((MoneyTransactionViewHolder)viewHolder).img_money_day.setColorFilter(Color.RED);
                    ((MoneyTransactionViewHolder)viewHolder).tv_daysleft_val.setText(message);
                    ((MoneyTransactionViewHolder)viewHolder).tv_daysleft_val.setTextColor(Color.RED);
                } else {

                    String message = "";
                    if(daysleft == 1 || daysleft == 0)
                        message = daysleft +" day left";
                    else
                        message = daysleft +" days left";
                    ((MoneyTransactionViewHolder)viewHolder).tv_daysleft_val.setText(message);
                }
                break;
            case TYPE_ITEM_HISTORY:
                File imgFile1 = new  File(transactionAttribute3);
                if(imgFile1.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile1.getAbsolutePath());
                    myBitmap = getRoundedShape(myBitmap);
                    ((ItemHistoryViewHolder)viewHolder).img_Hitem.setImageBitmap(myBitmap);
                    ((ItemHistoryViewHolder)viewHolder).img_Hitem.setScaleType(ImageView.ScaleType.CENTER);
                }
                ((ItemHistoryViewHolder)viewHolder).tv_Haccount_item.setText(name);
                ((ItemHistoryViewHolder)viewHolder).tv_Hitemname.setText(transactionAttribute1);
                ((ItemHistoryViewHolder)viewHolder).tv_Hretdateitem_val.setText(returnDate);

                ((ItemHistoryViewHolder)viewHolder).tv_Hstatusitem_val.setText(statusFinal);
                ((ItemHistoryViewHolder)viewHolder).rb_Hratingitem.setRating((float) rating);
                ((ItemHistoryViewHolder)viewHolder).item_container.setTag(R.id.key_entry_item_name, transactionAttribute1);
                ((ItemHistoryViewHolder)viewHolder).item_container.setTag(cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_ID)));
                ((ItemHistoryViewHolder)viewHolder).item_container.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        ItemHistoryViewHolder vh = (ItemHistoryViewHolder) viewHolder ;
                        String item_name =  vh.item_container.getTag(R.id.key_entry_item_name).toString();
                        OnHistoryLongClickListener.onButtonClick(Integer.parseInt(v.getTag().toString()), viewTypeFinal, Transaction.ITEM_TYPE, item_name);
                        return true;
                    }
                });
                ((ItemHistoryViewHolder)viewHolder).item_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ItemHistoryViewHolder vh = (ItemHistoryViewHolder) viewHolder ;
                        String item_name =  vh.item_container.getTag(R.id.key_entry_item_name).toString();
                        OnHistoryClickListener.onButtonClick(Integer.parseInt(v.getTag().toString()), viewTypeFinal, Transaction.ITEM_TYPE, item_name);
                    }
                });
                break;
            case TYPE_MONEY_HISTORY:
                ((MoneyHistoryViewHolder)viewHolder).tv_Haccount_money.setText(name);
                ((MoneyHistoryViewHolder)viewHolder).tv_Hretdatemoney_val.setText(returnDate);
                ((MoneyHistoryViewHolder)viewHolder).tv_Hstatusmoney_val.setText(statusFinal);
                ((MoneyHistoryViewHolder)viewHolder).tv_Hamount.setText(transactionAttribute1);
                ((MoneyHistoryViewHolder)viewHolder).rb_Hratingmoney.setRating((float) rating);
                ((MoneyHistoryViewHolder)viewHolder).money_container.setTag(R.id.key_entry_item_name, transactionAttribute1);
                ((MoneyHistoryViewHolder)viewHolder).money_container.setTag(cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_ID)));
                ((MoneyHistoryViewHolder)viewHolder).money_container.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        MoneyHistoryViewHolder vh = (MoneyHistoryViewHolder) viewHolder ;
                        String item_name =  vh.money_container.getTag(R.id.key_entry_item_name).toString();
                        OnHistoryLongClickListener.onButtonClick(Integer.parseInt(v.getTag().toString()), viewTypeFinal, Transaction.MONEY_TYPE, item_name);
                        return true;
                    }
                });
                ((MoneyHistoryViewHolder)viewHolder).money_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MoneyHistoryViewHolder vh = (MoneyHistoryViewHolder) viewHolder ;
                        String item_name =  vh.money_container.getTag(R.id.key_entry_item_name).toString();
                        OnHistoryClickListener.onButtonClick(Integer.parseInt(v.getTag().toString()), viewTypeFinal, Transaction.MONEY_TYPE, item_name);
                    }
                });
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch(viewType) {
            case TYPE_ITEM_TRANSACTION:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transaction, parent, false); //same size as parent but not binded to the parent
                return new ItemTransactionViewHolder(v);
            case TYPE_MONEY_TRANSACTION:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_money_transaction, parent, false); //same size as parent but not binded to the parent
                return new MoneyTransactionViewHolder(v);
            case TYPE_ITEM_HISTORY:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false); //same size as parent but not binded to the parent
                return new ItemHistoryViewHolder(v);
            case TYPE_MONEY_HISTORY:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_money_history, parent, false); //same size as parent but not binded to the parent
                return new MoneyHistoryViewHolder(v);
        }
        return null;
    }

    public class ItemTransactionViewHolder extends RecyclerView.ViewHolder {

        TextView tv_account_item, tv_itemname, tv_duedate_val, tv_daysleft_val;
        ImageView img_item, img_item_day;
        View item_container;

        public ItemTransactionViewHolder(View itemView) {
            super(itemView);
            tv_account_item = (TextView) itemView.findViewById(R.id.tv_account_item);
            tv_itemname = (TextView) itemView.findViewById(R.id.tv_itemname);
            tv_duedate_val = (TextView) itemView.findViewById(R.id.tv_duedateitem_val);
            tv_daysleft_val = (TextView) itemView.findViewById(R.id.tv_daysleftitem_val);

            img_item_day = (ImageView) itemView.findViewById(R.id.img_item_day);

            img_item = (ImageView) itemView.findViewById(R.id.img_item);
            img_item.setMaxWidth(img_item.getHeight());

            item_container = itemView.findViewById(R.id.item_container);
        }

    }

    public class MoneyTransactionViewHolder extends RecyclerView.ViewHolder{

        TextView tv_account_money, tv_amount, tv_duedate_val, tv_daysleft_val;
        View money_container;
        ImageView img_money_day;

        public MoneyTransactionViewHolder(View itemView) {
            super(itemView);
            tv_account_money = (TextView) itemView.findViewById(R.id.tv_account_money);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_duedate_val = (TextView) itemView.findViewById(R.id.tv_duedatemoney_val);
            tv_daysleft_val = (TextView) itemView.findViewById(R.id.tv_daysleftmoney_val);
            img_money_day = (ImageView) itemView.findViewById(R.id.img_money_day);

            money_container = itemView.findViewById( R.id.money_container);
        }
    }

    public class ItemHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView tv_Haccount_item, tv_Hitemname, tv_Hretdateitem_val, tv_Hstatusitem_val;
        ImageView img_Hitem;

        View item_container;
        RatingBar rb_Hratingitem;

        public ItemHistoryViewHolder(View itemView) {

            super(itemView);
            tv_Haccount_item = (TextView) itemView.findViewById(R.id.tv_Haccount_item);
            tv_Hitemname = (TextView) itemView.findViewById(R.id.tv_Hitemname);
            tv_Hretdateitem_val = (TextView) itemView.findViewById(R.id.tv_Hretdateitem_val);
            tv_Hstatusitem_val = (TextView) itemView.findViewById(R.id.tv_Hstatusitem_val);
            rb_Hratingitem = (RatingBar) itemView.findViewById(R.id.rb_Hratingitem);
            rb_Hratingitem.getProgressDrawable().setColorFilter(Color.parseColor("#f9a825"), PorterDuff.Mode.SRC_ATOP);

            img_Hitem = (ImageView) itemView.findViewById(R.id.img_Hitem);
            item_container = itemView.findViewById(R.id.Hitem_container);
        }

    }

    public class MoneyHistoryViewHolder extends RecyclerView.ViewHolder{

        TextView tv_Haccount_money, tv_Hamount, tv_Hretdatemoney_val, tv_Hstatusmoney_val;
        RatingBar rb_Hratingmoney;
        View money_container;

        public MoneyHistoryViewHolder(View itemView) {
            super(itemView);
            tv_Haccount_money = (TextView) itemView.findViewById(R.id.tv_Haccount_money);
            tv_Hamount = (TextView) itemView.findViewById(R.id.tv_Hamount);
            tv_Hretdatemoney_val = (TextView) itemView.findViewById(R.id.tv_Hretdatemoney_val);
            rb_Hratingmoney = (RatingBar) itemView.findViewById(R.id.rb_Hratingmoney);
            rb_Hratingmoney.getProgressDrawable().setColorFilter(Color.parseColor("#f9a825"), PorterDuff.Mode.SRC_ATOP);


            money_container = itemView.findViewById(R.id.Hmoney_container);
            tv_Hstatusmoney_val = (TextView) itemView.findViewById(R.id.tv_Hstatusmoney_val);
        }
    }

    public String parseMillisToDate(long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        Date resultdate = new Date(millis);
        return sdf.format(resultdate);
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        /* used to crop picture into round shape */
        int targetWidth = 160;
        int targetHeight = 160;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }

    public static Calendar getDatePart(Date date){
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

        return cal;                                  // return the date part
    }

    public int getDaysBetween(Date dueDate) {
        /* used to get the number of days between two dates */
        Date currentDate = new Date(Calendar.getInstance().getTimeInMillis());
        Calendar sDate = getDatePart(currentDate);
        Calendar eDate = getDatePart(dueDate);

        int daysBetween = 0;
        daysBetween = eDate.compareTo(sDate);       // if dueDate > currentDate, then value is greater than 0;
        if(daysBetween > 0) {
            daysBetween = 0;
            while (sDate.before(eDate)) {
                sDate.add(Calendar.DAY_OF_MONTH, 1);
                daysBetween++;
            }
        } else {
            daysBetween = 0;
            while (eDate.before(sDate)) {
                eDate.add(Calendar.DAY_OF_MONTH, 1);
                daysBetween--;
            }
        }

        return daysBetween;
    }
}
