package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
    ImageButton prev, myPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diffmenuselec);

        menu_lv = (ListView) findViewById(R.id.menu_lv);
        sv = (SearchView) findViewById(R.id.searchView);
        prev = (ImageButton) findViewById(R.id.prev);
        myPage = (ImageButton) findViewById(R.id.myPage);

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


        sv.setSubmitButtonEnabled(true);

        data = new ArrayList<MenuSelect>();
        data.add(new MenuSelect("해물순두부찌개", 4800, R.drawable.menu01));
        data.add(new MenuSelect("참치김치찌개", 4800, R.drawable.menu02));
        data.add(new MenuSelect("부대찌개", 4800, R.drawable.menu03));
        data.add(new MenuSelect("고추장뚝배기불고기", 4800, R.drawable.menu04));
        data.add(new MenuSelect("닭갈비뚝배기", 5000, R.drawable.menu05));
        data.add(new MenuSelect("뚝배기날치알밥", 4800, R.drawable.menu06));
        data.add(new MenuSelect("돌솥제육비빔밥", 4800, R.drawable.menu07));
        data.add(new MenuSelect("제육덮답", 4800, R.drawable.menu08));
        data.add(new MenuSelect("닭갈비덮밥", 5000, R.drawable.menu09));
        data.add(new MenuSelect("치즈라볶이", 4800, R.drawable.menu10));
        data.add(new MenuSelect("라면", 3000, R.drawable.menu11));
        data.add(new MenuSelect("라면(해장,치즈,떡만두)", 4000, R.drawable.menu12));
        data.add(new MenuSelect("직생돈", 5000, R.drawable.menu13));
        data.add(new MenuSelect("직생돈+알밥", 5800, R.drawable.menu14));
        data.add(new MenuSelect("직생돈+오므라이스", 5800, R.drawable.menu15));
        data.add(new MenuSelect("직생돈+우동", 5800, R.drawable.menu16));
        data.add(new MenuSelect("등심돈까스", 4800, R.drawable.menu17));
        data.add(new MenuSelect("치킨까스", 4800, R.drawable.menu18));
        data.add(new MenuSelect("고구마돈까스", 5000, R.drawable.menu19));
        data.add(new MenuSelect("치즈돈까스", 5000, R.drawable.menu20));
        data.add(new MenuSelect("치킨마요덮밥", 4800, R.drawable.menu21));
        data.add(new MenuSelect("돈까스덮밥", 4800, R.drawable.menu22));

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
        menuPrice.setText(mData.get(i).price+ "");


        return itemLayout;
    }
}
