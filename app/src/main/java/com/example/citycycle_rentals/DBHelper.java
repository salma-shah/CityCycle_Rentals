package com.example.citycycle_rentals;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    // database
    private static final String DATABASE_NAME = "CityCycleRentals.db";
    private static final int DATABASE_VERSION = 1;

    // users table
    public static final String TABLE_USERS = "user";
    public static final String USERS_COLUMN_ID = "id";
    public static final String USERS_COLUMN_FIRST_NAME = "first_name";
    public static final String USERS_COLUMN_LAST_NAME = "last_name";
    public static final String USERS_COLUMN_CONTACT = "contact";
    public static final String USERS_COLUMN_DOB = "dob";
    public static final String USERS_COLUMN_EMAIL = "email";
    public static final String USERS_COLUMN_PASSWORD = "password";
    public static final String USERS_COLUMN_PROFILE_IMAGE = "profile_image";


    // card information table
    private static final String TABLE_CARDS = "card_details";
    private static final String CARDS_COLUMN_ID = "card_id";
    private static final String CARDS_COLUMN_USER_EMAIL = "user_email";
    private static final String CARDS_COLUMN_TYPE = "card_type";
    private static final String CARDS_COLUMN_NUMBER = "card_number";
    private static final String CARDS_COLUMN_HOLDER_NAME = "card_holder_name";
    private static final String CARDS_COLUMN_EXPIRY_DATE = "expiry_date";
    private static final String CARDS_COLUMN_CCV = "ccv";

    // bikes table
    private static final String TABLE_BIKES = "bike";
    public static final String BIKES_COLUMN_ID = "bike_id";
    public static final String BIKES_COLUMN_NAME = "name";
    public static final String BIKES_COLUMN_CATEGORY = "category";
    public static final String BIKES_COLUMN_DESCRIPTION = "description";
    public static final String BIKES_COLUMN_LOCATION = "location";
    public static final String BIKES_COLUMN_PRICE_PER_HOUR = "price_hour";
    public static final String BIKES_COLUMN_PRICE_PER_DAY = "price_day";
    public static final String BIKES_COLUMN_PRICE_PER_WEEK = "price_week";
    public static final String BIKES_COLUMN_STATUS = "status";
    public static final String BIKES_COLUMN_RATING = "rating";
    public static final String BIKES_COLUMN_BIKE_IMAGE = "bike_image";

    // reservation table
    private static final String TABLE_RESERVATIONS = "reservation";
    public static final String RESERVATIONS_COLUMN_ID = "reservation_id";
    public static final String RESERVATIONS_COLUMN_USER_EMAIL = "user_email";
    public static final String RESERVATIONS_COLUMN_BIKE_ID = "bike_id";
    public static final String RESERVATIONS_COLUMN_START_TIME= "start_time";
    public static final String RESERVATIONS_COLUMN_DURATION = "duration";
    public static final String RESERVATIONS_COLUMN_STATUS = "status";

    // rental table
    private static final String TABLE_RENTALS = "rental";
    public static final String RENTALS_COLUMN_ID = "rental_id";
    public static final String RENTALS_COLUMN_USER_EMAIL = "user_email";
    public static final String RENTALS_COLUMN_BIKE_ID = "bike_id";
    public static final String RENTALS_COLUMN_START_DATE = "start_date";
    public static final String RENTALS_COLUMN_END_DATE = "end_date";
    public static final String RENTALS_COLUMN_DURATION = "duration";
    public static final String RENTALS_COLUMN_STATUS = "status";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }


    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                USERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERS_COLUMN_FIRST_NAME + " TEXT, " +
                USERS_COLUMN_LAST_NAME + " TEXT, " +
                USERS_COLUMN_CONTACT + " TEXT, " +
                USERS_COLUMN_DOB + " TEXT, " +
                USERS_COLUMN_EMAIL + " TEXT UNIQUE, " +
                USERS_COLUMN_PASSWORD + " TEXT, " +
                USERS_COLUMN_PROFILE_IMAGE + " BLOB)";
        try {
            db.execSQL(createUsersTable);
            Log.d("Database", "User table created successfully!");
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error creating table: " + e.getMessage());
        }

        String createCardTable = "CREATE TABLE " + TABLE_CARDS + " (" +
                CARDS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CARDS_COLUMN_USER_EMAIL + " TEXT, " +
                CARDS_COLUMN_TYPE + " TEXT, " +
                CARDS_COLUMN_NUMBER + " TEXT, " +
                CARDS_COLUMN_HOLDER_NAME + " TEXT, " +
                CARDS_COLUMN_CCV + " TEXT, " +
                CARDS_COLUMN_EXPIRY_DATE + " TEXT, " +
                "FOREIGN KEY(" + CARDS_COLUMN_USER_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + USERS_COLUMN_EMAIL + ") ON DELETE CASCADE)";
        try {
            db.execSQL(createCardTable);
            Log.d("Database", "Cards table created successfully!");
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error creating table: " + e.getMessage());
        }

        String createBikeTable = "CREATE TABLE " + TABLE_BIKES + " (" +
                BIKES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BIKES_COLUMN_NAME + " TEXT, " +
                BIKES_COLUMN_CATEGORY + " TEXT, " +
                BIKES_COLUMN_DESCRIPTION + " TEXT, " +
                BIKES_COLUMN_LOCATION + " TEXT, " +
                BIKES_COLUMN_PRICE_PER_HOUR + " TEXT, " +
                BIKES_COLUMN_PRICE_PER_DAY + " TEXT, " +
                BIKES_COLUMN_PRICE_PER_WEEK + " TEXT, " +
                BIKES_COLUMN_STATUS + " TEXT, " +
                BIKES_COLUMN_RATING + " TEXT, " +
                BIKES_COLUMN_BIKE_IMAGE + " INTEGER)";

        try {
            db.execSQL(createBikeTable);
            insertBikeData(db);
            Log.d("Database", "Bike table created successfully!");
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error creating table: " + e.getMessage());
        }

        String createReservationsTable = "CREATE TABLE " + TABLE_RESERVATIONS + " (" +
                RESERVATIONS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RESERVATIONS_COLUMN_USER_EMAIL + " TEXT, " +
                RESERVATIONS_COLUMN_BIKE_ID + " INTEGER, " +
                RESERVATIONS_COLUMN_START_TIME + " DATETIME, " +
                RESERVATIONS_COLUMN_DURATION + " TEXT, " +
                RESERVATIONS_COLUMN_STATUS + " TEXT DEFAULT 'Available', " +
                "FOREIGN KEY(" + RESERVATIONS_COLUMN_USER_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + USERS_COLUMN_EMAIL + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + RESERVATIONS_COLUMN_BIKE_ID + ") REFERENCES " + TABLE_BIKES + "(" + BIKES_COLUMN_ID + ") ON DELETE CASCADE);";
        try {
            db.execSQL(createReservationsTable);
            Log.d("Database", "Reservation table created successfully!");
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error creating table: " + e.getMessage());
        }

        String createRentalsTable = "CREATE TABLE " + TABLE_RENTALS + " (" +
                RENTALS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RENTALS_COLUMN_USER_EMAIL + " TEXT, " +
                RENTALS_COLUMN_BIKE_ID + " INTEGER, " +
                RENTALS_COLUMN_DURATION + " TEXT, " +
                RENTALS_COLUMN_START_DATE + " DATETIME, " +
                RENTALS_COLUMN_END_DATE + " DATETIME, " +
                RENTALS_COLUMN_STATUS + " TEXT DEFAULT 'Available', " +
                "FOREIGN KEY(" + RENTALS_COLUMN_USER_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + USERS_COLUMN_EMAIL + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + RENTALS_COLUMN_BIKE_ID + ") REFERENCES " + TABLE_BIKES + "(" + BIKES_COLUMN_ID + ") ON DELETE CASCADE);";
        try {
            db.execSQL(createRentalsTable);
            Log.d("Database", "Rental table created successfully!");
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error creating table: " + e.getMessage());
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RENTALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BIKES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void insertBikeData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_BIKES + " (name, category, description, location, price_hour, price_day, price_week, status, rating, bike_image) VALUES " +
                "('Urban Cruiser', 'City Bike', 'A lightweight and comfortable city bicycle designed for urban commuting. Features a sleek aluminum frame, smooth-rolling tires, and an ergonomic seat.', 'Colombo 03', 1600, 8000, 38000, 'Available', 4.5, " + R.drawable.urban_cruiser + "), " +
                "('Speedster Pro', 'Road Bike', 'A high-performance road bike built for speed and agility. Designed with an aerodynamic carbon frame and precision gears.', 'Colombo 05', 2500, 12000, 60000, 'Available', 4.8, " + R.drawable.speedstar_pro + "), " +
                "('Terrain Explorer', 'Mountain Bike', 'A rugged mountain bicycle designed to handle rough terrain and steep inclines. Features a durable alloy frame, shock-absorbing suspension, and thick tires.', 'Dehiwala', 2000, 10000, 50000, 'Available', 4.6, " + R.drawable.terrain_explorer + "), " +
                "('Coastal Cruiser', 'Electric Bike', 'An eco-friendly electric bike with a powerful motor for assisted pedaling. Great for long-distance rides with minimal effort.', 'Colombo 07', 3000, 15000, 70000, 'Available', 4.9, " + R.drawable.coastal_cruiser + "), " +
                "('Beach Explorer', 'Fat Tire Bike', 'A tough and durable fat tire bike built for sandy beaches and rugged trails. Wide tires provide excellent grip and comfort.', 'Mount Lavinia', 2900, 14500, 69000, 'Available', 4.7, " + R.drawable.beach_explorer + "), " +
                "('Elite Racer', 'Premium Road Bike', 'A top-tier road bike built with lightweight titanium alloy, perfect for competitive cycling with superior speed and handling.', 'Colombo 01', 1600, 35000, 120000, 'Available', 5.0, " + R.drawable.elite_racer + "), " +
                "('Carbon Fusion', 'Premium Mountain Bike', 'A premium mountain bicycle with carbon fiber frame and advanced suspension system designed for competitive off-road riding.', 'Colombo 06', 2500, 50000, 150000, 'Available', 4.9, " + R.drawable.carbon_fusion + "), " +
                "('Sky Racer', 'Premium BMX Bike', 'A BMX cycle designed for professionals, with reinforced alloy frame and smooth welds for high-impact tricks and stunts.', 'Colombo 09', 2200, 30000, 100000, 'Available', 5.0, " + R.drawable.sky_racer + "), " +
                "('Luxury Cruiser', 'Premium Electric Bike', 'A high-end electric bike with long-lasting battery, comfortable leather seat, and an elegant design for the discerning rider.', 'Colombo 02', 2800, 45000, 140000, 'Available', 5.0, " + R.drawable.luxury_cruiser + "), " +
                "('Mini Cycler', 'Children Bike', 'A cute and safe bicycle for children, designed with a colorful frame and sturdy wheels to ensure safety and fun during rides.', 'Colombo 10', 600, 3500, 12000, 'Available', 4.7, " + R.drawable.mini_cycler + "), " +
                "('Teddy Tread', 'Children Bike', 'A fun, kid-sized bicycle designed for learning to ride with bright colors, a small frame, and training wheels.', 'Kandy', 700, 3000, 11000, 'Available', 4.8, " + R.drawable.teddy_tread + "), " +
                "('Turbo Kid', 'Children Electric Bike', 'An electric bicycle designed for children, featuring a low-speed motor, sturdy frame, and easy-to-use controls for young riders.', 'Moratuwa', 900, 6000, 25000, 'Available', 4.9, " + R.drawable.turbo_kid + "), " +
                "('Urban Commuter', 'City Bike', 'A sleek and practical city bike designed for daily commutes with a lightweight steel frame and smooth tires.', 'Colombo 01', 1700, 9000, 42000, 'Available', 4.4, " + R.drawable.urban_commuter + "), " +
                "('Electric Glider', 'Electric Bike', 'A quiet, eco-friendly electric bike perfect for long-distance rides with fast charging and a smooth ride.', 'Colombo 04', 2800, 16000, 75000, 'Available', 5.0, " + R.drawable.electric_glider + "), " +
                "('Mountain Rover', 'Mountain Bike', 'A rugged mountain bike built for adventure, featuring top-tier suspension and durable tires.', 'Kandy', 2500, 14000, 70000, 'Available', 4.8, " + R.drawable.mountain_rover + "), " +
                "('City Cruiser', 'City Bike', 'A versatile hybrid bike ideal for urban commuting, offering a comfortable ride with smooth gear shifts.', 'Galle', 2200, 13000, 68000, 'Available', 4.7, " + R.drawable.city_cruiser + "), " +
                "('Desert Racer', 'BMX Bike', 'A tough and fast BMX bike designed to withstand desert-like conditions, ideal for stunts in hot climates.', 'Anuradhapura', 2000, 9500, 47000, 'Available', 4.5, " + R.drawable.desert_racer + ");");
    }

    public boolean addUser(String firstName, String lastName, String contact, String dob, String email, String password, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERS_COLUMN_FIRST_NAME, firstName);
        values.put(USERS_COLUMN_LAST_NAME, lastName);
        values.put(USERS_COLUMN_CONTACT, contact);
        values.put(USERS_COLUMN_DOB, dob);
        values.put(USERS_COLUMN_EMAIL, email);
        values.put(USERS_COLUMN_PASSWORD, password);
        values.put(USERS_COLUMN_PROFILE_IMAGE, image);

        Log.d("DBHelper", "Adding user: " + email);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email = ? AND password = ?",
                new String[]{email, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean addCard(String email, String cardType, String cardHolderName, String cardExpiry, String cardCCV, String cardNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CARDS_COLUMN_NUMBER, cardNumber);
        values.put(CARDS_COLUMN_TYPE, cardType);
        values.put(CARDS_COLUMN_HOLDER_NAME, cardHolderName);
        values.put(CARDS_COLUMN_EXPIRY_DATE, cardExpiry);
        values.put(CARDS_COLUMN_CCV, cardCCV);
        values.put(CARDS_COLUMN_USER_EMAIL, email);

        long result = db.insert(TABLE_CARDS, null, values);
        if (result == -1) {
            Log.e("DBHelper", "Error inserting card for user: " + email);
            return false;
        } else {
            Log.d("DBHelper", "Card inserted successfully for user: " + email);
            return true;
        }
    }

    public List<Bike> getAllBikes() {
        List<Bike> bikeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BIKES, null);

        if (cursor.moveToFirst()) {
            do {
                bikeList.add(new Bike(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getDouble(5),
                        cursor.getDouble(6),
                        cursor.getDouble(7),
                        cursor.getString(8),
                        cursor.getFloat(9),
                        cursor.getInt(10)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bikeList;
    }

    public User getUserData(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT u.id, u.email, u.first_name, u.last_name, u.contact, u.dob, u.password, u.profile_image FROM "
                + TABLE_USERS + " u WHERE u.email = ?", new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            int userID = cursor.getInt(cursor.getColumnIndexOrThrow(USERS_COLUMN_ID));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_LAST_NAME));
            String contact = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_CONTACT));
            String dob = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_DOB));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_PASSWORD));
            byte[] profileImage = cursor.getBlob(cursor.getColumnIndexOrThrow(USERS_COLUMN_PROFILE_IMAGE));

            cursor.close();
            return new User(userID, firstName, lastName, contact, dob, password, email, profileImage);
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public User getCardDetails(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CARDS + " WHERE user_email=? ", new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(CARDS_COLUMN_USER_EMAIL));
            String cardType = cursor.getString(cursor.getColumnIndexOrThrow(CARDS_COLUMN_TYPE));
            String cardHolderName = cursor.getString(cursor.getColumnIndexOrThrow(CARDS_COLUMN_HOLDER_NAME));
            String cardNumber = cursor.getString(cursor.getColumnIndexOrThrow(CARDS_COLUMN_NUMBER));
            String ccv = cursor.getString(cursor.getColumnIndexOrThrow(CARDS_COLUMN_CCV));
            String expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(CARDS_COLUMN_EXPIRY_DATE));

            cursor.close();
            return new User(userEmail, cardType, cardNumber, cardHolderName, expiryDate, ccv);
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String fullName = null;
        Cursor cursor = db.rawQuery("SELECT first_name, last_name FROM " + TABLE_USERS + " WHERE email = ?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_LAST_NAME));
            fullName = firstName + " " + lastName;
            cursor.close();
        }

        db.close();
        return fullName;
    }

    public User getUserImportantDetails(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT first_name, last_name, email, contact FROM " + TABLE_USERS + " WHERE email = ?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_LAST_NAME));
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_EMAIL));
            String contactNumber = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_CONTACT));

            cursor.close();
            return new User(firstName, lastName, contactNumber, userEmail);
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public Bike getBikeDetails(int bikeID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BIKES + " WHERE bike_id = ?", new String[]{String.valueOf(bikeID)});

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(BIKES_COLUMN_NAME));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(BIKES_COLUMN_LOCATION));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(BIKES_COLUMN_CATEGORY));
            int priceHour = cursor.getInt(cursor.getColumnIndexOrThrow(BIKES_COLUMN_PRICE_PER_HOUR));
            int priceDay = cursor.getInt(cursor.getColumnIndexOrThrow(BIKES_COLUMN_PRICE_PER_DAY));
            int priceWeek = cursor.getInt(cursor.getColumnIndexOrThrow(BIKES_COLUMN_PRICE_PER_WEEK));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(BIKES_COLUMN_DESCRIPTION));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(BIKES_COLUMN_STATUS));
            float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(BIKES_COLUMN_RATING));
            int imageBike = cursor.getInt(cursor.getColumnIndexOrThrow(BIKES_COLUMN_BIKE_IMAGE));

            cursor.close();
            return new Bike(bikeID, name, category, description, location, priceHour, priceDay, priceWeek, status, rating, imageBike);
        }

        cursor.close();
        return null;
    }

    public Bike getBikeImportantDetails(int bikeID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT bike_id, name, location, bike_image FROM " + TABLE_BIKES + " WHERE bike_id = ?", new String[]{String.valueOf(bikeID)});

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(BIKES_COLUMN_NAME));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(BIKES_COLUMN_LOCATION));
            int imageBike = cursor.getInt(cursor.getColumnIndexOrThrow(BIKES_COLUMN_BIKE_IMAGE));
            cursor.close();
            return new Bike(bikeID, name, location, imageBike);
        }

        cursor.close();
        return null;
    }

    public boolean updateProfile(String email, String firstName, String lastName, String contact, String password, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERS_COLUMN_FIRST_NAME, firstName);
        values.put(USERS_COLUMN_LAST_NAME, lastName);
        values.put(USERS_COLUMN_CONTACT, contact);
        values.put(USERS_COLUMN_PASSWORD, password);
        values.put(USERS_COLUMN_PROFILE_IMAGE, image);

        int rowsAffected = db.update("user", values, "email=?", new String[]{email});
        return rowsAffected > 0;
    }

    public boolean updateCard(String email, String cardType, String cardHolderName, String cardExpiry, String cardCCV, String cardNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CARDS_COLUMN_TYPE, cardType);
        values.put(CARDS_COLUMN_HOLDER_NAME, cardHolderName);
        values.put(CARDS_COLUMN_EXPIRY_DATE, cardExpiry);
        values.put(CARDS_COLUMN_CCV, cardCCV);
        values.put(CARDS_COLUMN_NUMBER, cardNumber);

        int rowsAffected = db.update(TABLE_CARDS, values, "user_email=?", new String[]{email});
        return rowsAffected > 0;
    }

    public long addReservation(String userEmail, int bikeId, String pickUpDate, String duration, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RESERVATIONS_COLUMN_USER_EMAIL, userEmail);
        values.put(RESERVATIONS_COLUMN_BIKE_ID, bikeId);
        values.put(RESERVATIONS_COLUMN_START_TIME, pickUpDate);
        values.put(RESERVATIONS_COLUMN_DURATION, duration);
        values.put(RESERVATIONS_COLUMN_STATUS, status);

        Log.d("DBHelper", "Adding reservation for user email: " + userEmail);

        long result = db.insert(TABLE_RESERVATIONS, null, values);
        return result;
    }

    public long addRental(String userEmail, int bikeId, String startDate, String endDate, String duration, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RENTALS_COLUMN_USER_EMAIL, userEmail);
        values.put(RENTALS_COLUMN_BIKE_ID, bikeId);
        values.put(RENTALS_COLUMN_START_DATE, startDate);
        values.put(RENTALS_COLUMN_END_DATE, endDate);
        values.put(RENTALS_COLUMN_DURATION, duration);
        values.put(RENTALS_COLUMN_STATUS, status);

        Log.d("DBHelper", "Adding rental for user email: " + userEmail);

        long result = db.insert(TABLE_RENTALS, null, values);
        return result;
    }

    public List<Rental> getAllRentals(String userEmail) {
        List<Rental> rentalList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT r.rental_id, r.user_email, r.bike_id, r.start_date, r.end_date, r.duration, r.status, " +
                "b.name, b.category, b.location, b.bike_image, b.status " +
                "FROM rental r " +
                "JOIN bike b ON r.bike_id = b.bike_id " +
                "WHERE user_email=? ", new String[]{userEmail});

        if (cursor.moveToFirst()) {
            do {
                // rental details
                int rentalId = cursor.getInt(0);
                userEmail = cursor.getString(1);
                int bikeId = cursor.getInt(2);
                String startDate = cursor.getString(3);
                String endDate = cursor.getString(4);
                String duration = cursor.getString(5);
                String status = cursor.getString(6);

                // bike details
                String bikeName = cursor.getString(7);
                String location = cursor.getString(8);
                String bikeCategory = cursor.getString(9);
                int bikeImage = cursor.getInt(10);
                String bikeStatus = cursor.getString(11);

                Bike bike = new Bike(bikeName,location,bikeCategory, bikeImage, bikeStatus);
                rentalList.add(new Rental(rentalId, userEmail, bikeId, startDate, endDate, duration, status, bike));
            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return rentalList;
    }

    public boolean updateBikeStatus(int bikeId, String bikeStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BIKES_COLUMN_STATUS, bikeStatus);
        int rowsAffected = db.update("bike", values, "bike_id=?", new String[]{String.valueOf(bikeId)});
        return rowsAffected > 0;
    }

    public boolean updateRentalStatus(int rentalId, String rentalStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RENTALS_COLUMN_STATUS, rentalStatus);
        int rowsAffected = db.update("rental", values, "rental_id=?", new String[]{String.valueOf(rentalId)});
        return rowsAffected > 0;
    }


}