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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    private int sNumber;
    public ArrayList<String> urlArray = new ArrayList<String>();


    View rootView;

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
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//        textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));

        updateView();

        sNumber = getArguments().getInt(ARG_SECTION_NUMBER);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private static final String NEWSFEED_NAME = "summary";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_TITLE = "title";
    private static final String TAG_SOURCE = "source";
    private static final String TAG_SUMMARY = "summary";
    private static final String TAG_LINK = "link";
    private static final String TAG_DATE = "date";

    public void updateView() {
        new ReadFromJSON().execute();
    }


    private class ReadFromJSON extends AsyncTask<Void, Void, ArrayList<JSONObject>> {

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... v) {

            String jsonString = "";

            try {
                //Log.d(TAG, getActivity().getFilesDir() + "data.json");
                File cacheFile = new File(getActivity().getFilesDir(), "data.json");

                BufferedReader br = new BufferedReader(new FileReader(cacheFile));
                jsonString = br.readLine();

            } catch (Exception e) {
                e.printStackTrace();
                if (Utils.isNetworkConnected(getActivity())) {
                    new UpdateNews(getActivity(), true, MainFragment.this).execute();
                } else {
                    Toast.makeText(getActivity(), "Please turn on your internet connection!", Toast.LENGTH_SHORT).show();
                }

            }

            ArrayList<JSONObject> selectedCategoryList = new ArrayList<JSONObject>();

            //Log.d(TAG, "jsonString" + jsonString);
            try {
                JSONObject mJSONObject = new JSONObject(jsonString);
                JSONArray jsonArray = mJSONObject.getJSONArray(NEWSFEED_NAME);

                for (int i = 1; i < jsonArray.length(); ++i) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    if (obj.getString(TAG_CATEGORY).equals(Utils.categoryMap[sNumber - 1])) {
                        selectedCategoryList.add(obj);
                        urlArray.add(obj.getString(TAG_LINK));
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return selectedCategoryList;
        }

        /* TODO:

            Make the listview better.
            Set onItemClickListener on the listview which will display the summary of the news.
         */

        protected void onPostExecute(ArrayList<JSONObject> mArrayList) {

            final ArrayList<JSONObject> fArrayList = mArrayList;

            ListView mLisView = (ListView) rootView.findViewById(R.id.list_news);
            mLisView.setAdapter(new ListViewLoader(mArrayList));
            mLisView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    try {
                        TextView summaryTitle = new TextView(getActivity());
                        summaryTitle.setText(fArrayList.get(i).getString(TAG_TITLE));
                        summaryTitle.setTextSize(16);
                        summaryTitle.setPadding(8, 8, 8, 8);
                        summaryTitle.setGravity(Gravity.CENTER);
                        summaryTitle.setTextColor(Color.GRAY);
                        AlertDialog summaryBox = new AlertDialog.Builder(getActivity())
                                .setCustomTitle(summaryTitle)
                                .setMessage(fArrayList.get(i).getString(TAG_SUMMARY))
                                .setPositiveButton("Instapaper", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent instapaper = new Intent(getActivity(), InstapaperViewer.class);
                                        instapaper.putExtra("URLID", i);
                                        instapaper.putExtra("URLList", urlArray);
                                        startActivity(instapaper);

                                    }
                                })
                                .setNeutralButton("Listen", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent tts = new Intent(getActivity(), TTS.class);
                                        try {
                                            tts.putExtra("TEXT", fArrayList.get(i).getString(TAG_SUMMARY));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        startActivity(tts);


                                    }
                                })
                                .setNegativeButton("Source", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        /* TODO :
                                            USE webview to implement in-app browser instead of this
                                         */
                                        String url = "http://www.google.com";
                                        try {
                                            url = fArrayList.get(i).getString(TAG_LINK);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                        startActivity(browserIntent);


                                    }
                                })
                                .show();
                        TextView summaryText = (TextView) summaryBox.findViewById(android.R.id.message);
                        summaryText.setTextSize(12);
                        if(!Utils.isBelowHolo())
                            summaryText.setTextIsSelectable(true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    private class ListViewLoader extends BaseAdapter {

        private ArrayList<JSONObject> selectedCategoryList;

        public ListViewLoader(ArrayList<JSONObject> selectedCategoryList) {

            this.selectedCategoryList = selectedCategoryList;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            //return 0;
            return selectedCategoryList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.row_list, null);
            } else {
                rowView = convertView;
            }

            LinearLayout newsItem = (LinearLayout) rowView.findViewById(R.id.news_item);
            TextView newsTitle = (TextView) rowView.findViewById(R.id.title_news);
            newsTitle.setTextColor(getResources().getColor(R.color.dark_teal));
            TextView sourceName = (TextView) rowView.findViewById(R.id.source_news);
            TextView newsDate = (TextView) rowView.findViewById(R.id.date_news);
            newsItem.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            newsItem.setPadding(5, 10, 5, 10);
            sourceName.setTextColor(getResources().getColor(R.color.colorAccent));
            sourceName.setTextSize(10);
            newsDate.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            newsDate.setTextSize(10);
            try {
                newsTitle.setText(selectedCategoryList.get(position).getString(TAG_TITLE));
                sourceName.setText(selectedCategoryList.get(position).getString(TAG_SOURCE));
                newsDate.setText(selectedCategoryList.get(position).getString(TAG_DATE));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return rowView;
        }

    }
}
