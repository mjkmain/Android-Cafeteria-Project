package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainDisplay extends AppCompatActivity {

    ImageButton myPage;
    TextView menuDisplay, menuDate;
    Button diffMenu, payment;
    String menuStr = "";
    public static String[] menuArr = new String[7];
    int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_display);

        myPage = (ImageButton) findViewById(R.id.myPage);
        menuDate = (TextView) findViewById(R.id.menuDate);
        menuDisplay = (TextView) findViewById(R.id.menuDisplay);
        diffMenu = (Button) findViewById(R.id.checkDiffMenu);
        payment = (Button) findViewById(R.id.payment);


        SimpleDateFormat mFormat = new SimpleDateFormat("MM-dd");
        Date mDate = new Date(System.currentTimeMillis());
        menuStr += mFormat.format(mDate);
        menuStr += " (";
        menuStr += getCurrentWeek();
        menuStr += ")";
        menuDate.setText(menuStr);

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
                    menuDisplay.setText(menuArr[dayOfWeekNumber - 2]);
                }
                else{
                    menuDisplay.setText("학식당 운영 안함");
                    menuDisplay.setTextSize(30);
                    menuDisplay.setGravity(Gravity.CENTER);
                }
                is.close();
            }catch(IOException e){
                Toast.makeText(getApplicationContext(), "Error2", Toast.LENGTH_SHORT).show();
            }
        }

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

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences menu = getSharedPreferences("Menu", Activity.MODE_PRIVATE);
                SharedPreferences.Editor menuEdit = menu.edit();

                menuEdit.putString("menuName", "백반");
                menuEdit.putInt("menuPrice", 4800);
                menuEdit.commit();

                Intent intent = new Intent(MainDisplay.this, PaymentActivity.class);
                startActivity(intent);
            }
        });



    }
    public static String getCurrentWeek() {
        Date currentDate = new Date();
        String[] dateArr = {"blank", "일", "월", "화", "수", "목", "금", "토"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);

        return dateArr[dayOfWeekNumber];
    }

}




