package com.fomono.fomono.supportclasses;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.fomono.fomono.R;
import com.fomono.fomono.activities.FomonoFilterActivity;

/**
 * Created by jsaluja on 4/14/2017.
 */

public class NavigationDrawerClass {
    private Context mContext;
    private DrawerLayout mDrawer;

    public NavigationDrawerClass(Context mContext, DrawerLayout mDrawer) {
        this.mContext = mContext;
        this.mDrawer = mDrawer;
    }

    public void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked

        switch (menuItem.getItemId()) {
            case R.id.drawerAboutId:
                Intent aboutIntent = new Intent(mContext,FomonoFilterActivity.class);
                mContext.startActivity(aboutIntent);
                break;
            case R.id.drawerFilterId:
                Intent filterIntent = new Intent(mContext,FomonoFilterActivity.class);
                mContext.startActivity(filterIntent);
                break;
            case R.id.drawerHomeId:
                Intent userProfileIntent = new Intent(mContext,FomonoFilterActivity.class);
                mContext.startActivity(userProfileIntent);
                break;
            default:
                break;
        }

        menuItem.setChecked(true);
     //   mContext.setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }


}
