package in.ac.dtu.subtlenews;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by omerjerk on 13/12/13.
 */
public class NewsAutoRefresh extends BroadcastReceiver {

    //The default constructor
    public NewsAutoRefresh() {}

    //This constructor is called by the MainActivity
    public NewsAutoRefresh(Context context, Bundle extras, int timeoutInSeconds){
        AlarmManager alarmMgr =
                (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NewsAutoRefresh.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.SECOND, timeoutInSeconds);

        //It'll repeat every 5 hours
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), 5 * 60 * 60 * 1000,
                pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(Utils.isNetworkConnected(context)){
            new UpdateNews(context).execute();
        }
    }
}
