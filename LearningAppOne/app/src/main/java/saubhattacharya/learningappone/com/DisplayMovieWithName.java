package saubhattacharya.learningappone.com;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DisplayMovieWithName extends AppCompatActivity {

    String over_view,release_date,original_title,vote_avg;
    ArrayList<String> movieList = new ArrayList<>();
    HashMap<String,ArrayList<String>> movieListChild = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie_with_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        ExpandableListView expandableListView = (ExpandableListView)findViewById(R.id.expListView);
        //TypedArray expandableListViewStyle = this.getTheme().obtainStyledAttributes(new int[]{android.R.attr.expandableListViewStyle});
        //TypedArray groupIndicator = this.getTheme().obtainStyledAttributes(expandableListViewStyle.getResourceId(0,0),new int[]{android.R.attr.groupIndicator});
        //expandableListView.setGroupIndicator(groupIndicator.getDrawable(0));
        //expandableListView.setGroupIndicator(android.R.drawable.arrow_down_float);

        String str_jsonresponse = extras.getString(MainActivity.STR_JSONRESPONSE).toString();

        try{
            JSONObject root_jsonObject = new JSONObject(str_jsonresponse);
            JSONArray jsonArray1 = root_jsonObject.optJSONArray("results");
            for(int i=0;i<jsonArray1.length();i++)
            {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                over_view = jsonObject1.getString("overview");
                release_date = jsonObject1.getString("release_date");
                release_date = "Release Date: " + release_date;
                original_title = jsonObject1.getString("original_title");
                vote_avg = jsonObject1.getString("vote_average");
                vote_avg = "Avg. Vote: " + vote_avg;

                movieList.add(original_title);

                ArrayList<String> movieListChildItem = new ArrayList<>();
                movieListChildItem.add(vote_avg);
                movieListChildItem.add(release_date);
                movieListChildItem.add(over_view);

                movieListChild.put(movieList.get(i),movieListChildItem);
            }

            ExpMovieAdapter movieAdapter = new ExpMovieAdapter(movieList,movieListChild,DisplayMovieWithName.this);
            expandableListView.setAdapter(movieAdapter);

            Toast.makeText(DisplayMovieWithName.this, "Tap on each movie name to know more!", Toast.LENGTH_SHORT).show();
        }
        catch(JSONException JE)
        {
            JE.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onBackPressed(){
        Intent tohome = new Intent(this, MainActivity.class);
        tohome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(tohome);
        finish();
    }

}
