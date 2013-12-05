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

    private class ReadFromJSON extends AsyncTask <Void, Void, String> {

        @Override
        protected String doInBackground(Void... v) {

            String jsonString = null;
            try {
                //Log.d(TAG, getActivity().getFilesDir() + "data.json");
                File cacheFile = new File(getActivity().getFilesDir() , "data.json");

                BufferedReader br = new BufferedReader(new FileReader(cacheFile));
                jsonString = br.readLine();

            } catch (Exception e){
                e.printStackTrace();
            }

            return jsonString;
        }

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