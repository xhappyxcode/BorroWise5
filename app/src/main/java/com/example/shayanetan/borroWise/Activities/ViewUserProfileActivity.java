package com.example.shayanetan.borrowise.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.shayanetan.borrowise.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise.Adapters.ViewPagerAdapter;
import com.example.shayanetan.borrowise.Fragments.ViewUserBorrowedFragment;
import com.example.shayanetan.borrowise.Fragments.ViewUserLentFragment;
import com.example.shayanetan.borrowise.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise.Models.User;
import com.example.shayanetan.borrowise.Views.SlidingTabLayout;

import edu.mobapde.borroWise.R;

public class ViewUserProfileActivity extends AppCompatActivity implements
        ViewUserBorrowedFragment.OnFragmentInteractionListener,
        ViewUserLentFragment.OnFragmentInteractionListener{


    private Toolbar toolbar;
    private TextView toolbar_title;

    private ImageView imageView;
    private TextView tv_name, tv_ratingbar;
    private RatingBar ratingBar;

    private SlidingTabLayout slidingTab;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private static String TITLE_TAB1 = "BORROWED";
    private static String TITLE_TAB2 = "LENT";

    private ViewUserBorrowedFragment borrowFragment;
    private ViewUserLentFragment lentFragment;
    private DatabaseOpenHelper dbHelper;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());
        imageView = (ImageView) findViewById(R.id.img_userprofile);
        tv_name = (TextView) findViewById(R.id.tv_userprofile_name);
        tv_ratingbar = (TextView) findViewById(R.id.tv_userprofile_rating);
        ratingBar = (RatingBar) findViewById(R.id.rb_userprofile_rating);

        setProfileDetails();

        // transactionsCursorAdapter = new TransactionsCursorAdapter(getBaseContext(),null);
        viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.view_pager_userprofile);

        borrowFragment = new ViewUserBorrowedFragment();
        borrowFragment.setOnFragmentInteractionListener(this);

        lentFragment = new ViewUserLentFragment();
        lentFragment.setOnFragmentInteractionListener(this);

        viewPagerAdapter.addFragment(lentFragment, TITLE_TAB1);
        viewPagerAdapter.addFragment(borrowFragment, TITLE_TAB2);

        // viewPagerAdapter.addFragment(new TransactionLentFragment(), TITLE_TAB2);
        viewPager.setAdapter(viewPagerAdapter);

        slidingTab = (SlidingTabLayout) findViewById(R.id.sliding_tab_userprofile);
        // True if Tabs are same Width
        slidingTab.setDistributeEvenly(true);
        slidingTab.setViewPager(viewPager);

    }

    public void setProfileDetails(){
        User u = dbHelper.queryUser(getIntent().getExtras().getInt(User.COLUMN_ID));
        username = u.getName();
        tv_name.setText(username);
        ratingBar.setRating((float) u.getTotalRate());
        tv_ratingbar.setText(String.format("%.2f", u.getTotalRate()));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_user_profile, menu);
        return true;
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
    public void deleteTransaction(HistoryCursorAdapter adapter, int id, String type, String classification) {

    }

    @Override
    public void retrieveTransaction(HistoryCursorAdapter adapter, String viewType) {
        Cursor cursor = null;
        if(viewType.equalsIgnoreCase(ViewUserLentFragment.VIEW_TYPE)) {
            cursor= dbHelper.queryAllLendTransactionsGivenUser(username);
        }
        else if(viewType.equalsIgnoreCase(ViewUserBorrowedFragment.VIEW_TYPE)){
            cursor= dbHelper.queryAllBorrowedTransactionsGivenUser(username);
        }
        adapter.swapCursor(cursor);
    }


}
