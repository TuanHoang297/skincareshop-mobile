package fpt.edu.vn.skincareshop.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.models.User;
import fpt.edu.vn.skincareshop.services.AuthService;
import fpt.edu.vn.skincareshop.ui.home.MainActivity;
import fpt.edu.vn.skincareshop.utils.SharedPrefManager;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView tvGoToRegister; // ✅ thêm TextView điều hướng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister); // ✅ ánh xạ ID

        btnLogin.setOnClickListener(v -> handleLogin());

        // ✅ xử lý chuyển sang RegisterActivity
        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        User.LoginRequest request = new User.LoginRequest(email, password);

        AuthService.login(this, request, new AuthService.LoginCallback() {
            @Override
            public void onSuccess(User.LoginResponse response) {
                String token = response.getData().getAccessToken();
                User user = response.getData().getUser();

                SharedPrefManager prefs = SharedPrefManager.getInstance(LoginActivity.this);
                prefs.saveToken(token);
                prefs.saveUserId(user.getId());
                prefs.saveUsername(user.getName());

                if (user.getEmail() != null) {
                    prefs.saveEmail(user.getEmail());
                }

                if (user.getSkinType() != null) {
                    prefs.saveSkinType(user.getSkinType());
                }

                Log.d("LOGIN", "UserId = " + prefs.getUserId());

                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
