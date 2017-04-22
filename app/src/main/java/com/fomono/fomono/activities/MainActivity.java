package com.fomono.fomono.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fomono.fomono.R;
import com.fomono.fomono.utils.FavoritesUtil;
import com.parse.GetCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (ParseAnonymousUtils.isLinked(currentUser)) {
            FavoritesUtil.getInstance();
            Intent i = new Intent(this, LoginUserActivity.class);
            startActivity(i);
        } else {
            currentUser.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    FavoritesUtil.getInstance();
                    Intent i = new Intent(MainActivity.this, FomonoActivity.class);
                    startActivity(i);
                }
            });
        }

    }
}
