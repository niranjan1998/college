package project.msc.college;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class no_internet extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        swipeRefreshLayout = findViewById(R.id.refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                checkConnection();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);

            }
        });

    }

    public void checkConnection() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (null != networkInfo) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                Intent intent = new Intent(no_internet.this, home.class);
                startActivity(intent);
                finish();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                Intent intent = new Intent(no_internet.this, home.class);
                startActivity(intent);
                finish();
            }
        } else {
            startActivity(getIntent());
            finish();
            overridePendingTransition(0, 0);
        }
    }
}
