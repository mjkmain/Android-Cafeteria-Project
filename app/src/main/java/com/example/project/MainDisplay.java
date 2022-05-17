package com.example.project;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainDisplay extends AppCompatActivity {

    ImageButton prev, myPage;
    TextView menuDisplay;
    Button diffMenu, payment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_display);

        prev = (ImageButton) findViewById(R.id.prev);
        myPage = (ImageButton) findViewById(R.id.myPage);
        menuDisplay = (TextView) findViewById(R.id.menuDisplay);
        diffMenu = (Button) findViewById(R.id.checkDiffMenu);
        payment = (Button) findViewById(R.id.payment);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
