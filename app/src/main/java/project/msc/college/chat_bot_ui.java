package project.msc.college;

        import android.annotation.SuppressLint;
        import android.os.Bundle;
        import android.webkit.WebSettings;
        import android.webkit.WebView;

        import androidx.appcompat.app.AppCompatActivity;

public class chat_bot_ui extends AppCompatActivity {
    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot_ui);

        webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String path = "file:android_asset/chatbotui";
        webView.loadUrl(path);

    }
}
