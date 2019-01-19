package rabi.com.cgpagarbage.advisor;

import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockApplication;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rabi.com.cgpagarbage.ListItem;
import rabi.com.cgpagarbage.MainActivity;
import rabi.com.cgpagarbage.MyDBHandler;
import rabi.com.cgpagarbage.R;
import rabi.com.cgpagarbage.presenter.CustomAdapter;

public class AdvisorActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mcgpa, mTotalCredit,messageT,mctext;
    ListView L1;
    EditText tarCGPA;
    MyDBHandler mydb;
    Typeface typeface;

    ListItem[] studentGradeSheet;

    Button Retake,NewO,NewRetake;

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

        Retake = (Button)findViewById(R.id.retake_only);
        NewO = ( Button)findViewById(R.id.new_only);
        NewRetake = (Button)findViewById(R.id.re_and_new);

        Retake.setOnClickListener(this);
        NewO.setOnClickListener(this);
        NewRetake.setOnClickListener(this);


        L1=(ListView)findViewById(R.id.listviewForList);




        Cursor cursor = mydb.getSortedGradelist();
        studentGradeSheet = getDbdataList(cursor);

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





    public void onRETAKEonly(double TARGET_CGPA){


            double analyzeCGPA = calcualteCGPA(studentGradeSheet);

            List<ListItem> gradeList = arrayToList(studentGradeSheet);

            analyze(TARGET_CGPA, gradeList);




    }

    public void onNEWonly(double TARGET_CGPA){



        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        double analyzeCGPA = calcualteCGPA(studentGradeSheet);
        //int decision=0;


        List<ListItem> gradeList = arrayToList(studentGradeSheet);
        List<ListItem> generatedStudens = newCourseGenerator(analyzeCGPA);
        gradeList.addAll(generatedStudens);

        analyze(TARGET_CGPA,gradeList);




    }

    public void onNEWandRETAKE(double TARGET_CGPA){


        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        double analyzeCGPA = calcualteCGPA(studentGradeSheet);
        //int decision=0;


        List<ListItem> gradeList = arrayToList(studentGradeSheet);
        List<ListItem> generatedStudens = newCourseGenerator(analyzeCGPA);

        generatedStudens.remove(generatedStudens.size() - 1);
        generatedStudens.remove(generatedStudens.size() - 2);
        gradeList.addAll(generatedStudens);

        analyze(TARGET_CGPA,gradeList);




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

    //calculate CGPA of the student starts
    public double lscalcualteCGPA(List<ListItem> selectedStudentGradeSheets){
        double sum = 0.0;
        double cgpa = 0.0;
        int totalCredit = 0;


        for(int i = 0; i < selectedStudentGradeSheets.size(); i++) {

            sum = sum + (Float.parseFloat(selectedStudentGradeSheets.get(i).getGrade()) * Float.parseFloat(selectedStudentGradeSheets.get(i).getCredit()));
        }

        for(int j = 0; j < selectedStudentGradeSheets.size(); j++) {

            totalCredit += Float.parseFloat(selectedStudentGradeSheets.get(j).getCredit());
        }
        cgpa = sum / totalCredit;

        return cgpa;




    }
    //calculate CGPA of the student ends




    public  void rows(ListItem[] listItems){
        CustomAdapter listAdapter = new CustomAdapter(getApplicationContext(), listItems);
        L1.setAdapter(listAdapter);
/*
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
        L1.requestLayout();*/
    }

    // genaraNew course starts
    public List<ListItem> newCourseGenerator(double currentCGPA) {

        List<ListItem> ssgs = new ArrayList<ListItem>();
        double newCGPA;
        //String newGrade;

        if((currentCGPA >= 0.0) && (currentCGPA <= 1)) {

            newCGPA = 1.0;
        }

        else if((currentCGPA >= 1.01) && (currentCGPA <= 1.30)) {

            newCGPA = 1.30;

        }

        else if((currentCGPA >= 1.31) && (currentCGPA <= 1.7)) {

            newCGPA = 1.7;

        }

        else if((currentCGPA >= 1.71) && (currentCGPA <= 2.0)) {

            newCGPA = 2.0;

        }

        else if((currentCGPA >= 2.01) && (currentCGPA <= 2.3)) {

            newCGPA = 2.3;

        }

        else if((currentCGPA >= 2.31) && (currentCGPA <= 2.7)) {

            newCGPA = 2.7;

        }

        else if((currentCGPA >= 2.71) && (currentCGPA <= 3)) {

            newCGPA = 3.0;

        }

        else if((currentCGPA >= 3.01) && (currentCGPA <= 3.30)) {

            newCGPA = 3.3;

        }

        else if((currentCGPA >= 3.31) && (currentCGPA <= 3.7)) {

            newCGPA = 3.7;

        }

        else {

            newCGPA = 4;
        }

        for(int i = 0; i < 4; i++) {

            ListItem selstuGraSheet = new ListItem("ABC" +
                    (i + 101),"3",newCGPA+"");
            ssgs.add(selstuGraSheet);


        }

        return ssgs;
    }

//  generateNew course ends



    public void analyze(double TARGETCGPA, List<ListItem> studentGradeSheetT){
        DecimalFormat df = new DecimalFormat("####0.00");
        ListItem[] finalResult = new ListItem[4];

        if (studentGradeSheetT!=null){
            double analyzeCGPA = lscalcualteCGPA(studentGradeSheetT);
            int decision=0;

            while (analyzeCGPA>=TARGETCGPA || Float.parseFloat(studentGradeSheetT.get(studentGradeSheetT.size()-1).getGrade())!=4.0
                    || Float.parseFloat(studentGradeSheetT.get(studentGradeSheetT.size()-2).getGrade())!=4.0
                    || Float.parseFloat(studentGradeSheetT.get(studentGradeSheetT.size()-3).getGrade())!=4.0
                    || Float.parseFloat(studentGradeSheetT.get(studentGradeSheetT.size()-4).getGrade())!=4.0){

                studentGradeSheetT.get(studentGradeSheetT.size()-1).upgradeOneGrade();
                finalResult[0] = studentGradeSheetT.get(studentGradeSheetT.size()-1);
                analyzeCGPA = lscalcualteCGPA(studentGradeSheetT);
                if (analyzeCGPA>=TARGETCGPA){
                    decision=1;
                    break;
                }

                studentGradeSheetT.get(studentGradeSheetT.size()-2).upgradeOneGrade();
                finalResult[1] = studentGradeSheetT.get(studentGradeSheetT.size()-2);
                analyzeCGPA = lscalcualteCGPA(studentGradeSheetT);
                if (analyzeCGPA>=TARGETCGPA){
                    decision=1;
                    break;
                }

                studentGradeSheetT.get(studentGradeSheetT.size()-3).upgradeOneGrade();
                finalResult[2] = studentGradeSheetT.get(studentGradeSheetT.size()-3);
                analyzeCGPA = lscalcualteCGPA(studentGradeSheetT);
                if (analyzeCGPA>=TARGETCGPA){
                    decision=1;
                    break;
                }

                studentGradeSheetT.get(studentGradeSheetT.size()-4).upgradeOneGrade();
                finalResult[3] = studentGradeSheetT.get(studentGradeSheetT.size()-4);
                analyzeCGPA = lscalcualteCGPA(studentGradeSheetT);
                if (analyzeCGPA>=TARGETCGPA){
                    decision=1;
                    break;
                }

            }
            if (decision==0
                    && Float.parseFloat(finalResult[0].getGrade())==4.0
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


            if (finalResult[0]!=null){
                rows(finalResult);
            }
            //finalResult=null;
            tarCGPA.setText("");

        }else {
            //Toast.makeText(getApplicationContext(),"we use online Database.So thatPlease check your Internet connection.And Press again on our ANALYZE button.",Toast.LENGTH_LONG).show();
        }




    }


    //list to array
    public ListItem[] listToArray(List<ListItem> listItems){
        ListItem[] stlistItems = new ListItem[listItems.size()];

        for(int i = 0; i < listItems.size(); i++) {
            stlistItems[i] = listItems.get(i);
        }
        return stlistItems;
    }
    //list to array




    //list to array
    public List<ListItem> arrayToList(ListItem[] listItems){
        List<ListItem> stlistItems = new ArrayList<>();

        stlistItems.addAll(Arrays.asList(listItems));
        return stlistItems;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_only:
                if (!tarCGPA.getText().toString().isEmpty()) {
                    double TARGET_CGPA =Double.parseDouble(tarCGPA.getText().toString()) ;
                    onNEWonly(TARGET_CGPA);
                    tarCGPA.setText("");
                    break;


                }else {
                    Toast.makeText(getApplicationContext(),"EMPTY",Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.retake_only:
                if (!tarCGPA.getText().toString().isEmpty()) {
                    double TARGET_CGPA =Double.parseDouble(tarCGPA.getText().toString()) ;
                    onRETAKEonly(TARGET_CGPA);
                    tarCGPA.setText("");
                    break;
                }else {
                    Toast.makeText(getApplicationContext(),"EMPTY",Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.re_and_new:
                if (!tarCGPA.getText().toString().isEmpty()) {
                    double TARGET_CGPA =Double.parseDouble(tarCGPA.getText().toString()) ;
                    onNEWandRETAKE(TARGET_CGPA);
                    tarCGPA.setText("");
                    break;
                }else {
                    Toast.makeText(getApplicationContext(),"EMPTY",Toast.LENGTH_SHORT).show();
                    break;
                }
        }

    }
    //list to array







}
