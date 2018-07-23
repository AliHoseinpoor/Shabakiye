package com.example.ali.shabakiye.Activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ali.shabakiye.Helper.CircleProgress;
import com.example.ali.shabakiye.Helper.DialogHelper;
import com.example.ali.shabakiye.Helper.SessionManager;
import com.example.ali.shabakiye.Helper.TransparentToolbar;
import com.example.ali.shabakiye.Holders.User;
import com.example.ali.shabakiye.Holders.UserInfo;
import com.example.ali.shabakiye.R;
import com.example.ali.shabakiye.Services.ApiClient;
import com.example.ali.shabakiye.Services.ApiService;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdslInfoActivity extends AppCompatActivity {
    private static final String TAG = "UserAdslInfoActivity";

    String AdslUsername = "";
    TextView fullName, userPhone, userMelliCode, userBirthday, progressRemainTrafic, progressRemainDay,
            cardServiceTitle, cardServicePrice, cardServieBandWidth, cardServieTrafic, cardServieStartDate, cardServieExpireDate;
    CircleImageView userProfile;
    CircleProgress remainData, remainDay;
    AlertDialog UserDetail = null, loadinData = null, noInternet = null;
    FloatingActionButton floatinUserDetail;
    User user = null;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_adsl_info);

        //transparent toolbar
        TransparentToolbar transparentToolbar = new TransparentToolbar(this);
        transparentToolbar.transparentToolbar();

        //collapsing toolbar
        initCollapsingToolbar();

        showLoadingDialogPrepareData();

        session = new SessionManager(this);

        //get username
        Bundle usernameBundle = getIntent().getExtras();
        if (usernameBundle != null) {
            AdslUsername = usernameBundle.getString("username");
        } else {
            AdslUsername = session.getUsername();
        }

        fullName = findViewById(R.id.AdslUserInfoFullName);
        userPhone = findViewById(R.id.AdslUserInfoPhone);
        userMelliCode = findViewById(R.id.AdslUserInfoMelicode);
        userBirthday = findViewById(R.id.AdslUserInfoBirthday);
        userProfile = findViewById(R.id.AdslUserInfoProfile);
        remainData = findViewById(R.id.AdslUserInfoRemainData);
        remainDay = findViewById(R.id.AdslUserInfoRemainDay);
        floatinUserDetail = findViewById(R.id.AdslUserInfoFloatinFullDetail);
        progressRemainTrafic = findViewById(R.id.AdslUserInfoProgressRemainTrafic);
        progressRemainDay = findViewById(R.id.AdslUserInfoProgressRemainDay);

        cardServicePrice = findViewById(R.id.AdslUserInfoCardServicePriceAns);
        cardServiceTitle = findViewById(R.id.AdslUserInfoCardServiceTitleAns);
        cardServieBandWidth = findViewById(R.id.AdslUserInfoCardServicePahnayeBandAns);
        cardServieTrafic = findViewById(R.id.AdslUserInfoCardServiceTraficAns);
        cardServieStartDate = findViewById(R.id.AdslUserInfoCardServiceStartDateAns);
        cardServieExpireDate = findViewById(R.id.AdslUserInfoCardServiceExpireDateAns);

        getUserAdslInfo();
    }

    private void showLoadingDialogPrepareData() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_loading_data_adsl, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView);
        loadinData = builder.create();
        loadinData.setCanceledOnTouchOutside(true);
        loadinData.show();
        DialogHelper dialog = new DialogHelper(this);
        dialog.resizeAlertDialog(loadinData);
    }

    public void getUserAdslInfo() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<UserInfo> call = apiService.getUser(AdslUsername);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    UserInfo userInfo = response.body();
                    if (userInfo != null) {
                        user = userInfo.getUser();
                        floatinUserDetail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ShowDialogUserDetail(user);
                            }
                        });
                        loadinData.dismiss();
                        showData(user);
                    }
                } else {
                    loadinData.dismiss();
                    Toast.makeText(UserAdslInfoActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                loadinData.dismiss();
                showDialogForAccessInternet();
            }
        });
    }

    private void showData(User user) {
        fullName.setText(getString(R.string.AdslUserInfoName, user.getName(), user.getLastname()));
        userPhone.setText(getString(R.string.AdslUserInfoMobile, user.getMobile()));
        userMelliCode.setText(getString(R.string.AdslUserInfoNatCode, user.getMelicode()));
        userBirthday.setText(getString(R.string.AdslUserInfoBirthday, user.getBirthday_year(), user.getBirthday_month(), user.getBirthday_day()));

        int remainTraficGig = Integer.parseInt(user.getTraffic()) / 1024;
        int traficGig = Integer.parseInt(user.getService_pahnaye_band()) / 1024;
        progressRemainTrafic.setText(String.format(getResources().getString(R.string.AdslUserInfoProgressRemainTrafic), remainTraficGig, traficGig));

        userProfile.setImageResource(user.getGender().equals("1") ? R.drawable.user_man : R.drawable.user_girl);

        int remainTrafick = 0;
        try {
            if (Integer.parseInt(user.getService_pahnaye_band()) != 0) {
                remainTrafick = (Integer.parseInt(user.getTraffic()) * 100) / (Integer.parseInt(user.getService_pahnaye_band()));
            }
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "error : " + e, Toast.LENGTH_SHORT).show();
        }

        progressRemainDay.setText(getString(R.string.AdslUserInfoProgressRemainDay, user.getService_remain_day(), user.getService_day()));

        remainData.setProgressWithAnimation(remainTrafick);
        remainDay.setRemainDay(true);
        remainDay.setMax(Integer.parseInt(user.getService_day()));
        remainDay.setProgressWithAnimation(Integer.parseInt(user.getService_remain_day()));

        cardServiceTitle.setText(user.getService_title());
        cardServicePrice.setText(getString(R.string.servicePrice, user.getService_price()));
        cardServieBandWidth.setText(getString(R.string.serviceHajm, traficGig));
        cardServieTrafic.setText(getString(R.string.serviceHajm, remainTraficGig));
        cardServieStartDate.setText(user.getDate_service_start_fa());
        cardServieExpireDate.setText(user.getDate_service_expire_fa());

    }

    private void showDialogForAccessInternet() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_error_access_internet, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Button ok = dialogView.findViewById(R.id.btnDialogNoInternet);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noInternet != null && noInternet.isShowing()) {
                    noInternet.dismiss();
                }
                getUserAdslInfo();
            }
        });
        builder.setView(dialogView);
        noInternet = builder.create();
        noInternet.setCanceledOnTouchOutside(false);
        noInternet.show();
        DialogHelper dialog = new DialogHelper(this);
        dialog.resizeAlertDialog(noInternet);
    }

    private void ShowDialogUserDetail(User user) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_adsl_user_info_complete_detail, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextView dialogName, dialogNatcode, dialogBirthday, dialogMobile, dialogPhone, dialogServiceTitle,
                dialogServiceRemainDay, dialogServiceRemainTrafic, dialogServiceStartDate, dialogServiceExpireDate;

        dialogName = dialogView.findViewById(R.id.DialogUserAdslInfoName);
        dialogNatcode = dialogView.findViewById(R.id.DialogUserAdslInfoNatCode);
        dialogBirthday = dialogView.findViewById(R.id.DialogUserAdslInfoBirthday);
        dialogMobile = dialogView.findViewById(R.id.DialogUserAdslInfoMobile);
        dialogPhone = dialogView.findViewById(R.id.DialogUserAdslInfoPhone);
        dialogServiceTitle = dialogView.findViewById(R.id.DialogUserAdslInfoServiceTitle);
        dialogServiceRemainDay = dialogView.findViewById(R.id.DialogUserAdslInfoServiceRemainDay);
        dialogServiceRemainTrafic = dialogView.findViewById(R.id.DialogUserAdslInfoServiceRemainTrafic);
        dialogServiceStartDate = dialogView.findViewById(R.id.DialogUserAdslInfoServiceStartDate);
        dialogServiceExpireDate = dialogView.findViewById(R.id.DialogUserAdslInfoServiceExpireDate);

        dialogName.setText(getString(R.string.AdslUserInfoName, user.getName(), user.getLastname()));
        dialogNatcode.setText(getString(R.string.AdslUserInfoNatCode, user.getMelicode()));
        dialogBirthday.setText(getString(R.string.AdslUserInfoBirthday, user.getBirthday_year(), user.getBirthday_month(), user.getBirthday_day()));
        dialogMobile.setText(getString(R.string.AdslUserInfoMobile, user.getMobile()));
        dialogPhone.setText(getString(R.string.DialogUserDetailPhone, user.getPhone_code(), user.getPhone()));
        dialogServiceTitle.setText(getString(R.string.DialogUserDetailServiceTitle, user.getService_title()));
        dialogServiceRemainDay.setText(getString(R.string.DialogUserDetailServiceRemainDay, user.getService_remain_day(), user.getService_day()));
        dialogServiceRemainTrafic.setText(getString(R.string.DialogUserDetailServiceRemainTrafic, user.getTraffic(), user.getService_pahnaye_band()));
        dialogServiceStartDate.setText(getString(R.string.DialogUserDetailServiceStartDate, user.getDate_service_start_fa()));
        dialogServiceExpireDate.setText(getString(R.string.DialogUserDetailServiceExpireDate, user.getDate_service_expire_fa()));

        builder.setView(dialogView);
        UserDetail = builder.create();
        UserDetail.setCanceledOnTouchOutside(true);
        UserDetail.show();
        resizeAlertDialog(UserDetail);
    }

    private void resizeAlertDialog(AlertDialog dialog) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setLayout((int) (0.9 * metrics.widthPixels), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.AdslUserInfoCollapsingToolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.AdslUserInfoAppbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.collapsingToolbarTitle));
                    collapsingToolbar.setCollapsedTitleGravity(Gravity.LEFT);
                    collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
                    collapsingToolbar.setCollapsedTitleTypeface(Typeface.DEFAULT_BOLD);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
