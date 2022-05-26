package com.example.project;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainDisplay extends AppCompatActivity {

    ImageButton myPage;
    TextView menuDisplay;
    Button diffMenu, payment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_display);

//        prev = (ImageButton) findViewById(R.id.prev);
        myPage = (ImageButton) findViewById(R.id.myPage);
        menuDisplay = (TextView) findViewById(R.id.menuDisplay);
        diffMenu = (Button) findViewById(R.id.checkDiffMenu);
        payment = (Button) findViewById(R.id.payment);



//        prev.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainDisplay.this, MyPage.class);
                startActivity(intent);
            }
        });

        diffMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainDisplay.this, DiffMenuSelec.class);
                startActivity(intent);
            }
        });

    }



}
