package com.example.project;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DiffMenuSelec extends AppCompatActivity {
    ListView menu_lv;
    ArrayList<MenuSelect> data;
    SearchView sv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diffmenuselec);

        menu_lv = (ListView) findViewById(R.id.menu_lv);
        sv = (SearchView) findViewById(R.id.searchView);

        sv.setSubmitButtonEnabled(true);

        data = new ArrayList<MenuSelect>();
        data.add(new MenuSelect("김치찌개", 3600, R.drawable.img1));
        data.add(new MenuSelect("짬뽕", 3600, R.drawable.img2));
        data.add(new MenuSelect("차돌박이", 3600, R.drawable.img3));
        data.add(new MenuSelect("연아", 3600, R.drawable.img4));
        data.add(new MenuSelect("산들", 3800, R.drawable.img5));

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
        return 20;
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i%5);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemLayout;
        int newPosition = i%5;

        if(view == null){
            itemLayout = View.inflate(mContext, R.layout.listview_item, null);

        }
        else{
            itemLayout = view;
        }

        ImageView imageView = (ImageView) itemLayout.findViewById(R.id.item_image);
        imageView.setImageResource(mData.get(newPosition).imageID);

        TextView menuName = (TextView) itemLayout.findViewById(R.id.menu_name);
        TextView menuPrice = (TextView) itemLayout.findViewById(R.id.menu_price);

        menuName.setText(mData.get(newPosition).mName);
        menuPrice.setText(mData.get(newPosition).price+ "");


        return itemLayout;
    }
}
