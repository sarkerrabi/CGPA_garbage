package rabi.com.cgpagarbage;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    BarChart barChart;
    MyDBHandler myDB;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        //adfor
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
        //adfor


        txt = (TextView)findViewById(R.id.t1);

        barChart = (BarChart)findViewById(R.id.BarChart);
        buildUPChart();
    }

    private void buildUPChart() {

        String[] listOfGrades;
        listOfGrades = allValues();

        // chart starts from here
       ArrayList<BarEntry> barEntries1 = new ArrayList<>();
        for (int i=0;i<listOfGrades.length;i++){
            barEntries1.add(new BarEntry(i, Float.parseFloat(listOfGrades[i])));
        }
        BarDataSet barDataSet1 = new BarDataSet(barEntries1,"grades");

        barDataSet1.calcMinMaxY(0,4);
        barDataSet1.setColors(ColorTemplate.PASTEL_COLORS);
        BarData barData = new BarData(barDataSet1);



        barChart.setBackgroundColor(Color.parseColor("#e33935"));
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);



      //  barChart.setTouchEnabled(true);
      //  barChart.setDragEnabled(false);


        // chart ends from here

    }
    private String[] allValues(){
        myDB = new MyDBHandler(this);
        Cursor cursor = myDB.display();
        float Cgpa = myDB.calCGPA(cursor);
        txt.setText("CGPA "+String.format("%.2f",Cgpa));
        final String[] grade = new  String[cursor.getCount()];
        cursor.moveToFirst();


        int n=0;
        do {
            grade[n]=cursor.getString(3);

            n++;

        }while (cursor.moveToNext());
        cursor.close();
//        txt.setText(grade[0]);

    return grade;
    }

}
