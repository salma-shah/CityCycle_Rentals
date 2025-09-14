package com.example.citycycle_rentals;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {
// importing widgets
    private EditText etFirstName, etLastName, etEmail, etContact, etDob, etPassword;
    private ImageView imgProfile;
    private byte[] imageBytes;
    private Button btnRegister;
    private CheckBox cbTerms;
    private TextView tvLogin;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DBHelper(this);

        // initializing the User Interface elements with their ids
        etFirstName =  findViewById(R.id.etFirstName);
        etLastName =  findViewById(R.id.etLastName);
        etEmail =  findViewById(R.id.etEmail);
        etContact =  findViewById(R.id.etContact);
        etPassword =  findViewById(R.id.etPassword);
        etDob =  findViewById(R.id.etDob);
        imgProfile = findViewById(R.id.imgProfile);
        cbTerms = findViewById(R.id.cbTerms);
        tvLogin = findViewById(R.id.tvLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // this is to show calendar when you need to choose date of birth onClick
        etDob.setOnClickListener(view -> showDatePicker());

        // to show ImageView or gallery to select image onClick
        imgProfile.setOnClickListener(v -> selectImageFromGallery());

        // Register button action
        btnRegister.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v -> {
          Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
          startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imgProfile.setImageURI(imageUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                imageBytes = outputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            etDob.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void registerUser() {
        // registering user information by taking data fed into fields and turning to String values
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String email = etEmail.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String dob = etDob.getText().toString();
        String password = etPassword.getText().toString();

        // validation values
        String emailValidation = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String passwordValidation = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";
        String contactValidation = "^\\+94\\d{9}$";

        // validations
        // no empty fields
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || contact.isEmpty() || dob.isEmpty() || password.isEmpty() || imageBytes==null) {
            Toast.makeText(this, "All fields need to be filled.", Toast.LENGTH_SHORT).show();
            return;
        }

//      //email validation
        if (!email.matches(emailValidation)){
            Toast.makeText(this, "Email address is invalid.", Toast.LENGTH_SHORT).show();
            return;
        }

        //phone number validation, ensuring local number is used
        if (!contact.matches(contactValidation)){
           Toast.makeText(this, "Number must be a local number.", Toast.LENGTH_SHORT).show();
             return;
        }

        //password validation
        if (!password.matches(passwordValidation) ){
            Toast.makeText(this, "Password needs to be at least 8 characters long.", Toast.LENGTH_LONG).show();
            return;
        }

      // terms and conditions must be agreed to
        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please agree to the Terms and Conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = dbHelper.addUser(firstName, lastName, contact, dob, email, password, imageBytes);

        if (result) {
            Toast.makeText(this, "User registration was successful!", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", email);
            editor.apply();
            // navigating to payment page activity
            startActivity(new Intent(SignUpActivity.this, PaymentActivity.class));
        } else {
            Toast.makeText(this, "Error registering user!", Toast.LENGTH_SHORT).show();
        }
    }

}