package fpt.edu.vn.skincareshop.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class FavoriteManager {

    private static final String PREF_NAME = "favorites_pref";
    private static final String KEY_FAVORITES = "favorite_ids";

    public static void setFavorite(Context context, String productId, boolean isFavorite) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> favorites = prefs.getStringSet(KEY_FAVORITES, new HashSet<>());

        Set<String> updatedFavorites = new HashSet<>(favorites);
        if (isFavorite) {
            updatedFavorites.add(productId);
        } else {
            updatedFavorites.remove(productId);
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(KEY_FAVORITES, updatedFavorites);
        editor.apply();
    }

    public static boolean isFavorite(Context context, String productId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> favorites = prefs.getStringSet(KEY_FAVORITES, new HashSet<>());
        return favorites.contains(productId);
    }

    public static Set<String> getAllFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getStringSet(KEY_FAVORITES, new HashSet<>());
    }
    public static void toggleFavorite(Context context, String productId) {
        boolean isFav = isFavorite(context, productId);
        setFavorite(context, productId, !isFav);
    }

}
