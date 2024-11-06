package com.darktornado.speedmeter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private final String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private LocationListener listener;
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);

        txt = new TextView(this);
        txt.setText("속도 측정 전");
        txt.setTextSize(32);
        txt.setGravity(Gravity.CENTER);
        layout.addView(txt);

        TextView maker = new TextView(this);
        maker.setText("\n© 2024 Dark Tornado, All rights reserved.\n");
        maker.setTextSize(13);
        maker.setGravity(Gravity.CENTER);
        layout.addView(maker);

        int pad = dip2px(16);
        layout.setPadding(pad, pad, pad, pad);
        ScrollView scroll = new ScrollView(this);
        scroll.addView(layout);
        setContentView(scroll);

        if (Build.VERSION.SDK_INT > 23) {
            if (checkPermission(permissions)) {
                startLocationListener();
            } else {
                requestPermissions(permissions, 5);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 5) return;
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationListener();
        } else {
            Toast.makeText(this, "위치 권한이 없으면 속도 측정 불가능", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public boolean checkPermission(String[] permissions) {
        if (Build.VERSION.SDK_INT < 23) return true;
        for (String permission : permissions) {
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) return false;
        }
        return true;
    }

    private void startLocationListener() {
        if (!checkPermission(permissions)) return;
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = location -> {
            float speed = location.getSpeed();
            txt.setText(round(speed) + " m/s\n" + round(speed * 3.6) + " km/h\n" +
                    round(speed * 1.944) + " knot\n" + round(speed * 2.237) + " mph");
        };
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
    }

    private void stopLocationListener() {
        if (listener == null) return;
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.removeUpdates(listener);
    }

    private String round(double num) {
        num = (double)Math.round(num * 100) / 100;
        return String.valueOf(num);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationListener();
    }

    private int dip2px(int dips) {
        return (int) Math.ceil(dips * getResources().getDisplayMetrics().density);
    }
}