package at.mc.android.max.thomas.microproject_01_snake;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;


public class GameView extends Activity implements SurfaceHolder.Callback, View.OnTouchListener {

    public static final String TAG = "Snake 01 GameView";
    Handler mHandler = new Handler();
    GestureDetector mG = null;
    String mNextMotion = "default";
    private SurfaceView gameSurfaceView = null;
    private SurfaceHolder sHolder;
    private int SViewWidth;
    private int SViewHeight;
    private int snakeWidth = 15;
    private int fruitWidth = snakeWidth;
    private int snakeHead = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);

//        //Set Screen to Fullscreen
//        View v = findViewById(android.R.id.content).getRootView();
//        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        //get Display with and heigth
        gameSurfaceView = (SurfaceView) findViewById(R.id.game_surface_view);
        gameSurfaceView.setOnTouchListener(this);
        gameSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i(TAG, "surfaceCreated!");
                sHolder = holder;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                SViewWidth = width;
                SViewHeight = height;
                setFruit();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        mG = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent _e1, MotionEvent _e2, float _distanceX, float _distanceY) {
                Toast.makeText(GameView.this, "scroll detected", Toast.LENGTH_SHORT).show();
                float xDif = _e1.getX() - _e2.getX();
                float yDif = _e1.getY() - _e2.getY();
                float max = Math.max(Math.abs(xDif), Math.abs(yDif));
                if (max - _distanceX < 0.0001) {
                    if (xDif < 0) {
                        mNextMotion = "right";
                        Log.e("snake", "right");
                    } else {
                        mNextMotion = "left";
                        Log.e("snake", "left");
                    }
                } else if (max - _distanceY < 0.0001) {
                    if (yDif < 0) {
                        mNextMotion = "down";
                        Log.e("snake", "down");
                    } else {
                        mNextMotion = "up";
                        Log.e("snake", "up");
                    }
                }
                //start motion method call
                return true;
            }

            @Override
            public void onLongPress(MotionEvent _e) {
                Toast.makeText(GameView.this, "long press detected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                return false;
            }
        }) {

        };
    }


    @Override
    public boolean onTouch(View _v, MotionEvent _e) {
        mG.onTouchEvent(_e);
        return true;
    }



    //Set a new Fruit into the game-view
    private boolean setFruit() {
        int x;
        int y;

        //Set the Fruit
        if(sHolder != null) {
            Canvas c = sHolder.lockCanvas();

            SharedPreferences sharedPrefs = getSharedPreferences(StartScreen.SHARED_PREFS, MODE_PRIVATE);

            c.drawColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_BACKGROUND_COLOUR, 0x0));

            Paint p = new Paint();
            p.setStrokeWidth(3.0f);
            p.setColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_FRUIT_COLOUR, 0x0));

            do {
                x = (int) (Math.random() * SViewWidth);
            } while (x < fruitWidth / 2 || x > SViewWidth - fruitWidth / 2 );

            do {
                y = (int) (Math.random() * SViewHeight);
            } while (y < fruitWidth / 2 || y > SViewHeight - fruitWidth / 2 );


            RectF rect = new RectF(x-fruitWidth/2, y+fruitWidth/2, x+fruitWidth/2, y-fruitWidth/2);
            c.drawRect(rect,p);

            Log.i(TAG, "draw fruit --> " + rect);
            sHolder.unlockCanvasAndPost(c);
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder _holder) {
        sHolder = _holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        sHolder = null;
    }
}
