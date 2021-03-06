package lk.cse13.www.uomwireless.Views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import lk.cse13.www.uomwireless.BackgroundLogin;
import lk.cse13.www.uomwireless.BackgroundLogout;
import lk.cse13.www.uomwireless.Operations;
import lk.cse13.www.uomwireless.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Button loginButton;
    public static boolean loggedIn = false;
    public static boolean screenShowing = false;
    public static Context mainContext;
    public static Boolean loginScreenShowing = false;//Settings page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContext = MainActivity.this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!Operations.isLocationEnabled()) {
                    return;
                }
                if (Operations.isConnectedToUoMWireless() || Operations.isConnectedToOtherSSID()) {
                    if (loggedIn) {
                        Operations.toast("Logging out...");
                        new BackgroundLogout().execute();
                    } else {
                        Operations.toast("Logging in...");
                        new BackgroundLogin(0).execute();
                    }
                } else {
                    String otherssid = Operations.getOtherSSID();
                    Operations.toast("Connect to UoM Wireless" + (otherssid == null ? "" : "/" + otherssid) + " first");
                }

            }
        });
        if (Operations.isLocationEnabled()) {
            new BackgroundLogin(0).execute();
        }
        Operations.cancelNotification();
    }

    @Override
    protected void onResume() {
        super.onResume();
        screenShowing = true;
    }

    protected void onStop() {
        super.onStop();
        screenShowing = false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_guide) {
//            startActivity(new Intent(MainActivity.this, MainActivity.class));
            Intent i = new Intent(getApplicationContext(), HuaweiStepsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_moodle) {
            startMoodleApp();
        } else if (id == R.id.nav_lms) {
            Intent i = new Intent(getApplicationContext(), WebActivity.class);
            i.putExtra("site", "lms");
            startActivity(i);
        } else if (id == R.id.nav_webmail) {
            Intent i = new Intent(getApplicationContext(), WebActivity.class);
            i.putExtra("site", "webmail");
            startActivity(i);
        } else if (id == R.id.nav_source) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/kmchmk1026/UoM-WiFi-Login"));
            startActivity(i);
        } else if (id == R.id.nav_windows) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wearetrying.info/uomw/"));
            startActivity(i);
        } else if (id == R.id.nav_email) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"kmchmk@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "About UoM Login App");
            i.putExtra(Intent.EXTRA_TEXT, "Hi,\n\n");
            startActivity(i);
        } else if (id == R.id.nav_messenger) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://messaging/" + "100001539300658"));
            startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Operations.MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //Permission denied
                    Operations.toast("Please grant location permission");
                }
            }
        }
    }

    private void startMoodleApp() {
        String moodlePackageName = "com.moodle.moodlemobile";
        Intent intent = MainActivity.mainContext.getPackageManager().getLaunchIntentForPackage(moodlePackageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + moodlePackageName));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            MainActivity.mainContext.startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" + moodlePackageName));
            MainActivity.mainContext.startActivity(intent);
        }
    }
}
