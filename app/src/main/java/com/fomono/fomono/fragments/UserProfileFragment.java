package com.fomono.fomono.fragments;

/**
 * Created by Saranu on 4/14/17.
 */

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fomono.fomono.R;
import com.fomono.fomono.databinding.FragmentUserProfileBinding;
import com.fomono.fomono.models.user.User;
import com.fomono.fomono.properties.Properties;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;


public class UserProfileFragment extends android.support.v4.app.Fragment implements PhotoAlertDialogFragment.PhotoAlertDialogFragmentListener {
    FragmentUserProfileBinding fragmentUserProfile;
    User user;
    Uri file;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static UserProfileFragment newInstance() {
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        Bundle args = new Bundle();
        userProfileFragment.setArguments(args);
        return userProfileFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user = getDummyUser();
        fragmentUserProfile.setUser(user);

    }


    //  @BindingAdapter({"imageUrl"})
    private static void setImageUrl(ImageView view, String imageUrl) {
        Glide.with(view.getContext()).load(imageUrl).placeholder(R.drawable.botaimage).
                error(R.drawable.botaimage).into(view);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragmentUserProfile = DataBindingUtil.inflate(
                inflater, R.layout.fragment_user_profile, parent, false);
        View view = fragmentUserProfile.getRoot();
        ButterKnife.bind(this, view);
        fragmentUserProfile.ivCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                PhotoAlertDialogFragment fdf = PhotoAlertDialogFragment.newInstance();
                fdf.setTargetFragment(UserProfileFragment.this, 300);
                fdf.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
                fdf.show(fm, "FRAGMENT_MODAL_ALERT");
            }
        });

        return fragmentUserProfile.getRoot();
    }

    public User getDummyUser() {

        User u = new User();
        u.setFirstName("Malle");
        u.setLastName("Saranu");
        u.setGender("Female");
        u.setPhonenNumber(00000000);
        u.setAboutMe("blah blah blah...............");
        u.setLocation("SanFrancisco, CA");
        u.setEmail("somebody@something.com");
        return u;

    }


    public void clickPicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        startActivityForResult(intent, 100);


    }

    private void convertImagetoBitmap(int requestCode) {
        int rotate = 0;
        String filePath =null;
        if (requestCode == 100) {
            filePath = file.getPath();
        } else if (requestCode == 200) {
            filePath=getPath(file);
        }

        try {
            getContext().getContentResolver().notifyChange(file, null);
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
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeFile(filePath, options);
        Matrix matrix = new Matrix();
        if (rotate != 0) {
            matrix.preRotate(rotate);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        fragmentUserProfile.ivUserImage.setImageBitmap(bitmap);

    }


    public String getPath(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Gallery");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }


    public void pickPicture() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 200);

    }

    @Override
    public void onFinishAlertDialog(String photoType) {
        if (photoType.equals(Properties.PHOTO_SELECT)) {
            pickPicture();

        } else if (photoType.equals(Properties.PHOTO_TAKE)) {
            clickPicture();

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                file = imageReturnedIntent.getData();
                convertImagetoBitmap(requestCode);

            }
        } else if (requestCode == 100) {
            if (resultCode == RESULT_OK) {

                convertImagetoBitmap(requestCode);
            }
        }


    }

}