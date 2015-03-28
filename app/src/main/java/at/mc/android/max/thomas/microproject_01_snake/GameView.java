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
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String UP = "up";
    public static final String DOWN = "down";
    Handler mHandler = new Handler();
    GestureDetector mG = null;
    String mNextMotion = "default";
    private SurfaceView gameSurfaceView = null;
    private SurfaceHolder sHolder;
    private int SViewWidth;
    private int SViewHeight;
    private int snakeBodyWidth = 25;
    private int fruitWidth = snakeBodyWidth;
    private int snakeHeadWidth = 35;
    private int snakeHeadHeight = 18;
    private int snakeHeadEye = 4;

    private SnakeList snList = new SnakeList();
    private float delayTime = 500;


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
                Log.i(TAG, "surfaceChanged!");
                SViewWidth = width;
                SViewHeight = height;
                //                Canvas c = sHolder.lockCanvas();
//                SharedPreferences sharedPrefs = getSharedPreferences(StartScreen.SHARED_PREFS, MODE_PRIVATE);
//                c.drawColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_BACKGROUND_COLOUR, 0x0));


                setSnakeAtBeginning();
                setFruit();


                drawGameView();
                playGame();
            }

            private void playGame() {
                Log.i(TAG, "playGame!");


                //This is for playing the game
                if (!gameOver()) {

                    moveSnake();
                    if(!isFruitEaten()){
                        cutTail();
                    } else {
                        setFruit();
                    }
                    delayTime =delayTime * 0.99f;
                    drawGameView();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playGame();
                        }
                    }, ((int) delayTime));


                }
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
                        if (!mNextMotion.equals(LEFT) && !mNextMotion.equals(RIGHT)) {
                            mNextMotion = RIGHT;//dir
                            Log.i("snake", RIGHT);
                        } else {
                            Log.i("snake", "right/left detected");
                        }
                    } else {
                        if (!mNextMotion.equals(LEFT) && !mNextMotion.equals(RIGHT)) {
                            mNextMotion = LEFT;
                            Log.i("snake", LEFT);
                        } else {
                            Log.i("snake", "left/right detected");
                        }
                    }
                } else {
                    if (yDif < 0) {
                        if (!mNextMotion.equals(UP) && !mNextMotion.equals(DOWN)) {
                            mNextMotion = UP;
                            Log.i("snake", UP);
                        } else {
                            Log.i("snake", "down/up detected");
                        }
                    } else {
                        if (!mNextMotion.equals(UP) && !mNextMotion.equals(DOWN)) {
                            mNextMotion = DOWN;
                            Log.i("snake", DOWN);
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



    private boolean gameOver() {
        Log.i(TAG, "gameOver!");
        if (snList.getHead().getValueX() >= SViewWidth - snakeHeadHeight/* / 2 */ || snList.getHead().getValueX() <= 0 + snakeHeadHeight /* / 2 */ || snList.getHead().getValueY() <= 0 + snakeHeadHeight /* / 2 */ || snList.getHead().getValueY() >= SViewHeight - snakeHeadHeight /* / 2 */) {
            return true;
        }

        SnakeNode n = snList.getHead().getNext();
        SnakeNode head = snList.getHead();

        for (int i = 0; i <= snList.elements() - 3; i++) {
            if(head.getValueX() >= n.getValueX() - snakeBodyWidth/2 && head.getValueX() <= n.getValueX() + snakeBodyWidth/2){
                //Head and Body has the same x-coordinate
                if(head.getValueY() >= n.getValueY() - snakeBodyWidth/2 && head.getValueY() <= n.getValueY() + snakeBodyWidth/2){
                    //Head and Body are in identical regions
                    return true;
                }
            }
            n=n.getNext();
        }

        return false;
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
    private void drawGameView() {


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

            //The eye
            Paint e = new Paint();
            e.setStrokeWidth(3.0f);
            e.setColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_SNAKE_EYE_COLOUR, 0x0));


            /**
             * This method sets the snake and the foot
             * The first element of the list is the head, the last is the foot
             * Between is the tail of the snake.
             */
            SnakeNode n = snList.getHead();
            double factorsnakeHeadWidth = 0.3;
            double factorsnakeHeadHeight = 0.5;
            for (int i = 0; i < snList.elements(); i++) {


                if (i == 0) {
                    //Draw the head of the snake
                    if (mNextMotion.equals(LEFT) || mNextMotion.equals(RIGHT)) {
                        c.drawOval(new RectF(n.getValueX() - snakeHeadHeight / 2, n.getValueY() + snakeHeadWidth / 2, n.getValueX() + snakeHeadHeight / 2, n.getValueY() - snakeHeadWidth / 2), h);

                        if (mNextMotion.equals(RIGHT)) {
                            c.drawOval(new RectF(n.getValueX() + ((int) (snakeHeadWidth * factorsnakeHeadWidth)) - snakeHeadEye, n.getValueY() - ((int) (snakeHeadHeight * factorsnakeHeadHeight)) + snakeHeadEye, n.getValueX() + ((int) (snakeHeadWidth * factorsnakeHeadWidth)) + snakeHeadEye, n.getValueY() - ((int) (snakeHeadHeight * factorsnakeHeadHeight)) - snakeHeadEye), e);
                            c.drawOval(new RectF(n.getValueX() + ((int) (snakeHeadWidth * factorsnakeHeadWidth)) - snakeHeadEye, n.getValueY() + ((int) (snakeHeadHeight * factorsnakeHeadHeight)) + snakeHeadEye, n.getValueX() + ((int) (snakeHeadWidth * factorsnakeHeadWidth)) + snakeHeadEye, n.getValueY() + ((int) (snakeHeadHeight * factorsnakeHeadHeight)) - snakeHeadEye), e);
                        } else {
                            c.drawOval(new RectF(n.getValueX() - ((int) (snakeHeadWidth * factorsnakeHeadWidth)) - snakeHeadEye, n.getValueY() - ((int) (snakeHeadHeight * factorsnakeHeadHeight)) + snakeHeadEye, n.getValueX() - ((int) (snakeHeadWidth * factorsnakeHeadWidth)) + snakeHeadEye, n.getValueY() - ((int) (snakeHeadHeight * factorsnakeHeadHeight)) - snakeHeadEye), e);
                            c.drawOval(new RectF(n.getValueX() - ((int) (snakeHeadWidth * factorsnakeHeadWidth)) - snakeHeadEye, n.getValueY() + ((int) (snakeHeadHeight * factorsnakeHeadHeight)) + snakeHeadEye, n.getValueX() - ((int) (snakeHeadWidth * factorsnakeHeadWidth)) + snakeHeadEye, n.getValueY() + ((int) (snakeHeadHeight * factorsnakeHeadHeight)) - snakeHeadEye), e);
                        }
                    } else {
                        c.drawOval(new RectF(n.getValueX() - snakeHeadWidth / 2, n.getValueY() + snakeHeadHeight / 2, n.getValueX() + snakeHeadWidth / 2, n.getValueY() - snakeHeadHeight / 2), h);

                        if (mNextMotion.equals(UP)) {
                            c.drawOval(new RectF(n.getValueX() - ((int) (snakeHeadWidth * factorsnakeHeadWidth)) - snakeHeadEye, n.getValueY() + ((int) (snakeHeadHeight * factorsnakeHeadHeight)) + snakeHeadEye, n.getValueX() - ((int) (snakeHeadWidth * factorsnakeHeadWidth)) + snakeHeadEye, n.getValueY() + ((int) (snakeHeadHeight * factorsnakeHeadHeight)) - snakeHeadEye), e);
                            c.drawOval(new RectF(n.getValueX() + ((int) (snakeHeadWidth * factorsnakeHeadWidth)) - snakeHeadEye, n.getValueY() + ((int) (snakeHeadHeight * factorsnakeHeadHeight)) + snakeHeadEye, n.getValueX() + ((int) (snakeHeadWidth * factorsnakeHeadWidth)) + snakeHeadEye, n.getValueY() + ((int) (snakeHeadHeight * factorsnakeHeadHeight)) - snakeHeadEye), e);
                        } else {
                            c.drawOval(new RectF(n.getValueX() - ((int) (snakeHeadWidth * factorsnakeHeadWidth)) - snakeHeadEye, n.getValueY() - ((int) (snakeHeadHeight * factorsnakeHeadHeight)) + snakeHeadEye, n.getValueX() - ((int) (snakeHeadWidth * factorsnakeHeadWidth)) + snakeHeadEye, n.getValueY() - ((int) (snakeHeadHeight * factorsnakeHeadHeight)) - snakeHeadEye), e);
                            c.drawOval(new RectF(n.getValueX() + ((int) (snakeHeadWidth * factorsnakeHeadWidth)) - snakeHeadEye, n.getValueY() - ((int) (snakeHeadHeight * factorsnakeHeadHeight)) + snakeHeadEye, n.getValueX() + ((int) (snakeHeadWidth * factorsnakeHeadWidth)) + snakeHeadEye, n.getValueY() - ((int) (snakeHeadHeight * factorsnakeHeadHeight)) - snakeHeadEye), e);
                        }
                    }
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

        SnakeNode n = snList.getHead();
        for (int i = 0; i < (snList.elements() - 2); i++) {
            //Because last node is the foot
            if (x >= n.getValueX() - snakeBodyWidth && x <= n.getValueX() + snakeBodyWidth) {
                //Fruit is in the same x-line
                if (y >= n.getValueY() - snakeBodyWidth && y <= n.getValueY() + snakeBodyWidth) {
                    //Fruit is in the same x-line and y-line
                    setFruit();
                }
            }
            n = n.getNext();
        }
        snList.getTail().setValueX(x);
        snList.getTail().setValueY(y);

    }

    private boolean isFruitEaten() {
        if (snList != null) {
            if (snList.getTail().getValueY() >= snList.getHead().getValueY() - snakeHeadWidth / 2 && snList.getTail().getValueY() <= snList.getHead().getValueY() + snakeHeadWidth / 2) {
                //same Height of Head and Fruit
                if (snList.getTail().getValueX() >= snList.getHead().getValueX() - snakeHeadHeight / 2 && snList.getTail().getValueX() <= snList.getHead().getValueX() + snakeHeadHeight / 2) {
                    //same Height of Head and Fruit
                    //same Width of Head and Fruit

                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private void moveSnake() {
        switch (mNextMotion) {
            case LEFT: {
                snList.pushFront(snList.getHead().getValueX() - snakeBodyWidth, snList.getHead().getValueY());
            }
            break;

            case RIGHT: {
                snList.pushFront(snList.getHead().getValueX() + snakeBodyWidth, snList.getHead().getValueY());
            }
            break;

            case UP: {
                snList.pushFront(snList.getHead().getValueX(), snList.getHead().getValueY() + snakeBodyWidth);
            }
            break;

            case DOWN: {
                snList.pushFront(snList.getHead().getValueX(), snList.getHead().getValueY() - snakeBodyWidth);
            }
            break;
            default:
        }
    }

    private void cutTail() {
        snList.removeTail();
    }


    //Sets the first Snake in the GameField
    private void setSnakeAtBeginning() {
        int x = getRandom(3 * Math.max(snakeHeadWidth, snakeHeadHeight), SViewWidth);
        int y = getRandom(3 * Math.max(snakeHeadWidth, snakeHeadHeight), SViewHeight);
        int dir = (int) (Math.random() * 5);

        snList.pushFront(x, y);

        switch (dir) {
            case 1: {
                snList.pushFront(x + snakeBodyWidth, y);
                snList.pushFront(x + 2 * snakeBodyWidth, y);
                mNextMotion = RIGHT;
                snList.getHead().setValueX(snList.getHead().getValueX() - snakeHeadHeight / 2);
            }
            break;

            case 2: {
                snList.pushFront(x - snakeBodyWidth, y);
                snList.pushFront(x - 2 * snakeBodyWidth, y);
                mNextMotion = LEFT;
                snList.getHead().setValueX(snList.getHead().getValueX() + snakeHeadHeight / 2);
            }
            break;

            case 3: {
                snList.pushFront(x, y + snakeBodyWidth);
                snList.pushFront(x, y + 2 * snakeBodyWidth);
                mNextMotion = UP;
                snList.getHead().setValueY(snList.getHead().getValueY() - snakeHeadHeight / 2);
            }
            break;

            case 4: {
                snList.pushFront(x, y - snakeBodyWidth);
                snList.pushFront(x, y - 2 * snakeBodyWidth);
                mNextMotion = DOWN;
                snList.getHead().setValueY(snList.getHead().getValueY() + snakeHeadHeight / 2);
            }
            break;
            default: {
                snList.pushFront(x, y - snakeBodyWidth);
                snList.pushFront(x, y - 2 * snakeBodyWidth);
                mNextMotion = DOWN;
                snList.getHead().setValueY(snList.getHead().getValueY() + snakeHeadHeight / 2);
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
