package rabi.com.cgpagarbage;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PaasswordActivity extends AppCompatActivity implements  View.OnKeyListener {

    EditText pass_1,pass_2,pass_3,pass_4;
    TextView error_txt,hint_txt;
    MyDBHandler mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_activity);




        // for textFields
        pass_1 = (EditText)findViewById(R.id.pass1);
        pass_2 = (EditText)findViewById(R.id.pass2);
        pass_3 = (EditText)findViewById(R.id.pass3);
        pass_4 = (EditText)findViewById(R.id.pass4);

        //for textViews
        error_txt = (TextView)findViewById(R.id.error);
        hint_txt = (TextView)findViewById(R.id.hint_pass);

        //database
        mydb = new MyDBHandler(this);

        //isPass 5555
        if ( mydb.isPassword5555() ){
            Toast.makeText(this, "Please change your password after login", Toast.LENGTH_SHORT).show();

        }else {
            hint_txt.setText("");
        }


        pass_1.setOnKeyListener(this);
        pass_2.setOnKeyListener(this);
        pass_3.setOnKeyListener(this);
        pass_4.setOnKeyListener(this);






    }
    protected boolean checkPass(String ispass){
        Cursor cursor = mydb.checkPassword(ispass);
        //            Toast.makeText(this, "PASS FOUND", Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "NO PASS FOUND", Toast.LENGTH_SHORT).show();
        return cursor.getCount() == 1;
    }



    public void login(){
        String ps1,ps2,ps3,ps4;
        ps1 = pass_1.getText().toString();
        ps2 = pass_2.getText().toString();
        ps3 = pass_3.getText().toString();
        ps4 = pass_4.getText().toString();

        String pass = ps1+ps2+ps3+ps4;


        if (checkPass(pass) ){
            if ( pass.equals("5555") ){
                Toast.makeText(this, "Please change your password now ...", Toast.LENGTH_LONG).show();
            }

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            this.finish();
            startActivity(intent);
        }else {
            error_txt.setText("sorry try again !!! ");
            pass_1.setText("");
            pass_2.setText("");
            pass_3.setText("");
            pass_4.setText("");

        }


    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ( pass_1.getText().length() == 1 ){
            pass_2.requestFocus();
        }
        if ( pass_2.getText().length() == 1 ){
            pass_3.requestFocus();
        }
        if ( pass_3.getText().length() == 1 ){
            pass_4.requestFocus();
        }
        if ( pass_4.getText().length()== 1 ){
            login();
            pass_1.requestFocus();

        }
        return false;
    }
}
