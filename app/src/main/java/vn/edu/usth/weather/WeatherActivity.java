package vn.edu.usth.weather;
//import static android.os.SystemClock.sleep;
//import static com.android.volley.toolbox.Volley.newRequestQueue;
//import static com.google.android.material.internal.ContextUtils.getActivity;
//
//import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

//import com.android.volley.*;
//import com.android.volley.toolbox.ImageRequest;
import com.google.android.material.tabs.TabLayout;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
//import android.os.Handler;
//import android.os.LocaleList;
//import android.os.Looper;
//import android.os.Message;
import android.os.LocaleList;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
//import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
//import java.util.ResourceBundle;

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
    public static final String SERVER_RESPONSE = "server_response";
    private static final String TAG = "WeatherActivity";
    private static final String MP3_FILE_PATH = Environment.getExternalStorageDirectory() + "/Music/wonderhoy.mp3";
    private String selectedLanguage;
    private MediaPlayer mediaPlayer;
    private void extractMP3File() {
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + "/Music");
            if (!dir.exists())
            {
                dir.mkdirs();
            }

            InputStream source = getAssets().open("wonderhoy.mp3");

            OutputStream target = new FileOutputStream(MP3_FILE_PATH);

            byte[] buffer = new byte[8192];
            int length;
            while ((length = source.read(buffer)) != -1) {
                target.write(buffer, 0, length);
            }

        source.close();
        target.close();
        }
        catch(IOException e){
                e.printStackTrace();
            }
    }

    private void playMP3File() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(MP3_FILE_PATH);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        // Get the selected language from the shared preferences
//        SharedPreferences preferences = base.getSharedPreferences("language", MODE_PRIVATE);
//        String selectedLanguage = preferences.getString("selectedLanguage", null);
//        if (selectedLanguage != null)
//        {
//            setAppLocale(base, selectedLanguage);
//        }
//    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // Get the selected language from the shared preferences
        SharedPreferences preferences = base.getSharedPreferences("language", MODE_PRIVATE);
        String selectedLanguage = preferences.getString("selectedLanguage", null);
        if (selectedLanguage != null)
        {
            setAppLocale(base, selectedLanguage);
        }
    }

//    private void setAppLocale(Context context, String language) {
//        // Get the current configuration and set its locale to the given language
//        Resources resources = context.getResources();
//        Configuration configuration = resources.getConfiguration();
//        configuration.setLocale(new Locale(language));
//
//        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
//    }

    private void setAppLocale(Context context, String language) {
        // Get the current configuration and set its locale to the given language
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(new Locale(language));

        // Use the new API to set the application locales
//        LocaleList locales = new LocaleList(new Locale(language));
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Locale locale = newConfig.getLocales().get(0);

        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        String appTitle = getString(R.string.app_name);
        Activity activity = this;
        activity.setTitle(appTitle);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_language) {
//            String[] languages = {getString(R.string.english), getString(R.string.vietnamese)};
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(getString(R.string.language));
//            builder.setSingleChoiceItems(languages, getCheckedItem(languages), new DialogInterface.OnClickListener()
//            {
//                @Override public void onClick(DialogInterface dialog, int which){
//                    selectedLanguage = languages[which];
//                    if (selectedLanguage.equals(getString(R.string.english)))
//                    {
//                        setAppLocale(getApplicationContext(), "en");
//                    }
//                    else if (selectedLanguage.equals(getString(R.string.vietnamese)))
//                    {
//                        setAppLocale(getApplicationContext(), "vi");
//                    }
//                    // Save the selected language in the shared preferences
//                    SharedPreferences preferences = getSharedPreferences("language", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("selectedLanguage", selectedLanguage);
//                    editor.apply();
//
////                    Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
////                    PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 123456, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
////                    AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
////                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1 * 1_000L, pendingIntent);
////                    finish();
//
//                    Context ctx = getApplicationContext();
//                    PackageManager pm = ctx.getPackageManager();
//                    Intent intent = pm.getLaunchIntentForPackage(ctx.getPackageName());
//                    Intent mainIntent = Intent.makeRestartActivityTask(intent.getComponent());
//                    ctx.startActivity(mainIntent);
//                    Runtime.getRuntime().exit(0);
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//            return true;
//        }
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
                        setAppLocale(getApplicationContext(), "en");
                    }
                    else if (selectedLanguage.equals(getString(R.string.vietnamese)))
                    {
                        setAppLocale(getApplicationContext(), "vi");
                    }
                    // Save the selected language in the shared preferences
                    SharedPreferences preferences = getSharedPreferences("language", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("selectedLanguage", selectedLanguage);
                    editor.apply();
                    // Restart the activity to apply the new locale
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        if (id == R.id.refresh) {
//            recreate();
              Toast.makeText(getBaseContext(), R.string.dev_in_progress, Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.settings) {
            Intent intent = new Intent(this, PrefActivity.class);
            startActivity(intent);
            Toast.makeText(getBaseContext(), R.string.pref_open, Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_about);
            builder.setMessage(R.string.about_message);
            builder.setIcon(R.drawable.ic_smile);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private int getCheckedItem(String[] languages) {
//        // If the selected language is null, return -1 (no item checked)
//        if (selectedLanguage == null) {
//            return -1;
//        }
//        if (selectedLanguage.equals(getString(R.string.english))){
//            return 0;
//        }
//        if (selectedLanguage.equals(getString(R.string.vietnamese))){
//            return 1;
//        }
//        // Return -1 if no item checked
//        return -1;
//    }
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SharedPreferences preferences = getSharedPreferences("language", MODE_PRIVATE);
//        String selectedLanguage = preferences.getString("selectedLanguage", null);
//        if (selectedLanguage != null)
//        {
//            if (selectedLanguage.equals(getString(R.string.english)))
//            {
//                setAppLocale(getBaseContext(),"en");
//            }
//            else if (selectedLanguage.equals(getString(R.string.vietnamese)))
//            {
//                setAppLocale(getBaseContext(),"vi");
//            }
//        }
        SharedPreferences preferences = getSharedPreferences("language", MODE_PRIVATE);
        String selectedLanguage = preferences.getString("selectedLanguage", null);
        if (selectedLanguage != null)
        {
            if (selectedLanguage.equals(getString(R.string.english)))
            {
                setAppLocale(getBaseContext(),"en");
            }
            else if (selectedLanguage.equals(getString(R.string.vietnamese)))
            {
                setAppLocale(getBaseContext(),"vi");
            }
        }
        setContentView(R.layout.activity_weather);
//        SharedPreferences preferences = getSharedPreferences("language", MODE_PRIVATE);
//        selectedLanguage = preferences.getString("selectedLanguage", null);
//        if (selectedLanguage != null)
//        {
//            setAppLocale("en");
//        }

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            PagerAdapter adapter = new HomeFragmentPagerAdapter(getSupportFragmentManager(), this);
            ViewPager pager = (ViewPager) findViewById(R.id.pager);
            pager.setOffscreenPageLimit(3);
            pager.setAdapter(adapter);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
            tabLayout.setupWithViewPager(pager);

            extractMP3File();
            playMP3File();
        } else {
            TextView textView = new TextView(this);
            textView.setText(R.string.check_Internet);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            setContentView(textView);
        }

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
//    @Override public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_language) {
//            String[] languages = {getString(R.string.english), getString(R.string.vietnamese)};
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(getString(R.string.language));
//            builder.setSingleChoiceItems(languages, getCheckedItem(languages), new DialogInterface.OnClickListener()
//            {
//                @Override public void onClick(DialogInterface dialog, int which){
//                    selectedLanguage = languages[which];
//                    if (selectedLanguage.equals(getString(R.string.english)))
//                    {
//                        setAppLocale("en");
//                    }
//                    else if (selectedLanguage.equals(getString(R.string.vietnamese)))
//                    {
//                        setAppLocale("vi");
//                    }
//                    // Save the selected language in the shared preferences
//                    SharedPreferences preferences = getSharedPreferences("language", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("selectedLanguage", selectedLanguage);
//                    editor.apply();
//                    recreate();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//            return true;
//        }
//        if (id == R.id.refresh) {
////            Practical Work 13 code:
////            final Handler handler = new Handler(Looper.getMainLooper()) {
////                @Override
////                public void handleMessage(Message msg) {
////                    // This method is executed in main thread
////                    String content = msg.getData().getString(SERVER_RESPONSE);
////                    Toast.makeText(getBaseContext(), content, Toast.LENGTH_LONG).show();
////                }
////            };
////            Thread t = new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    // this method is run in a worker thread
////                    try {
////                        // wait for 5 seconds to simulate a long network access
////                        Thread.sleep(1000);
////                    }
////                    catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                    // Assume that we got our data from server
////                    Bundle bundle = new Bundle();
////                    bundle.putString(SERVER_RESPONSE, getString(R.string.fetch_network));
////                    // notify main thread
////                    Message msg = new Message();
////                    msg.setData(bundle);
////                    handler.sendMessage(msg);
////                }
////            });
////            t.start();
////            Toast.makeText(getBaseContext(), R.string.refreshed, Toast.LENGTH_LONG).show();
//
////            Practical 15 code:
////            AsyncTask<String, Integer, Bitmap> task = new AsyncTask<String, Integer, Bitmap>() {
////                @Override
////                protected Bitmap doInBackground(String... strings) {
//////                    sleep(2000);
////                    // Make a request to server
////                    try {
////                        URL url = new URL("https://usth.edu.vn/wp-content/uploads/2021/11/logo.png");
////                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////                        connection.setRequestMethod("GET");
////                        connection.setDoInput(true);
////                        connection.connect();
////                        int response = connection.getResponseCode();
////                        Log.i("USTHWeather", "The response is: " + response);
////                        InputStream is = connection.getInputStream();
////                        Bitmap bitmap = BitmapFactory.decodeStream(is);
////                        runOnUiThread(new Runnable() {
////                            @Override
////                            public void run() {
////                                ImageView logo = (ImageView) findViewById(R.id.weather_icon);
////                                logo.setImageBitmap(bitmap);
////                            }
////                        });
////                    } catch (Exception e) {
////                        Log.i(TAG, "ERROR: " + e);
////                    }
////                    return null;
////                }
////
////                @Override
////                protected void onPostExecute(Bitmap bitmap) {
////                    Toast.makeText(getBaseContext(), R.string.refreshed, Toast.LENGTH_LONG).show();
////                }
////            };
////            task.execute("https://usth.edu.vn/wp-content/uploads/2021/11/logo.png");
//
//            // Practical Work 16 code:
////            RequestQueue queue = newRequestQueue(getBaseContext());
////            Response.Listener<Bitmap> listener = new Response.Listener<Bitmap>() {
////                @Override
////                public void onResponse(Bitmap response) {
////                    ImageView iv = (ImageView) findViewById(R.id.weather_icon);
////                    iv.setImageBitmap(response);
////                }
////            };
////            ImageRequest imageRequest = new ImageRequest("https://usth.edu.vn/wp-content/uploads/2021/11/logo.png", listener, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888,null);
////            // go!
////            queue.add(imageRequest);
////            Toast.makeText(getBaseContext(), R.string.refreshed, Toast.LENGTH_LONG).show();
//
//            recreate();
//        }
//
//        if (id == R.id.settings) {
//            Intent intent = new Intent(this, PrefActivity.class);
//            startActivity(intent);
//            Toast.makeText(getBaseContext(), R.string.pref_open, Toast.LENGTH_LONG).show();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private int getCheckedItem(String[] languages) {
//        // If the selected language is null, return -1 (no item checked)
//        if (selectedLanguage == null) {
//            return -1;
//        }
//        if (selectedLanguage.equals(getString(R.string.english))){
//            return 0;
//        }
//        if (selectedLanguage.equals(getString(R.string.vietnamese))){
//            return 1;
//        }
//        // Return -1 if no item checked
//        return -1;
//    }
//
//    // Define a method to set the app locale based on the language code
//    private void setAppLocale(String language) {
//        // Get the current configuration and set its locale to the given language
//        Resources resources = getResources();
//        Configuration configuration = resources.getConfiguration();
//        configuration.setLocale(new Locale(language));
//        // Update the configuration for the app context and resources
//        getApplicationContext().createConfigurationContext(configuration);
//        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//
//        // Add these lines of code to update the app title as well
//        String appTitle = getString(R.string.app_name); // Get the app name from strings.xml
//        Activity activity = this; // Get the current activity
//        activity.setTitle(appTitle); // Set the app title with the new language
//    }

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