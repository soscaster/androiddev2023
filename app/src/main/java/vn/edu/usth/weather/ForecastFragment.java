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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;

import java.util.Calendar;

public class ForecastFragment extends Fragment {
    public ForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Create a ScrollView
        ScrollView scrollView = new ScrollView(getActivity());

        // Create a vertical LinearLayout
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        linearLayout.setBackgroundColor(Color.parseColor("#A7B5B5FF"));

        // Create a FrameLayout.LayoutParams object with margins
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(30, 30, 30, 30);

        // Set the layout parameters for the LinearLayout
        linearLayout.setLayoutParams(params);
        linearLayout.setPadding(15,5,15,5);

        // Create an array of weather data for 7 days
        String[] days = getResources().getStringArray(R.array.days_of_the_week);
        String[] conditions = getResources().getStringArray(R.array.condition);
        int[] degrees = getResources().getIntArray(R.array.degrees);
        int[] degrees2 = getResources().getIntArray(R.array.degrees2);
        int[] icons = {R.drawable.cloudy_1, R.drawable.sunny, R.drawable.rainy_1, R.drawable.cloudy_2, R.drawable.rainy_2, R.drawable.snowy, R.drawable.stormy};

        // Loop through the weather data and create a UI for each day
        for (int i = 0; i < 7; i++) {
            // Create a horizontal LinearLayout for each day
            LinearLayout dayLayout = new LinearLayout(getActivity());
            dayLayout.setOrientation(LinearLayout.HORIZONTAL);
            dayLayout.setGravity(Gravity.CENTER);

            // Create a TextView for day of the week
            TextView dayTextView = new TextView(getActivity());
            dayTextView.setText(days[i]);
            dayTextView.setTextSize(17);
            dayTextView.setTextColor(Color.BLACK);
            dayTextView.setPadding(30, 10, 10, 10);
//            dayTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            dayTextView.setLayoutParams(new LinearLayout.LayoutParams(220, LinearLayout.LayoutParams.WRAP_CONTENT));

            // Create an ImageView for weather icon
            ImageView iconImageView = new ImageView(getActivity());
            iconImageView.setImageResource(icons[i]);
            iconImageView.setPadding(10, 10, 10, 10);
            iconImageView.setLayoutParams(new LinearLayout.LayoutParams(144, 144));

            // Create a TextView for weather condition and degree
            TextView conditionTextView = new TextView(getActivity());
            int temp1 = degrees[i] + degrees2[i];
            conditionTextView.setText(conditions[i] + "\n" + degrees[i] + "°C - " + temp1 + "°C");
            conditionTextView.setTextSize(17);
            conditionTextView.setTextColor(Color.BLACK);
            conditionTextView.setPadding(70, 10, 10, 10);
            conditionTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            // Add the TextViews and ImageView to the horizontal LinearLayout
            dayLayout.addView(dayTextView);
            dayLayout.addView(iconImageView);
            dayLayout.addView(conditionTextView);

            // Add the horizontal LinearLayout to the vertical LinearLayout
            linearLayout.addView(dayLayout);
        }

        // Add the LinearLayout to the ScrollView
        scrollView.addView(linearLayout);

        // Return the LinearLayout as the root view of the Fragment
        return scrollView;
    }
}