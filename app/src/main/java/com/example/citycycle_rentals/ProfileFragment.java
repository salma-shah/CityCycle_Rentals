package com.example.citycycle_rentals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    private EditText etFirstName, etLastName, etContact, etPassword;
    private TextView tvEmail, tvCardType, tvCardNumber, tvDob, tvCardExpiry, tvCardCCV, tvCardHolderName;
    private Button btnUpdateUser, btnUpdateCard;
    private ImageView imgProfile;
    private byte[] imageBytes;
    private DBHelper dbHelper;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        tvEmail = view.findViewById(R.id.tvEmail);
        etContact = view.findViewById(R.id.etContact);
        tvDob = view.findViewById(R.id.tvDob);
        imgProfile = view.findViewById(R.id.imgProfile);
        etPassword = view.findViewById(R.id.etPassword);
        tvCardHolderName = view.findViewById(R.id.tvCardHolderName);
        tvCardType = view.findViewById(R.id.tvCardType);
        tvCardCCV = view.findViewById(R.id.tvCardCCV);
        tvCardNumber = view.findViewById(R.id.tvCardNumber);
        tvCardExpiry = view.findViewById(R.id.tvCardExpiry);
        btnUpdateUser = view.findViewById(R.id.btnUpdateUser);
        btnUpdateCard = view.findViewById(R.id.btnUpdateCard);
        dbHelper = new DBHelper(requireActivity());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");

        if (userEmail != null) {
            // get user details if their email exists already
            User user = dbHelper.getUserData(userEmail);
            User cardDetails = dbHelper.getCardDetails(userEmail);

            // set the values
            if (user != null) {
                etFirstName.setText(user.getFirstName());
                etLastName.setText(user.getLastName());
                etContact.setText(user.getContact());
                etPassword.setText(user.getPassword());
                tvDob.setText(user.getDob());
                tvEmail.setText(user.getEmail());

                // if profile image is available, set it
                if (user.getProfileImage() != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(user.getProfileImage(), 0, user.getProfileImage().length);
                    imgProfile.setImageBitmap(bitmap);
                } else {
                    // otherwise default
                    imgProfile.setImageResource(R.drawable.baseline_person_24);
                }
            } else {
                // if no values found, just to ensure there will be no crashes
                etFirstName.setText("No First Name");
                etLastName.setText("No Last Name");
                etContact.setText("No Contact");
                tvDob.setText("No Date of Birth");
                etPassword.setText("No Password");
                tvEmail.setText("No Email");
            }

            // same applies for card details
            if (cardDetails != null) {
                tvCardType.setText(cardDetails.getCardType());
                tvCardHolderName.setText(cardDetails.getCardHolderName());

                // ensuring card number is not visible for security purposes
                String cardNumber = cardDetails.getCardNumber();
                String maskedCard = "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
                tvCardNumber.setText(maskedCard);
                tvCardCCV.setText(cardDetails.getCcv());
                tvCardExpiry.setText(cardDetails.getExpiryDate());
            } else {
                tvCardType.setText("No Card Type");
                tvCardHolderName.setText("No Card Holder Name");
                tvCardNumber.setText("No Card Number");
                tvCardCCV.setText("No CCV");
                tvCardExpiry.setText("No Expiry Date");
            }
        }

        imgProfile.setOnClickListener(v -> selectImageFromGallery());
        btnUpdateUser.setOnClickListener(v-> updateUserProfile());
        // to update card, user will be redirected to payment activity
        btnUpdateCard.setOnClickListener(v-> {
            Intent intent = new Intent(requireContext(), PaymentActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        return view;
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == requireActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imgProfile.setImageURI(imageUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                imageBytes = outputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUserProfile() {
        String newFirstName = etFirstName.getText().toString().trim();
        String newLastName = etLastName.getText().toString().trim();
        String newContact = etContact.getText().toString().trim();
        String newPassword = etPassword.getText().toString().trim();

        String passwordValidation = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";
        String contactValidation = "^\\+94\\d{9}$";

        // no updating dob
        // no email validation because email is unique and cannot be changed
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", requireActivity().MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        // validations
        if (!newContact.matches(contactValidation)) {
            Toast.makeText(requireActivity(), "Contact number is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.matches(passwordValidation)) {
            Toast.makeText(requireActivity(), "Password needs to be at least 8 characters long.", Toast.LENGTH_LONG).show();
            return;
        }

        // all fields must have valid values
        if (!newFirstName.isEmpty() && !newLastName.isEmpty() && !newContact.isEmpty() && !newPassword.isEmpty()) {
            boolean isUpdated = dbHelper.updateProfile(email, newFirstName, newLastName, newContact, newPassword, imageBytes);
            if (isUpdated) {
                Toast.makeText(requireActivity(), "Your profile details were updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireActivity(), "We could not update your profile.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireActivity(), "All fields are required!", Toast.LENGTH_SHORT).show();
        }
    }
}