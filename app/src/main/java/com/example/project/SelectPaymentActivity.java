package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectPaymentActivity extends AppCompatActivity {
    private ImageButton ib_kakao, ib_naver, ib_payco, ib_credit;
    private String payMethod;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);

        ib_kakao = findViewById(R.id.ib_kakao);
        ib_naver = findViewById(R.id.ib_naver);
        ib_payco = findViewById(R.id.ib_payco);
        ib_credit = findViewById(R.id.ib_credit);

        SharedPreferences pay = getSharedPreferences("payment", Activity.MODE_PRIVATE);
        SharedPreferences.Editor payEdit = pay.edit();

        SharedPreferences menu = getSharedPreferences("Menu", Activity.MODE_PRIVATE);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("project");
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        View.OnClickListener onClickListener = new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.ib_kakao:
                        payMethod = "카카오페이";
                    case R.id.ib_naver:
                        payMethod = "네이버페이";
                    case R.id.ib_payco:
                        payMethod = "페이코";
                    case R.id.ib_credit:
                        payMethod = "신용카드";
                }
                payEdit.putString("payMethod", payMethod);
                int payPrice = menu.getInt("menuPrice", 0);
                String payMenu = menu.getString("menuName", null);
                int payNumber = menu.getInt("orderNumber", 0);

                payEdit.putInt("payPrice", payPrice);
                payEdit.putInt("payTotalPrice", payPrice*payNumber);
                payEdit.commit();

                Toast.makeText(getApplicationContext(), payMenu +"수량 : " +payNumber+"\n"+ payMethod + payPrice*payNumber +"원 \n 결제완료", Toast.LENGTH_SHORT).show();

                UserToken userToken = new UserToken();
                userToken.setMenuPrice(payPrice);
                userToken.setMenuNumber(payNumber);
                userToken.setMenuTotalPrice(payPrice*payNumber);
                userToken.setPayMethod(payMethod);


                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("Tokens").child(payMenu).setValue(userToken);


                Intent intent = new Intent(SelectPaymentActivity.this, MyPage.class);
                startActivity(intent);
                finish();
            }
        };

        ib_kakao.setOnClickListener(onClickListener);
        ib_naver.setOnClickListener(onClickListener);
        ib_payco.setOnClickListener(onClickListener);
        ib_credit.setOnClickListener(onClickListener);
    }
}