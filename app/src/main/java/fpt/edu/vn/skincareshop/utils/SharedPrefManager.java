package fpt.edu.vn.skincareshop.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String PREF_NAME = "skincare_prefs";
    private static final String KEY_TOKEN = "access_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_SKIN_TYPE = "skin_type";
    private static final String KEY_ORDER_ID = "latest_order_id";
    private static final String KEY_EMAIL = "user_email";
    private static SharedPrefManager instance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public static SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context.getApplicationContext());
        }
        return instance;
    }

    private SharedPrefManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // ===== TOKEN =====
    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    // ===== USER ID =====
    public void saveUserId(String userId) {
        editor.putString(KEY_USER_ID, userId).apply();
    }

    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    // ===== USERNAME =====
    public void saveUsername(String username) {
        editor.putString(KEY_USERNAME, username).apply();
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "Ẩn danh");
    }

    // ===== SKIN TYPE =====
    public void saveSkinType(String skinType) {
        editor.putString(KEY_SKIN_TYPE, skinType.toLowerCase()).apply();
    }

    public String getSkinType() {
        return prefs.getString(KEY_SKIN_TYPE, null);
    }

    // ===== ORDER ID =====
    public void saveLatestOrderId(String orderId) {
        editor.putString(KEY_ORDER_ID, orderId).apply();
    }

    public String getLatestOrderId() {
        return prefs.getString(KEY_ORDER_ID, null);
    }

    // ===== CHECK LOGIN STATUS =====
    public boolean isLoggedIn() {
        return getToken() != null && getUserId() != null;
    }
    public void saveEmail(String email) {
        editor.putString(KEY_EMAIL, email).apply();
    }

    public String getUserEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    // ===== REMOVE SINGLE ITEM =====
    public void remove(String key) {
        editor.remove(key).apply();
    }

    // ===== CLEAR ALL =====
    public void clear() {
        editor.clear().apply();
    }
}
