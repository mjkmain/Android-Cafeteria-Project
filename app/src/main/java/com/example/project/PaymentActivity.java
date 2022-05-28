package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {
    TextView tv_detail, tv_count;
    Button btn_pay, btn_plus, btn_minus;
    ImageButton prev;

    ImageView iv;
    int number = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tv_detail = findViewById(R.id.tv_detail);
        btn_pay = findViewById(R.id.btn_pay);
        prev = findViewById(R.id.prev);
        iv = findViewById(R.id.imageView);
        btn_plus = findViewById(R.id.btn_plus);
        btn_minus = findViewById(R.id.btn_minus);
        tv_count = findViewById(R.id.tv_count);

        tv_count.setText(1 + "");

        SharedPreferences menu = getSharedPreferences("Menu", Activity.MODE_PRIVATE);
        SharedPreferences.Editor menuEdit = menu.edit();


        int menuImage = menu.getInt("menuImage", 0);
        String menuName = menu.getString("menuName", null);
        int menuPrice = menu.getInt("menuPrice", 0);

        tv_detail.setText("주문 내역 : " + menuName + "\n수랑 : "+number+"\n가격 : " + menuPrice);
        iv.setImageResource(menuImage);


        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuEdit.putInt("orderNumber", number);
                menuEdit.commit();
                Intent intent = new Intent(PaymentActivity.this, SelectPaymentActivity.class);
                startActivity(intent);
                finish();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number --;
                tv_count.setText(number + "");
                int totalPrice = menuPrice*number;
                tv_detail.setText("주문 내역 : " + menuName + "\n수랑 : "+number+"\n가격 : " + totalPrice);

            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number ++;
                tv_count.setText(number + "");
                int totalPrice = menuPrice*number;
                tv_detail.setText("주문 내역 : " + menuName + "\n수랑 : "+number+"\n가격 : " + totalPrice);

            }
        });
    }
}