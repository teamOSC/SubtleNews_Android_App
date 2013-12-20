package in.ac.dtu.subtlenews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by saurav on 19/12/13.
 */
public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle foo){
        super.onCreate(foo);
        setContentView(R.layout.splash);

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(4000);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    Intent openStartingPoint = new Intent("in.ac.dtu.subtlenews.MAINACTIVITY");
                    startActivity(openStartingPoint);
                }

            }
        };

        timer.start();

    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
