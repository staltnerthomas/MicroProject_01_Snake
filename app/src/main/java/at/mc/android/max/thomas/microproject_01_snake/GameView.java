package at.mc.android.max.thomas.microproject_01_snake;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class GameView extends Activity implements SurfaceHolder.Callback, View.OnTouchListener {

    public static final String TAG = "Snake 01 GameView";

    private SurfaceView gameSurfaceView = null;
    private SurfaceHolder sHolder;
    Handler mHandler = new Handler();

    private int SViewWidth;
    private int SViewHeight;

    private int snakeWidth = 15;
    private int snakeHead = 20;
    private int fruitWidth = snakeWidth;







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
                //switch id //test commit
            }
        });
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
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
