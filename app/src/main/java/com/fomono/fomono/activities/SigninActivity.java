package com.fomono.fomono.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fomono.fomono.R;
import com.fomono.fomono.utils.FavoritesUtil;
import com.parse.ParseUser;


public class SigninActivity extends AppCompatActivity {
    ImageView ivSignin;
    EditText etUserId;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ivSignin = (ImageView)  findViewById(R.id.ivSignin);
        etUserId = (EditText) findViewById(R.id.etUserId);
        etPassword = (EditText) findViewById(R.id.etPassword);


        //Fomono Login
        ivSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFomono(etUserId.getText().toString(), etPassword.getText().toString() );
            }
        });

    }

    private void loginWithFomono(String userId, String password) {
        ParseUser.logInInBackground(userId, password, (user, e) -> {
            if (user != null) {
                FavoritesUtil.getInstance();
                homePageIntent();
            } else {
                Toast.makeText(SigninActivity.this,"Login Failed" +e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void homePageIntent() {
        Intent i = new Intent(SigninActivity.this, FomonoActivity.class);
        startActivity(i);
    }
}
