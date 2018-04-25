package com.nj.sanji;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView mIvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIvShow = findViewById(R.id.iv_show);
        findViewById(R.id.btn_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyBitmapLoadUtils bitmapLoadUtils = new MyBitmapLoadUtils();
                String url = "http://192.168.16.122:8080/yang.jpg";
                bitmapLoadUtils.display(MainActivity.this, mIvShow, url);
            }
        });
    }
}
