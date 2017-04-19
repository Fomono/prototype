package com.fomono.fomono.services;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.databinding.FragmentUserProfileBinding;
import com.fomono.fomono.models.user.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Saranu on 4/15/17.
 */

public class UserService {

    private static UserService instance;
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    boolean userUpdated;

    private UserService() {
    }

    public String saveParseFile(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                stream);
        byte[] imageBytes = stream.toByteArray();
        ParseFile file = new ParseFile("profile_picture_file", imageBytes);
        ParseUser finalPUser = ParseUser.getCurrentUser();
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    finalPUser.put("profilePicture", file.getUrl());
                    finalPUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.d("USER_SERVICE", "Profile Picture Saved successfully");
                        }
                    });


                }
            }
        });
        setUserUpdated(true);
        return (file.getUrl());

    }


    public Bitmap getBitMap(Context context, String filePath, Uri uri) {
        Bitmap bitmap = null;
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(uri, null);
            File imageFile = new File(filePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            InputStream input = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(input, null, options);
            Matrix matrix = new Matrix();
            if (rotate != 0) {
                matrix.preRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public String getPath(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static Uri getOutputMediaFileUri(Context context) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), FomonoApplication.APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(FomonoApplication.APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File file = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

            // wrap File object into a content provider
            // required for API >= 24
            // See http://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
            return FileProvider.getUriForFile(context, "com.fomono.fomono.fileprovider", file);
        }
        return null;
    }

    public static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    public User retriveUserFromParseUser(ParseUser pUser) {
        User user = new User();
        if (pUser != null) {
            if (pUser.get("firstName") != null) {
                user.setFirstName(pUser.get("firstName").toString());
            }
            if (pUser.get("lastName") != null) {
                user.setLastName(pUser.get("lastName").toString());
            }
            if (pUser.get("email") != null) {
                user.setEmail(pUser.get("email").toString());
            }
            if (pUser.get("phone") != null) {
                user.setPhonenNumber(Long.parseLong(pUser.get("phone").toString()));
            }
            if (pUser.get("gender") != null) {
                user.setGender(pUser.get("gender").toString());
            }
            if (pUser.get("aboutMe") != null) {
                user.setAboutMe(pUser.get("aboutMe").toString());
            }
            if (pUser.get("location") != null) {
                user.setLocation(pUser.get("location").toString());
            }
            if (pUser.get("profilePicture") != null) {
                user.setImageUrl(pUser.get("profilePicture").toString());
            }
        }
        return user;
    }

    public void saveParseUser(User u){
        ParseUser pUser = ParseUser.getCurrentUser();
        if(u.getFirstName() !=null){
            pUser.put("firstName", u.getFirstName());
        }
        if(u.getLastName() !=null){
            pUser.put("lastName", u.getLastName());
        }
        if(u.getLastName() !=null){
            pUser.put("email", u.getLastName());
        }
        if(u.getImageUrl() !=null){
            pUser.put("profilePicture", u.getImageUrl());
        }
        pUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("USER_SERVICE", "User Saved successfully");
            }
        });
    }

    public void saveParseUser(FragmentUserProfileBinding fView) {
        ParseUser pUser = ParseUser.getCurrentUser();
        if (fView.etFirstName.getText() != null && !(fView.etFirstName.getText().toString().equals(pUser.get("firstName")))) {
            pUser.put("firstName", fView.etFirstName.getText().toString());
        }

        if (fView.etLastName.getText() != null && !(fView.etLastName.getText().toString().equals(pUser.get("lastName")))) {
            pUser.put("lastName", fView.etLastName.getText().toString());
        }
        if (fView.etEmail.getText() != null && !(fView.etEmail.getText().toString().equals(pUser.get("email")))) {
            pUser.put("email", fView.etEmail.getText().toString());
        }
        if (fView.etPhone.getText() != null && !(fView.etPhone.getText().toString().equals(pUser.get("phone")))) {
            pUser.put("phone", fView.etPhone.getText().toString());
        }
        if (fView.etGender.getText() != null && !(fView.etGender.getText().toString().equals(pUser.get("gender")))) {
            pUser.put("gender", fView.etGender.getText().toString());
        }
        if (fView.etAboutMe.getText() != null && !(fView.etAboutMe.getText().toString().equals(pUser.get("aboutMe")))) {
            pUser.put("aboutMe", fView.etAboutMe.getText().toString());
        }
        if (fView.etLocation.getText() != null && !(fView.etLocation.getText().toString().equals(pUser.get("location")))) {
            pUser.put("location", fView.etLocation.getText().toString());
        }
        pUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("USER_SERVICE", "User Saved successfully");
            }
        });

    }

    public boolean isUserUpdated() {
        return userUpdated;
    }

    public void setUserUpdated(boolean userUpdated) {
        this.userUpdated = userUpdated;
    }
}
