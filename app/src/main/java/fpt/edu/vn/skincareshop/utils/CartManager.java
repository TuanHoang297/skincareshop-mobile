package fpt.edu.vn.skincareshop.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fpt.edu.vn.skincareshop.models.CartItem;
import fpt.edu.vn.skincareshop.models.Product;

public class CartManager {
    private static final String PREF_NAME = "cart_pref";
    private static final String KEY_CART = "cart_items";

    // ✅ Trả về true nếu thêm thành công, false nếu đã đạt giới hạn
    public static boolean addToCart(Context context, Product product) {
        List<CartItem> cart = getCart(context);

        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(product.getId())) {
                if (item.getQuantity() >= 50) {
                    return false; // Đã đạt giới hạn
                }
                item.setQuantity(item.getQuantity() + 1);
                saveCart(context, cart);
                return true;
            }
        }

        // Nếu chưa có trong giỏ → thêm mới
        cart.add(new CartItem(product, 1));
        saveCart(context, cart);
        return true;
    }

    public static List<CartItem> getCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_CART, "");
        if (json.isEmpty()) return new ArrayList<>();

        Type type = new TypeToken<List<CartItem>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    private static void saveCart(Context context, List<CartItem> cart) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(cart);
        editor.putString(KEY_CART, json);
        editor.apply();
    }

    public static void clearCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_CART);
        editor.apply();
    }

    public static void removeItemFromCart(Context context, String productId) {
        List<CartItem> cart = getCart(context);

        List<CartItem> updatedCart = new ArrayList<>();
        for (CartItem item : cart) {
            if (!item.getProduct().getId().equals(productId)) {
                updatedCart.add(item);
            }
        }

        saveCart(context, updatedCart);
    }

    public static void updateQuantity(Context context, String productId, int quantity) {
        if (quantity > 50) quantity = 50; // ✅ Giới hạn luôn tại đây

        List<CartItem> cart = getCart(context);
        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                break;
            }
        }

        saveCart(context, cart);
    }
}
