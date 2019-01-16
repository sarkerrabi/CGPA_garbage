package rabi.com.cgpagarbage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {


    EditText changePass,confirmPass;
    Button changePassword;
    MyDBHandler myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //adfor
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
        //adfor

        //buttons
        changePassword = (Button)findViewById(R.id.Change_password);

        //editTexts

        changePass = (EditText)findViewById(R.id.new_password);
        confirmPass = (EditText)findViewById(R.id.confirm_password);

        changePassword.setOnClickListener(this);
        myDB = new MyDBHandler(this);


    }

    private void ChangePASSWORD() {
        String newPass = changePass.getText().toString();
        String conPass = confirmPass.getText().toString();

        if ( !conPass.equals("") ) {
            if ( newPass.equals(conPass) ) {
                updatePassword(newPass);
            }
        }else {
            Toast.makeText(this,"Password Field is Empty",Toast.LENGTH_SHORT).show();
        }



    }

    private void updatePassword(String newPass) {
        boolean p = myDB.changePassword(newPass);
        if ( p ){
            Toast.makeText(this, "Updated Successfully ", Toast.LENGTH_SHORT).show();
            this.finish();
        }else {
            Toast.makeText(this, "Not Updated", Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Change_password:
                ChangePASSWORD();
                break;

        }
    }

}
