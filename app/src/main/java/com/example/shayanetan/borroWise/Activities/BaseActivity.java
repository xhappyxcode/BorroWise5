package com.example.shayanetan.borrowise.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import edu.mobapde.borroWise.R;


/*
    Edited 07/16//2016 by Stephanie Dy
 */

public class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static int CURRENT_ID = 0;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private Toolbar toolbar;
    private TextView toolbar_title;

    private ActionBarDrawerToggle drawerToggle;
    private FrameLayout activityContainer;
    private ListView listView;

    private int selectedNavItemId;


    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        activityContainer = (FrameLayout) drawerLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(drawerLayout);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
        navigationView.setCheckedItem(CURRENT_ID);

        setUpNavView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void setToolbar_title(int title){
        toolbar_title.setText(title);
    }

    private void selectDrawerItem(MenuItem menuItem) {

        Intent i = new Intent();
        CURRENT_ID = menuItem.getItemId();
        switch(menuItem.getItemId()) {
            case R.id.menuitem_transaction:
                i.setClass(getBaseContext(),ViewTransactionListActivity.class);
                break;
            case R.id.menuitem_history:
                i.setClass(getBaseContext(),HistoryActivity.class);
                break;
            case R.id.menuitem_account:
                i.setClass(getBaseContext(),ViewUserActivity.class);
                break;
            case R.id.menuitem_search:
                i.setClass(getBaseContext(), SearchActivity.class);
                break;
            default:
                i.setClass(getBaseContext(),ViewTransactionListActivity.class);
        }

        drawerLayout.closeDrawers();
        startActivity(i);
        overridePendingTransition(0, 0);
    }

    protected boolean useToolbar() {
        return true;
    }

    protected void setUpNavView() {

        if (useDrawerToggle()) { // use the hamburger menu
            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                    R.string.drawer_open,
                    R.string.drawer_close);

            drawerLayout.setDrawerListener(drawerToggle);
            drawerToggle.syncState();
        } else if (useToolbar() && getSupportActionBar() != null) {
            // Use home/back button instead
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        }

    }

    protected boolean useDrawerToggle() {
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        selectedNavItemId = menuItem.getItemId();

        return onOptionsItemSelected(menuItem);
    }

}