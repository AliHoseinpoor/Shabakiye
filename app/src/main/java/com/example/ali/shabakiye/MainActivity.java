package com.example.ali.shabakiye;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ali.shabakiye.Activity.AdslLoginActivity;
import com.example.ali.shabakiye.Activity.UserAdslInfoActivity;
import com.example.ali.shabakiye.Helper.Constants;
import com.example.ali.shabakiye.Helper.SessionManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    FloatingActionButton submit;
    RadioButton adsl, wifi;
    RadioGroup group;
    SessionManager session;
    HashMap<String, String> userIsLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set-up Animation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setUpAnimationForMainActivity();

        submit = findViewById(R.id.mainBuSubmit);
        adsl = findViewById(R.id.mainRaAdsl);
        wifi = findViewById(R.id.mainRaWifi);
        group = findViewById(R.id.group);

    }

    @Override
    protected void onResume() {
        super.onResume();

        /*dar on-Resume kar ha ro mikonim chon age hamoon moghe login kard va save kard session ro va
        hamoon moghe bargasht baz befahme ke login boode chon zamane bargasht onResume baz ejra mishe*/

        session = new SessionManager(this);
        userIsLogin = session.getUserDetail();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adsl.isChecked()) {
                    if (userIsLogin.get(SessionManager.IS_LOGIN).equals("true")) {
                        startActivity(new Intent(MainActivity.this, UserAdslInfoActivity.class));
                    } else {
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                        Intent intent = new Intent(MainActivity.this, AdslLoginActivity.class);
                        intent.putExtra(Constants.KEY_TYPE, Constants.AnimType.FadeJava);
                        intent.putExtra(Constants.KEY_NAME, "Fade By Java");
                        intent.putExtra(Constants.KEY_TITLE, "Fade Animation");
                        startActivity(intent, options.toBundle());
                    }
                } else if (wifi.isChecked()) {
                    Toast.makeText(MainActivity.this, "name : " + wifi.getText(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpAnimationForMainActivity() {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.START);
        slide.setDuration(400);
        getWindow().setReenterTransition(slide);
        getWindow().setExitTransition(slide);
        getWindow().setAllowReturnTransitionOverlap(false);
    }
}
