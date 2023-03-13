package com.shape.app.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.shape.app.BuildConfig;
import com.shape.app.Forms.Login;
import com.shape.app.Helper.Constant;
import com.shape.app.Helper.FileUtils;
import com.shape.app.Helper.MediaPickerDialog;
import com.shape.app.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.shape.app.Forms.Login.FATHER_NAME;
import static com.shape.app.Forms.Login.FATHER_PHONE;
import static com.shape.app.Forms.Login.FNAME;
import static com.shape.app.Forms.Login.GENDER;
import static com.shape.app.Forms.Login.LNAME;
import static com.shape.app.Forms.Login.PROFILE_PIC;
import static com.shape.app.Forms.Login.SCHOOL_ID;
import static com.shape.app.Forms.Login.SCHOOL_LOGO;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.STUDENT_ID;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    TextView user_name, student_code;
    ImageView image;
    RelativeLayout rl;
    SharedPreferences sharedPreferences;
    String str_userid, sub_id, chapter_id, exam_id;
    String PDFUrl, from, name, str_pic, id, updated_date, created_date, chapter_name;
    String intent_grade, str_theme, str_code, str_fname, str_lname;
    private String imageTakenPath;
    private Bitmap image1Bmp;
    String str_image_1 = "";
    private int GALLERY = 1, CAMERA = 2;
    CropImageView cropImage;
    public static final int PERMISSIONS_DENIED = 1;
    private static final String PACKAGE_URL_SCHEME = "package:";
    AlertDialog alertDialog;
    SharedPreferences.Editor editor;
    private static final int TAKE_IMAGE1_REQ_CODE = 18;

    private static final int PICK_IMAGE1_REQ_CODE = 21;

    private static final int UPLOADING_IMAGE_SIZE = 800;

    Button btn_submit_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ((TextView) findViewById(R.id.toolbr_lbl)).setText("Profile");
        findViewById(R.id.imgbck).setOnClickListener(view -> onBackPressed());
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_code = sharedPreferences.getString(STUDENT_ID, "");
        str_fname = sharedPreferences.getString(FNAME, "");
        str_lname = sharedPreferences.getString(LNAME, "");
        str_theme = sharedPreferences.getString(THEME_COLOR, "");
        str_pic = sharedPreferences.getString(PROFILE_PIC, "");


        btn_submit_mobile = findViewById(R.id.btn_submit_mobile);
        student_code = findViewById(R.id.student_code);
        user_name = findViewById(R.id.user_name);
        image = findViewById(R.id.image);
        rl = findViewById(R.id.rl);

        user_name.setText(str_fname + " " + str_lname);
        student_code.setText(str_code);
        if (!str_theme.equals("")) {
            setStatusBarColor(str_theme);
            setStatusBarColor(str_theme);
            btn_submit_mobile.setBackgroundColor(Color.parseColor(str_theme));
            RelativeLayout rl_bg = findViewById(R.id.rl_tool);
            rl_bg.setBackgroundColor(Color.parseColor(str_theme));
        }
       // Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(image);
        if (sharedPreferences.getString(GENDER, "").equals("2")) {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.ic_female).into(image);
        } else {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(image);

        }
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  initDialog(11, 22);
                requestMultiplePermissions();

            }
        });
        btn_submit_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendImage();
            }
        });

    }


    public void showPictureDialog() {
        alertDialog = new AlertDialog.Builder(Profile.this).create();
        LayoutInflater inflater = LayoutInflater.from(Profile.this);
        View layout_pwd = inflater.inflate(R.layout.capture_image_dialog, null);
        alertDialog.setView(layout_pwd);
        layout_pwd.findViewById(R.id.btn_gallery_dialog).setOnClickListener(this);
        layout_pwd.findViewById(R.id.btn_camera_dialog).setOnClickListener(this);
        alertDialog.show();
    }

    public void setStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            int statusBarColor = Color.parseColor(color);
            if (statusBarColor == Color.BLACK && window.getNavigationBarColor() == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setStatusBarColor(statusBarColor);
        }
        /*
        * getSupportActionBar().setBackgroundDrawable(
        new ColorDrawable(Color.parseColor("#AA3939")));
        *
        * */
    }
    // IMAGE/VIDEO PICKED DELEGATE ------------------------------


    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri selectedImage = data.getData();
                CropImage.activity(selectedImage)
                        .setAspectRatio(1, 1)
                        .start(Profile.this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                image.setImageURI(resultUri);

                InputStream imageStream = null;
                try {
                    imageStream = Profile.this.getContentResolver().openInputStream(resultUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), null, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                //  imageView.setImageBitmap(bitmap);
                String strDocument = Constant.getStringImageBase64(rotatedBitmap);
                Log.d("IMAGE_1sstrDocument_ga", "onActivityResult: " + strDocument);
                str_image_1 = strDocument;


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == CAMERA) {
            Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));
            CropImage.activity(selectedImage).start(Profile.this);
        }
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(Profile.this.getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Profile.this, Profile.this.getPackageName() + ".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, CAMERA);
            }
        }
    }

    String imageFilePath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = Profile.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix /
                ".jpg",         // suffix /
                storageDir      // directory /
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(Profile.this).withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    showPictureDialog();
                }
                if (report.isAnyPermissionPermanentlyDenied()) {
                    showMissingPermissionDialog();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(Profile.this, "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showMissingPermissionDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Profile.this);
        dialogBuilder.setTitle(R.string.string_permission_help);
        dialogBuilder.setMessage(R.string.string_permission_help_text);
        dialogBuilder.setNegativeButton(R.string.string_permission_quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Profile.this.setResult(PERMISSIONS_DENIED);
                Profile.this.finish();
            }
        });
        dialogBuilder.setPositiveButton(R.string.string_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse(PACKAGE_URL_SCHEME + Profile.this.getPackageName()));
                startActivity(intent);
            }
        });
        dialogBuilder.show();
    }

    private void SendImage() {

        final ProgressDialog progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.UpdateUserProfile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String code, message, id, user_id;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            code = jsonObject.getString("code");
                            message = jsonObject.getString("message");

                            Log.d("resp", "onResponse: " + jsonObject + "===" + code);
                            if (code.equals("200")) {
                                getUser();

                                Toast.makeText(Profile.this, "" + message, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Profile.this, "" + message, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Profile.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", str_userid);
                params.put("profile_pic", str_image_1);
                Log.d("params", "getParams: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Constant.Token);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);


    }

    private void getUser() {

        final ProgressDialog progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.UserProfile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String code, message;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            code = jsonObject.getString("code");
                            message = jsonObject.getString("message");

                            Log.d("resp", "onResponse: " + jsonObject + "===" + code);
                            if (code.equals("200")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("Data");
                                Log.d("resp", "onResponse: " + jsonObject1 + "===" + code);

                                String str_id = jsonObject1.getString("id");
                                String school_id = jsonObject1.getString("school_id");
                                String student_code = jsonObject1.getString("student_code");
                                String first_name = jsonObject1.getString("first_name");
                                String last_name = jsonObject1.getString("last_name");
                                String father_mobile = jsonObject1.getString("father_mobile");
                                String father_name = jsonObject1.getString("father_name");
                                String theme_color = jsonObject1.getString("theme_color");
                                String logo = jsonObject1.getString("logo");
                                String profile_pic = jsonObject1.getString("profile_pic");
                                String gender = jsonObject1.getString("gender");

                                SetDataToSherf(str_id, school_id, student_code, first_name, last_name, father_mobile, father_name, theme_color, logo, profile_pic,gender);
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                finish();
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", str_userid);
                Log.d("params", "getParams: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Constant.Token);
                return headers;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


    }

    private void SetDataToSherf(String str_user_id, String school_id, String student_code, String first_name, String last_name, String father_mobile, String father_name, String theme_color, String logo, String profile_pic, String gender) {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString(USER_ID, str_user_id);

        editor.putString(PROFILE_PIC, profile_pic);
        editor.putString(SCHOOL_ID, school_id);
        editor.putString(STUDENT_ID, student_code);
        editor.putString(FNAME, first_name);
        editor.putString(LNAME, last_name);
        editor.putString(FATHER_NAME, father_name);
        editor.putString(FATHER_PHONE, father_mobile);
        editor.putString(THEME_COLOR, theme_color);
        editor.putString(SCHOOL_LOGO, logo);
        editor.putString(GENDER, gender);
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_camera_dialog:
                openCameraIntent();
                alertDialog.dismiss();
                break;

            case R.id.btn_gallery_dialog:
                choosePhotoFromGallary();
                alertDialog.dismiss();
                break;


        }
    }
}