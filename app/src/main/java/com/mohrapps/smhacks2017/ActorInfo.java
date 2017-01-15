package com.mohrapps.smhacks2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ActorInfo extends AppCompatActivity {

    ImageView actorPictureView;
    TextView actorNameView;
    TextView actorLinkView;
    ListView moviesListView;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_info);

        Intent intent = getIntent();
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("jsonData"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        actorLinkView = (TextView)findViewById(R.id.actorIMDBLink);
        actorNameView = (TextView)findViewById(R.id.ActorName);
        actorPictureView = (ImageView)findViewById(R.id.actorPicture);
        moviesListView = (ListView)findViewById(R.id.moviesList);

    }


}
