package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

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
    ImageButton prev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);
        mFirebaseAuth = FirebaseAuth.getInstance();


        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        String ID = firebaseUser.getUid();

        iv_qr = findViewById(R.id.iv_qr);
        prev = findViewById(R.id.prev);

        Intent intent = getIntent();
        String menuName = intent.getStringExtra("menuName");
        int tokenNumber = intent.getIntExtra("tokenNumber", 0);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(menuName+","+ID, BarcodeFormat.QR_CODE, 200, 200);
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
                finish();
            }
        });

    }
}