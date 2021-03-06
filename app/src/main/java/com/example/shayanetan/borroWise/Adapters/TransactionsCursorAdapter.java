package com.example.shayanetan.borrowise.Adapters;

/**
 * Created by ShayaneTan on 3/11/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shayanetan.borrowise.Models.Transaction;
import com.example.shayanetan.borrowise.Models.User;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.mobapde.borroWise.R;

/**
 * Created by ShayaneTan on 3/11/2016.
 * Edited by Stephanie pn 7/15/2016
 */
public class TransactionsCursorAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder>{
    public static final int BTN_TYPE_RETURN = 1;
    public static final int BTN_TYPE_LOST = 2;
    public static final int BTN_TYPE_PARTIAL = 3;

    public static final int TYPE_ITEM = 1;
    public static final int TYPE_MONEY = 2;
    public static final int TRAN_ID = 0;
    private OnButtonClickListener mOnClickListener;

    public TransactionsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    public interface OnButtonClickListener {
        // type is whether it is money or item
        public void onButtonClick(int id, int type, String item_name);
    }

    public void setmOnClickListener(OnButtonClickListener m){
        this.mOnClickListener = m;
    }

    @Override
    public int getItemViewType(int position) {
        Cursor itemCursor = super.getCursor();
        itemCursor.moveToPosition(position);
        if(itemCursor.getString(itemCursor.getColumnIndex(Transaction.COLUMN_CLASSIFICATION)).equalsIgnoreCase("item"))
            return TYPE_ITEM;
        else
            return TYPE_MONEY;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, Cursor cursor) {

        String name = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME));
        int id = cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_ID));
        String dueDate = parseMillisToDate(cursor.getLong(cursor.getColumnIndex(Transaction.COLUMN_DUE_DATE)));
        int daysleft = getDaysBetween(new Date(cursor.getLong(cursor.getColumnIndex(Transaction.COLUMN_DUE_DATE))));
        String transactionAttribute1 = cursor.getString(cursor.getColumnIndex("Attribute1")); //item and description
        String transactionAttribute2 = cursor.getString(cursor.getColumnIndex("Attribute2"));
        String transactionAttribute3 = cursor.getString(cursor.getColumnIndex("Attribute3")); //path ng picture file

        switch (viewHolder.getItemViewType()) {
            case TYPE_ITEM:
                File imgFile = new  File(transactionAttribute3);
                if(imgFile.exists()){
                   // ItemTransaction.bmpOptions.inSampleSize = 8;
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    myBitmap = getRoundedShape(myBitmap);
                    ((BorrowedItemViewHolder)viewHolder).img_item.setImageBitmap(myBitmap);
                    ((BorrowedItemViewHolder)viewHolder).img_item.setScaleType(ImageView.ScaleType.CENTER);

                }
                ((BorrowedItemViewHolder)viewHolder).tv_account_item.setText(name);
                ((BorrowedItemViewHolder)viewHolder).tv_duedate_val.setText(dueDate);
                ((BorrowedItemViewHolder)viewHolder).tv_itemname.setText(transactionAttribute1);
                ((BorrowedItemViewHolder)viewHolder).item_container.setTag(R.id.key_entry_item_name, transactionAttribute1);
                ((BorrowedItemViewHolder)viewHolder).item_container.setTag(R.id.key_entry_id, id);
                ((BorrowedItemViewHolder)viewHolder).item_container.setTag(R.id.key_entry_type, TYPE_ITEM);
                ((BorrowedItemViewHolder)viewHolder).item_container.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BorrowedItemViewHolder vh = (BorrowedItemViewHolder) viewHolder;

                        int tran_id = Integer.parseInt(vh.item_container.getTag(R.id.key_entry_id).toString());
                        int tran_type = Integer.parseInt(vh.item_container.getTag(R.id.key_entry_type).toString());
                        String item_name = vh.item_container.getTag(R.id.key_entry_item_name).toString();
                        mOnClickListener.onButtonClick(tran_id, tran_type, item_name);
                    }
                });

                if(daysleft < 0) {
                    /* if overdue, change label to overdue and text color to red*/
                    int daysOverdue = daysleft * -1;
                    String message = "";

                    if(daysOverdue == 1 || daysOverdue == 0)
                        message = daysOverdue +" day overdue";
                    else
                        message = daysOverdue +" days overdue";

                    ((BorrowedItemViewHolder)viewHolder).img_item_day.setColorFilter(Color.RED);
                    ((BorrowedItemViewHolder)viewHolder).tv_daysleftitem_val.setTextColor(Color.RED);
                    ((BorrowedItemViewHolder)viewHolder).tv_daysleftitem_val.setText(message);
                } else {
                    String message = "";

                    if(daysleft == 1 || daysleft == 0)
                        message = daysleft +" day left";
                    else
                        message = daysleft +" days left";

                    ((BorrowedItemViewHolder)viewHolder).tv_daysleftitem_val.setText(message);
                }
                break;

            case TYPE_MONEY:

                ((BorrowedMoneyViewHolder)viewHolder).tv_account_money.setText(name);
                ((BorrowedMoneyViewHolder)viewHolder).tv_duedate_val.setText(dueDate);
                ((BorrowedMoneyViewHolder)viewHolder).tv_amount.setText(transactionAttribute2);
                ((BorrowedMoneyViewHolder)viewHolder).money_container.setTag(R.id.key_entry_item_name, transactionAttribute2);
                ((BorrowedMoneyViewHolder)viewHolder).money_container.setTag(R.id.key_entry_id, id);
                ((BorrowedMoneyViewHolder)viewHolder).money_container.setTag(R.id.key_entry_type, TYPE_MONEY);
                ((BorrowedMoneyViewHolder)viewHolder).money_container.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BorrowedMoneyViewHolder vh = (BorrowedMoneyViewHolder) viewHolder;

                        int tran_id = Integer.parseInt(vh.money_container.getTag(R.id.key_entry_id).toString());
                        int tran_type = Integer.parseInt(vh.money_container.getTag(R.id.key_entry_type).toString());
                        String item_name = vh.money_container.getTag(R.id.key_entry_item_name).toString();
                        mOnClickListener.onButtonClick(tran_id, tran_type, item_name);
                    }
                });
                if(daysleft < 0) {
                    int daysOverdue = daysleft * -1;

                    String message = "";
                    if(daysOverdue == 1 || daysOverdue == 0)
                        message = daysOverdue +" day overdue";
                    else
                        message = daysOverdue +" days overdue";

                    ((BorrowedMoneyViewHolder)viewHolder).img_money_day.setColorFilter(Color.RED);
                    ((BorrowedMoneyViewHolder)viewHolder).tv_daysleftmoney_val.setTextColor(Color.RED);
                    ((BorrowedMoneyViewHolder)viewHolder).tv_daysleftmoney_val.setText(message);
                } else {

                    String message = "";

                    if(daysleft == 1 || daysleft == 0)
                        message = daysleft +" day left";
                    else
                        message = daysleft +" days left";
                    ((BorrowedMoneyViewHolder)viewHolder).tv_daysleftmoney_val.setText(message);
                }
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transaction, parent, false); //same size as parent but not binded to the parent
            return new BorrowedItemViewHolder(v);
        }else if(viewType == TYPE_MONEY){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_money_transaction, parent, false); //same size as parent but not binded to the parent
            return new BorrowedMoneyViewHolder(v);
        }

        return null;
    }

    /*********************************************
     * ITEM VIEW HOLDER
     *********************************************/
    public class BorrowedItemViewHolder extends RecyclerView.ViewHolder {
        // TODO

        TextView tv_account_item, tv_itemname, tv_duedate_val, tv_daysleftitem_val;
        ImageView img_item, img_item_day;
        View item_container;

        public BorrowedItemViewHolder(View itemView) {
            super(itemView);
            tv_account_item = (TextView) itemView.findViewById(R.id.tv_account_item);
            tv_itemname = (TextView) itemView.findViewById(R.id.tv_itemname);
            tv_duedate_val = (TextView) itemView.findViewById(R.id.tv_duedateitem_val);
            tv_daysleftitem_val = (TextView) itemView.findViewById(R.id.tv_daysleftitem_val);
            img_item_day = (ImageView) itemView.findViewById(R.id.img_item_day);

            img_item = (ImageView) itemView.findViewById(R.id.img_item);
            img_item.setMaxWidth(img_item.getHeight());

            item_container = itemView.findViewById(R.id.item_container);
        }

    }

    /*********************************************
     * MONEY VIEW HOLDER
     *********************************************/
    public class BorrowedMoneyViewHolder extends RecyclerView.ViewHolder{
        // TODO

        TextView tv_account_money, tv_amount, tv_duedate_val, tv_daysleftmoney_val;
        View money_container;
        ImageView img_money_day;

        public BorrowedMoneyViewHolder(View itemView) {
            super(itemView);
            tv_account_money = (TextView) itemView.findViewById(R.id.tv_account_money);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_duedate_val = (TextView) itemView.findViewById(R.id.tv_duedatemoney_val);
            tv_daysleftmoney_val = (TextView) itemView.findViewById(R.id.tv_daysleftmoney_val);
            img_money_day = (ImageView) itemView.findViewById(R.id.img_money_day);

            money_container = itemView.findViewById(R.id.money_container);
        }
    }

    public String parseMillisToDate(long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        Date resultdate = new Date(millis);
        return sdf.format(resultdate);
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

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        /* makes the picture round */
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
}

