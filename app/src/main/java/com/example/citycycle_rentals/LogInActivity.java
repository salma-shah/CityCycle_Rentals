package com.example.citycycle_rentals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LogInActivity extends AppCompatActivity {
    private EditText etLoginPassword, etLoginEmail;
    private Button btnLogin;
    private TextView tvSignUp;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initializing the User Interface elements with their ids
        etLoginEmail =  findViewById(R.id.etLoginEmail);
        etLoginPassword =  findViewById(R.id.etLoginPassword);
        btnLogin =  findViewById(R.id.btnLogin);
        tvSignUp =  findViewById(R.id.tvSignUp);

        dbHelper = new DBHelper(this);

        btnLogin.setOnClickListener(view -> {
            String email = etLoginEmail.getText().toString().trim();
            String password = etLoginPassword.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter the email.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter the password.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.checkUser(email, password)) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

            // save user email in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", email);
            editor.apply();

            // Navigate to home page from Login activity
                Intent intent = new Intent(LogInActivity.this, NavigationBar.class);
                startActivity(intent);
                finish();
        } else {
            Toast.makeText(this, "The email or password is invalid. Login was unsuccessful!", Toast.LENGTH_SHORT).show();
        }
        });

        // redirects to sign up activity, if user does not have an account already
        tvSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

    }
}
