package saubhattacharya.learningappone.com;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    RadioButton radio1,radio2,radio3;
    EditText editText1,editText2,editText3;
    LinearLayout Layout1,Layout2,Layout3;
    TextInputLayout textInputLayout1,textInputLayout2;
    TextInputLayout textInputLayout3;
    String searchby = "name";
    String movie_name,movie_rel_year,movie_rating,str_jsonresponse,data;
    URL url;
    public final static String STR_JSONRESPONSE = "saubhattacharya.learningappone.com.STR_JSONRESPONSE";
    public final static String URL = "saubhattacharya.learningappone.com.URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radio_grp1);
        radioGroup.check(R.id.radio1);

        /*final LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.ll1);
        final EditText editText1 = new EditText(getApplicationContext());
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        editText1.setLayoutParams(layoutParams);
        linearLayout1.addView(editText1);*/

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        try{
            textInputLayout1 = new TextInputLayout(this);
            textInputLayout1.setLayoutParams(layoutParams);
            textInputLayout2 = new TextInputLayout(this);
            textInputLayout2.setLayoutParams(layoutParams);
            textInputLayout3 = new TextInputLayout(this);
            textInputLayout3.setLayoutParams(layoutParams);
            editText1 = new EditText(getApplicationContext());
            editText1.setLayoutParams(layoutParams);
            editText1.setHint("Search by Name:");
            editText2 = new EditText(getApplicationContext());
            editText2.setLayoutParams(layoutParams);
            editText2.setHint("Search by Release Year:");
            editText3 = new EditText(getApplicationContext());
            editText3.setLayoutParams(layoutParams);
            editText3.setHint("Search by Rating:");

            textInputLayout1.addView(editText1);
            textInputLayout2.addView(editText2);
            textInputLayout3.addView(editText3);

            radio1 = (RadioButton)findViewById(R.id.radio1);
            radio2 = (RadioButton)findViewById(R.id.radio2);
            radio3 = (RadioButton)findViewById(R.id.radio3);

            Layout1 = (LinearLayout) findViewById(R.id.ll1);
            Layout2 = (LinearLayout) findViewById(R.id.ll2);
            Layout3 = (LinearLayout) findViewById(R.id.ll3);

            Layout1.addView(textInputLayout1);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    if(checkedId == radio1.getId()) {
                        Layout2.removeView(textInputLayout2);
                        Layout3.removeView(textInputLayout3);
                        Layout1.addView(textInputLayout1);
                        searchby = "name";
                    }

                    if(checkedId == radio2.getId()) {
                        Layout1.removeView(textInputLayout1);
                        Layout3.removeView(textInputLayout3);
                        Layout2.addView(textInputLayout2);
                        searchby = "rel_year";
                    }

                    if(checkedId == radio3.getId()) {
                        Layout2.removeView(textInputLayout2);
                        Layout1.removeView(textInputLayout1);
                        Layout3.addView(textInputLayout3);
                        searchby = "rating";
                    }
                }
            });
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void OnQueryButtonClicked (View view)
    {
        try{
            if(searchby.equals("name"))
            {
                movie_name = editText1.getText().toString();
                url = new URL("https://api.themoviedb.org/3/search/movie");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("api_key", "2746c770523806d2bb8972e136bc5bcb")
                        .appendQueryParameter("query", movie_name);
                data = builder.build().getEncodedQuery();
            }
            else if(searchby.equals("rel_year"))
            {
                movie_rel_year = editText2.getText().toString();
                url = new URL("https://api.themoviedb.org/3/discover/movie?api_key=2746c770523806d2bb8972e136bc5bcb&primary_release_year="+movie_rel_year+"&original_language=en&sort_by=release_date.desc");
            }
            else if(searchby.equals("rating"))
            {
                movie_rating = editText3.getText().toString();
                url = new URL("https://api.themoviedb.org/3/discover/movie?api_key=2746c770523806d2bb8972e136bc5bcb&vote_average.gte="+movie_rating+"&original_language=en&sort_by=release_date.desc");
            }

            MakeRequestTask queryTask = new MakeRequestTask();
            queryTask.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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

                if(data != null) {
                    OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream());
                    output.write(data);
                    output.flush();
                    output.close();
                }

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
                    Toast.makeText(MainActivity.this,
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
            Toast.makeText(MainActivity.this,
                    "Please wait..while we bring you the movie details!", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            if(searchby.equals("name"))
            {
                Intent intent = new Intent(getApplicationContext(),DisplayMovieWithName.class);
                Bundle extras = new Bundle();
                extras.putString(STR_JSONRESPONSE, str_jsonresponse);
                intent.putExtras(extras);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(getApplicationContext(),DisplayMovieActivity.class);
                Bundle extras = new Bundle();
                extras.putString(STR_JSONRESPONSE, str_jsonresponse);
                extras.putString(URL,url.toString());
                intent.putExtras(extras);
                startActivity(intent);
            }
        }
    }
}
