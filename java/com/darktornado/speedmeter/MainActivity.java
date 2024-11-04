package com.darktornado.speedmeter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView txt;

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
    }

    private int dip2px(int dips) {
        return (int) Math.ceil(dips * getResources().getDisplayMetrics().density);
    }
}