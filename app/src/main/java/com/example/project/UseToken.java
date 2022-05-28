package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.util.ArrayList;

public class UseToken extends AppCompatActivity {
    ListView listView;
    ArrayList<selectedMenu> data;
    ImageButton prev;
    MyPage mp = new MyPage();

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef, mTokenRef;
    ArrayList<String> menuName = new ArrayList<String>();
    ArrayList<Integer> numberToken = new ArrayList<Integer>();
    String strMenuList ="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_token);

        listView = (ListView) findViewById(R.id.useTokenListview);
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        prev = (ImageButton) findViewById(R.id.prevUseToken);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        /*
        * 복붙
        * */


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("project").child("UserAccount")
                .child(firebaseUser.getUid());
        mTokenRef = mDatabaseRef.child("Tokens");
        mTokenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    UserToken userToken = data.getValue(UserToken.class);
                    menuName.add(userToken.getMenuName());
                    numberToken.add(userToken.getTokenNumber());

                }
                data = new ArrayList<selectedMenu>();
                for(int i = 0; i < menuName.size(); i++){
                    data.add(new selectedMenu(menuName.get(i), numberToken.get(i)));
                }
                SharedPreferences tokenNumber = getSharedPreferences("TokenNumber", Activity.MODE_PRIVATE);
                SharedPreferences.Editor tokenNumberEdit = tokenNumber.edit();
                tokenNumberEdit.putInt("number", menuName.size());
                tokenNumberEdit.commit();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        UseTokenAdapter adapter = new UseTokenAdapter(this, data);
        listView.setAdapter(adapter);
    }
}


class selectedMenu{
    String mName;
    Integer numberToken;

    public selectedMenu(String mName, Integer numberToken){
        this.mName = mName;
        this.numberToken = numberToken;
    }
}



class UseTokenAdapter extends BaseAdapter{
    Context mContext = null;
    ArrayList<selectedMenu> mData = null;

    public UseTokenAdapter(Context context, ArrayList<selectedMenu> data){
        mContext = context;
        mData = data;
    }
    @Override
    public int getCount() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("TokenNumber", Activity.MODE_PRIVATE);
        int temp = sharedPreferences.getInt("number", 0);
        return temp;
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemLayout;

        if(view == null){
            itemLayout = View.inflate(mContext, R.layout.use_token_item, null);

        }
        else{
            itemLayout = view;
        }
//        TextView menuName = (TextView) itemLayout.findViewById(R.id.use_menu_name);
//        TextView numberToken = (TextView)itemLayout.findViewById(R.id.use_number_token);
//
//        menuName.setText(mData.get(i).mName);
//        numberToken.setText("식권 수 : "+mData.get(i).numberToken + "");


        return itemLayout;
    }
}
