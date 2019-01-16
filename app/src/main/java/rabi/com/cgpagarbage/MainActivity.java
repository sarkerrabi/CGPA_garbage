package rabi.com.cgpagarbage;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import rabi.com.cgpagarbage.advisor.AdvisorActivity;
import rabi.com.cgpagarbage.presenter.CustomAdapter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
  //  TextView table;
  static MyDBHandler mydb;
    EditText course, credit, gpa;
    TextView CGPA,Tcredit,ctext,tcredit;
    ListView L1;
    CustomAdapter listAdapter;
    Typeface typeface;
    TableLayout table;
    // Button add;

    public  int i = 0;

    public static MyDBHandler getDbAdapter() {
        return mydb;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //adfor
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
        //adfor


       // table = (TableLayout)findV iewById(R.id.table);

        typeface =Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Light.ttf");

        CGPA =(TextView)findViewById(R.id.mCgPa);
        Tcredit=(TextView)findViewById(R.id.metcredit);
        ctext=(TextView)findViewById(R.id.c_text);
        tcredit=(TextView)findViewById(R.id.totalCre);
        ctext.setTypeface(typeface);


        tcredit.setTypeface(typeface);





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(getApplicationContext(),AdvisorActivity.class);
                startActivity(intent);

            }
        });

        //    add =(Button)findViewById(R.id.add);

        //  table = (TextView)findViewById(R.id.table);
        course = (EditText)findViewById(R.id.crs);
        credit = (EditText)findViewById(R.id.crdt);
        gpa = (EditText)findViewById(R.id.gpa);

        L1=(ListView)findViewById(R.id.listview);

        mydb = new MyDBHandler(this);

        printDatabase();

        course.setOnKeyListener(this);







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.reset) {
            deleteALL();


            return true;
        }else if (id == R.id.stats){
            Cursor cur = mydb.display();
            int pc = cur.getCount();
            if (pc==0){
                Toast.makeText(getApplicationContext(),"NO DATA FOUND ! !! !!",Toast.LENGTH_SHORT).show();
                return false;
            }else {
                Intent intent = new Intent(getApplicationContext(),StatsActivity.class);
                startActivity(intent);
                return true;
            }

        }else if ( id== R.id.setting ){
            Intent in = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(in);
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteALL() {
        mydb.deleteALLcourse();
        Intent intent = getIntent();
        finish();
        overridePendingTransition( 0, 0);
        startActivity(intent);
        overridePendingTransition( 0, 0);
    }

    //add course
    public void onclickADD(View v){

        String co= course.getText().toString();
        String cr=credit.getText().toString();
        String grade=gpa.getText().toString();


        float credt;
        try {
            credt = Float.parseFloat(cr);
        }catch (NumberFormatException e){
            credt=0;
        }
        float gr;
        try {
            gr = Float.parseFloat(grade);
        }catch (NumberFormatException e){
            gr=0;
        }

        if(!co.isEmpty()) {

            Course c = new Course(co, credt, gr);




            //check inserted or not starts
            if (!isCourseFound(c)) {
                if (mydb.addCourse(c)) {
                    // Toast.makeText(MainActivity.this,"successfully add !!!",Toast.LENGTH_LONG).show();
                    Snackbar.make(v, "successfully add !!!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    Toast.makeText(this, "Not inserted !!!", Toast.LENGTH_LONG).show();
                }
                printDatabase();

            }//check inserted or not end
        }else {
            Toast.makeText(getApplicationContext(),"course name please !!!",Toast.LENGTH_LONG).show();
        }





    }//add course

    //delete course starts
    public void onclickDELETE(View v){
        String cr=credit.getText().toString();
        String grade=gpa.getText().toString();
        int credt;
        try {
            credt = Integer.parseInt(cr);
        }catch (NumberFormatException e){
            credt=0;
        }
        float gr;
        try {
            gr = Float.parseFloat(grade);
        }catch (NumberFormatException e){
            gr=0;
        }



        Course c = new Course(course.getText().toString(),credt,gr);
        if(mydb.deleteCourse(c)>0){
            Snackbar.make(v,"successfully DELETED!!!",Snackbar.LENGTH_LONG).setAction("Action",null).show();
            Cursor cursor =mydb.display();
            if(cursor.getCount()==0){
                Intent intent = getIntent();
                finish();
                overridePendingTransition( 0, 0);
                startActivity(intent);
                overridePendingTransition( 0, 0);
            }
        }else{
            Snackbar.make(v,"NOT DELETED!!!",Snackbar.LENGTH_LONG).setAction("Action",null).show();

        }


        printDatabase();


    }//delete course starts



    //Update course starts
    public void onclickUPDATE(View v){
        String cr=credit.getText().toString();
        String grade=gpa.getText().toString();
        int credt;
        try {
            credt = Integer.parseInt(cr);
        }catch (NumberFormatException e){
            credt=0;
        }
        float gr;
        try {
            gr = Float.parseFloat(grade);
        }catch (NumberFormatException e){
            gr=0;
        }



        Course c = new Course(course.getText().toString(),credt,gr);
       if(mydb.updateCourse(c)){
           if(mydb.display().getCount()!=0) {
               Snackbar.make(v, "successfully UPDATED !!!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
           }

       }else{
           Snackbar.make(v, "NOT UPDATED !!!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
       }
        printDatabase();

    }//update course ends


//print data
    public void printDatabase(){




        setBlank();
        Cursor c1= mydb.display();

        final String[] course1 = new  String[c1.getCount()];
        final String[] credit1 = new  String[c1.getCount()];
        final String[] grade1 = new  String[c1.getCount()];
        float a = mydb.sumCredit(c1);

        Tcredit.setTypeface(typeface);     Tcredit.setText(String.format("%.2f",a)+"");
        float Ccgpa = mydb.calCGPA(c1);

        CGPA.setTypeface(typeface);        CGPA.setText(String.format("%.2f",Ccgpa)+"");


       if(c1.getCount()==0) {
            Toast.makeText(this, "COURSE LIST EMPTY!!!! !!!", Toast.LENGTH_LONG).show();

            return;
        }
        int n=0;
        c1.moveToFirst();

        ListItem[] listItems = new  ListItem[c1.getCount()];

        do {
            course1[n] =c1.getString(1);
            credit1[n]=c1.getString(2);
            grade1[n]=c1.getString(3);

            listItems[n]=new ListItem(course1[n],credit1[n],grade1[n]);

            n=n+1;

        }while (c1.moveToNext());




        //startfrom here
        rows(listItems);
        c1.close();




        //last here


    }//print the table
    private void setBlank(){
        course.setText("");
        credit.setText("");
        gpa.setText("");
    }


    @Override
    public void onClick(View v) {

    }

    public  void rows(ListItem[] listItems){
        listAdapter = new CustomAdapter(this,listItems);
        L1.setAdapter(listAdapter);

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(L1.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, L1);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = L1.getLayoutParams();
        params.height = totalHeight + (L1.getDividerHeight() * (listAdapter.getCount() - 1));
        L1.setLayoutParams(params);
        L1.requestLayout();
    }
    public boolean isCourseFound(Course course){
        Cursor cursor = mydb.searchCourse(course);
        if (cursor.getCount()!=0){
            Toast.makeText(this,"Already inserted "+course.get_courseName(),Toast.LENGTH_SHORT).show();
            cursor.close();
            return true;
        }

        cursor.close();
        return false;



    }//end search course


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {


        return false;
    }
}


