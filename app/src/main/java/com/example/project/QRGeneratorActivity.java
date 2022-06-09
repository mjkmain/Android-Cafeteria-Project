package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRGeneratorActivity extends AppCompatActivity {
    private IntentIntegrator qrScan;
    private FirebaseAuth mFirebaseAuth;

    ImageView iv_qr;
    ImageButton prev, myPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_qrgenerator);

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


        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        String ID = firebaseUser.getUid();

        iv_qr = findViewById(R.id.iv_qr);
        prev = findViewById(R.id.QRprev);
        myPage = findViewById(R.id.QRmypage);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QRGeneratorActivity.this, MyPage.class);
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        String menuName = intent.getStringExtra("menuName");
        String uniMenuName = korToUni(menuName);


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(uniMenuName+","+ID, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv_qr.setImageBitmap(bitmap);
        } catch ( Exception e ){}
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //딜레이 후 시작할 코드 작성
                finish();
            }
        }, 10000);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(QRGeneratorActivity.this, UseToken.class);
                startActivity(intent1);
                finish();
            }
        });

    }
    /*
    * QR코드에 한국어 디코딩이 안되어서 유니코드로 변경
    *
    *           ************************* QR Code Progress*************************
    *
    *       (한국어 메뉴 이름, 사용자 UID) --(korToUni)--> (유니코드 메뉴 이름, 사용자 UID)--> QR 인코딩
    *
    *       -->스캔-->
    *
    *       QR 디코딩 --> (유니코드 메뉴 이름, 사용자 UID) --(uniToKor)--> (한국어 메뉴 이름, 사용자 UID)
    *
    *           ************************* QR Code Progress*************************
    * */
    public String korToUni(String kor){
        StringBuffer result = new StringBuffer();

        for(int i=0; i<kor.length(); i++){
            int cd = kor.codePointAt(i);
            if (cd < 128){
                result.append(String.format("%c", cd));
            }else{
                result.append(String.format("\\u%04x", cd));
            }
        }
        return result.toString();
    }
}