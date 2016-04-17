package rabi.com.cgpagarbage;

/**
 * Created by RABI on 15-Mar-16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by RABI on 04-Feb-16.
 */

public class MyDBHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myusers.db";


    //tables
    public static final String TABLE_STUDENTS = "students";
    public static final String TABLE_CURSEMESTER = "currentSemester";



    //column for students table
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COURSE = "course";
    public static final String COLUMN_CREDIT = "credit" ;
    public static final String COLUMN_GRADE = "grade" ;

    //Create Tables
    public static String createStudents = "create table " + TABLE_STUDENTS +
            " (" +COLUMN_ID + " integer primary key autoincrement, "
            +COLUMN_COURSE + " varchar not null , "
            +COLUMN_CREDIT + " integer not null, "
            +COLUMN_GRADE + " float not null"
            +");";

public static String createCursemester = "create table " + TABLE_CURSEMESTER +
            " (" +COLUMN_ID + " integer primary key autoincrement, "
            +COLUMN_COURSE + " varchar not null , "
            +COLUMN_CREDIT + " integer not null, "
            +");";





    public MyDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createStudents);
        db.execSQL(createCursemester);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURSEMESTER);
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
        if(checker==-1){
            return false;
        }else{
            return true;
        }
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


    //Delete a course from the database
    public int deleteCourse(Course course){
        SQLiteDatabase db = getWritableDatabase();

       return db.delete(TABLE_STUDENTS,"course = ?",new String[]{course.get_courseName()});
    }//Delete a course from the database

    public Cursor display(){

        SQLiteDatabase db = getWritableDatabase();

        Cursor c ;
        c= db.rawQuery( "SELECT * FROM "+ TABLE_STUDENTS,null);

        return c;
    }


    // total credit counting
    public int sumCredit(Cursor cursor){
        String sql = "select sum(credit) from students;";
        SQLiteDatabase db = getWritableDatabase();
        int sum = 0;
        cursor = db.rawQuery(sql,null);
        if(cursor!=null){
            try{
                if(cursor.moveToFirst()){
                    sum = cursor.getInt(0);

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

    public boolean checkCourseName(String course){
      //  String sql ="select * from students where "+COLUMN_COURSE+" = "+course+" ;" ;
    //    SQLiteDatabase db = getWritableDatabase();
     //   String naa = null;
      //  Cursor cursor = db.rawQuery(sql,null);
//


        return true;
    }



}
