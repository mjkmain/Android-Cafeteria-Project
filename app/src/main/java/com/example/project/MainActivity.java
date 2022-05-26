package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button btn_login, btn_withoutLogin, btn_register;
    CheckBox cb_auto, cb_faceID;
    EditText et_ID, et_pw;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_withoutLogin = (Button) findViewById(R.id.btn_withoutLogin);
        btn_register = (Button) findViewById(R.id.btn_register);
        cb_auto = (CheckBox) findViewById(R.id.cb_auto);
        cb_faceID = (CheckBox) findViewById(R.id.cb_faceID);
        et_ID = findViewById(R.id.et_ID);
        et_pw = findViewById(R.id.et_pw);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("project");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = et_ID.getText().toString();
                String strPass = et_pw.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), MainDisplay.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}