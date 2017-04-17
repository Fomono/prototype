
package com.fomono.fomono.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.fomono.fomono.R;
import com.fomono.fomono.models.user.User;
import com.fomono.fomono.services.UserService;
import com.fomono.fomono.utils.FavoritesUtil;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class LoginUserActivity extends AppCompatActivity {
    Button btnFB;
    Button btnFomono;
    TextView btnSignup;
    Profile mFbProfile;
    ParseUser parseUser;
    UserService uService;
    EditText etUserId;
    EditText etPassword;
    Button btnGuestLogin;

    String name = null, email = null;

    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        uService = new UserService();

        btnFB = (Button) findViewById(R.id.btnFB);
        btnFomono = (Button) findViewById(R.id.btnFomono);
        btnSignup = (TextView) findViewById(R.id.btnSignup) ;
        mFbProfile = Profile.getCurrentProfile();
        etUserId = (EditText) findViewById(R.id.etUserId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnGuestLogin =(Button) findViewById(R.id.btnGuestLogin);

        //  Use this to output your Facebook Key Hash to Logs
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.fomono.fomono",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {

        }

        //Guest Login
        btnGuestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePageIntent();
            }
        });

        //Firsttime signup
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginUserActivity.this,SignupActivity.class);
                startActivity(i);
            }
        });

        //Fomono Login
        btnFomono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    loginWithFomono(etUserId.getText().toString(), etPassword.getText().toString() );
            }
        });

        //FaceBook Login
        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser remove = ParseUser.getCurrentUser();
                loginWithFB();
               /* if (!ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
                    linkCurrentUsertoFB();
                } else if(ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())){
                    loginWithFB();
                }*/
            }
        });
    }

    private void loginWithFomono(String userId, String password) {
        ParseUser.logInInBackground(userId, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    FavoritesUtil.getInstance().initialize(user);
                    homePageIntent();
                } else {
                    Toast.makeText(LoginUserActivity.this,"Login Failed" +e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void loginWithFB() {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginUserActivity.this, mPermissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {

                if (user == null) {
                    Log.d("Login", "Cancelled Facebook login.");
                } else if (user.isNew()) {
                    Log.d("Login", "Facebook Login Successful");
                    //getUserDetailsFromFB();
                    linkCurrentUsertoFB();
                    FavoritesUtil.getInstance().initialize(user);
                    homePageIntent();
                } else {
                    Log.d("Login", "User logged in through Facebook!");
                    getUserDetailsFromParse();
                    FavoritesUtil.getInstance().initialize(user);
                    homePageIntent();
                }
            }
        });

    }

    public void linkCurrentUsertoFB() {
        ParseFacebookUtils.linkWithReadPermissionsInBackground(ParseUser.getCurrentUser(), LoginUserActivity.this,
                mPermissions, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                                getUserDetailsFromFB();
                                Log.d("Login", "Current User logged in with Facebook!");
                        }else{
                            Toast.makeText(LoginUserActivity.this,"Linking to FB Failed" +e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void saveNewUser() {
        User u = new User();
        String[] nameArray = name.split("\\s+");
        u.setFirstName(nameArray[0]);
        u.setLastName(nameArray[1]);
        u.setEmail(email);
        uService.saveParseUser(u);
        homePageIntent();
    }

    public void homePageIntent(){
        Intent i = new Intent(LoginUserActivity.this, FomonoActivity.class);
        startActivity(i);
    }

    private void getUserDetailsFromFB() {

        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,name,picture");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {

                            Log.d("Response", response.getRawResponse());

                            email = response.getJSONObject().getString("email");
                            name = response.getJSONObject().getString("name");

                            JSONObject picture = response.getJSONObject().getJSONObject("picture");
                            JSONObject data = picture.getJSONObject("data");

                            //  Returns a 50x50 profile picture
                            String pictureUrl = data.getString("url");

                            Log.d("Profile pic", "url: " + pictureUrl);

                            new ProfilePhotoAsync(pictureUrl).execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();

    }


    private void getUserDetailsFromParse() {
        parseUser = ParseUser.getCurrentUser();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }


    class ProfilePhotoAsync extends AsyncTask<String, String, String> {
        public Bitmap bitmap;
        String url;

        public ProfilePhotoAsync(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(String... params) {
            bitmap = DownloadImageBitmap(url);
            uService.saveParseFile(bitmap);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            saveNewUser();
        }
    }

    public static Bitmap DownloadImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("IMAGE", "Error getting bitmap", e);
        }
        return bm;
    }

}