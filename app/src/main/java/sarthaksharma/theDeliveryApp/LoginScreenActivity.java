package sarthaksharma.theDeliveryApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class LoginScreenActivity extends AppCompatActivity {

    EditText txtMobileNo;
    String strMobileNo, strUserName;
    EditText txtUserName;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#201a32"));
        }

        preferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = preferences.edit();

        txtMobileNo = (EditText) findViewById(R.id.txtMobileNo);
        txtUserName = (EditText) findViewById(R.id.txtUserName);

    }

    public void fncSendOtp(View view) {

        strMobileNo = txtMobileNo.getText().toString();
        strUserName = txtUserName.getText().toString();

        editor.putString("FirstUserName", strUserName);
        editor.putString("FirstMobileNumber", strMobileNo);
        editor.commit();

        if (!isNetworkAvailable()) {
            Intent intent = new Intent(LoginScreenActivity.this, NoConnectionActivity.class);
            startActivity(intent);
        } else if (TextUtils.isEmpty(strUserName)) {
            txtUserName.setError(Html.fromHtml("<font color=white>" + "Specify your name" + "</font>"));
        } else if (TextUtils.isEmpty(strMobileNo)) {
            txtMobileNo.setError(Html.fromHtml("<font color=white>" + "Mobile Number is required" + "</font>"));
        } else {
            Intent intent = new Intent(LoginScreenActivity.this, OtpScreenActivity.class);
            startActivity(intent);
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
