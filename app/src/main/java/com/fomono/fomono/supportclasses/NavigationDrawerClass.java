package com.fomono.fomono.supportclasses;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.fomono.fomono.R;
import com.fomono.fomono.activities.FomonoFilterActivity;
import com.fomono.fomono.activities.LoginUserActivity;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

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
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            MenuItem item = navigationView.getMenu().findItem(R.id.drawerSignOutId);
            item.setTitle(R.string.drawer_menu_signin);
        }
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
                //TODO: implement about screen
                Toast.makeText(mContext, "This app is awesome.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.drawerFilterId:
                Intent filterIntent = new Intent(mContext,FomonoFilterActivity.class);
                mContext.startActivity(filterIntent);
                break;
            case R.id.drawerSignOutId:
                if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                    ParseUser.logOutInBackground();
                }
                Intent i = new Intent(mContext, LoginUserActivity.class);
                mContext.startActivity(i);
            default:
                break;
        }

        menuItem.setChecked(true);
     //   mContext.setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }


}
