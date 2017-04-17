package com.fomono.fomono.fragments;

/**
 * Created by Saranu on 4/14/17.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.fomono.fomono.R;
import com.fomono.fomono.databinding.FragmentUserProfileBinding;
import com.fomono.fomono.models.user.User;
import com.fomono.fomono.properties.Properties;
import com.fomono.fomono.services.UserService;
import com.fomono.fomono.utils.RoundedTransformation;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;


public class UserProfileFragment extends android.support.v4.app.Fragment implements PhotoAlertDialogFragment.PhotoAlertDialogFragmentListener {
    FragmentUserProfileBinding fragmentUserProfile;
    User user;
    Uri file;
    private String photoType;
    ParseUser pUser;
    UserService uService;
    int screenSize;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uService = UserService.getInstance();
        pUser = ParseUser.getCurrentUser();
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
        user = uService.retriveUserFromParseUser(pUser);
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        screenSize = (int) (pxWidth / displayMetrics.density);
        if( pUser.get("profilePicture") !=null) {
            String fileUrl = pUser.get("profilePicture").toString();
            setImageUrl(fragmentUserProfile.ivUserImage, fileUrl,screenSize);
        }
        fragmentUserProfile.setUser(user);

    }


    private static void setImageUrl(ImageView view, String imageUrl,int screenSize) {
        Picasso.with(view.getContext()).load(imageUrl).transform(new RoundedTransformation(6, 3)).
                placeholder(R.drawable.ic_fomono_big).
                resize(screenSize, 0).into(view);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                callCameraPhotoGallery();
            }

        }
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

        fragmentUserProfile.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                uService.saveParseUser(fragmentUserProfile);
                Toast.makeText(getActivity(), "Successfully Saved",
                        Toast.LENGTH_LONG).show();
                uService.setUserUpdated(true);
            }
        });

        return fragmentUserProfile.getRoot();
    }

    public void clickPicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(uService.getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        startActivityForResult(intent, 100);


    }

    private void saveProfileImage(int requestCode) {
        int rotate = 0;
        String filePath = null;
        if (requestCode == 100) {
            filePath = file.getPath();
        } else if (requestCode == 200) {
            filePath = getPath(file);
        }
        Bitmap bitmap = uService.getBitMap(getContext(), filePath);
        fragmentUserProfile.ivUserImage.setImageBitmap(bitmap);
        uService.saveParseFile(bitmap);

    }


    public String getPath(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void pickPicture() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 200);

    }

    @Override
    public void onFinishAlertDialog(String photoType) {
        this.photoType = photoType;
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]
                    {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {

            callCameraPhotoGallery();
        }
    }

    private void callCameraPhotoGallery() {
        if (photoType.equals(Properties.PHOTO_SELECT)) {
            pickPicture();

        } else if (photoType.equals(Properties.PHOTO_TAKE)) {
            clickPicture();

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                file = imageReturnedIntent.getData();
            }
            saveProfileImage(requestCode);
        }
    }

}