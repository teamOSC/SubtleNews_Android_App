package in.ac.dtu.subtlenews;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

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

    public UpdateNews(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... v){

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet("http://subtlenews.appspot.com/.json"));
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (Exception e) {
            Log.d("[GET REQUEST]", "Network exception", e);
            return null;
        }
    }

    protected void onPostExecute(String r) {

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

        } catch (Exception e){
            e.printStackTrace();
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
