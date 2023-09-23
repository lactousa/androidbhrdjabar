package com.example.bhrdjawabarat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class KompasKiblatActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private ImageView compassKiblat;
    private static SensorManager manager;

    private Sensor sensor;
    private float current_degree;
    private LocationManager locationManager;

    private double userLatitude = 0.0;
    private double userLongitude = 0.0;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_compaskiblat);

        compassKiblat = findViewById(R.id.compasskiblat);
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);

        // Deklarasikan TextView
        TextView orientationTextView = findViewById(R.id.orientationTextView);

        double qiblaDirection = 0;
        setInitialKiblatOrientation((float) qiblaDirection);

        ImageView imageViewBack = findViewById(R.id.imageView10);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    // Fungsi untuk mengatur orientasi awal kompas kiblat
    private void setInitialKiblatOrientation(float degrees) {
        RotateAnimation animation = new RotateAnimation(0, degrees, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(0); // Durasi animasi diatur ke 0 agar tidak terlihat perubahan tiba-tiba
        animation.setFillAfter(true);
        compassKiblat.startAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);

        // Hentikan pembaruan lokasi saat aplikasi di-pause
        locationManager.removeUpdates(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);
        RotateAnimation animation = new RotateAnimation(current_degree, -degree, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(120);
        animation.setFillAfter(true);


        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            compassKiblat.startAnimation(animation); // Gunakan kompasKiblat untuk sensor orientasi kompas kiblat
        }

        current_degree = -degree;

        // Hitung arah kiblat berdasarkan lokasi dan orientasi
        double qiblaDirection = calculateQiblaDirection(userLatitude, userLongitude, degree);

        // Sekarang, Anda bisa menggunakan nilai qiblaDirection untuk tujuan apa pun, seperti menampilkan informasi arah kiblat ke pengguna.
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Tidak perlu mengisi ini jika tidak diperlukan
    }

    @Override
    public void onLocationChanged(Location location) {
        // Mendapatkan pembaruan lokasi saat tersedia
        userLatitude = location.getLatitude();
        userLongitude = location.getLongitude();

        // Anda dapat mengupdate perhitungan arah kiblat di sini setiap kali lokasi berubah.
        double qiblaDirection = calculateQiblaDirection(userLatitude, userLongitude, current_degree);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Metode ini dipanggil ketika status provider lokasi berubah (misalnya, ketika GPS dinonaktifkan).
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Metode ini dipanggil ketika provider lokasi diaktifkan oleh pengguna.
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Metode ini dipanggil ketika provider lokasi dinonaktifkan oleh pengguna.
    }

    private double calculateQiblaDirection(double latitude, double longitude, float degree) {
        // Implementasikan perhitungan arah kiblat berdasarkan data lokasi dan orientasi di sini.
        // Saya akan memberikan contoh perhitungan sederhana berdasarkan koordinat Ka'bah (Mekah).

        double kaabaLatitude = 21.4225; // Latitude Ka'bah
        double kaabaLongitude = 39.8262; // Longitude Ka'bah

        double longitudeDifference = kaabaLongitude - longitude;
        double qiblaDirection = Math.atan2(
                Math.sin(Math.toRadians(longitudeDifference)),
                (Math.cos(Math.toRadians(latitude)) * Math.tan(Math.toRadians(kaabaLatitude))) -
                        (Math.sin(Math.toRadians(latitude)) * Math.cos(Math.toRadians(longitudeDifference)))
        );

        qiblaDirection = Math.toDegrees(qiblaDirection);
        qiblaDirection = (qiblaDirection + 360) % 360; // Pastikan hasilnya dalam rentang 0 hingga 360 derajat.

        return qiblaDirection;
    }
}
