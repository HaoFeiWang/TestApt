package com.whf.testapt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.whf.apt.annotation.BindView;

public class MainActivity extends AppCompatActivity {

    @BindView
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HelloWorld helloWorld = new HelloWorld();
    }
}