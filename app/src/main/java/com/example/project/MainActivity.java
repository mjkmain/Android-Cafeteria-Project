package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button btn_login, btn_withoutLogin, btn_register;
    CheckBox cb_auto, cb_saveID;
    EditText et_ID, et_pw;
    TextView login_menu;

    private FirebaseAuth mFirebaseAuth;
    public static String[] menuArr = new String[7];
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_withoutLogin = (Button) findViewById(R.id.btn_withoutLogin);
        btn_register = (Button) findViewById(R.id.btn_register);
        cb_auto = (CheckBox) findViewById(R.id.cb_auto);
        cb_saveID = (CheckBox) findViewById(R.id.cb_saveID);
        et_ID = findViewById(R.id.et_ID);
        et_pw = findViewById(R.id.et_pw);
        login_menu = findViewById(R.id.login_menu);

        mFirebaseAuth = FirebaseAuth.getInstance();


        /*
        * 자동로그인에 관련된 데이터 저장 : autoLogin
        * */
        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLoginEdit = auto.edit();

        /*
        * 저장된 값 반영 (ID 저장, 자동 로그인)
        * */

        Boolean isAutoChecked = auto.getBoolean("autoChecked", false);
        if(isAutoChecked){
            cb_auto.setChecked(true);
        }else{
            cb_auto.setChecked(false);
        }

        Boolean isSaveChecked = auto.getBoolean("SaveChecked", false);
        if(isSaveChecked){
            cb_saveID.setChecked(true);
            et_ID.setText(auto.getString("saveID", null));
        }else{
            cb_saveID.setChecked(false);
        }

        /*
        * 금일 메뉴 보이게
        * */
        String str = "";
        InputStream is = this.getResources().openRawResource(R.raw.data_t2);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try{
            String line;

            while((line = reader.readLine()) != null){
                str = line + "";
                str = str.replace(",", "\n");
                menuArr[i] = str;
                i++;
            }
        }catch (IOException e){
            Toast.makeText(getApplicationContext(), "Error1", Toast.LENGTH_SHORT).show();
        }
        finally {
            try{
                Calendar calendar = Calendar.getInstance();
                int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
                //주말이라 메뉴 확인을 위해서 임의로 지정
                dayOfWeekNumber = 4;
                if(dayOfWeekNumber!=1 && dayOfWeekNumber!=7) {
                    login_menu.setText(menuArr[dayOfWeekNumber - 2]);
                }
                else{
                    login_menu.setText("학식당 운영 안함");
                    login_menu.setTextSize(30);
                    login_menu.setGravity(Gravity.CENTER);
                }
                is.close();
            }catch(IOException e){
                Toast.makeText(getApplicationContext(), "Error2", Toast.LENGTH_SHORT).show();
            }
        }

        /*
        * 자동로그인
        * */
        if(cb_auto.isChecked()) {
            String userId = auto.getString("userID", null);
            String passwordNo = auto.getString("passwordNo", null);

            if (userId != null && passwordNo != null) {
                mFirebaseAuth.signInWithEmailAndPassword(userId, passwordNo).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), MainDisplay.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }


        /*
        * 로그인 버튼 클릭 --> 로그인 로직--> 성공 시 main 화면으로 전환
        * */
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = et_ID.getText().toString();
                String strPass = et_pw.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        /*
                         * 자동로그인 데이터, 로그인 로직
                         * */
                        if(task.isSuccessful()){
                            if(cb_saveID.isChecked()){
                                autoLoginEdit.putString("saveID", strEmail);
                            }
                            if(cb_auto.isChecked()){
                                autoLoginEdit.putString("userID", strEmail);
                                autoLoginEdit.putString("passwordNo", strPass);
                                autoLoginEdit.putBoolean("autoChecked", true);
                            }
                            Intent intent = new Intent(getApplicationContext(), MainDisplay.class);
                            startActivity(intent);
                            finish();
                        }else{
                            autoLoginEdit.putBoolean("autoChecked", false);
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                        autoLoginEdit.commit();
                    }
                });


                /*
                * ID 저장 관련
                */
                if(cb_saveID.isChecked()){
                    autoLoginEdit.putBoolean("SaveChecked", true);
                }else{
                    autoLoginEdit.putBoolean("SaveChecked", false) ;
                }
                autoLoginEdit.commit();

            }
        });


        /*
        * 회원가입 버튼 클릭 --> 회원가입 창
        * */
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });



    }
}