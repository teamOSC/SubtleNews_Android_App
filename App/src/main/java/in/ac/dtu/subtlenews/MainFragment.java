package in.ac.dtu.subtlenews;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by omerjerk on 2/12/13.
 */
public class MainFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "MAIN_FRAGMENT";

    private int sNumber ;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));

        new ReadFromJSON().execute();

        sNumber = getArguments().getInt(ARG_SECTION_NUMBER);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(sNumber);
    }

    private static final String TAG_CATEGORY = "category";
    private static final String TAG_TITLE = "title";
    private static final String TAG_SOURCE = "source";
    private static final String TAG_SUMMARY = "summary";
    private static final String TAG_LINK = "link";
    private static final String TAG_DATE = "date";



    private class ReadFromJSON extends AsyncTask<Void, Void, String[][]> {

        @Override
        protected String[][] doInBackground(Void... v) {

            String jsonString = "";
            JSONObject newsObj = new JSONObject();
            String newsArray[][] = null;
            JSONArray news = null;

            try {
                //Log.d(TAG, getActivity().getFilesDir() + "data.json");
                File cacheFile = new File(getActivity().getFilesDir() , "data.json");

                BufferedReader br = new BufferedReader(new FileReader(cacheFile));
                jsonString = br.readLine();

            } catch (Exception e){
                e.printStackTrace();
            }

            try {
                newsObj = new JSONObject(jsonString);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            try {
                news = newsObj.getJSONArray("JSON");
                for (int i = 1; i < news.length(); i++) {
                    JSONObject n = news.getJSONObject(i);
                    newsArray[i][0] = n.getString(TAG_CATEGORY);
                    newsArray[i][1] = n.getString(TAG_DATE);
                    newsArray[i][2] = n.getString(TAG_TITLE);
                    newsArray[i][3] = n.getString(TAG_SOURCE);
                    newsArray[i][4] = n.getString(TAG_SUMMARY);
                    newsArray[i][5] = n.getString(TAG_LINK);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }




            return newsArray;
        }

        /* TODO:

            Inside the post execute method we need to populate the view which consists of
            news items.
            For now we have got a 2d array of strings, with newsArray[3][x] means we are
            speaking of 3rd news item.
            Furthen inside each news item, the newsArray[3][1] is category and then
            newsArray[3][2] is date and so on.
         */

        protected void onPostExecute(String jsonString){

            ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();

            Log.d(TAG, "jsonString" + jsonString);
            try {
                JSONArray jsonArray = new JSONArray(jsonString);

                for(int i = 0; i < jsonArray.length(); ++i){
                    JSONObject obj = jsonArray.getJSONObject(i);

                    if(obj.getString("category").equals(Utils.categoryMap[sNumber - 1])){
                        jsonList.add(obj);
                    }

                }

                for(JSONObject lol : jsonList){
                    Log.d(TAG, lol.getString("category"));
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}