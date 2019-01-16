package rabi.com.cgpagarbage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ImageView circle=(ImageView)findViewById(R.id.circle);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);
       // final Animation an = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fa);

        circle.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {


                finish();
                Intent intent = new Intent(getApplicationContext(), PaasswordActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
