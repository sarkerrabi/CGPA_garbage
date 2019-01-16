package rabi.com.cgpagarbage;

/**
 * Created by RABI on 15-Mar-16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;


/**
 * Created by RABI on 04-Feb-16.
 */

public class MyDBHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myusers.db";


    //tables
    public static final String TABLE_STUDENTS = "students";
    public static final  String TABLE_PASSWORD = "passwords";

    //columns for password table
    public static final String COLUMN_passID = "id";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_PASS = "pass";


    //column for students table
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_COURSE = "course";
    public static final String COLUMN_CREDIT = "credit" ;
    public static final String COLUMN_GRADE = "grade" ;

    //Create Tables
    public static String createStudents = "create table " + TABLE_STUDENTS +
            " (" +COLUMN_ID + " integer primary key autoincrement, "
            +COLUMN_COURSE + " varchar(30) not null , "
            +COLUMN_CREDIT + " float not null, "
            +COLUMN_GRADE + " float not null"
            + " );";
    public static String createPasstable = "create table "+ TABLE_PASSWORD+
            " (" + COLUMN_passID + " integer primary key autoincrement, "
            + COLUMN_CODE + " varchar not null, "
            + COLUMN_PASS + " varchar not null "
            + ");";
    public static String addPass = "INSERT INTO " +
            TABLE_PASSWORD+"( `code`, `pass`) VALUES " +
            "('usr',5555)";







    public MyDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createStudents);
        db.execSQL(createPasstable);
        db.execSQL(addPass);



    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORD);

        onCreate(db);
    }

    //Add a new row to the database
    public boolean addCourse(Course course){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(COLUMN_COURSE, course.get_courseName());
        values.put(COLUMN_CREDIT, course.get_credit());
        values.put(COLUMN_GRADE, course.get_gpa());




        long checker = db.insert(TABLE_STUDENTS, null, values);
        return checker != -1;
        // db.close();
    }//Add a new row to the database




    //update course from database
    public boolean updateCourse(Course course){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_COURSE, course.get_courseName());
        values.put(COLUMN_CREDIT, course.get_credit());
        values.put(COLUMN_GRADE, course.get_gpa());
        sqLiteDatabase.update(TABLE_STUDENTS,values,"course = ?", new String[]{course.get_courseName()});
        return true;

    }//update course from database





    //change password starts
    public boolean changePassword(String newPass){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE,"usr");
        values.put(COLUMN_PASS,newPass);
        long checker = sqLiteDatabase.update(TABLE_PASSWORD,values,"code = ?",new String[]{"usr"});
        return checker != -1;
    }//change password ends



    //search course from database
    public Cursor searchCourse(Course course){

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_STUDENTS+ " WHERE "+COLUMN_COURSE +" = '"
                +course.get_courseName()+ "' ;";

        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);

        return cursor;
    }//search course from database end

    //check password starts
    public Cursor checkPassword(String pass){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_PASSWORD+ " WHERE "+COLUMN_PASS +" = '"
                +pass+ "' ;";

        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);

        return cursor;
    }

    //check password end


    //check password starts 5555
    public boolean isPassword5555(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_PASSWORD+ " WHERE "+COLUMN_PASS +" = '5555' ;";

        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        return cursor.getCount() == 1;

//        return cursor;
    }

    //check password end 5555


    //delete all data from database
    public void deleteALLcourse(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_STUDENTS,null,null);
        db.close();
    }
    //delete all data from database


    //Delete a course from the database
    public int deleteCourse(Course course){
        SQLiteDatabase db = getWritableDatabase();

       return db.delete(TABLE_STUDENTS,"course = ?",new String[]{course.get_courseName()});
    }//Delete a course from the database


    //list of data on table
    public Cursor display(){

        SQLiteDatabase db = getWritableDatabase();

        Cursor c ;
        c= db.rawQuery( "SELECT * FROM "+ TABLE_STUDENTS,null);

        return c;


    }//list of data on table


    // total credit counting
    public float sumCredit(Cursor cursor){
        String sql = "select sum(credit) from students;";
        SQLiteDatabase db = getWritableDatabase();
        float sum = 0;
        cursor = db.rawQuery(sql,null);
        if(cursor!=null){
            try{
                if(cursor.moveToFirst()){
                    sum = cursor.getFloat(0);

                }
            }finally {
                cursor.close();
            }
        }
    return sum;
    }// total credit counting


    // CGPA calculating
    public float calCGPA(Cursor cursor){
        String sql ="select sum(credit*grade)/sum(credit) from students";
        SQLiteDatabase db = getWritableDatabase();
        float ccgpa=0;
        cursor = db.rawQuery(sql,null);
        if(cursor!=null){
            try {
                if (cursor.moveToFirst()){
                    ccgpa = cursor.getFloat(0);
                }
            }finally {
                cursor.close();
            }
        }
        return ccgpa;
    }// CGPA calculating

    //Get sorted grade-sheet and sorted with grades starts

    public Cursor getSortedGradelist(){
        SQLiteDatabase db = getWritableDatabase();

        Cursor c ;
        //c= db.rawQuery( "SELECT * FROM "+ TABLE_STUDENTS+ "",null);

        String[] tableStudentsColumns = {COLUMN_ID,COLUMN_COURSE,COLUMN_CREDIT,COLUMN_GRADE};

        c= db.query(TABLE_STUDENTS,tableStudentsColumns,null,null,null,null,COLUMN_GRADE+" ASC");

        return c;


    }

    //Get sorted grade-sheet and sorted with grades ends



}
