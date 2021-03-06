package com.example.project;

import static com.example.project.R.layout.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button btn_login, btn_register;
    CheckBox cb_auto, cb_saveID;
    EditText et_ID, et_pw;
    TextView login_menu;

    private DatabaseReference mDatabaseRef;

    private SignInButton btn_google;
    private static final int RC_SIGN_IN = 100;

    private FirebaseAuth mFirebaseAuth;
    public static String[] menuArr = new String[7];
    int i = 0;

    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(login);

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




        mDatabaseRef = FirebaseDatabase.getInstance().getReference("project");
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        cb_auto = (CheckBox) findViewById(R.id.cb_auto);
        cb_saveID = (CheckBox) findViewById(R.id.cb_saveID);
        et_ID = findViewById(R.id.et_ID);
        et_pw = findViewById(R.id.et_pw);
        login_menu = findViewById(R.id.login_menu);

        mFirebaseAuth = FirebaseAuth.getInstance();


        /*
        * ?????????????????? ????????? ????????? ?????? : autoLogin
        * */
        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLoginEdit = auto.edit();

        /*
        * ????????? ??? ?????? (ID ??????, ?????? ?????????)
        * */

        Boolean isAutoChecked = auto.getBoolean("autoChecked", false);
        if(isAutoChecked){
            cb_auto.setChecked(true);
        }else{
            cb_auto.setChecked(false);
        }

        Boolean isSaveChecked = auto.getBoolean("SaveChecked", false);
        if(isSaveChecked){
            cb_saveID.setChecked(true);
            et_ID.setText(auto.getString("saveID", null));
        }else{
            cb_saveID.setChecked(false);
        }

        /*
        * ?????? ?????? ?????????
        * */
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

                if(dayOfWeekNumber!=1 && dayOfWeekNumber!=7) {
                    login_menu.setText(menuArr[dayOfWeekNumber - 2]);
                }
                else{
                    login_menu.setText("????????? ?????? ??????");
                    login_menu.setTextSize(30);
                    login_menu.setGravity(Gravity.CENTER);
                }
                is.close();
            }catch(IOException e){
                Toast.makeText(getApplicationContext(), "Error2", Toast.LENGTH_SHORT).show();
            }
        }

        /*
        * ???????????????
        * */
        if(cb_auto.isChecked()) {
            String userId = auto.getString("userID", null);
            String passwordNo = auto.getString("passwordNo", null);

            if (userId != null && passwordNo != null) {
                mFirebaseAuth.signInWithEmailAndPassword(userId, passwordNo).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), MainDisplay.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "????????? ??????", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }


        /*
        * ????????? ?????? ?????? --> ????????? ??????--> ?????? ??? main ???????????? ??????
        * */
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = et_ID.getText().toString();
                String strPass = et_pw.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("GoogleLogin", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("Google", false);
                editor.commit();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        /*
                         * ??????????????? ?????????, ????????? ??????
                         * */
                        if(task.isSuccessful()){
                            if(cb_saveID.isChecked()){
                                autoLoginEdit.putString("saveID", strEmail);
                            }
                            if(cb_auto.isChecked()){
                                autoLoginEdit.putString("userID", strEmail);
                                autoLoginEdit.putString("passwordNo", strPass);
                                autoLoginEdit.putBoolean("autoChecked", true);
                            }
                            Intent intent = new Intent(getApplicationContext(), MainDisplay.class);
                            startActivity(intent);
                            finish();
                        }else{
                            autoLoginEdit.putBoolean("autoChecked", false);
                            Toast.makeText(getApplicationContext(), "????????? ??????", Toast.LENGTH_SHORT).show();
                        }
                        autoLoginEdit.commit();
                    }
                });


                /*
                * ID ?????? ??????
                */
                if(cb_saveID.isChecked()){
                    autoLoginEdit.putBoolean("SaveChecked", true);
                }else{
                    autoLoginEdit.putBoolean("SaveChecked", false) ;
                }
                autoLoginEdit.commit();

            }
        });


        /*
        * ???????????? ?????? ?????? --> ???????????? ???
        * */
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        /*
         * For google login
         * */

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

        btn_google = findViewById(R.id.googleLogin);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(intent,RC_SIGN_IN);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//?????? ????????? ?????? ???????????? ??? ????????? ?????? ???

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                resultLogin(account);

            }
        }
    }

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String email = account.getEmail();
                            int index = email.indexOf("@");
                            if(email.substring(index+1).equals("edu.hanbat.ac.kr")) {//?????? ????????? ????????? ??????
                                Toast.makeText(MainActivity.this, "????????? ??????", Toast.LENGTH_SHORT).show();

                                SharedPreferences sharedPreferences = getSharedPreferences("GoogleLogin", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("Google", true);
                                editor.putString("UserName", email.substring(0,index));
                                editor.putString("UserID", email);
                                editor.commit();

                                Intent intent = new Intent(getApplicationContext(), MainDisplay.class);

                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(), "??????????????? ?????? ??????????????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
                                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "????????? ??????", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



}
