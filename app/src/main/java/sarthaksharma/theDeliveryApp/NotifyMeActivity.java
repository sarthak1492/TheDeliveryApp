package sarthaksharma.theDeliveryApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotifyMeActivity extends AppCompatActivity {

    EditText txtName;
    EditText txtEmail;
    String strName, strEmail, strPossibleEmail;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_me);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().hide();

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#201a32"));
        }

        GeometricProgressView progressView = (GeometricProgressView) findViewById(R.id.progLoading);
        progressView.setType(TYPE.TRIANGLE);
        progressView.setNumberOfAngles(3);
        progressView.setColor(Color.parseColor("#000000"));
        progressView.setDuration(1000);

        progressView.setVisibility(View.INVISIBLE);

        txtName = (EditText) findViewById(R.id.txtName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void fncSendMail(View view) {
        strName = txtName.getText().toString();
        strEmail = txtEmail.getText().toString();

        if (!isNetworkAvailable()){
            Intent intent = new Intent(NotifyMeActivity.this, NoConnectionActivity.class);
            startActivity(intent);
        }else if (TextUtils.isEmpty(strName)){
            txtName.setError(Html.fromHtml("<font color=white>" + "Name is required" + "</font>"));
        }else if (TextUtils.isEmpty(strEmail)){
            txtEmail.setError(Html.fromHtml("<font color=white>" + "Email id is mandatory" + "</font>"));
        }else if (!isEmailValid(strEmail)){
            txtEmail.setError(Html.fromHtml("<font color=white>" + "Invalid Email Pattern" + "</font>"));
        } else{
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(txtEmail.getWindowToken(), 0);

            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();

            editor.putString("name", strName);
            editor.commit();

            new SendMailTask().execute();
        }
    }

    public class SendMailTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            GeometricProgressView progressView = (GeometricProgressView) findViewById(R.id.progLoading);
            progressView.setType(TYPE.TRIANGLE);
            progressView.setNumberOfAngles(3);
            progressView.setColor(Color.parseColor("#000000"));
            progressView.setDuration(1000);

            progressView.setVisibility(View.VISIBLE);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                GmailSender sender = new GmailSender("deliveroztheapp@gmail.com", "p$wp$w01");
                sender.sendMail("Subscription Confirmation",
                        "Dear " + strName + ",\nThank you for your subscription.\n\nYou will be notified as soon as we are on the roll!"
                                + "\n\n Regards,\nTeam Deliveroz",
                        "deliveroztheapp@gmail.com",
                        strEmail);
            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            GeometricProgressView progressView = (GeometricProgressView) findViewById(R.id.progLoading);
            progressView.setType(TYPE.TRIANGLE);
            progressView.setNumberOfAngles(3);
            progressView.setColor(Color.parseColor("#000000"));
            progressView.setDuration(1000);

            progressView.setVisibility(View.INVISIBLE);

            if (isNetworkAvailable()){
                Intent intent = new Intent(NotifyMeActivity.this, SubscriptionConfimationActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(NotifyMeActivity.this, NoConnectionActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_notify_me, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
}
