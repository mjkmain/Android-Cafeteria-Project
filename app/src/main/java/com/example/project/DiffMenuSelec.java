package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.util.ArrayList;

public class DiffMenuSelec extends AppCompatActivity {
    ListView menu_lv;
    ArrayList<MenuSelect> data;
    ImageButton prev, myPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.diffmenuselec);


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


        menu_lv = (ListView) findViewById(R.id.menu_lv);
        prev = (ImageButton) findViewById(R.id.prevDiff);
        myPage = (ImageButton) findViewById(R.id.myPageDiff);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiffMenuSelec.this, MyPage.class);
                startActivity(intent);
            }
        });




        data = new ArrayList<MenuSelect>();
        data.add(new MenuSelect("?????????????????????", 4800, R.drawable.menu01));
        data.add(new MenuSelect("??????????????????", 4800, R.drawable.menu02));
        data.add(new MenuSelect("????????????", 4800, R.drawable.menu03));
        data.add(new MenuSelect("???????????????????????????", 4800, R.drawable.menu04));
        data.add(new MenuSelect("??????????????????", 5000, R.drawable.menu05));
        data.add(new MenuSelect("?????????????????????", 4800, R.drawable.menu06));
        data.add(new MenuSelect("?????????????????????", 4800, R.drawable.menu07));
        data.add(new MenuSelect("????????????", 4800, R.drawable.menu08));
        data.add(new MenuSelect("???????????????", 5000, R.drawable.menu09));
        data.add(new MenuSelect("???????????????", 4800, R.drawable.menu10));
        data.add(new MenuSelect("??????", 3000, R.drawable.menu11));
        data.add(new MenuSelect("??????(??????,??????,?????????)", 4000, R.drawable.menu12));
        data.add(new MenuSelect("?????????", 5000, R.drawable.menu13));
        data.add(new MenuSelect("?????????+??????", 5800, R.drawable.menu14));
        data.add(new MenuSelect("?????????+???????????????", 5800, R.drawable.menu15));
        data.add(new MenuSelect("?????????+??????", 5800, R.drawable.menu16));
        data.add(new MenuSelect("???????????????", 4800, R.drawable.menu17));
        data.add(new MenuSelect("????????????", 4800, R.drawable.menu18));
        data.add(new MenuSelect("??????????????????", 5000, R.drawable.menu19));
        data.add(new MenuSelect("???????????????", 5000, R.drawable.menu20));
        data.add(new MenuSelect("??????????????????", 4800, R.drawable.menu21));
        data.add(new MenuSelect("???????????????", 4800, R.drawable.menu22));

        MyAdapter adapter = new MyAdapter(this, data);
        menu_lv.setAdapter(adapter);
    }
}


class MenuSelect{
    String mName;
    int price;
    int imageID;

    public MenuSelect(String mName, int price, int imageID){
        this.mName = mName;
        this.price = price;
        this.imageID = imageID;
    }
}



class MyAdapter extends BaseAdapter{
    Context mContext = null;
    ArrayList<MenuSelect> mData = null;

    public MyAdapter(Context context, ArrayList<MenuSelect> data){
        mContext = context;
        mData = data;
    }
    @Override
    public int getCount() {
        return 22;
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
            itemLayout = View.inflate(mContext, R.layout.listview_item, null);

        }
        else{
            itemLayout = view;
        }

        ImageView imageView = (ImageView) itemLayout.findViewById(R.id.item_image);
        imageView.setImageResource(mData.get(i).imageID);

        TextView menuName = (TextView) itemLayout.findViewById(R.id.menu_name);
        TextView menuPrice = (TextView) itemLayout.findViewById(R.id.menu_price);

        menuName.setText(mData.get(i).mName);
        menuPrice.setText("?????? : "+mData.get(i).price +"???");

        Button btn_order = itemLayout.findViewById(R.id.btn_order);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int menuPrice = mData.get(i).price;
                int menuImage = mData.get(i).imageID;
                String menuName = mData.get(i).mName;

                SharedPreferences menu = mContext.getSharedPreferences("Menu", Activity.MODE_PRIVATE);
                SharedPreferences.Editor menuEdit = menu.edit();

                menuEdit.putInt("menuPrice", menuPrice);
                menuEdit.putInt("menuImage", menuImage);
                menuEdit.putString("menuName", menuName);
                menuEdit.commit();

                Intent intent = new Intent(mContext, PaymentActivity.class);
                mContext.startActivity(intent);

            }
        });


        return itemLayout;
    }
}
