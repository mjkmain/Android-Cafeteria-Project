package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {
    TextView tv_detail;
    Button btn_pay;
    ImageButton prev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tv_detail = findViewById(R.id.tv_detail);
        btn_pay = findViewById(R.id.btn_pay);
        prev = findViewById(R.id.prev);
        SharedPreferences menu = getSharedPreferences("Menu", Activity.MODE_PRIVATE);
        SharedPreferences.Editor menuEdit = menu.edit();

        String menuName = menu.getString("menuName", null);
        int menuPrice = menu.getInt("menuPrice", 0);

        tv_detail.setText("주문 내역 : " + menuName + "\n가격 : " + menuPrice);


        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}