package vn.edu.usth.weather;

import static android.os.SystemClock.sleep;
import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.*;

import java.util.Locale;
import java.util.ResourceBundle;


class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 3;
    private Context context;
    private String[] titles;
    public HomeFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        titles = new String[]{context.getString(R.string.hanoi), context.getString(R.string.paris), context.getString(R.string.toulouse)};
    }

    @Override
    public int getCount() {
        return PAGE_COUNT; // number of pages for a ViewPager
    }

    @Override
    public Fragment getItem(int page) {
        // returns an instance of WeatherAndForecastFragment corresponding to the specified page and city name
        switch (page) {
            case 0:
                return WeatherAndForecastFragment.newInstance(context.getString(R.string.hanoi));
            case 1:
                return WeatherAndForecastFragment.newInstance(context.getString(R.string.paris));
            case 2:
                return WeatherAndForecastFragment.newInstance(context.getString(R.string.toulouse));
        }

        return new EmptyFragment(); // failsafe
    }

    @Override
    public CharSequence getPageTitle(int page) {
        // returns a tab title corresponding to the specified page and city name
        return titles[page];
    }
}
public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";

    public static final String SERVER_RESPONSE = "server_response";
    private String selectedLanguage;
    private static final String MP3_FILE_PATH = Environment.getExternalStorageDirectory() + "/Music/donate.mp3";
    private MediaPlayer mediaPlayer;
    private void extractMP3File() {
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + "/Music");
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            // Get the input stream from the res/raw folder
            InputStream source = getAssets().open("donate.mp3");
            // Create the output stream to the sdcard
            OutputStream target = new FileOutputStream(MP3_FILE_PATH);
            // Copy the bytes from the input stream to the output stream
            byte[] buffer = new byte[8192];
            int length;
            while ((length = source.read(buffer)) != -1) {
                target.write(buffer, 0, length);
            }
        // Close the streams
        source.close();
        target.close();
        }
        catch(IOException e){
                e.printStackTrace();
            }
    }

    // This method plays the MP3 file using the MediaPlayer class
    private void playMP3File() {
        try {
            // Create a new MediaPlayer object
            mediaPlayer = new MediaPlayer();
            // Set the data source to the MP3 file path
            mediaPlayer.setDataSource(MP3_FILE_PATH);
            // Prepare and start the media player
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("language", MODE_PRIVATE);
        String selectedLanguage = preferences.getString("selectedLanguage", null);
        if (selectedLanguage != null)
        {
            if (selectedLanguage.equals(getString(R.string.english)))
            {
                setAppLocale("en");
            }
            else if (selectedLanguage.equals(getString(R.string.vietnamese)))
            {
                setAppLocale("vi");
            }
        }
        setContentView(R.layout.activity_weather);

//        SharedPreferences preferences = getSharedPreferences("language", MODE_PRIVATE);
//        selectedLanguage = preferences.getString("selectedLanguage", null);
//        if (selectedLanguage != null)
//        {
//            setAppLocale("en");
//        }

        PagerAdapter adapter = new HomeFragmentPagerAdapter(getSupportFragmentManager(), this);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(pager);

        extractMP3File();
        playMP3File();

        Log.i(TAG, "LOG: onCreate - New process");

        // Insert ForecastFragment
//        ForecastFragment firstFragment = new ForecastFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.container, firstFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        // Get the shared preferences and retrieve the selected language
        SharedPreferences preferences = getSharedPreferences("language", MODE_PRIVATE);
        selectedLanguage = preferences.getString("selectedLanguage", null);
        return true;
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_language) {
            String[] languages = {getString(R.string.english), getString(R.string.vietnamese)};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.language));
            builder.setSingleChoiceItems(languages, getCheckedItem(languages), new DialogInterface.OnClickListener()
            {
                @Override public void onClick(DialogInterface dialog, int which){
                    selectedLanguage = languages[which];
                    if (selectedLanguage.equals(getString(R.string.english)))
                    {
                        setAppLocale("en");
                    }
                    else if (selectedLanguage.equals(getString(R.string.vietnamese)))
                    {
                        setAppLocale("vi");
                    }
                    // Save the selected language in the shared preferences
                    SharedPreferences preferences = getSharedPreferences("language", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("selectedLanguage", selectedLanguage);
                    editor.apply();
                    recreate();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        if (id == R.id.refresh) {
//            Practical Work 13 code:
//            final Handler handler = new Handler(Looper.getMainLooper()) {
//                @Override
//                public void handleMessage(Message msg) {
//                    // This method is executed in main thread
//                    String content = msg.getData().getString(SERVER_RESPONSE);
//                    Toast.makeText(getBaseContext(), content, Toast.LENGTH_LONG).show();
//                }
//            };
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    // this method is run in a worker thread
//                    try {
//                        // wait for 5 seconds to simulate a long network access
//                        Thread.sleep(1000);
//                    }
//                    catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    // Assume that we got our data from server
//                    Bundle bundle = new Bundle();
//                    bundle.putString(SERVER_RESPONSE, getString(R.string.fetch_network));
//                    // notify main thread
//                    Message msg = new Message();
//                    msg.setData(bundle);
//                    handler.sendMessage(msg);
//                }
//            });
//            t.start();
//            Toast.makeText(getBaseContext(), R.string.refreshed, Toast.LENGTH_LONG).show();

            AsyncTask<String, Integer, Bitmap> task = new AsyncTask<String, Integer, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... strings) {
                    sleep(2000);
                    return null;
                }
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                Toast.makeText(getBaseContext(), R.string.refreshed, Toast.LENGTH_LONG).show();
                }
            };
            task.execute("https://usth.edu.vn/wp-content/uploads/2021/11/logo.png");
        }
        if (id == R.id.settings) {
            Intent intent = new Intent(this, PrefActivity.class);
            startActivity(intent);
            Toast.makeText(getBaseContext(), R.string.pref_open, Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private int getCheckedItem(String[] languages) {
        // If the selected language is null, return -1 (no item checked)
        if (selectedLanguage == null) {
            return -1;
        }
        if (selectedLanguage.equals(getString(R.string.english))){
            return 0;
        }
        if (selectedLanguage.equals(getString(R.string.vietnamese))){
            return 1;
        }
        // Return -1 if no item checked
        return -1;
    }

    // Define a method to set the app locale based on the language code
    private void setAppLocale(String language) {
        // Get the current configuration and set its locale to the given language
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(language));
        // Update the configuration for the app context and resources
        getApplicationContext().createConfigurationContext(configuration);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        // Add these lines of code to update the app title as well
        String appTitle = getString(R.string.app_name); // Get the app name from strings.xml
        Activity activity = this; // Get the current activity
        activity.setTitle(appTitle); // Set the app title with the new language
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "LOG: onStart - App is running");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "LOG: onPause - App paused");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "LOG: onResume - App continued");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "LOG: onStop - App stopped");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "LOG: onDestroy - Process stopped");
    }
}