/*
 * Copyright (C) 2013
 *     Arnav Gupta
 *     Saurav Tomar
 *     Umair Khan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package in.ac.dtu.subtlenews;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by omerjerk on 13/12/13.
 */
public class NewsAutoRefresh extends BroadcastReceiver {

    //The default constructor
    public NewsAutoRefresh() {
    }

    //This constructor is called by the MainActivity
    public NewsAutoRefresh(Context context, int timeoutInSeconds, int interval) {
        AlarmManager alarmMgr =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NewsAutoRefresh.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.SECOND, timeoutInSeconds);

        //It'll repeat every 5 hours
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), interval,
                pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Utils.isNetworkConnected(context)) {
            new UpdateNews(context, true).execute();
        }
    }
}
