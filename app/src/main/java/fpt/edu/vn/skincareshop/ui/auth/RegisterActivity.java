package fpt.edu.vn.skincareshop.ui.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.models.User;
import fpt.edu.vn.skincareshop.services.AuthService;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPassword;
    private Button btnRegister;
    private TextView tvGoToLogin; // ✅ TextView điều hướng về login

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoToLogin = findViewById(R.id.tvGoToLogin); // ✅ ánh xạ ID

        btnRegister.setOnClickListener(v -> handleRegister());

        // ✅ xử lý khi bấm “Đã có tài khoản? Đăng nhập”
        tvGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish(); // hoặc bỏ nếu muốn quay lại
        });
    }

    private void handleRegister() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        AuthService.register(this, user, new AuthService.RegisterCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                finish(); // quay về LoginActivity hoặc MainActivity nếu muốn
            }

            @Override
            public void onError(String message) {
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
