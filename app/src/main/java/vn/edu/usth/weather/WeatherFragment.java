package vn.edu.usth.weather;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

public class WeatherFragment extends Fragment {

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
        // Create a RelativeLayout
        RelativeLayout relativeLayout = new RelativeLayout(getActivity());
//        relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorWeather));
        relativeLayout.setBackground(getResources().getDrawable(R.drawable.frame));

        int height = (int) getResources().getDimension(R.dimen.weather_frame_size);
        // Create a FrameLayout.LayoutParams object with margins
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
        params.setMargins(30, 30, 30, 5);

        // Set the layout parameters for the RelativeLayout
        relativeLayout.setLayoutParams(params);
        relativeLayout.setPadding(140,35,150,35);

        // Create a TextView for current temperature
        TextView tempTextView = new TextView(getActivity());
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String[] conditions = getResources().getStringArray(R.array.condition);
        int[] degrees = getResources().getIntArray(R.array.degrees);
        int[] icons = {R.drawable.cloudy_1, R.drawable.sunny, R.drawable.rainy_1, R.drawable.cloudy_2, R.drawable.rainy_2, R.drawable.snowy, R.drawable.stormy};

        switch (day) {
            case Calendar.MONDAY:
                tempTextView.setText(degrees[0]+"°C\n"+conditions[0]);
                break;
            case Calendar.TUESDAY:
                tempTextView.setText(degrees[1]+"°C\n"+conditions[1]);
                break;
            case Calendar.WEDNESDAY:
                tempTextView.setText(degrees[2]+"°C\n"+conditions[2]);
                break;
            case Calendar.THURSDAY:
                tempTextView.setText(degrees[3]+"°C\n"+conditions[3]);
                break;
            case Calendar.FRIDAY:
                tempTextView.setText(degrees[4]+"°C\n"+conditions[4]);
                break;
            case Calendar.SATURDAY:
                tempTextView.setText(degrees[5]+"°C\n"+conditions[5]);
                break;
            case Calendar.SUNDAY:
                tempTextView.setText(degrees[6]+"°C\n"+conditions[6]);
                break;
        }
        tempTextView.setTextSize(12);
        tempTextView.setTextColor(Color.BLACK);
        tempTextView.setId(View.generateViewId());

        // Create a RelativeLayout.LayoutParams object for the temperature TextView
        RelativeLayout.LayoutParams tempParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
        tempParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        tempParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        // Set the layout parameters for the temperature TextView
        tempTextView.setLayoutParams(tempParams);

        // Create an ImageView for current weather icon
        ImageView iconImageView = new ImageView(getActivity());
        switch (day) {
            case Calendar.MONDAY:
                iconImageView.setImageResource(icons[0]);
                break;
            case Calendar.TUESDAY:
                iconImageView.setImageResource(icons[1]);
                break;
            case Calendar.WEDNESDAY:
                iconImageView.setImageResource(icons[2]);
                break;
            case Calendar.THURSDAY:
                iconImageView.setImageResource(icons[3]);
                break;
            case Calendar.FRIDAY:
                iconImageView.setImageResource(icons[4]);
                break;
            case Calendar.SATURDAY:
                iconImageView.setImageResource(icons[5]);
                break;
            case Calendar.SUNDAY:
                iconImageView.setImageResource(icons[6]);
                break;
        }
        iconImageView.setId(View.generateViewId());

        // Create a RelativeLayout.LayoutParams object for the icon ImageView
        RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(300,300);
        iconParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        // Set the layout parameters for the icon ImageView
        iconImageView.setLayoutParams(iconParams);

        // Create a TextView for city name
        TextView cityTextView = new TextView(getActivity());
        String city = getArguments().getString("city");
        cityTextView.setText(city);
        cityTextView.setTextSize(12);
        cityTextView.setTextColor(Color.BLACK);
        cityTextView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        // Create a RelativeLayout.LayoutParams object for the city TextView
        RelativeLayout.LayoutParams cityParams = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
        cityParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        cityParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        // Set the layout parameters for the city TextView
        cityTextView.setLayoutParams(cityParams);

        // Add the views to the RelativeLayout
        relativeLayout.addView(tempTextView);
        relativeLayout.addView(iconImageView);
        relativeLayout.addView(cityTextView);

        // Return the RelativeLayout as the root view of the Fragment
        return relativeLayout;
    }

}