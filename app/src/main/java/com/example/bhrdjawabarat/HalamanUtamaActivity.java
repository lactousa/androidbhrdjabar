package com.example.bhrdjawabarat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HalamanUtamaActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private TextView cityNameTextView;
    private TextView textViewJadwalSholatSelanjutnya;

    private RecyclerView recyclerView;
    private BeritaAdapter beritaAdapter;
    private ArrayList<BeritaModel> beritaModel;

    private TextView textViewCurrentPrayerTime;

    private TextView textViewNextPrayerTime;
    private TextView fajrTimeTextView;
    private TextView shurooqTimeTextView;
    private TextView dhuhrTimeTextView;
    private TextView asrTimeTextView;
    private TextView maghribTimeTextView;
    private TextView ishaTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_utama);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        cityNameTextView = findViewById(R.id.cityNameTextView);
        textViewJadwalSholatSelanjutnya = findViewById(R.id.textViewJadwalSholatSelanjutnya);

        TextView textViewDate = findViewById(R.id.textViewDate);
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Inisialisasi ArrayList
        beritaModel = new ArrayList<>();

        // Inisialisasi dan set adapter untuk RecyclerView
        beritaAdapter = new BeritaAdapter(beritaModel);
        recyclerView.setAdapter(beritaAdapter);

        fajrTimeTextView = findViewById(R.id.fajrTimeTextView);
        shurooqTimeTextView = findViewById(R.id.shurooqTimeTextView);
        dhuhrTimeTextView = findViewById(R.id.dhuhrTimeTextView);
        asrTimeTextView = findViewById(R.id.asrTimeTextView);
        maghribTimeTextView = findViewById(R.id.maghribTimeTextView);
        ishaTimeTextView = findViewById(R.id.ishaTimeTextView);

        cityNameTextView = findViewById(R.id.cityNameTextView);
        getData();
        fetchPrayerTimes();

        // Inisialisasi locationManager dan locationListener
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // Simpan nilai latitude dan longitude ke SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("user_location", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("latitude", String.valueOf(latitude));
                editor.putString("longitude", String.valueOf(longitude));
                editor.apply();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        // Meminta izin lokasi jika belum diberikan
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            enableLocation();
        }

        // Menampilkan tanggal saat ini
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);
        textViewDate.setText(formattedDate);

        ImageView imageViewKiblat = findViewById(R.id.imageView9);
        imageViewKiblat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HalamanUtamaActivity.this, KiblatActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        // Load data from berita.json
        String json = loadJSONFromAsset();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("beritaModel"); // Ambil array beritaModel

            beritaModel = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject beritaJson = jsonArray.getJSONObject(i);
                String judulBerita = beritaJson.getString("title");
                String isiBerita = beritaJson.getString("content");
                String gambarBerita = beritaJson.getString("imageResource");

                // Assume that gambarBerita is the file name of the image in drawable folder
                int imageResourceId = getResources().getIdentifier(gambarBerita, "drawable", getPackageName());

                BeritaModel berita = new BeritaModel(judulBerita, imageResourceId, isiBerita);
                beritaModel.add(berita);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Set adapter for RecyclerView
        beritaAdapter = new BeritaAdapter(beritaModel);
        recyclerView.setAdapter(beritaAdapter);
    }

    // Load JSON from assets folder
    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getAssets().open("berita.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void enableLocation() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    private void fetchPrayerTimes() {
        // Mengambil koordinat dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_location", MODE_PRIVATE);
        String latitudeString = sharedPreferences.getString("latitude", "");
        String longitudeString = sharedPreferences.getString("longitude", "");

        if (!latitudeString.isEmpty() && !longitudeString.isEmpty()) {
            double latitude = Double.parseDouble(latitudeString);
            double longitude = Double.parseDouble(longitudeString);

            // Mengambil nama kota berdasarkan koordinat
            convertCoordinatesToCityName(latitude, longitude);

            // Memanggil API untuk mendapatkan jadwal sholat
            PrayerTimesAPIManager.fetchPrayerTimes(cityNameTextView.getText().toString(), new PrayerTimesAPIManager.PrayerTimesListener() {
                @Override
                public void onPrayerTimesFetched(PrayerTimes prayerTimes) {
                    // Set the prayer times to the respective TextViews
                    setPrayerTimes(prayerTimes);

                    // Update TextView dengan jadwal sholat selanjutnya
                    updateJadwalSholatSelanjutnya(prayerTimes);
                }
            });
        } else {
            cityNameTextView.setText("Tidak dapat mendapatkan koordinat.");
        }
    }

    private void setPrayerTimes(PrayerTimes prayerTimes) {
        fajrTimeTextView.setText("Fajr: " + prayerTimes.getFajr());
        shurooqTimeTextView.setText("Shurooq: " + prayerTimes.getShurooq());
        dhuhrTimeTextView.setText("Dhuhr: " + prayerTimes.getDhuhr());
        asrTimeTextView.setText("Asr: " + prayerTimes.getAsr());
        maghribTimeTextView.setText("Maghrib: " + prayerTimes.getMaghrib());
        ishaTimeTextView.setText("Isha: " + prayerTimes.getIsha());
    }



    private void convertCoordinatesToCityName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String cityName = address.getLocality();
                cityNameTextView.setText("Kota: " + cityName);
            } else {
                cityNameTextView.setText("Tidak dapat menemukan nama kota.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            cityNameTextView.setText("Gagal mengonversi koordinat ke nama kota. Pastikan perangkat terhubung ke internet dan koordinat valid.");
        }
    }

    // Implementasikan metode untuk mendapatkan jadwal sholat selanjutnya
    private String getJadwalSholatSelanjutnya(PrayerTimes prayerTimes) {
        // Implementasikan logika untuk mendapatkan waktu sholat selanjutnya
        // Anda dapat menggunakan logika sesuai kebutuhan, seperti membandingkan waktu sholat dengan waktu saat ini
        // dan mengambil waktu sholat yang belum terjadi.
        // Di sini, saya akan menunjukkan contoh sederhana:
        String[] sholatTimes = {
                prayerTimes.getFajr(),
                prayerTimes.getDhuhr(),
                prayerTimes.getAsr(),
                prayerTimes.getMaghrib(),
                prayerTimes.getIsha()
        };

        // Dapatkan waktu saat ini
        String waktuSaatIni = getCurrentTime(); // Implementasi getCurrentTime sesuai kebutuhan Anda

        // Temukan sholat yang belum terjadi
        for (String time : sholatTimes) {
            if (time.compareTo(waktuSaatIni) > 0) {
                return time;
            }
        }

        // Jika semua sholat sudah lewat, kembalikan pesan
        return "Semua sholat telah lewat.";
    }

    // Implementasikan metode untuk mendapatkan waktu saat ini
    private String getCurrentTime() {
        // Implementasikan logika untuk mendapatkan waktu saat ini
        // Misalnya, Anda dapat menggunakan SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }

    // Implementasikan metode untuk update jadwal sholat selanjutnya
    private void updateJadwalSholatSelanjutnya(PrayerTimes prayerTimes) {
        // Dapatkan waktu sholat selanjutnya berdasarkan waktu saat ini
        String jadwalSelanjutnya = getJadwalSholatSelanjutnya(prayerTimes);

        // Update TextView dengan jadwal sholat selanjutnya
        textViewJadwalSholatSelanjutnya.setText("Jadwal Sholat Selanjutnya: " + jadwalSelanjutnya);
    }

    public void goToTentangBHRD(View view) {
        Intent intent = new Intent(this, TentangBhrdActivity.class);
        startActivity(intent);
    }

    public void goToCalenderHijriyah(View view) {
        Intent intent = new Intent(this, CalenderHijriyahActivity.class);
        startActivity(intent);
    }

    public void goToKompasKiblat(View view) {
        Intent intent = new Intent(this, KompasKiblatActivity.class);
        startActivity(intent);
    }

    public void goToJadwalSholat(View v) {
        try {
            // Pindah ke JadwalSholatActivity
            Intent intent = new Intent(HalamanUtamaActivity.this, JadwalSholatActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // Tambahkan penanganan kesalahan di sini, seperti menampilkan pesan kesalahan kepada pengguna.
        }
    }

    public void goToInfoHilalActivity(View view) {
        Intent intent = new Intent(this, InfoHilalActivity.class);
        startActivity(intent);
    }

    public void goToRealTimeDataActivity(View view) {
        Intent intent = new Intent(this, RealTimeDataActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation();
            } else {
                Toast.makeText(this, "Izin lokasi ditolak.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
