package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SelectPaymentActivity extends AppCompatActivity {
    private ImageButton ib_kakao, ib_naver, ib_payco, ib_credit, prev, myPage;
    private String payMethod;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    private Integer tokenNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_select_payment);

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

        myPage=findViewById(R.id.sel_mypage);
        myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SelectPaymentActivity.this, MyPage.class);
                startActivity(intent);
            }
        });

        prev = findViewById(R.id.sel_prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tokenNumber = 0;

        ib_kakao = findViewById(R.id.ib_kakao);
        ib_naver = findViewById(R.id.ib_naver);
        ib_payco = findViewById(R.id.ib_payco);
        ib_credit = findViewById(R.id.ib_credit);

        SharedPreferences pay = getSharedPreferences("payment", Activity.MODE_PRIVATE);
        SharedPreferences.Editor payEdit = pay.edit();

        SharedPreferences menu = getSharedPreferences("Menu", Activity.MODE_PRIVATE);

        SharedPreferences tokenNum = getSharedPreferences("Menu", Activity.MODE_PRIVATE);


        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();


        View.OnClickListener onClickListener = new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.ib_kakao:
                        payMethod = "???????????????";
                        break;
                    case R.id.ib_naver:
                        payMethod = "???????????????";
                        break;
                    case R.id.ib_payco:
                        payMethod = "?????????";
                        break;
                    case R.id.ib_credit:
                        payMethod = "????????????";
                        break;
                }
                payEdit.putString("payMethod", payMethod);
                int payPrice = menu.getInt("menuPrice", 0);
                String payMenu = menu.getString("menuName", null);
                int menuNumber = menu.getInt("orderNumber", 0);



                payEdit.putInt("payPrice", payPrice);
                payEdit.putInt("menuNumber", menuNumber);
                payEdit.putInt("payTotalPrice", payPrice*menuNumber);
                payEdit.commit();

                Toast.makeText(getApplicationContext(), payMenu +"?????? : " +menuNumber+"\n"+ payMethod + payPrice*menuNumber +"??? \n ????????????", Toast.LENGTH_SHORT).show();

                /*
                * Firebase?????? ????????? ?????? ?????? ??? ????????????
                * */
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("project");
                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("Tokens").child(payMenu)
                        .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().exists()){
                                DataSnapshot dataSnapshot = task.getResult();
                                String existTokenNumber = String.valueOf(dataSnapshot.child("tokenNumber").getValue());
                                tokenNumber = Integer.parseInt(existTokenNumber);


                                /*
                                 * Firebase??? ????????? ?????? (menuPrice, menuTotalPrice, payMethod, tokenNumber)
                                 * */

                            }
                            tokenNumber += menuNumber;
                            UserToken userToken = new UserToken();
                            userToken.setMenuName(payMenu);
                            userToken.setMenuPrice(payPrice);
                            userToken.setTokenNumber(tokenNumber);
//                            userToken.setMenuTotalPrice(payPrice*menuNumber);
                            userToken.setPayMethod(payMethod);

                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("Tokens").child(payMenu).setValue(userToken);

                        }else{
                            Toast.makeText(getApplicationContext(), "Failed to read", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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