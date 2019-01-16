package rabi.com.cgpagarbage.advisor;

import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockApplication;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import rabi.com.cgpagarbage.ListItem;
import rabi.com.cgpagarbage.MainActivity;
import rabi.com.cgpagarbage.MyDBHandler;
import rabi.com.cgpagarbage.R;
import rabi.com.cgpagarbage.presenter.CustomAdapter;

public class AdvisorActivity extends AppCompatActivity {

    TextView mcgpa, mTotalCredit,messageT,mctext;
    ListView L1;
    EditText tarCGPA;
    MyDBHandler mydb;
    Typeface typeface;
    ListItem[] finalResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor);

        mydb = MainActivity.getDbAdapter();

        tarCGPA = (EditText)findViewById(R.id.targetCGPA);

        mctext = (TextView)findViewById(R.id.mc_text);
        typeface =Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Light.ttf");
        mcgpa = (TextView)findViewById(R.id.mCgPa);
        mTotalCredit = (TextView)findViewById(R.id.metcredit);
        messageT = (TextView)findViewById(R.id.message);

        L1=(ListView)findViewById(R.id.listviewForList);



        printDB();
    }

    private void printDB() {
        Cursor c1= mydb.display();

        final String[] course1 = new  String[c1.getCount()];
        final String[] credit1 = new  String[c1.getCount()];
        final String[] grade1 = new  String[c1.getCount()];
        float a = mydb.sumCredit(c1);

        mTotalCredit.setTypeface(typeface);     mTotalCredit.setText(String.format("%.2f",a)+"");
        float Ccgpa = mydb.calCGPA(c1);

        mcgpa.setTypeface(typeface);        mcgpa.setText(String.format("%.2f",Ccgpa)+"");

    }


    public void onRETAKEonly(View v){


        finalResult = new ListItem[4];
        double TARGET_CGPA =Double.parseDouble(tarCGPA.getText().toString()) ;

        Cursor cursor = mydb.getSortedGradelist();
        ListItem[] studentGradeSheet = getDbdataList(cursor);

        double analyzeCGPA = calcualteCGPA(studentGradeSheet);
        int decision=0;
        DecimalFormat df = new DecimalFormat("####0.00");

       // Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

        assert studentGradeSheet != null;
        while (
                analyzeCGPA>=TARGET_CGPA
                || Float.parseFloat(studentGradeSheet[0].getGrade())!=4.0
                || Float.parseFloat(studentGradeSheet[1].getGrade())!=4.0
                || Float.parseFloat(studentGradeSheet[2].getGrade())!=4.0
                || Float.parseFloat(studentGradeSheet[3].getGrade())!=4.0

                )
        {


            studentGradeSheet[0].upgradeOneGrade();
            finalResult[0] = studentGradeSheet[0];
            analyzeCGPA = calcualteCGPA(studentGradeSheet);
            if (analyzeCGPA>=TARGET_CGPA){
                decision=1;
                break;
            }



            studentGradeSheet[1].upgradeOneGrade();
            finalResult[1] = studentGradeSheet[1];
            analyzeCGPA = calcualteCGPA(studentGradeSheet);
            if (analyzeCGPA>=TARGET_CGPA){
                decision=1;
                break;
            }



            studentGradeSheet[2].upgradeOneGrade();
            finalResult[2] = studentGradeSheet[2];
            analyzeCGPA = calcualteCGPA(studentGradeSheet);
            if (analyzeCGPA>=TARGET_CGPA){
                decision=1;
                break;
            }



            studentGradeSheet[3].upgradeOneGrade();
            finalResult[3] = studentGradeSheet[3];
            analyzeCGPA = calcualteCGPA(studentGradeSheet);
            if (analyzeCGPA>=TARGET_CGPA){
                decision=1;
                break;
            }



        }
        if (decision==0 && Float.parseFloat(finalResult[0].getGrade())==4.0
                && Float.parseFloat(finalResult[1].getGrade())==4.0
                && Float.parseFloat(finalResult[2].getGrade())==4.0
                && Float.parseFloat(finalResult[3].getGrade())==4.0)
        {
           // Toast.makeText(getApplicationContext()," "+df.format(analyzeCGPA),Toast.LENGTH_SHORT).show();
            messageT.setText(R.string.decision_true_message);

        }else {
           // Toast.makeText(getApplicationContext(),"Just retake this courses and achieve your target CGPA "+df.format(analyzeCGPA),Toast.LENGTH_LONG).show();
            messageT.setText(R.string.decision_false_message);
        }


        mctext.setText("Your CGPA will be");
        mcgpa.setText(" "+df.format(analyzeCGPA));
        rows(finalResult);





    }

    public void onNEWonly(View v){


    }

    public void onNEWandRETAKE(View v){

    }

    private ListItem[] getDbdataList(Cursor c1){

        final String[] course1 = new  String[c1.getCount()];
        final String[] credit1 = new  String[c1.getCount()];
        final String[] grade1 = new  String[c1.getCount()];


        if(c1.getCount()==0) {
            Toast.makeText(this, "COURSE LIST EMPTY!!!! !!!", Toast.LENGTH_LONG).show();

            return null;
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






        return listItems;
    }




    public double calcualteCGPA(ListItem[] selectedStudentGradeSheets){
        double sum = 0.0;
        double cgpa = 0.0;
        int totalCredit = 0;


        for (ListItem selectedStudentGradeSheet1 : selectedStudentGradeSheets) {

            sum = sum + (Float.parseFloat(selectedStudentGradeSheet1.getGrade()) * Float.parseFloat(selectedStudentGradeSheet1.getCredit()));
        }

        for (ListItem selectedStudentGradeSheet : selectedStudentGradeSheets) {

            totalCredit += Float.parseFloat(selectedStudentGradeSheet.getCredit());
        }
        cgpa = sum / totalCredit;

        return cgpa;
    }
    public  void rows(ListItem[] listItems){
        CustomAdapter listAdapter = new CustomAdapter(getApplicationContext(), listItems);
        L1.setAdapter(listAdapter);

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(L1.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, L1);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = L1.getLayoutParams();
        params.height = totalHeight + (L1.getDividerHeight() * (listAdapter.getCount() - 1));
        L1.setLayoutParams(params);
        L1.requestLayout();
    }




}
