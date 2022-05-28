package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPage extends AppCompatActivity {
    ImageButton prev;
    Button btn_logout, btn_history, btn_useToken;
    TextView tv_userName, tv_userID, tv_list;


    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef, mTokenRef;
    ArrayList<String> menuName = new ArrayList<String>();
    ArrayList<Integer> numberToken = new ArrayList<Integer>();





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);
        mFirebaseAuth = FirebaseAuth.getInstance();

        btn_history = findViewById(R.id.btn_history);
        btn_useToken = findViewById(R.id.btn_useToken);
        tv_userID = findViewById(R.id.tv_userID);
        tv_userName = findViewById(R.id.tv_userName);
        tv_list = findViewById(R.id.tv_list);
        SharedPreferences selectedMenu = getSharedPreferences("SelectedMenu", Activity.MODE_PRIVATE);

        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("project").child("UserAccount")
                .child(firebaseUser.getUid());


        mDatabaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        String userName = String.valueOf(dataSnapshot.child("userName").getValue());
                        tv_userName.setText("User Name : \n"+userName);

                        String userID = String.valueOf(dataSnapshot.child("emailID").getValue());
                        tv_userID.setText("User ID : \n"+userID);

                    }
                }
            }
        });

        /*
        * 보유중인 식권에 결과 표시
        * */
        mTokenRef = mDatabaseRef.child("Tokens");
        mTokenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String strMenuList ="";
                for(DataSnapshot data : snapshot.getChildren()){
                    UserToken userToken = data.getValue(UserToken.class);
                    menuName.add(userToken.getMenuName());
                    numberToken.add(userToken.getTokenNumber());

                }
                for(int i = 0; i < menuName.size(); i++){
                    strMenuList += menuName.get(i) +" : " +numberToken.get(i) +"개\n" ;
                }
                tv_list.setText(strMenuList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

        btn_useToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPage.this, UseToken.class);
                startActivity(intent);
            }
        });

    }
}
