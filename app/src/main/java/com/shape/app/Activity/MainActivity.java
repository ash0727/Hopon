package com.shape.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.shape.app.Forms.Login;
import com.shape.app.Fragment.AchievementFragment;
import com.shape.app.Fragment.AppointmentFragment;
import com.shape.app.Fragment.HomeFragment;
import com.shape.app.Fragment.NewEventFragment;
import com.shape.app.Fragment.TestFragment_Tab;
import com.shape.app.Helper.Constant;
import com.shape.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.shape.app.Forms.Login.CLASS_ID;
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

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private FloatingActionButton fab;
    private BottomAppBar bottomAppBar;
    BottomNavigationView bottomNavigationView;
    private ActionBar actionBar;
    private Toolbar toolbar;
    ImageButton bt_menu, bt_profile;
    Button btnterms;
    boolean doubleBackToExitPressedOnce = false;
    LinearLayout main_ll;
    ImageView profile_image, image;
    SharedPreferences sharedPreferences;
    String str_userid, str_theme, str_fname, str_pic, str_logo;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_fname = sharedPreferences.getString(FNAME, "");
        str_theme = sharedPreferences.getString(THEME_COLOR, "");
        str_logo = sharedPreferences.getString(SCHOOL_LOGO, "");
        str_pic = sharedPreferences.getString(PROFILE_PIC, "");
        initComponent();

        if (!str_theme.equals("")) {
            setStatusBarColor(str_theme);
            main_ll.setBackgroundColor(Color.parseColor(str_theme));
        }
        //main_ll.setBackgroundColor(Integer.parseInt("#00B0FF"));
        //main_ll.setBackgroundColor(Color.parseColor("#00B0FF"));


        initNavigationMenu();

    }

    void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Drawer News");
    }

    @Override
    public void onResume() {
        super.onResume();
        getUser();
        if (sharedPreferences.getString(GENDER, "").equals("2")) {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.ic_female).into(profile_image);
        } else {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(profile_image);

        }

    }

    void initNavigationMenu() {
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        View hView = nav_view.getHeaderView(0);
        //TextView nav_user = (TextView)hView.findViewById(R.id.nav_name);
        image = hView.findViewById(R.id.image);
        btnterms=findViewById(R.id.btnterms);
        Glide.with(getApplicationContext()).load(str_logo).into(image);

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.nav_help) {
                    DialogForLogout();
                }
                if (id == R.id.nav_about) {
                    Intent intent = new Intent(getApplicationContext(), About_Us.class);
                    intent.putExtra("flag", "0");
                    startActivity(intent);
                }
                if (id == R.id.nav_user) {
                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                    intent.putExtra("flag", "0");
                    startActivity(intent);
                }
                if (id == R.id.nav_contact) {
                    Intent intent = new Intent(getApplicationContext(), ContactUs.class);
                    intent.putExtra("flag", "1");
                    startActivity(intent);
                }
                if (id == R.id.nav_tc_c) {
                    Intent intent = new Intent(getApplicationContext(), About_Us.class);
                    intent.putExtra("flag", "2");
                    startActivity(intent);
                }
                if (id == R.id.rate_us) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.shape.app"));
                    startActivity(intent);
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        bt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);

            }
        });

        // open drawer at start
        drawer.closeDrawer(GravityCompat.START);
    }

    private void DialogForLogout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(R.string.confirmation);
        dialog.setMessage(R.string.logout_confirmation_text);
        dialog.setNegativeButton(R.string.CANCEL, null);
        dialog.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ClearAllSherf();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void ClearAllSherf() {
        SharedPreferences sharedpreferences =
                getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);

    }

    private void initComponent() {
        loadFragment(new HomeFragment());//Change Here for default Fragment

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setItemIconTintList(null);

        bt_menu = findViewById(R.id.bt_menu);
        profile_image = findViewById(R.id.profile_image);
        main_ll = findViewById(R.id.main_ll);
        bt_profile = findViewById(R.id.bt_profile);
        fab = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.bottom_bar);
        bottomAppBar.setFabCradleMargin(0);
        bottomAppBar.setFabCradleRoundedCornerRadius(0);
        // bottomAppBar.setCradleVerticalOffset(0);

        if (sharedPreferences.getString(GENDER, "").equals("2")) {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.ic_female).into(profile_image);
        } else {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(profile_image);

        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "fab", Toast.LENGTH_SHORT).show();
            }
        });
        bt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Notification_Activity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_feed:
//                item.setIcon(R.drawable.color_home);
                fragment = new TestFragment_Tab();
                break;

            case R.id.navigation_achievement:
                fragment = new AchievementFragment();


                break;

            case R.id.navigation_mid:
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
                fragment = new HomeFragment();


                break;

            case R.id.navigation_appointment:
                fragment = new AppointmentFragment();
//                item.setIcon(R.drawable.color_profile);
                break;
            case R.id.navigation_event:
                fragment = new NewEventFragment();

                break;
        }

        return loadFragment(fragment);
    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.design_bottom_sheet_slide_in,
                            R.anim.design_bottom_sheet_slide_out
                    )
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
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

//    @Override
//    public void onBackPressed() {
//
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 2000);
//    }


    private void getUser() {

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
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
                                String class_id = jsonObject1.getString("class_id");

                                SetDataToSherf(str_id, school_id, student_code, first_name, last_name, father_mobile, father_name, theme_color, logo, profile_pic, class_id);


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

    private void SetDataToSherf(String str_user_id, String school_id, String student_code, String first_name, String last_name, String father_mobile, String father_name, String theme_color, String logo, String profile_pic, String class_id) {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(USER_ID, str_user_id);
        editor.putString(PROFILE_PIC, profile_pic);
        editor.putString(SCHOOL_ID, school_id);
        editor.putString(CLASS_ID, class_id);
        editor.putString(STUDENT_ID, student_code);
        editor.putString(FNAME, first_name);
        editor.putString(LNAME, last_name);
        editor.putString(FATHER_NAME, father_name);
        editor.putString(FATHER_PHONE, father_mobile);
        editor.putString(THEME_COLOR, theme_color);
        editor.putString(SCHOOL_LOGO, logo);
        editor.apply();
    }

}