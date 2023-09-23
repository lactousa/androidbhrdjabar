package com.example.bhrdjawabarat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JadwalSholatActivity extends AppCompatActivity implements PrayerTimesAPIManager.PrayerTimesListener {

    private TextView cityNameTextView;
    private TextView cityResultTextView;
    private TextView lokasiTextView, fajrTimeTextView, shurooqTimeTextView, dhuhrTimeTextView, asrTimeTextView, maghribTimeTextView, ishaTimeTextView;
    private static final int PERMISSIONS_REQUEST_LOCATION = 100;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_sholat);

        ImageView imageViewBack = findViewById(R.id.imageView15);
        cityNameTextView = findViewById(R.id.cityNameTextView);
        cityResultTextView = findViewById(R.id.cityResultTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);

        lokasiTextView = findViewById(R.id.lokasiTextView);
        fajrTimeTextView = findViewById(R.id.fajrTimeTextView);
        shurooqTimeTextView = findViewById(R.id.shurooqTimeTextView);
        dhuhrTimeTextView = findViewById(R.id.dhuhrTimeTextView);
        asrTimeTextView = findViewById(R.id.asrTimeTextView);
        maghribTimeTextView = findViewById(R.id.maghribTimeTextView);
        ishaTimeTextView = findViewById(R.id.ishaTimeTextView);

        // Di dalam onCreate
        String currentDate = getCurrentDate();
        dateTextView.setText(currentDate);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Peroleh koordinat aktual perangkat
        getLocation();
    }

    private void setPrayerTimes(PrayerTimes prayerTimes) {
        // Tampilkan jadwal sholat di TextViews
        fajrTimeTextView.setText(": " + prayerTimes.getFajr());
        shurooqTimeTextView.setText(": " + prayerTimes.getShurooq());
        dhuhrTimeTextView.setText(": " + prayerTimes.getDhuhr());
        asrTimeTextView.setText(": " + prayerTimes.getAsr());
        maghribTimeTextView.setText(": " + prayerTimes.getMaghrib());
        ishaTimeTextView.setText(": " + prayerTimes.getIsha());
    }

    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Cek izin lokasi
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Jika izin tidak diberikan, minta izin
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            return;
        }

        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (locationGPS != null) {
            latitude = locationGPS.getLatitude();
            longitude = locationGPS.getLongitude();
            convertCoordinatesToCityName(latitude, longitude);
        } else if (locationNetwork != null) {
            latitude = locationNetwork.getLatitude();
            longitude = locationNetwork.getLongitude();
            convertCoordinatesToCityName(latitude, longitude);
        } else {
            cityNameTextView.setText("Tidak dapat mendapatkan koordinat.");
        }
    }

    private void convertCoordinatesToCityName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                Address address = addresses.get(0);

                // Cek apakah ada alamat
                if (address != null) {
                    String cityName = address.getLocality();

                    // Jika nama kota mengandung spasi, ambil kata kedua
                    if (cityName != null && cityName.contains(" ")) {
                        String[] cityNameParts = cityName.split(" ");
                        cityName = cityNameParts[1];
                    }

                    // Fetch prayer times using the obtained city
                    if (cityName != null) {
                        PrayerTimesAPIManager.fetchPrayerTimes(cityName, this);
                        cityResultTextView.setText("Kota: " + cityName);
                    } else {
                        cityNameTextView.setText("Tidak dapat menemukan nama kota.");
                    }
                } else {
                    cityNameTextView.setText("Tidak dapat menemukan alamat.");
                }
            } else {
                cityNameTextView.setText("Tidak dapat menemukan alamat.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            cityNameTextView.setText("Gagal mengonversi koordinat ke nama kota. Pastikan perangkat terhubung ke internet dan koordinat valid.");
        }
    }


    private String getCurrentDate() {
        // Create a SimpleDateFormat to format the date
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy", new Locale("id", "ID"));

        // Get the current date
        Date currentDate = new Date();

        // Format the date and return it as a string
        return sdf.format(currentDate);
    }

    @Override
    public void onPrayerTimesFetched(PrayerTimes prayerTimes) {
        Log.d("JadwalSholatActivity", "Prayer times fetched: " + prayerTimes.toString());

        // Set the prayer times to the respective TextViews
        setPrayerTimes(prayerTimes);

        // Optional: Display a toast to indicate successful retrieval of prayer times
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                Toast.makeText(JadwalSholatActivity.this, "Prayer times fetched", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, dapatkan lokasi
                getLocation();
            } else {
                // Izin ditolak, lakukan penanganan sesuai kebutuhan
                // Misalnya, tampilkan pesan bahwa izin diperlukan untuk mendapatkan waktu sholat
            }
        }
    }

    public void onImageView48Click(View view) {
        goToNotifikasiAdzan();
    }

    private void goToNotifikasiAdzan() {
        Intent intent = new Intent(JadwalSholatActivity.this, NotifikasiAdzanActivity.class);
        startActivity(intent);
    }
}
