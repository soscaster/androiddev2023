package vn.edu.usth.weather;

import static com.android.volley.toolbox.Volley.newRequestQueue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.VelocityTrackerCompat;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

//public class PrefActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pref);
//
//        RequestQueue queue = newRequestQueue(getBaseContext());
//        Response.Listener<Bitmap> listener = new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap response) {
//                ImageView iv = (ImageView) findViewById(R.id.logo);
//                iv.setImageBitmap(response);
//                iv.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View view, MotionEvent motionEvent) {
//                        checkTouch(iv, motionEvent);
//                        return true;
//                    }
//                });
//            }
//        };
//        ImageRequest imageRequest = new ImageRequest("https://usth.edu.vn/wp-content/uploads/2021/11/logo.png", listener, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888,null);
//        queue.add(imageRequest);
//    }
//
//    private void checkTouch(View iv, MotionEvent motionEvent) {
//        int action = motionEvent.getActionMasked();
//        int posX = (int) motionEvent.getX(0);
//        int posY = (int) motionEvent.getY(0);
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                startTouch(iv, motionEvent, posX, posY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                updateTouch(iv, motionEvent, posX, posY);
//                break;
//            case MotionEvent.ACTION_UP:
//                finishTouch(iv, motionEvent, posX, posY);
//                break;
//        }
//    }
//
//    int startTouchX, startTouchY;
//    int startViewX, startViewY;
//
//    private void startTouch(View iv, MotionEvent motionEvent,
//                            int posX, int posY) {
//        startTouchX = posX;
//        startTouchY = posY;
//        FrameLayout.LayoutParams lp =
//                (FrameLayout.LayoutParams) iv.getLayoutParams();
//        startViewX = lp.leftMargin;
//        startViewY = lp.topMargin;
//    }
//
//    private void updateTouch(View iv, MotionEvent motionEvent,
//                             int posX, int posY) {
//        int dx = posX - startTouchX;
//        int dy = posY - startTouchY;
//        FrameLayout.LayoutParams lp =
//                (FrameLayout.LayoutParams) iv.getLayoutParams();
//        lp.leftMargin = startViewX + dx;
//        lp.topMargin = startViewY + dy;
//        iv.setLayoutParams(lp);
//    }
//
//    private void finishTouch(View iv, MotionEvent motionEvent,
//                             int posX, int posY) {
//        startViewX = 0;
//        startViewY = 0;
//        startTouchX = 0;
//        startTouchY = 0;
//    }
//}

public class PrefActivity extends AppCompatActivity {
    private View logo;
    private GestureDetectorCompat detector;
    private VelocityTracker velocityTracker = null;
    private float xVelocity, yVelocity;
    private final int MIN_DISTANCE_MOVED = 100;
    private final int MAX_VELOCITY = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
        RequestQueue queue = newRequestQueue(getBaseContext());
        Response.Listener<Bitmap> listener = new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                ImageView iv = (ImageView) findViewById(R.id.logo);
                iv.setImageBitmap(response);
                detector = new GestureDetectorCompat(PrefActivity.this, new TouchGestureListener());
                iv.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return onTouchEvent(motionEvent);
                    }
                });
            }
        };
        ImageRequest imageRequest = new ImageRequest("https://usth.edu.vn/wp-content/uploads/2021/11/logo.png", listener, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888,null);
        queue.add(imageRequest);
        logo = findViewById(R.id.logo);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.detector.onTouchEvent(event);
//        int index = event.getActionIndex();
//        int action = event.getActionMasked();
//        int pointerId = event.getPointerId(index);
//
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                if (velocityTracker == null) {
//                    velocityTracker = VelocityTracker.obtain();
//                } else {
//                    velocityTracker.clear();
//                }
//                velocityTracker.addMovement(event);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                velocityTracker.addMovement(event);
//                velocityTracker.computeCurrentVelocity(1000);
//                xVelocity = VelocityTrackerCompat.getXVelocity(velocityTracker, pointerId);
//                yVelocity = VelocityTrackerCompat.getYVelocity(velocityTracker, pointerId);
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                velocityTracker.recycle();
//                velocityTracker = null;
//                break;
//        }
        return super.onTouchEvent(event);
    }

    private class TouchGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int dx = (int) (e2.getX() - e1.getX());
            int dy = (int) (e2.getY() - e1.getY());
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) logo.getLayoutParams();
            lp.leftMargin = lp.leftMargin + dx;
            lp.topMargin = lp.topMargin + dy;
            logo.setLayoutParams(lp);
            return true;
        }

//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            if (Math.abs(xVelocity) > MAX_VELOCITY || Math.abs(yVelocity) > MAX_VELOCITY)
//                return false;
//
//            float distanceX = e2.getX() - e1.getX();
//            float distanceY = e2.getY() - e1.getY();
//
//            if (Math.abs(distanceX) > MIN_DISTANCE_MOVED || Math.abs(distanceY) > MIN_DISTANCE_MOVED) {
//                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) logo.getLayoutParams();
//                lp.leftMargin += xVelocity / 10;
//                lp.topMargin += yVelocity / 10;
//                logo.setLayoutParams(lp);
//            }
//            return true;
//        }
    }
}
