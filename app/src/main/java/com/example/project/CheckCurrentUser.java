package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CheckCurrentUser extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference currDatabaseRef;
    String currUser;
    int userNum = 0;

    ImageButton prev, mypage;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_current_user);

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

        mFirebaseAuth = FirebaseAuth.getInstance();
        currDatabaseRef = FirebaseDatabase.getInstance().getReference("project");

        tv = findViewById(R.id.tv_user);
        prev = findViewById(R.id.check_prev);
        mypage = findViewById(R.id.check_mp);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckCurrentUser.this, MyPage.class);
                startActivity(intent);
                finish();
            }
        });
        /**
         *  project/CurrentUser/ 의 String을 가져와서
         *  인원 처리
         *
         * */

        currDatabaseRef = FirebaseDatabase.getInstance().getReference("project");
        currDatabaseRef.child("CurrentUser").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        currUser = String.valueOf(dataSnapshot.getValue());
                        String[] timeArr = currUser.split("sep");



                        /**
                         * 현재 시간
                         * */
                        Date now = new Date();
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar cal = Calendar.getInstance();

                        cal.setTime(now);
                        cal.add(Calendar.MINUTE, -20);//현재시간 -20분을 기준으로

                        String currTime = df.format(cal.getTime());
                        //비교를 위해서 String to Date
                        try {
                            Date curr = df.parse(currTime);
                            /**
                             * 저장된 시간과 비교 --> 현재 기준으로 20분 전보다 가까운 시간에 들어온 사람 수 카운트
                             * */
                            String storeTime = "";
                            for(int i = 0; i < timeArr.length; i++) {
                                try {
                                    Date entryTime = df.parse(timeArr[i]);
                                    if (entryTime.after(curr)){
                                        storeTime = storeTime + df.format(entryTime)+"sep";
                                        currDatabaseRef.child("CurrentUser").setValue(storeTime);
                                        userNum ++;
                                        Toast.makeText(getApplicationContext(), "if", Toast.LENGTH_SHORT).show();

                                    }else{
                                        Toast.makeText(getApplicationContext(), "else", Toast.LENGTH_SHORT).show();

                                    }
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(), "catch", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                            tv.setText("현재 식당 이용자 수\n\n"+userNum + "명");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }else{
                        Toast.makeText(getApplicationContext(), "현재 아무도 없습니다.", Toast.LENGTH_LONG).show();
                        userNum = 0;
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}