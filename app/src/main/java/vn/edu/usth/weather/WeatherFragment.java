package vn.edu.usth.weather;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class WeatherFragment extends Fragment {

    private String city;

    public WeatherFragment() {
        // Required empty public constructor
    }

    public static WeatherFragment newInstance(String city) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString("city", city);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout relativeLayout = new RelativeLayout(getActivity());
//        relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorWeather));
        relativeLayout.setBackground(getResources().getDrawable(R.drawable.frame));

        int height = (int) getResources().getDimension(R.dimen.weather_frame_size);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
        params.setMargins(30, 30, 30, 5);

        relativeLayout.setLayoutParams(params);
        relativeLayout.setPadding(120,45,130,45);

        TextView tempTextView = new TextView(getActivity());
//        Calendar calendar = Calendar.getInstance();
//        int day = calendar.get(Calendar.DAY_OF_WEEK);
//        String[] conditions = getResources().getStringArray(R.array.condition);
//        int[] degrees = getResources().getIntArray(R.array.degrees);
//        int[] icons = {R.drawable.cloudy_1, R.drawable.sunny, R.drawable.rainy_1, R.drawable.cloudy_2, R.drawable.rainy_2, R.drawable.snowy, R.drawable.stormy};

        ImageView iconImageView = new ImageView(getActivity());
        iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int width = iconImageView.getWidth();
                int height = iconImageView.getHeight();

                ViewGroup.LayoutParams lp = iconImageView.getLayoutParams();
                lp.width = width / 2;
                lp.height = height / 2;
                iconImageView.setLayoutParams(lp);
            }
        });

        String city = getArguments().getString("city");
        HashMap<String, String> locations = new HashMap<String, String>();
        locations.put(getString(R.string.hanoi), getString(R.string.location_Hanoi));
        locations.put(getString(R.string.paris), getString(R.string.location_Paris));
        locations.put(getString(R.string.toulouse), getString(R.string.location_Toulouse));
        String location = locations.get(city);

        AsyncTask<String, Void, String> task1 = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String response = null;
                try {
                    URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+ location + "&APPID=d09e0283bb90ea6d4255b1b0336f276a&units=metric");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();
                    int code_URL = connection.getResponseCode();
                    Log.i("USTHWeather", "The response is: " + code_URL);
                    InputStream is = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    response = sb.toString();
                    is.close();
                    reader.close();
                    Log.i("WeatherActivity", "JSON 1: " + response);
                } catch (Exception e) {
                    Log.i("WeatherActivity", "ERROR 1: " + e);
                }
                return response;
            }
            @Override
                protected void onPostExecute(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray weatherArray = obj.getJSONArray("weather");
                    JSONObject weather = weatherArray.getJSONObject(0);
                    // get the first element of the array
                    String condition = weather.getString("main");
                    JSONObject main = obj.getJSONObject("main");
                    String temp = main.getString("temp") + "°C";
                    tempTextView.setText(temp + "\n" + condition);
                    // set the text view in the onPostExecute method
                    String icon = weather.getString("icon");
                    if (icon.startsWith("01")) {
                        iconImageView.setImageResource(R.drawable.sunny);
                    } else if (icon.startsWith("02")) {
                        iconImageView.setImageResource(R.drawable.cloudy_sun);
                    } else if (icon.startsWith("03")) {
                        iconImageView.setImageResource(R.drawable.cloudy_1);
                    } else if (icon.startsWith("04")) {
                        iconImageView.setImageResource(R.drawable.cloudy_2);
                    } else if (icon.startsWith("09")) {
                        iconImageView.setImageResource(R.drawable.rainy_2);
                    } else if (icon.startsWith("10")) {
                        iconImageView.setImageResource(R.drawable.rainy_1);
                    } else if (icon.startsWith("11")) {
                        iconImageView.setImageResource(R.drawable.stormy);
                    } else if (icon.startsWith("13")) {
                        iconImageView.setImageResource(R.drawable.snowy);
                    } else if (icon.startsWith("50")) {
                        iconImageView.setImageResource(R.drawable.night_snow);
                    }
                } catch (JSONException e)
                {
                    e.getMessage();
                }
//                Toast.makeText(getContext(), R.string.fetch_weather_complete, Toast.LENGTH_SHORT).show();
                }
            };
            task1.execute();
//        StringRequest request = new StringRequest("https://api.openweathermap.org/data/2.5/weather?q=Hanoi,vn&APPID=d09e0283bb90ea6d4255b1b0336f276a", new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.i("WeatherActivity", "JSON: " + response);
//                    try {
//                        JSONObject obj = new JSONObject(response);
//                        JSONObject weather = obj.getJSONObject("weather");
//                        String condition = weather.getString("main");
//                        JSONObject main = obj.getJSONObject("main");
//                        String temp = main.getString("temp") + "°F";
//                        tempTextView.setText(temp + "\n" + condition);
//                        String icon = weather.getString("icon");
//                        if (icon.startsWith("01")) {
//                            iconImageView.setImageResource(R.drawable.sunny);
//                        } else if (icon.startsWith("02")) {
//                            iconImageView.setImageResource(R.drawable.cloudy_sun);
//                        } else if (icon.startsWith("03")) {
//                            iconImageView.setImageResource(R.drawable.cloudy_1);
//                        } else if (icon.startsWith("04")) {
//                            iconImageView.setImageResource(R.drawable.cloudy_2);
//                        } else if (icon.startsWith("09")) {
//                            iconImageView.setImageResource(R.drawable.rainy_2);
//                        } else if (icon.startsWith("10")) {
//                            iconImageView.setImageResource(R.drawable.rainy_1);
//                        } else if (icon.startsWith("11")) {
//                            iconImageView.setImageResource(R.drawable.stormy);
//                        } else if (icon.startsWith("13")) {
//                            iconImageView.setImageResource(R.drawable.snowy);
//                        } else if (icon.startsWith("50")) {
//                            iconImageView.setImageResource(R.drawable.night_snow);
//                        }
//                    } catch (JSONException e)
//                    {
//                        e.getMessage();
//                    }
//                }
//            },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                        }
//                    });

        tempTextView.setTextSize(12);
        tempTextView.setTextColor(Color.BLACK);
        tempTextView.setId(R.id.weather_info);

        RelativeLayout.LayoutParams tempParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
        tempParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        tempParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        tempTextView.setLayoutParams(tempParams);
        iconImageView.setId(R.id.weather_icon);

        RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(350,350);
        iconParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        iconImageView.setLayoutParams(iconParams);

        TextView cityTextView = new TextView(getActivity());
        cityTextView.setText(city);
        cityTextView.setTextSize(12);
        cityTextView.setTextColor(Color.BLACK);
        cityTextView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        RelativeLayout.LayoutParams cityParams = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
        cityParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        cityParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        cityTextView.setLayoutParams(cityParams);

        relativeLayout.addView(tempTextView);
        relativeLayout.addView(iconImageView);
        relativeLayout.addView(cityTextView);

        return relativeLayout;
    }
}