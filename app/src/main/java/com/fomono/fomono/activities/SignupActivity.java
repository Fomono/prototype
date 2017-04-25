package com.fomono.fomono.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fomono.fomono.R;
import com.fomono.fomono.utils.FavoritesUtil;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {
    Button btnSignup;
    EditText etFirstName;
    EditText etLastName;
    EditText etEmail;
    EditText etUserId;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etUserId = (EditText) findViewById(R.id.etUserId);
        etPassword = (EditText) findViewById(R.id.etPassword);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser p = ParseUser.getCurrentUser();
                if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                    setupFomonoLogin(etUserId.getText().toString(), etPassword.getText().toString(), etEmail.getText().toString(),
                            etFirstName.getText().toString(), etLastName.getText().toString());
                }
            }
        });

    }

    private void setupFomonoLogin(String userId, String password, String email, String firstName,
                                  String lastName) {
        ParseUser user = ParseUser.getCurrentUser();
        user.setUsername(userId);
        user.setPassword(password);
        user.setEmail(email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    FavoritesUtil.getInstance().initialize(ParseUser.getCurrentUser());
                    homePageIntent();
                } else {
                    Toast.makeText(SignupActivity.this,"Signup failed"+e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public void homePageIntent() {
        Intent i = new Intent(SignupActivity.this, FomonoActivity.class);
        startActivity(i);
    }
}
