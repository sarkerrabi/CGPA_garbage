package rabi.com.cgpagarbage;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    TextView table;
    MyDBHandler mydb;
    EditText course, credit, gpa;
    TextView CGPA,Tcredit;
    ListView L1;
    CustomAdapter listAdapter;
    // Button add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CGPA =(TextView)findViewById(R.id.CgPa);
        Tcredit=(TextView)findViewById(R.id.etcredit);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //    add =(Button)findViewById(R.id.add);

        //  table = (TextView)findViewById(R.id.table);
        course = (EditText)findViewById(R.id.crs);
        credit = (EditText)findViewById(R.id.crdt);
        gpa = (EditText)findViewById(R.id.gpa);

        L1=(ListView)findViewById(R.id.listView);

        mydb = new MyDBHandler(this);

        printDatabase();



    }


    //add course
    public void onclickADD(View v){
        String co= course.getText().toString();
        String cr=credit.getText().toString();
        String grade=gpa.getText().toString();

        if (co.isEmpty()){
            co="NO Course Name!!!";
        }
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



        Course c = new Course(co,credt,gr);
        if(mydb.checkCourseName(c.get_courseName())==true){
            if(mydb.addCourse(c)==true){
                // Toast.makeText(MainActivity.this,"successfully add !!!",Toast.LENGTH_LONG).show();
                Snackbar.make(v,"successfully add !!!",Snackbar.LENGTH_LONG).setAction("Action",null).show();
            }
            else{
                Toast.makeText(this, "Not inserted !!!", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(this, "Kam korse !!!", Toast.LENGTH_LONG).show();
        }




        printDatabase();
    }//add course

    //delete course
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
                startActivity(intent);
            }
        }else{
            Snackbar.make(v,"NOT DELETED!!!",Snackbar.LENGTH_LONG).setAction("Action",null).show();

        }


        printDatabase();


    }//Delete course



    //Update course
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

    }//update course


//print the table
    public void printDatabase(){
        setBlank();
        Cursor c1= mydb.display();
        final String[] course1 = new  String[c1.getCount()];
        final String[] credit1 = new  String[c1.getCount()];
        final String[] grade1 = new  String[c1.getCount()];
       int a = mydb.sumCredit(c1);
        Tcredit.setText(a+"");
        float Ccgpa = mydb.calCGPA(c1);
        CGPA.setText(Ccgpa+"");



       if(c1.getCount()==0) {
            Toast.makeText(this, "NO Course FOUND !!!! !!!", Toast.LENGTH_LONG).show();

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



           /* listItems[n].setCourse(course1[n]);
            listItems[n].setCredit(credit1[n]);
            listItems[n].setGrade(grade1[n]); */




            n=n+1;

        }while (c1.moveToNext());


     listAdapter = new CustomAdapter(this,listItems);

        L1.setAdapter(listAdapter);




        /*L1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s1= String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(MainActivity.this,s1, Toast.LENGTH_LONG).show();
               // course.setText(course1[position]);
               // credit.setText(credit1[position]);
               // gpa.setText(grade1[position]);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(s1);
                builder.setMessage("Credit = " + credit1[position] + "\n" + "Grade = " + grade1[position]);

                builder.setCancelable(true);
                builder.create();
                builder.show();

            }
        });

        */


    }//print the table
    private void setBlank(){
        course.setText("");
        credit.setText("");
        gpa.setText("");
    }




}
