package com.example.btl_java;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityChangeImage extends AppCompatActivity {
    ImageView imgCover;
    ImageView avtUser, imgChangeAvt;
    TextView userName;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutfragmentinformation);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }
}
