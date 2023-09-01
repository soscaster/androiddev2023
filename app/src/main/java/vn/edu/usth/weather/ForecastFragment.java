package vn.edu.usth.weather;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class ForecastFragment extends Fragment {
    public ForecastFragment() {
        // Required empty public constructor
    }
    public static ForecastFragment newInstance(String city) {
        ForecastFragment fragment = new ForecastFragment();
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
        ScrollView scrollView = new ScrollView(getActivity());

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        linearLayout.setBackgroundColor(Color.parseColor("#A7B5B5FF"));

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(30, 30, 30, 30);

        linearLayout.setLayoutParams(params);
        linearLayout.setPadding(15, 5, 15, 5);

//        // Create an array of weather data for 7 days
//        String[] days = getResources().getStringArray(R.array.days_of_the_week);
//        String[] conditions = getResources().getStringArray(R.array.condition);
//        int[] degrees = getResources().getIntArray(R.array.degrees);
//        int[] degrees2 = getResources().getIntArray(R.array.degrees2);
//        int[] icons = {R.drawable.cloudy_1, R.drawable.sunny, R.drawable.rainy_1, R.drawable.cloudy_2, R.drawable.rainy_2, R.drawable.snowy, R.drawable.stormy};
//
//        // Loop through the weather data and create a UI for each day
//        for (int i = 0; i < 7; i++) {
//            // Create a horizontal LinearLayout for each day
//            LinearLayout dayLayout = new LinearLayout(getActivity());
//            dayLayout.setOrientation(LinearLayout.HORIZONTAL);
//            dayLayout.setGravity(Gravity.CENTER);
//
//            // Create a TextView for day of the week
//            TextView dayTextView = new TextView(getActivity());
//            dayTextView.setText(days[i]);
//            dayTextView.setTextSize(17);
//            dayTextView.setTextColor(Color.BLACK);
//            dayTextView.setPadding(30, 10, 10, 10);
////            dayTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//            dayTextView.setLayoutParams(new LinearLayout.LayoutParams(220, LinearLayout.LayoutParams.WRAP_CONTENT));
//
//            // Create an ImageView for weather icon
//            ImageView iconImageView = new ImageView(getActivity());
//            iconImageView.setImageResource(icons[i]);
//            iconImageView.setPadding(10, 10, 10, 10);
//            iconImageView.setLayoutParams(new LinearLayout.LayoutParams(144, 144));
//
//            // Create a TextView for weather condition and degree
//            TextView conditionTextView = new TextView(getActivity());
//            int temp1 = degrees[i] + degrees2[i];
//            conditionTextView.setText(conditions[i] + "\n" + degrees[i] + "°C - " + temp1 + "°C");
//            conditionTextView.setTextSize(17);
//            conditionTextView.setTextColor(Color.BLACK);
//            conditionTextView.setPadding(70, 10, 10, 10);
//            conditionTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//
//            // Add the TextViews and ImageView to the horizontal LinearLayout
//            dayLayout.addView(dayTextView);
//            dayLayout.addView(iconImageView);
//            dayLayout.addView(conditionTextView);
//
//            // Add the horizontal LinearLayout to the vertical LinearLayout
//            linearLayout.addView(dayLayout);
//        }

        String city = getArguments().getString("city");

        AsyncTask<String, Void, String> task2 = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String response2 = null;
                String lat = null;
                String lon = null;
                try {
                    if (city == getString(R.string.hanoi)) {
                        lat = getString(R.string.lat_Hanoi);
                        lon = getString(R.string.long_Hanoi);
                    } else if (city == getString(R.string.paris)) {
                        lat = getString(R.string.lat_Paris);
                        lon = getString(R.string.long_Paris);
                    } else if (city == getString(R.string.toulouse)) {
                        lat = getString(R.string.lat_Toulouse);
                        lon = getString(R.string.long_Toulouse);
                    }
                    URL url = new URL("https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon + "&daily=weathercode,temperature_2m_max,temperature_2m_min&timezone=Asia%2FBangkok");
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
                    response2 = sb.toString();
                    is.close();
                    reader.close();
                    Log.i("WeatherActivity", "JSON 2: " + response2);
                } catch (Exception e) {
                    Log.i("WeatherActivity", "ERROR 2: " + e);
                }
                return response2;
            }

        @Override
        protected void onPostExecute (String response2){
            try {
                JSONObject obj = new JSONObject(response2);
                JSONObject daily = obj.getJSONObject("daily");
                JSONArray timeArray = daily.getJSONArray("time");
                JSONArray weathercodeArray = daily.getJSONArray("weathercode");
                JSONArray temperatureMaxArray = daily.getJSONArray("temperature_2m_max");
                JSONArray temperatureMinArray = daily.getJSONArray("temperature_2m_min");

                for (int i = 0; i < 7; i++) {

                    String day = timeArray.getString(i);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = format.parse(day);
                    Locale locale = new Locale("en"); // Set the desired locale
                    SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", locale);
                    String dayOfWeek = dayFormat.format(date);
                    int weathercode = weathercodeArray.getInt(i);
                    double temperatureMax = temperatureMaxArray.getDouble(i);
                    double temperatureMin = temperatureMinArray.getDouble(i);

                    LinearLayout dayLayout = new LinearLayout(getActivity());
                    dayLayout.setOrientation(LinearLayout.HORIZONTAL);
                    dayLayout.setGravity(Gravity.CENTER);

                    TextView dayTextView = new TextView(getActivity());
                    dayTextView.setText(dayOfWeek);
                    dayTextView.setTextSize(17);
                    dayTextView.setTextColor(Color.BLACK);
                    dayTextView.setPadding(30, 10, 10, 10);
                    dayTextView.setLayoutParams(new LinearLayout.LayoutParams(220, LinearLayout.LayoutParams.WRAP_CONTENT));

                    String conditionDescription = null;
                    String[] conditions = getResources().getStringArray(R.array.condition);

                    ImageView iconImageView = new ImageView(getActivity());
                    iconImageView.setPadding(10, 10, 10, 10);
                    if (weathercode == 0) {
                        conditionDescription = conditions[0];
                        iconImageView.setImageResource(R.drawable.sunny);
                    } else if (weathercode == 1 || weathercode == 2 || weathercode == 3) {
                        conditionDescription = conditions[1];
                        iconImageView.setImageResource(R.drawable.cloudy_sun);
                    } else if (weathercode == 45 || weathercode == 48) {
                        conditionDescription = conditions[2];
                        iconImageView.setImageResource(R.drawable.cloudy_2);
                    } else if (weathercode == 51 || weathercode == 53 || weathercode == 55) {
                        conditionDescription = conditions[3];
                        iconImageView.setImageResource(R.drawable.rainy_1);
                    } else if (weathercode == 56 || weathercode == 57) {
                        conditionDescription = conditions[4];
                        iconImageView.setImageResource(R.drawable.rainy_1);
                    } else if (weathercode == 61 || weathercode == 63 || weathercode == 65) {
                        conditionDescription = conditions[5];
                        iconImageView.setImageResource(R.drawable.rainy_2);
                    } else if (weathercode == 66 || weathercode == 67) {
                        conditionDescription = conditions[6];
                        iconImageView.setImageResource(R.drawable.rainy_2);
                    } else if (weathercode == 71 || weathercode == 73 || weathercode == 75) {
                        conditionDescription = conditions[7];
                        iconImageView.setImageResource(R.drawable.snowy);
                    } else if (weathercode == 77) {
                        conditionDescription = conditions[8];
                        iconImageView.setImageResource(R.drawable.snowy);
                    } else if (weathercode == 80 || weathercode == 81 || weathercode == 82) {
                        conditionDescription = conditions[9];
                        iconImageView.setImageResource(R.drawable.rainy_2);
                    } else if (weathercode == 85 || weathercode == 86) {
                        conditionDescription = conditions[10];
                        iconImageView.setImageResource(R.drawable.snowy);
                    } else if (weathercode == 95) {
                        conditionDescription = conditions[11];
                        iconImageView.setImageResource(R.drawable.stormy);
                    } else if (weathercode == 96 || weathercode == 99) {
                        conditionDescription = conditions[12];
                        iconImageView.setImageResource(R.drawable.stormy);
                    }
                    iconImageView.setLayoutParams(new LinearLayout.LayoutParams(144, 144));

                    TextView conditionTextView = new TextView(getActivity());
                    conditionTextView.setText(conditionDescription + "\n" + temperatureMin + "°C - " + temperatureMax + "°C");
                    conditionTextView.setTextSize(17);
                    conditionTextView.setTextColor(Color.BLACK);
                    conditionTextView.setPadding(70, 10, 10, 10);
                    conditionTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                    dayLayout.addView(dayTextView);
                    dayLayout.addView(iconImageView);
                    dayLayout.addView(conditionTextView);

                    linearLayout.addView(dayLayout);
                }
            } catch (JSONException e) {
                e.getMessage();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Toast.makeText(getContext(), R.string.fetch_forecast_complete, Toast.LENGTH_SHORT).show();
            }
        };

        task2.execute();
        scrollView.addView(linearLayout);
        return scrollView;
    }
}