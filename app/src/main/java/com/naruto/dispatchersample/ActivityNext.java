package com.naruto.dispatchersample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.naruto.dispatchersample.databinding.ActivityMainBinding;


public class ActivityNext extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.txtName.setText("Activity:"+ActivityNext.class.getSimpleName());
    }

    @Override
    protected void onDestroy() {
        Log.e("Next","onDestroy");
        super.onDestroy();
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        binding.btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        binding.btnSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ActivityNext.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
