package saubhattacharya.learningappone.com;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class DisplayMovieActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    String[] poster_path = new String[1000];
    String[] over_view = new String[1000];
    String[] release_date = new String[1000];
    String[] original_title = new String[1000];
    String[] vote_avg = new String[1000];
    ArrayList<ListRowItem> movieList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    URL url;
    URL[] posterURL = new URL[1000];
    String str_jsonresponse = null;
    RecyclerView recyclerView;
    String pURL1 = "http://image.tmdb.org/t/p/w92";
    String pURLLong;
    Bitmap[] poster_image = new Bitmap[1000];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Movies");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);

        str_jsonresponse = extras.getString(MainActivity.STR_JSONRESPONSE).toString();
        String str_url = extras.getString(MainActivity.URL).toString();
        try
        {
            url = new URL(str_url);
        }
        catch(MalformedURLException mae)
        {
            mae.printStackTrace();
        }

        getImagefromURL(str_jsonresponse);
        //parseOriginalJSON(str_jsonresponse);
    }

    public void getImagefromURL(String json_resp)
    {
        try{
            JSONObject root_jsonObject = new JSONObject(json_resp);
            JSONArray jsonArray1 = root_jsonObject.optJSONArray("results");
            for(int i=0;i<jsonArray1.length();i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                poster_path[i] = jsonObject1.getString("poster_path");

                pURLLong = pURL1 + poster_path[i];
                posterURL[i] = new URL(pURLLong);

                final int j = i;
                GetImageTask getImageTask = new GetImageTask(j);
                getImageTask.execute();

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void parseOriginalJSON(String str_json, Bitmap[] poster, int count)
    {
        try{
            JSONObject root_jsonObject = new JSONObject(str_json);
            JSONArray jsonArray1 = root_jsonObject.optJSONArray("results");
            JSONObject jsonObject1 = jsonArray1.getJSONObject(count);

            over_view[count] = jsonObject1.getString("overview");
            release_date[count] = jsonObject1.getString("release_date");
            original_title[count] = jsonObject1.getString("original_title");
            vote_avg[count] = jsonObject1.getString("vote_average");

            ListRowItem listRowItem = new ListRowItem();
            listRowItem.setPoster(poster[count]);
            listRowItem.setOverview(over_view[count]);
            listRowItem.setReleasedate(release_date[count]);
            listRowItem.setOriginaltitle(original_title[count]);
            listRowItem.setVoteavg(vote_avg[count]);

            movieList.add(listRowItem);

            MovieAdapter movieAdapter = new MovieAdapter(movieList,DisplayMovieActivity.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(DisplayMovieActivity.this, LinearLayoutManager.VERTICAL));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(movieAdapter);
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        MakeRequestTask queryTask = new MakeRequestTask();
        queryTask.execute();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void onBackPressed(){
        Intent tohome = new Intent(this, MainActivity.class);
        tohome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(tohome);
        finish();
    }

    public class MakeRequestTask extends AsyncTask<Void, Integer, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {

            try{

                HttpsURLConnection connection = null;
                connection = (HttpsURLConnection)url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setConnectTimeout(7200000);
                connection.setReadTimeout(720000);
                connection.connect();

                int HttpResult = connection.getResponseCode();

                if(HttpResult == 200) {
                    StringBuilder jsonresponse = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String receiveString = null;

                    while ((receiveString = bufferedReader.readLine()) != null) {
                        jsonresponse.append(receiveString);
                    }

                    str_jsonresponse = jsonresponse.toString();

                    bufferedReader.close();
                }
                else{
                    Toast.makeText(DisplayMovieActivity.this,
                            "Something went wrong here!", Toast.LENGTH_LONG).show();
                }

                connection.disconnect();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            movieList.clear();
            getImagefromURL(str_jsonresponse);
            //parseOriginalJSON(str_jsonresponse);
        }
    }

    public class GetImageTask extends AsyncTask<Void, Integer, Void> {

        int c = 0;

        public GetImageTask(int j)
        {
            this.c = j;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpURLConnection connection = null;
                connection = (HttpURLConnection) posterURL[c].openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setUseCaches(true);
                connection.setConnectTimeout(7200000);
                connection.setReadTimeout(720000);
                connection.connect();

                poster_image[c] = BitmapFactory.decodeResource(getResources(), R.drawable.posternotavlbl);
                poster_image[c] = BitmapFactory.decodeStream(connection.getInputStream());

                connection.disconnect();

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            parseOriginalJSON(str_jsonresponse,poster_image,c);
        }
    }
}
