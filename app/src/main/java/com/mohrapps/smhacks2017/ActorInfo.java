package com.mohrapps.smhacks2017;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActorInfo extends AppCompatActivity {

    ImageView actorPictureView;
    TextView actorNameView;
    TextView actorLinkView;
    ListView moviesListView;
    String actorName;
    JSONObject jsonObject;
    String TAG = "ActorInfo()";
    TextView bioView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_info);
        actorLinkView = (TextView) findViewById(R.id.actorIMDBLink);
        actorNameView = (TextView) findViewById(R.id.ActorName);
        actorPictureView = (ImageView) findViewById(R.id.actorPicture);
        moviesListView = (ListView) findViewById(R.id.moviesList);
        bioView = (TextView)findViewById(R.id.biotextView);
        Intent intent = getIntent();
        actorName = intent.getStringExtra("name");
        if (actorName != null) {
            actorName = actorName.replace(' ', '+');
            try {
                new thisAsyncTask().execute(new URL("http://www.myapifilms.com/imdb/idIMDB?name=" + actorName + "&token=6282a924-e31a-49af-b9da-96bebca9bedc&format=json&language=en-us&filmography=1"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        } else {
            actorNameView.setText("Sorry, I do not recognize that actor.");
        }


    }

    public void setUpLayout(String jsonStr) throws JSONException {

        if (jsonStr != null) {

            JSONObject actorJSON = new JSONObject(jsonStr);
            actorNameView.setText(actorJSON.getJSONObject("data").getJSONArray("names").getJSONObject(0).getString("name"));
            actorLinkView.setText(actorJSON.getJSONObject("data").getJSONArray("names").getJSONObject(0).getString("urlIMDB"));
            String tempUrl = actorJSON.getJSONObject("data").getJSONArray("names").getJSONObject(0).getString("urlPhoto");
            Picasso.with(ActorInfo.this).load(tempUrl).into(actorPictureView);
            bioView.setText(actorJSON.getJSONObject("data").getJSONArray("names").getJSONObject(0).getString("bio"));
            List<String> list = new ArrayList<String>();
            JSONArray array = actorJSON.getJSONObject("data").getJSONArray("names").getJSONObject(0).getJSONArray("filmographies").getJSONObject(0).getJSONArray("filmography");
            for(int i = 0 ; i < array.length() ; i++){
                list.add(array.getJSONObject(i).getString("title"));
            }
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
            moviesListView.setAdapter(itemsAdapter);
        } else {
            Log.d(TAG, "jsonObject = null");
        }

    }


    class thisAsyncTask extends AsyncTask<URL, Void, Long> {
        private String result;

        @Override
        protected Long doInBackground(URL... urls) {
            int count = urls.length;
            long totalSize = 0;
            StringBuilder resultBuilder = new StringBuilder();
            for (int i = 0; i < count; i++) {
                try {
                    // Read all the text returned by the server
                    InputStreamReader reader = new InputStreamReader(urls[i].openStream());
                    Log.d(TAG, "past input reader");
                    BufferedReader in = new BufferedReader(reader);
                    Log.d(TAG, "past buffer reader");
                    String resultPiece;
                    while ((resultPiece = in.readLine()) != null) {
                        resultBuilder.append(resultPiece);
                    }
                    in.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // if cancel() is called, leave the loop early
                if (isCancelled()) {
                    break;
                }
            }
            // save the result
            this.result = resultBuilder.toString();
            Log.d(TAG, result);
            return totalSize;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            try {
                setUpLayout(this.result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
