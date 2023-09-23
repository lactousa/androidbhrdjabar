package com.example.bhrdjawabarat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class KiblatActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private ImageView imageViewKompas;

    // Koordinat Kaaba di Mekah
    private static final double LATITUDE_KAABA = 21.422487;
    private static final double LONGITUDE_KAABA = 39.826206;

    private double latitudeLokasi;
    private double longitudeLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arahkiblat);

        imageViewKompas = findViewById(R.id.imageViewKompas);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        ImageView imageViewBack = findViewById(R.id.imageView10);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Menambahkan TextView untuk menampilkan nilai Latitude dan Longitude
        TextView textViewLatitudeValue = findViewById(R.id.textViewLatitudeValue);
        TextView textViewLongitudeValue = findViewById(R.id.textViewLongitudeValue);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Lakukan cek izin lokasi di sini, dan minta izin jika belum diberikan.
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, minta izin
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return;
        }

        // Mendapatkan lokasi saat aplikasi dijalankan
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Hentikan pembaruan lokasi saat aplikasi dihentikan
        locationManager.removeUpdates(locationListener);
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // Dapatkan latitude dan longitude dari location
            latitudeLokasi = location.getLatitude();
            longitudeLokasi = location.getLongitude();

            // Gunakan nilai latitudeLokasi dan longitudeLokasi untuk perhitungan arah kiblat
            double azimuthKiblat = hitungArahKiblat(latitudeLokasi, longitudeLokasi);

            // Tampilkan nilai azimuthKiblat dalam TextView
            TextView textViewAzimuthKiblat = findViewById(R.id.textViewAzimuthKiblat);
            textViewAzimuthKiblat.setText("Azimuth Kiblat: " + azimuthKiblat + "°");

            // Tampilkan nilai latitude dan longitude dalam TextView
            TextView textViewLatitudeValue = findViewById(R.id.textViewLatitudeValue);
            textViewLatitudeValue.setText("Latitude: " + latitudeLokasi);

            TextView textViewLongitudeValue = findViewById(R.id.textViewLongitudeValue);
            textViewLongitudeValue.setText("Longitude: " + longitudeLokasi);

            // Rotasi imageViewKompas sesuai dengan azimuthKiblat
            float rotation = (float) azimuthKiblat;
            imageViewKompas.setRotation(-rotation);

            rotateCompassToNorth();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    // Metode untuk menghitung arah kiblat berdasarkan koordinat lokasi
    public static double hitungArahKiblat(double latitudeLokasi, double longitudeLokasi) {
        double deltaLongitude = LONGITUDE_KAABA - longitudeLokasi;

        double azimuth = Math.atan2(
                Math.sin(Math.toRadians(deltaLongitude)),
                (Math.cos(Math.toRadians(LATITUDE_KAABA)) * Math.tan(Math.toRadians(latitudeLokasi))) -
                        (Math.sin(Math.toRadians(LATITUDE_KAABA)) * Math.cos(Math.toRadians(deltaLongitude)))
        );

        azimuth = Math.toDegrees(azimuth);
        if (azimuth < 0) {
            azimuth += 360;
        }

        return azimuth;
    }

    private void rotateCompassToNorth() {
        // Mendapatkan nilai azimuth saat ini
        float currentAzimuth = (float) hitungArahKiblat(latitudeLokasi, longitudeLokasi);

        // Mengatur rotasi untuk imageViewKompas sehingga arah utara menunjuk ke atas
        float rotation = 360 - currentAzimuth;
        imageViewKompas.setRotation(rotation);
    }
}
