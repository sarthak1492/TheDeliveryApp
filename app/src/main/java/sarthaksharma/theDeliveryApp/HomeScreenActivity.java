package sarthaksharma.theDeliveryApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import sarthaksharma.theDeliveryApp.Adapters.CategoriesGridAdapter;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    boolean doubleBackToExitPressedOnce = false;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    List<String> arrCategoriesImages =
            new ArrayList<>(Arrays.asList("https://orig00.deviantart.net/03f3/f/2014/303/6/5/milk_carton_icon_by_tinylab-d4jvz9h.png",
            "https://storybookstorage.s3.amazonaws.com/items/images/000/351/776/original/20160421-9-11rxguq.png?1461268299",
                    "https://cdn.pixabay.com/photo/2017/03/03/11/27/beer-2113854_960_720.png",
                    "https://cdn.pixabay.com/photo/2012/04/11/17/16/bread-29006_960_720.png",
                    "https://www.gulfood.com/__media/Icons/Exhibitors/ic-meatpoultry.png",
                    "https://cdn0.iconfinder.com/data/icons/bakery-icons-set-cartoon-style/512/a739-512.png"));

    List<String> arrCategoriesNames =
            new ArrayList<>(Arrays.asList("Milk", "Fruits & Vegetables Juice", "Shakes & Smoothies", "Fresh Vegetables",
                    "Poultry Products", "Breads"));

    GridView gridCategories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#201a32"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        preferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = preferences.edit();

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        TextView lblGreetings = (TextView) view.findViewById(R.id.lblGreetings);
        TextView lblName = (TextView) view.findViewById(R.id.lblName);

        if(timeOfDay >= 0 && timeOfDay < 12){
            lblGreetings.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            lblGreetings.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 22){
            lblGreetings.setText("Good Evening");
        }else if(timeOfDay >= 22 && timeOfDay < 24){
            lblGreetings.setText("Good Night");
        }
        lblName.setText(preferences.getString("FirstUserName", null));

        gridCategories = (GridView) findViewById(R.id.gridCategories);
        gridCategories.setAdapter(new CategoriesGridAdapter(this, arrCategoriesImages, arrCategoriesNames));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gridCategories.setNestedScrollingEnabled(true);
        }

        gridCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(HomeScreenActivity.this, arrCategoriesNames.get(i), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            return true;
        }

        if(id == R.id.action_search){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(HomeScreenActivity.this, ProfileScreenActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_myOrders) {

        } else if (id == R.id.nav_my_address) {
            Intent intent = new Intent(HomeScreenActivity.this, LocationScreenActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_my_subscriptions) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_feedback){

        } else if (id == R.id.nav_about_us){
//            Intent intent = new Intent(HomeScreenActivity.this, AboutUsScreenActivity.class);
//            startActivity(intent);

        } else if (id == R.id.nav_logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you really want to Log out from Deliveroz?");
            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    editor.clear();
                    editor.commit();

                    Intent intent = new Intent(HomeScreenActivity.this, LoginScreenActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
