package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {
    TextView tv_detail, tv_count;
    Button btn_pay, btn_plus, btn_minus;
    ImageButton prev, myPage;

    ImageView iv;
    int number = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_payment);

        /********************************************/
        /*
         * custom tool bar
         * */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getSupportActionBar().getTitle());
        getSupportActionBar().setTitle(null);
        /*******************************************/


        tv_detail = findViewById(R.id.tv_detail);
        btn_pay = findViewById(R.id.btn_pay);

        myPage = findViewById(R.id.pay_mypage);
        prev = findViewById(R.id.pay_prev);
        iv = findViewById(R.id.imageView);
        btn_plus = findViewById(R.id.btn_plus);
        btn_minus = findViewById(R.id.btn_minus);
        tv_count = findViewById(R.id.tv_count);



        tv_count.setText(1 + "");
        myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, MyPage.class);
                startActivity(intent);
            }
        });
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
                if(number< 1){
                    btn_minus.setClickable(false);
                }
                tv_count.setText(number + "");
                int totalPrice = menuPrice*number;
                tv_detail.setText("주문 내역 : " + menuName + "\n수랑 : "+number+"\n가격 : " + totalPrice);
            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                number ++;
                if(number >= 1){
                    btn_minus.setClickable(true);
                }
                tv_count.setText(number + "");
                int totalPrice = menuPrice*number;
                tv_detail.setText("주문 내역 : " + menuName + "\n수랑 : "+number+"\n가격 : " + totalPrice);

            }
        });
    }
}