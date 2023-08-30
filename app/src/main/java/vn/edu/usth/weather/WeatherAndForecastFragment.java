package vn.edu.usth.weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class WeatherAndForecastFragment extends Fragment {
    // A static method to create a new instance of the fragment with a city name as an argument
    public static WeatherAndForecastFragment newInstance(String city) {
        WeatherAndForecastFragment fragment = new WeatherAndForecastFragment();
        Bundle args = new Bundle();
        args.putString("city", city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create a vertical LinearLayout
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT ));

        // Set an id for the linearLayout
        linearLayout.setId(View.generateViewId());

        // Get the city name from the arguments
        String city = getArguments().getString("city");

        // Create a WeatherFragment with the city name as an argument
        WeatherFragment weatherFragment = WeatherFragment.newInstance(city);

        // Create a ForecastFragment
        ForecastFragment forecastFragment = new ForecastFragment();

        // Add the fragments to the LinearLayout using FragmentManager and FragmentTransaction
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(linearLayout.getId(), weatherFragment);
        fragmentTransaction.add(linearLayout.getId(), forecastFragment);
        fragmentTransaction.commit();

        // Return the LinearLayout as the root view of the Fragment
        return linearLayout;
    }
}
