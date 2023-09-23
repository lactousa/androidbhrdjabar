package com.example.bhrdjawabarat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class RealTimeDataActivity extends AppCompatActivity implements SensorEventListener {

    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private TextView azimuthKaabaTextView; // Menggunakan azimuthKaabaTextView saja
    private TextView algoritmaTextView;
    private TextView cityNameTextView;

    private TextView backAzimuthTextView;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private TextView rashdulKiblatTextView;
    private float[] gravity = new float[3];

    // Tambahkan variabel global untuk menyimpan nilai latitude dan longitude perangkat
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;

    // Tambahkan ImageView untuk menampilkan gambar yang akan dirotasi
    private ImageView rotatingImageView;

    public RealTimeDataActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtimedatahitungkiblat);

        ImageView imageViewBack = findViewById(R.id.imageView15);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        azimuthKaabaTextView = findViewById(R.id.azimuthKaabaTextView);
        algoritmaTextView = findViewById(R.id.algoritmaTextView);
        cityNameTextView = findViewById(R.id.cityNameTextView);
        backAzimuthTextView = findViewById(R.id.backAzimuthTextView);
        rotatingImageView = findViewById(R.id.rotatingImageView); // Inisialisasi ImageView
        rashdulKiblatTextView = findViewById(R.id.rashdulKiblatTextView);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initializeLocationManager();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void updateData(double latitude, double longitude, float azimuth, String algoritma) {
        latitudeTextView.setText("Latitude: " + latitude);
        longitudeTextView.setText("Longitude: " + longitude);
        azimuthKaabaTextView.setText("Azimuth to Kaaba: " + azimuth);
        algoritmaTextView.setText("Algoritma: " + algoritma);
    }

    @SuppressLint("MissingPermission")
    private void initializeLocationManager() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Simpan nilai latitude dan longitude perangkat
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();

                updateData(currentLatitude, currentLongitude, 0.0f, "Algoritma penghitungan kiblat yang digunakan");
                convertCoordinatesToCityName(currentLatitude, currentLongitude);
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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeLocationManager();
            } else {
                Toast.makeText(this, "Izin lokasi diperlukan untuk menghitung arah kiblat.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == accelerometer) {
            gravity = event.values;
        } else if (event.sensor == magnetometer) {
            float[] geomagnetic = event.values;

            float[] rotationMatrix = new float[9];
            boolean success = SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic);

            if (success) {
                float[] orientationValues = new float[3];
                SensorManager.getOrientation(rotationMatrix, orientationValues);
                float azimuth = (float) Math.toDegrees(orientationValues[0]);

                float backAzimuth = (azimuth + 180) % 360;

                // Hitung azimuth ke Kaaba dengan rumus trigonometri yang benar
                double kaabaLatitude = 21.3891; // Latitude Kaaba
                double kaabaLongitude = 39.8579; // Longitude Kaaba
                double latitudeRad = Math.toRadians(currentLatitude); // Latitude perangkat Anda
                double kaabaLatitudeRad = Math.toRadians(kaabaLatitude);
                double deltaLongitudeRad = Math.toRadians(kaabaLongitude - currentLongitude);

                // Hitung azimuth ke Kaaba menggunakan rumus trigonometri
                double azimuthToKaaba = Math.atan2(
                        Math.sin(deltaLongitudeRad),
                        (Math.cos(latitudeRad) * Math.tan(kaabaLatitudeRad)) -
                                (Math.sin(latitudeRad) * Math.cos(kaabaLatitudeRad) * Math.cos(deltaLongitudeRad))
                );

                // Konversi hasil dari radian ke derajat
                azimuthToKaaba = Math.toDegrees(azimuthToKaaba);

                // Pastikan hasil selalu positif dalam rentang 0 hingga 360 derajat
                if (azimuthToKaaba < 0) {
                    azimuthToKaaba += 360;
                }

                // Mendapatkan nilai deklinasi magnetik (perbedaan antara utara magnetik dan utara geografis)
                double declination = getMagneticDeclination();

                // Menyesuaikan azimuth dengan deklinasi magnetik
                azimuthToKaaba += declination;

                backAzimuthTextView.setText("Back Azimuth: " + backAzimuth);
                azimuthKaabaTextView.setText("Azimuth to Kaaba: " + azimuthToKaaba);

                // Rotasi ImageView berdasarkan azimuth
                rotateImageView(rotatingImageView, azimuthToKaaba);

                // Menghitung dan menampilkan Rashdul Kiblat
                String rashdulKiblat = calculateRashdulKiblat((float) azimuthToKaaba);
                rashdulKiblatTextView.setText("Rashdul Kiblat: " + rashdulKiblat);
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Tidak ada tindakan khusus yang diperlukan saat akurasi sensor berubah.
        // Anda dapat mengosongkan metode ini jika tidak memerlukan tindakan tambahan.
    }

    // Metode untuk menghitung Rashdul Kiblat
    private String calculateRashdulKiblat(float azimuth) {
        double sinLT = Math.sin(Math.toRadians(-0.119270449));
        double cotanAQ = 2.126977059;
        double cotanA = -75.76518684;

        double tanDekl = 0.433487128;
        double cotanLT = -8.324457663;
        double cosA = 0.24589638;
        double cosB = 152.5394077;

        double WH = ((cosA + cosB) / 15) + 12;
        double WD = WH - (cotanAQ + (cotanA - azimuth) / 15);

        int hours = (int) WH;
        int minutes = (int) ((WH - hours) * 60);
        int seconds = (int) (((WH - hours) * 60 - minutes) * 60);

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

    private double getMagneticDeclination() {
        try {
            // Buka file WMM.COF dari folder assets
            InputStream inputStream = getAssets().open("WMM.COF");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Membaca baris-baris file hingga menemukan nilai deklinasi magnetik yang sesuai
            String line;
            while ((line = reader.readLine()) != null) {
                // Memeriksa jika baris ini adalah baris yang berisi data deklinasi magnetik (dimulai dengan "1")
                if (line.startsWith("1 ")) {
                    // Memisahkan baris menjadi bagian-bagian dengan spasi sebagai pemisah
                    String[] parts = line.split("\\s+");

                    // Mengambil nilai deklinasi magnetik dari kolom kedua (indeks 1)
                    String declinationStr = parts[1].trim();
                    double declination = Double.parseDouble(declinationStr); // Konversi ke double

                    // Tutup file dan kembalikan nilai deklinasi magnetik
                    reader.close();
                    return declination;
                }
            }

            // Jika tidak ada nilai deklinasi magnetik yang sesuai ditemukan, Anda dapat mengembalikan nilai default
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Jika terjadi kesalahan atau data tidak tersedia, kembalikan nilai default
        return 0.0;
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

    // Metode untuk merotasi ImageView
    private void rotateImageView(ImageView imageView, double angle) {
        // Membuat objek Matrix untuk rotasi gambar
        Matrix matrix = new Matrix();
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        matrix.postRotate((float) angle, imageView.getDrawable().getBounds().width() / 2, imageView.getDrawable().getBounds().height() / 2);
        imageView.setImageMatrix(matrix);
    }
}
