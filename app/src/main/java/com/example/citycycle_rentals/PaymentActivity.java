package com.example.citycycle_rentals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class PaymentActivity extends AppCompatActivity {
    private EditText etCardNumber, etCardHolderName, etCardExpiry, etCardCCV;
    private RadioGroup rgCardType;
    private Button btnSaveCard, btnSkipCard;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        etCardNumber = findViewById(R.id.etCardNumber);
        etCardHolderName = findViewById(R.id.etCardHolderName);
        etCardCCV = findViewById(R.id.etCardCCV);
        etCardExpiry = findViewById(R.id.etCardExpiry);
        rgCardType = findViewById(R.id.rgCardType);
        btnSaveCard = findViewById(R.id.btnSaveCard);
        btnSkipCard = findViewById(R.id.btnSkipCard);
        dbHelper = new DBHelper(this);

        // retrieving user email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");

        // checking is existing card exists for that email already
        User existingCard = dbHelper.getCardDetails(userEmail);
        // if exists, retrieve those details
        if (existingCard != null) {
            etCardHolderName.setText(existingCard.getCardHolderName());
            etCardNumber.setText(existingCard.getCardNumber());
            etCardCCV.setText(existingCard.getCcv());
            etCardExpiry.setText(existingCard.getExpiryDate());

            // card type from database
            for (int i = 0; i < rgCardType.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) rgCardType.getChildAt(i);
                if (radioButton.getText().toString().equals(existingCard.getCardType())) {
                    radioButton.setChecked(true);
                    break;
                }
            }
        }

        // save or update button
        btnSaveCard.setOnClickListener(view -> saveOrUpdateCard());
        // skip saving card option
        btnSkipCard.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentActivity.this, NavigationBar.class);
            startActivity(intent); }
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void saveOrUpdateCard() {
        String cardHolderName = etCardHolderName.getText().toString();
        String cardNumber = etCardNumber.getText().toString().trim();
        String cardCCV = etCardCCV.getText().toString();
        String cardExpiry = etCardExpiry.getText().toString();
        int card = rgCardType.getCheckedRadioButtonId();
        String cardType = card != -1 ? ((RadioButton) findViewById(card)).getText().toString() : "";

        dbHelper = new DBHelper(this);
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");

        // validations
        String ccvValidation = "^[0-9]{3,4}$";
        String expiryDateValidation = "^(0[1-9]|1[0-2])\\/([0-9]{2})$";
        String cardHolderNameValidation = "^[A-Za-z]+([ '-][A-Za-z]+)*$";
        if (cardHolderName.isEmpty() || cardNumber.isEmpty() || cardCCV.isEmpty() || cardExpiry.isEmpty() || cardType.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cardHolderName.matches(cardHolderNameValidation))
        {
            Toast.makeText(this, "Card Holder Name is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cardCCV.matches(ccvValidation)) {
            Toast.makeText(this, "CCV is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cardExpiry.matches(expiryDateValidation)) {
            Toast.makeText(this, "Expiry Date is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isExpiryDateValid(cardExpiry)) {
            Toast.makeText(this, "Expiry date is in the past", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cardNumber.length() > 19 || cardNumber.length() < 12) {
            Toast.makeText(this, "Card number is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

      User existingCard = dbHelper.getCardDetails(userEmail);
       if (existingCard == null) {
           // adding new card for that user
            boolean result = dbHelper.addCard(userEmail, cardType, cardHolderName, cardExpiry, cardCCV, cardNumber);
            if (result) {
                Toast.makeText(this, "Card saved successfully!", Toast.LENGTH_SHORT).show();

                // navigating to home page activity
                Intent intent = new Intent(PaymentActivity.this, NavigationBar.class);
                startActivity(intent);
          } else {
                Toast.makeText(this, "Error registering card!", Toast.LENGTH_SHORT).show();
            }
        } else {
//            // update existing card details
//               btnSkipCard.setVisibility(View.GONE);  // removing rent button since that bike is already currently rented
//               LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btnSaveCard.getLayoutParams();
//               params.weight = 2;
//               btnSaveCard.setLayoutParams(params); // let reserve button take up most space, since rent button will not be visible

            dbHelper.updateCard(userEmail, cardType, cardHolderName, cardNumber, cardCCV, cardExpiry);
            Toast.makeText(this, "Card updated successfully!", Toast.LENGTH_SHORT).show();
           // navigating back to the profile
           Intent intent = new Intent(PaymentActivity.this, ProfileFragment.class);
           startActivity(intent);
       }
    }

    private boolean isExpiryDateValid(String cardExpiry) {
        String[] dateParts = cardExpiry.split("/");
        int month = Integer.parseInt(dateParts[0]);
        int year = Integer.parseInt(dateParts[1]);
        if (year < 100) {
            year += 2000;
        }
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        if (year < currentYear || (year == currentYear && month < currentMonth)) {
            return false; // expiry date is in the past
        }
        return true; // expiry date is valid
    }
}
