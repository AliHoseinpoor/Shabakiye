package com.example.ali.shabakiye.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ali.shabakiye.Application.MyApplication;
import com.example.ali.shabakiye.Helper.ColorSnackbar;
import com.example.ali.shabakiye.Helper.DialogHelper;
import com.example.ali.shabakiye.Helper.SessionManager;
import com.example.ali.shabakiye.Helper.TransparentToolbar;
import com.example.ali.shabakiye.R;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdslLoginActivity extends AppCompatActivity {

    AppCompatEditText username, phone;
    TextInputLayout usernameLayout, phoneLayout;
    Button submit;
    CoordinatorLayout coordinator;
    ProgressBar loginProgress;
    CircleImageView home;
    CheckBox isLogin;
    AlertDialog userNotFoundDialog = null, noInternet = null;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //for animation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adsl_login);

        //transparent the toolbar
        TransparentToolbar transparentToolbar = new TransparentToolbar(this);
        transparentToolbar.transparentToolbar();

        session = new SessionManager(this);

        username = (AppCompatEditText) findViewById(R.id.AdslLoginEtUsername);
        usernameLayout = (TextInputLayout) findViewById(R.id.AdslLoginEtUsername_input);

        phone = (AppCompatEditText) findViewById(R.id.AdslLoginEtPhone);
        phoneLayout = (TextInputLayout) findViewById(R.id.AdslLoginEtPhone_input);

        submit = findViewById(R.id.AdslLoginBtnSubmit);
        coordinator = findViewById(R.id.AdslLoginCoordinatior);
        loginProgress = findViewById(R.id.AdslLoginProgressLogin);
        home = findViewById(R.id.AdslLoginImage);
        isLogin = findViewById(R.id.AdslLoginCbIsLogin);

        //setup Animation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setUpAnimation();
            getWindow().setAllowEnterTransitionOverlap(false);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(coordinator, "نام کاربری خود را وارد نمایید", Snackbar.LENGTH_SHORT);
                    ColorSnackbar.errorSnackbar(snackbar).show();
                    username.requestFocus();
                } else if (phone.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(coordinator, "تلفن خود را وارد نمایید", Snackbar.LENGTH_SHORT);
                    ColorSnackbar.errorSnackbar(snackbar).show();
                    phone.requestFocus();
                } else {
                    home.animate().alpha(0).setDuration(200).start();
                    loginProgress.animate().alpha(1).setStartDelay(200).setDuration(300).start();
                    getUser(username.getText().toString(), phone.getText().toString());
                }
            }
        });
    }

    public void getUser(final String user_name, final String user_phone) {
        String URL = String.format("http://esfwifi.ir/webservice/rest/customer_info?username=%s", user_name);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean resualt = response.getBoolean("result");
                    if (resualt) {
                        home.animate().alpha(1).start();
                        loginProgress.animate().alpha(0).start();
                        if (isLogin.isChecked()) {
                            session.createLoginSession(user_name, user_phone);
                        }
                        Intent intent = new Intent(AdslLoginActivity.this, UserAdslInfoActivity.class);
                        intent.putExtra("username", user_name);
                        startActivity(intent);
                        finish();

                    } else {
                        home.animate().alpha(1).start();
                        loginProgress.animate().alpha(0).start();
                        showDialogError();
                        username.setText("");
                        phone.setText("");
                        username.requestFocus();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                home.animate().alpha(1).start();
                loginProgress.animate().alpha(0).start();
                showDialogForAccessInternet(user_name, user_phone);
            }
        });
        MyApplication.getInstance().addToRequestQueue(request);
    }

    private void showDialogForAccessInternet(final String username, final String phone) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_error_access_internet, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Button ok = dialogView.findViewById(R.id.btnDialogNoInternet);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noInternet != null && noInternet.isShowing()) {
                    noInternet.dismiss();
                }
                home.animate().alpha(0).setDuration(200).start();
                loginProgress.animate().alpha(1).setStartDelay(200).setDuration(300).start();
                getUser(username, phone);
            }
        });
        builder.setView(dialogView);
        noInternet = builder.create();
        noInternet.setCanceledOnTouchOutside(true);
        noInternet.show();
        DialogHelper dialog = new DialogHelper(this);
        dialog.resizeAlertDialog(noInternet);
    }

    private void showDialogError() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_user_not_found, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Button ok = dialogView.findViewById(R.id.btnDialogUserNotFound);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userNotFoundDialog != null && userNotFoundDialog.isShowing()) {
                    userNotFoundDialog.dismiss();
                }
            }
        });
        builder.setView(dialogView);
        userNotFoundDialog = builder.create();
        userNotFoundDialog.setCanceledOnTouchOutside(true);
        userNotFoundDialog.show();
        DialogHelper dialog = new DialogHelper(this);
        dialog.resizeAlertDialog(userNotFoundDialog);

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpAnimation() {
        Fade fade = new Fade();
        fade.setDuration(500);
        getWindow().setEnterTransition(fade);
    }
}
