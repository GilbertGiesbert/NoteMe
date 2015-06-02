package com.mediaworx.noteme.navigation;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mediaworx.noteme.R;

/**
 * Activities that inherit from NavigationDrawerActivity have the ability to show and use a NavigationDrawer.
 */
public abstract class NavigationDrawerActivity extends ActionBarActivity{

    private static final String TAG = NavigationDrawerActivity.class.getSimpleName();

    private NavigationDrawerAdapter drawerAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle   drawerToggle;

    protected abstract int getMainContentLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.navigationdrawer_activity);
        setMainContentView();

        initNavigationDrawer();
        getSupportActionBar().setTitle(getTitle());
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState){
        Log.d(TAG, "onPostCreate()");
        super.onPostCreate(savedInstanceState);

        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");

        int itemId = item.getItemId();

        String s_itemId = getResources().getResourceEntryName(itemId);// to view in debugger
        Log.d(TAG, "itemId="+s_itemId);

        if(drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void setMainContentView(){
        Log.d(TAG, "setMainContentView()");

        ViewStub stub = (ViewStub)findViewById(R.id.vs_mainView);
        stub.setLayoutResource(getMainContentLayoutId());
        stub.inflate();
    }

    private void initNavigationDrawer(){
        Log.d(TAG, "initNavigationDrawer()");

        drawerAdapter = new NavigationDrawerAdapter(this);

        ListView lv_drawer = (ListView) findViewById(R.id.lv_navigationDrawer_list);
        lv_drawer.setAdapter(drawerAdapter);
        lv_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick()");

                handleNavigationDrawerItemClick(position);
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.dl_navigationDrawer_root);
        drawerToggle = new DrawerToggle( this, drawerLayout, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        boolean isTopLevelActivity = isTopLevelActivity();
        Log.d(TAG, "isTopLevelActivity="+isTopLevelActivity);
        drawerToggle.setDrawerIndicatorEnabled(isTopLevelActivity);
    }

    private boolean isTopLevelActivity(){
        Log.d(TAG, "isTopLevelActivity()");
        return NavigationDrawerItem.valueOf(this.getClass()) != null;
    }

    private void handleNavigationDrawerItemClick(int position){
        Log.d(TAG, "handleNavigationDrawerItemClick()");
        Log.d(TAG, "position="+position);

        NavigationDrawerItem clickedItem = (NavigationDrawerItem) drawerAdapter.getItem(position);
        Log.d(TAG, "clickedItem="+clickedItem);

        drawerLayout.closeDrawers();

        if(this.getClass() != clickedItem.getTargetActivityClass()){

            Intent intent = new Intent(this, clickedItem.getTargetActivityClass());
            startActivity(intent);
        }
    }

    private class DrawerToggle extends ActionBarDrawerToggle{

        public DrawerToggle(Activity activity, DrawerLayout drawerLayout, int openDrawerContentDescRes, int closeDrawerContentDescRes){
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            Log.d(TAG, "onDrawerOpened()");
            super.onDrawerOpened(drawerView);

            if(!isTopLevelActivity()){
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }

            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            Log.d(TAG, "onDrawerClosed()");
            super.onDrawerClosed(drawerView);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setTitle(getTitle());
        }
    }
}