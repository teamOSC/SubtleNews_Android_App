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

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by omerjerk on 2/12/13.
 */
public class UpdateNews extends AsyncTask <Void, Void, String> {

    private Context context;
    private Boolean ranAutomatically;
    private MainFragment mainFragment = null;

    public UpdateNews(Context context, Boolean ranAutomatically){
        this(context, ranAutomatically, null);
    }

    public UpdateNews(Context context, Boolean ranAutomatically, MainFragment mainFragment){

        this.context = context;
        this.ranAutomatically = ranAutomatically;

        if(mainFragment != null){
            this.mainFragment = mainFragment;
        }

        Log.d("Update News", "Inside the constructor of UpdateNews class");
        if (!ranAutomatically) {
            ((Activity)context).setProgressBarIndeterminateVisibility(true);
        }
    }

    @Override
    protected String doInBackground(Void... v){

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet("http://162.243.238.19/sauravtom/summary.txt"));
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (Exception e) {
            ((Activity)context).setProgressBarIndeterminateVisibility(false);
            Log.d("[GET REQUEST]", "Network exception", e);
            return null;
        }
    }

    protected void onPostExecute(String r) {

        if (!ranAutomatically) {
            ((Activity)context).setProgressBarIndeterminateVisibility(false);

            // mainFragment will never be null when ranAutomatically is false
            if(mainFragment == null){
                Log.d("UpdateNews", "This fucking thing is null");
            } else {
                mainFragment.updateView();
            }

        }

        Log.d("[GET RESPONSE]", r);
        File cacheFile = new File(context.getFilesDir(), "data.json");


        BufferedWriter bw = null;
        try {
            if (!cacheFile.exists()) {
                cacheFile.createNewFile();
            }

            FileWriter fw = new FileWriter(cacheFile.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(r);

            Toast.makeText(context, "News refreshed!", Toast.LENGTH_SHORT).show();

        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Sorry! Something went wrong.", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
                //Should never happen
            }

        }

    }
}
