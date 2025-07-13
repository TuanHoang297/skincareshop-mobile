package fpt.edu.vn.skincareshop.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.ui.auth.LoginActivity;
import fpt.edu.vn.skincareshop.ui.cart.CartFragment;
import fpt.edu.vn.skincareshop.ui.product.ProductFragment;
import fpt.edu.vn.skincareshop.ui.profile.ProfileFragment;
import fpt.edu.vn.skincareshop.ui.routine.RoutineFragment;
import fpt.edu.vn.skincareshop.utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra đăng nhập
        SharedPrefManager pref = SharedPrefManager.getInstance(this);
        if (!pref.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.bottom_nav);

        // Listener cho tab
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_products) {
                selectedFragment = new ProductFragment();
            } else if (id == R.id.nav_cart) {
                selectedFragment = new CartFragment();
            } else if (id == R.id.nav_routine) {
                selectedFragment = new RoutineFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, selectedFragment)
                        .commit();
                return true;
            }

            return false;
        });

        // ✅ Xử lý tab mở theo intent
        boolean openCart = getIntent().getBooleanExtra("openCartTab", false);
        if (openCart) {
            bottomNav.setSelectedItemId(R.id.nav_cart); // Mở Cart
        } else {
            bottomNav.setSelectedItemId(R.id.nav_products); // Mặc định mở Product
        }
    }
}
