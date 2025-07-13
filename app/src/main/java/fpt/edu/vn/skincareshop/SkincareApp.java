package fpt.edu.vn.skincareshop;

import android.app.Application;
import android.content.Context;

public class SkincareApp extends Application {

    private static SkincareApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Nếu sau này bạn cần init Firebase, logging, Crashlytics... thì thêm tại đây
    }

    public static SkincareApp getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
