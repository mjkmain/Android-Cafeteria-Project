package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MyPage extends AppCompatActivity {
    ImageButton prev;
    Button btn_logout;
    CardView cd;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        mFirebaseAuth = FirebaseAuth.getInstance();

        prev = (ImageButton) findViewById(R.id.mp_prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_logout = findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAuth.signOut();
                Intent intent = new Intent(MyPage.this, MainActivity.class);
                startActivity(intent);

                SharedPreferences sp = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor spEdit = sp.edit();
                spEdit.clear();
                spEdit.commit();

                finish();
            }
        });
    }
}
