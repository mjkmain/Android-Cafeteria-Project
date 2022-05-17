package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {

    Button btn_login, btn_withoutLogin;
    CheckBox cb_auto, cb_faceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_withoutLogin = (Button) findViewById(R.id.btn_withoutLogin);
        cb_auto = (CheckBox) findViewById(R.id.cb_auto);
        cb_faceID = (CheckBox) findViewById(R.id.cb_faceID);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainDisplay.class);
                startActivity(intent);
            }
        });

    }
}