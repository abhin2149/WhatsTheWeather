package com.example.abhinav.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText city;
    Button b1;
    TextView textView;

    public class DownloadContent extends AsyncTask<String,Void,String> {

        URL url=null;
        String result="";
        HttpURLConnection connection;


        @Override
        protected String doInBackground(String... strings) {

            try {
                url=new URL(strings[0]);

                connection=(HttpURLConnection)url.openConnection();

                InputStream is=connection.getInputStream();

                InputStreamReader reader=new InputStreamReader(is);

                int data=reader.read();

                while(data!=-1){

                    char b=(char)data;

                    result+=b;


                    data=reader.read();

                }

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String result="";

            try {

                if(s!=null) {
                    JSONObject jsonObject = new JSONObject(s);

                    String weatherInfo = jsonObject.getString("weather");

                    Log.i("Weather info", weatherInfo);

                    JSONArray arr = new JSONArray(weatherInfo);

                    for (int i = 0; i < arr.length(); i++) {

                        JSONObject jsonpart = arr.getJSONObject(i);

                        result += jsonpart.getString("main") + "\n";

                        Log.i("main", jsonpart.getString("main"));


                        result += jsonpart.get("description") + "\n";


                        Log.i("des", jsonpart.getString("description"));

                    }

                    Log.i("final", result);
                }


                textView.setText(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void check(View v){

        String cityName=city.getText().toString();

        InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(city.getWindowToken(),0);

        DownloadContent content=new DownloadContent();

        content.execute("http://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid=abfe4417a13985e99cc426343b7d43fd");





    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city=(EditText)findViewById(R.id.editText);
        b1=(Button)findViewById(R.id.button);
        textView=(TextView)findViewById(R.id.textView);

    }
}
