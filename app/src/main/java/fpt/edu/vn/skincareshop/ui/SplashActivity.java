package fpt.edu.vn.skincareshop.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.ui.auth.LoginActivity;
import fpt.edu.vn.skincareshop.ui.home.MainActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000; // 2 giây
    private static final String PREF_NAME = "user_pref";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Fade-in animation cho logo
        ImageView imgLogo = findViewById(R.id.imgSplashLogo);
        imgLogo.setAlpha(0f);
        imgLogo.animate().alpha(1f).setDuration(1500).start();

        new Handler().postDelayed(() -> {
            if (isUserLoggedIn()) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, SPLASH_DURATION);
    }

    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
