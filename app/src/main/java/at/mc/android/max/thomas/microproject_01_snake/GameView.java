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
    private SnakeList snList = new SnakeList();



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
                //                Canvas c = sHolder.lockCanvas();
//                SharedPreferences sharedPrefs = getSharedPreferences(StartScreen.SHARED_PREFS, MODE_PRIVATE);
//                c.drawColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_BACKGROUND_COLOUR, 0x0));
                snList.pushBack(getRandom(fruitWidth, SViewWidth), getRandom(fruitWidth, SViewHeight));
                snList.pushFront(getRandom(fruitWidth, SViewWidth), getRandom(fruitWidth, SViewHeight));
                setPixel();
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
                float xDif = _e1.getX(0) - _e2.getX(0);
                float yDif = _e1.getY(0) - _e2.getY(0);
                float max = Math.max(Math.abs(xDif), Math.abs(yDif));
                //float max = Math.max(_distanceX, _distanceY);
                if (Math.abs(xDif) > Math.abs(yDif)) {
                    if (xDif < 0) {
                        if (!mNextMotion.equals("left") && !mNextMotion.equals("right")) {
                            mNextMotion = "right";//dir
                            Log.i("snake", "right");
                        } else {
                            Log.i("snake", "right/left detected");
                        }
                    } else {
                        if (!mNextMotion.equals("left") && !mNextMotion.equals("right")) {
                            mNextMotion = "left";
                            Log.i("snake", "left");
                        } else {
                            Log.i("snake", "left/right detected");
                        }
                    }
                } else {
                    if (yDif < 0) {
                        if (!mNextMotion.equals("up") && !mNextMotion.equals("down")) {
                            mNextMotion = "down";
                            Log.i("snake", "down");
                        } else {
                            Log.i("snake", "down/up detected");
                        }
                    } else {
                        if (!mNextMotion.equals("up") && !mNextMotion.equals("down")) {
                            mNextMotion = "up";
                            Log.i("snake", "up");
                        } else {
                            Log.i("snake", "up/down detected");
                        }
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
        Log.i("snake", _e.getX() + "/" + _e.getY());
        mG.onTouchEvent(_e);
        return true;
    }


    //Set a new Fruit into the game-view
    private boolean setFruit() {
        int x;
        int y;

        //Set the Fruit
        if (sHolder != null) {
            Canvas c = sHolder.lockCanvas();

            SharedPreferences sharedPrefs = getSharedPreferences(StartScreen.SHARED_PREFS, MODE_PRIVATE);

            c.drawColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_BACKGROUND_COLOUR, 0x0));

            Paint p = new Paint();
            p.setStrokeWidth(3.0f);
            p.setColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_FRUIT_COLOUR, 0x0));

            do {
                x = (int) (Math.random() * SViewWidth);
            } while (x < fruitWidth / 2 || x > SViewWidth - fruitWidth / 2);

            do {
                y = (int) (Math.random() * SViewHeight);
            } while (y < fruitWidth / 2 || y > SViewHeight - fruitWidth / 2);


            RectF rect = new RectF(x - fruitWidth / 2, y + fruitWidth / 2, x + fruitWidth / 2, y - fruitWidth / 2);
            c.drawRect(rect, p);

            Log.i(TAG, "draw fruit --> " + rect);
            sHolder.unlockCanvasAndPost(c);
        }
        return true;
    }



    private int getRandom(int border, int max) {
        int ret = Integer.MIN_VALUE;

        do {
            ret = (int) (Math.random() * max);
        } while (ret < border / 2 || ret > max - border / 2);

        return ret;
    }

    private int[] getRandomOld(int border) {
        int[] ret = new int[2];

        do {
            ret[0] = (int) (Math.random() * SViewWidth);
        } while (ret[0] < border / 2 || ret[0] > SViewWidth - border / 2);

        do {
            ret[1] = (int) (Math.random() * SViewHeight);
        } while (ret[1] < border / 2 || ret[1] > SViewHeight - border / 2);

        return ret;
    }


    //    private boolean first = true;
    private void setPixel() {

        if (sHolder != null) {
            Canvas c = sHolder.lockCanvas();

            SharedPreferences sharedPrefs = getSharedPreferences(StartScreen.SHARED_PREFS, MODE_PRIVATE);

            c.drawColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_BACKGROUND_COLOUR, 0x0));

            //The fruit
            Paint f = new Paint();
            f.setStrokeWidth(3.0f);
            f.setColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_FRUIT_COLOUR, 0x0));

            //The head
            Paint h = new Paint();
            h.setStrokeWidth(3.0f);
            h.setColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_SNAKE_HEAD_COLOUR, 0x0));

            //The tail
            Paint t = new Paint();
            t.setStrokeWidth(3.0f);
            t.setColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_SNAKE_BODY_COLOUR, 0x0));


            /**
             * This method sets the snake and the foot
             * The first element of the list is the head, the last is the foot
             * Between is the tail of the snake.
             */
            SnakeNode n = snList.getHead();
            for (int i = 0; i < snList.elements(); i++) {

                if (i == 0) {
                    c.drawRect(new RectF(n.getValueX() - fruitWidth / 2, n.getValueY() + fruitWidth / 2, n.getValueX() + fruitWidth / 2, n.getValueY() - fruitWidth / 2), h);
                } else if (i == snList.elements() - 1) {
                    c.drawRect(new RectF(n.getValueX() - fruitWidth / 2, n.getValueY() + fruitWidth / 2, n.getValueX() + fruitWidth / 2, n.getValueY() - fruitWidth / 2), f);
                } else {
                    c.drawRect(new RectF(n.getValueX() - fruitWidth / 2, n.getValueY() + fruitWidth / 2, n.getValueX() + fruitWidth / 2, n.getValueY() - fruitWidth / 2), t);
                }

                n = n.getNext();
                Log.i(TAG, "draw fruit --> ");

            }
            sHolder.unlockCanvasAndPost(c);
        }
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
