package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class CheckCurrentUser extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference currDatabaseRef;
    String currUser;
    int userNum = 0;
    ProgressBar progressBar;
    ImageButton prev, mypage;
    TextView tv;
    private XYPlot plot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_check_current_user);

        /**
         * Plot
         * */
        plot = (XYPlot) findViewById(R.id.plot);
        plot.setRangeBoundaries(0, 450, BoundaryMode.FIXED);
        plot.getLegend().setVisible(false);


        // create a couple arrays of y-values to plot:
        final String[] domainLabels = {"", "11:00", "", "", "12:00", "",
                "", "13:00", "", ""};

        Number[] series1Numbers = {102, 144, 321, 343, 287, 362, 244, userNum};

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");


        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels);

        LineAndPointFormatter series2Format =
                new LineAndPointFormatter(this, R.xml.line_plot_formatter_labels_2);

        // add an "dash" effect to the series2 line:
        series2Format.getLinePaint().setPathEffect(new DashPathEffect(new float[] {

                // always use DP when specifying pixel sizes, to keep things consistent across devices:
                PixelUtils.dpToPix(0),
                PixelUtils.dpToPix(0)}, 0));

        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        series2Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });

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

        progressBar = findViewById(R.id.progress);
        progressBar.setMax(10);

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
         *  Firebase 의 project/CurrentUser/ 의 String을 가져와서
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
                        cal.add(Calendar.MINUTE, -10);//현재시간 -20분을 기준으로

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

                                    }else{

                                    }
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(), "catch", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                            tv.setText("현재 식당 이용자 수\n"+userNum + "명");
                            progressBar.setProgress(userNum);

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