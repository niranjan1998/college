package project.msc.college;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import java.util.Timer;
import java.util.TimerTask;

public class home extends AppCompatActivity {

    Animation name_ani;
    TextView name;

    Boolean isNightModeOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setNavigationBarColor(ContextCompat.getColor(home.this, R.color.colorLight));

        SharedPreferences sharedPreferences = getSharedPreferences("system", MODE_PRIVATE);
        isNightModeOn = sharedPreferences.getBoolean("NightMode", false);


        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        name_ani = AnimationUtils.loadAnimation(this, R.anim.name_animation);
        name = findViewById(R.id.college_app);
        name.setAnimation(name_ani);

        checkConnection();
        /*
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {


                Intent intent = new Intent(home.this, userLogin.class);
                startActivity(intent);
                finish();

            }
        }, 1000);*/
    }

    public void checkConnection() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (null != networkInfo) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {


                        Intent intent = new Intent(home.this, userLogin.class);
                        startActivity(intent);
                        finish();

                    }
                }, 1000);
                // Toast.makeText(getApplicationContext(),"Wifi Enabled",Toast.LENGTH_SHORT).show();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(home.this, userLogin.class);
                        startActivity(intent);
                        finish();

                    }
                }, 1000);
                //  Toast.makeText(getApplicationContext(),"Data Enabled",Toast.LENGTH_SHORT).show();
            }
        } else {

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    Intent intent = new Intent(home.this, no_internet.class);
                    startActivity(intent);
                    finish();

                }
            }, 1000);

            //   Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

    }


}
