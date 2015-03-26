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
    private int snakeBody = 15;
    private int fruitWidth = snakeBody;
    private int snakeHead = 30;
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


                setSnakeAtBeginning();
                setFruit();


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
                Toast.makeText(GameView.this, "scroll detected", Toast.LENGTH_SHORT).show();
                float xDif = _e1.getX() - _e2.getX();
                float yDif = _e1.getY() - _e2.getY();
                float max = Math.max(Math.abs(xDif), Math.abs(yDif));
                //float max = Math.max(_distanceX, _distanceY);
                if (Math.abs(xDif) > Math.abs(yDif)) {
                    if (xDif < 0) {
                        if (!mNextMotion.equals("left")) {
                            mNextMotion = "right";//dir
                            Log.i("snake", "right");
                        } else {
                            Log.i("snake", "right detected");
                        }
                    } else {
                        if (!mNextMotion.equals("right")) {
                            mNextMotion = "left";
                            Log.i("snake", "left");
                        } else {
                            Log.i("snake", "left detected");
                        }
                    }
                } else {
                    if (yDif < 0) {
                        if (!mNextMotion.equals("up")) {
                            mNextMotion = "down";
                            Log.i("snake", "down");
                        } else {
                            Log.i("snake", "down detected");
                        }
                    } else {
                        if (!mNextMotion.equals("down")) {
                            mNextMotion = "up";
                            Log.i("snake", "up");
                        } else {
                            Log.i("snake", "up detected, not done...");
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
        // Max
        Log.i("snake", _e.getX() + "/" + _e.getY());
        mG.onTouchEvent(_e);
        return true;
    }


    //Set a new Fruit into the game-view
//    private boolean setFruit() {
//        int x;
//        int y;
//
//        //Set the Fruit
//        if (sHolder != null) {
//            Canvas c = sHolder.lockCanvas();
//
//            SharedPreferences sharedPrefs = getSharedPreferences(StartScreen.SHARED_PREFS, MODE_PRIVATE);
//
//            c.drawColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_BACKGROUND_COLOUR, 0x0));
//
//            Paint p = new Paint();
//            p.setStrokeWidth(3.0f);
//            p.setColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_FRUIT_COLOUR, 0x0));
//
//            do {
//                x = (int) (Math.random() * SViewWidth);
//            } while (x < fruitWidth / 2 || x > SViewWidth - fruitWidth / 2);
//
//            do {
//                y = (int) (Math.random() * SViewHeight);
//            } while (y < fruitWidth / 2 || y > SViewHeight - fruitWidth / 2);
//
//
//            RectF rect = new RectF(x - fruitWidth / 2, y + fruitWidth / 2, x + fruitWidth / 2, y - fruitWidth / 2);
//            c.drawRect(rect, p);
//
//            Log.i(TAG, "draw fruit --> " + rect);
//            sHolder.unlockCanvasAndPost(c);
//        }
//        return true;
//    }


    private int getRandom(int border, int max) {
        int ret = Integer.MIN_VALUE;

        do {
            ret = (int) (Math.random() * (max + 1));
        } while (ret < border / 2 || ret > max - border / 2);

        return ret;
    }

//    private int[] getRandomOld(int border) {
//        int[] ret = new int[2];
//
//        do {
//            ret[0] = (int) (Math.random() * SViewWidth);
//        } while (ret[0] < border / 2 || ret[0] > SViewWidth - border / 2);
//
//        do {
//            ret[1] = (int) (Math.random() * SViewHeight);
//        } while (ret[1] < border / 2 || ret[1] > SViewHeight - border / 2);
//
//        return ret;
//    }


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

    private void setFruit() {
        int x = getRandom(fruitWidth, SViewWidth);
        int y = getRandom(fruitWidth, SViewHeight);

        snList.pushBack(x, y);

    }


    //Sets the first Snake in the GameField
    private void setSnakeAtBeginning() {
        int x = getRandom(3 * snakeHead, SViewWidth);
        int y = getRandom(3 * snakeHead, SViewHeight);
        int dir = (int) (Math.random() * 5);

        snList.pushFront(x, y);

        switch (dir) {
            case 1: {
                snList.pushFront(x + snakeBody, y);
                snList.pushFront(x + 2 * snakeBody, y);
                mNextMotion = "right";
            }
            break;

            case 2: {
                snList.pushFront(x - snakeBody, y);
                snList.pushFront(x - 2 * snakeBody, y);
                mNextMotion = "left";
            }
            break;

            case 3: {
                snList.pushFront(x, y + snakeBody);
                snList.pushFront(x, y + 2 * snakeBody);
                mNextMotion = "up";
            }
            break;

            case 4: {
                snList.pushFront(x, y - snakeBody);
                snList.pushFront(x, y - 2 * snakeBody);
                mNextMotion = "down";
            }
            break;
            default: {
                snList.pushFront(x, y - snakeBody);
                snList.pushFront(x, y - 2 * snakeBody);
                mNextMotion = "down";
            }

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
