package sarthaksharma.theDeliveryApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#201a32"));
        }

        preferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        TextView lblLogoName= (TextView) findViewById(R.id.lblLogoName);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation_fadein);
        lblLogoName.startAnimation(myFadeInAnimation);


//        GeometricProgressView progressView = (GeometricProgressView) findViewById(R.id.progLoading);
//        progressView.setType(TYPE.TRIANGLE);
//        progressView.setNumberOfAngles(3);
//        progressView.setColor(Color.parseColor("#000000"));
//        progressView.setDuration(1000);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isNetworkAvailable()){
                    AlertDialog alertDialog = new AlertDialog.Builder(SplashScreenActivity.this).create();
                    alertDialog.setTitle(getApplicationContext().getString(R.string.connectionErrorString));
                    alertDialog.setMessage(getApplicationContext().getString(R.string.connectionErrorMessage));
                    alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(SplashScreenActivity.this, NoConnectionActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    alertDialog.show();
                }else {

                    if (preferences.getString("FirstUserName", null) == null || preferences.getString("FirstMobileNumber", null) == null){
                        Intent intent = new Intent(SplashScreenActivity.this, IntroSliderScreenActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(SplashScreenActivity.this, HomeScreenActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        }, 4000);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
