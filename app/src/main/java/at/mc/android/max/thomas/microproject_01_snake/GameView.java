package at.mc.android.max.thomas.microproject_01_snake;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class GameView extends Activity implements SensorEventListener, SurfaceHolder.Callback, View.OnTouchListener {

    public static final String TAG = "GameView";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String UP = "up";
    public static final String DOWN = "down";
    public List<Coordinates> snakeList = Collections.synchronizedList(new LinkedList<Coordinates>());
    //defines the SurfaceView
    public SurfaceView gameSurfaceView = null;
    Handler mHandler = new Handler();
    GestureDetector mG = null;
    String mNextMotion = "default";
    private SurfaceHolder sHolder;
    private int SViewWidth;
    private int SViewHeight;
    //defines the Fruit
    private int dimensionFruit = 25;            // 25
    //Defines the snake
    private int dimensionSnakeBodyWidth = 25;   // 25
    private int dimensionSnakeHeadWidth = 35;   // 35
    private int dimensionSnakeHeadHeight = 18;  // 18
    private int snakeHeadEye = 4;               // 4

    //defines constants
    private float delayTime;                            //theese two variables are used to calculate the delaytime
    private float slowestDelayTime = 450;               //      which means the snake is in off state

    private float gamePoints = 0;                       //theese two variables are used for calculating
    private float gamePointsBonus = 80;                 //  the points after eating

    private boolean firstLongPress = true;              //Set if it was the first Long press for go on pause and resume
    private boolean gamePause = false;                  //is true if game is in pause-modus
    private boolean firstSurfaceCreated = false;        //is used for pause function not to create a new snake after resume
    private int tongueCounter;                          //is used for how often should go the tongue out of the mouth
    private boolean mayNextMotion = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);
        if (StartScreen.testMode)
            Toast.makeText(GameView.this, "onCreate...", Toast.LENGTH_SHORT).show();

        Toast.makeText(this, R.string.game_toast_start_the_game, Toast.LENGTH_SHORT).show();

        //Set the dimensions of the Snake and Fruit
        //as preselected in the settings
        SharedPreferences sharedPrefs = getSharedPreferences(StartScreen.SHARED_PREFS, MODE_PRIVATE);
        switch (sharedPrefs.getInt(Settings.OPTIONS_SEEKBAR_SIZE_OF_THE_SNAKE, 3)) {
            case 0: {
                //defines the Fruit
                dimensionFruit = 19;            // 25
                //Defines the snake
                dimensionSnakeBodyWidth = 19;   // 25
                dimensionSnakeHeadWidth = 27;   // 35
                dimensionSnakeHeadHeight = 14;  // 18
                snakeHeadEye = 3;
            }
            break;

            case 1: {
                //defines the Fruit
                dimensionFruit = 22;
                //Defines the snake
                dimensionSnakeBodyWidth = 22;
                dimensionSnakeHeadWidth = 31;
                dimensionSnakeHeadHeight = 16;
                snakeHeadEye = 4;
            }
            break;

            case 2: {
                //defines the Fruit
                dimensionFruit = 25;
                //Defines the snake
                dimensionSnakeBodyWidth = 25;
                dimensionSnakeHeadWidth = 35;
                dimensionSnakeHeadHeight = 18;
                snakeHeadEye = 4;
            }
            break;

            case 3: {
                //defines the Fruit
                dimensionFruit = 28;
                //Defines the snake
                dimensionSnakeBodyWidth = 28;
                dimensionSnakeHeadWidth = 39;
                dimensionSnakeHeadHeight = 20;
                snakeHeadEye = 4;
            }
            break;

            default: {
                //defines the Fruit
                dimensionFruit = 31;
                //Defines the snake
                dimensionSnakeBodyWidth = 31;
                dimensionSnakeHeadWidth = 43;
                dimensionSnakeHeadHeight = 22;
                snakeHeadEye = 5;
            }
        }

        //Initialize the SensorManager
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        //get Display with and heigth
        //for calculating random numbers for each display
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
                if (!firstSurfaceCreated) {
                        setSnakeAtBeginning();
                    firstSurfaceCreated = true;
                }
                drawGameView();
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
                SharedPreferences sharedPrefs = getSharedPreferences(StartScreen.SHARED_PREFS, MODE_PRIVATE);

                //if a scroll is detected the code below sets
                //the variables for "UP" "DOWN" "LEFT" "RIGHT"
                if (sharedPrefs.getInt(Settings.OPTIONS_SWITCH_CONTROL, -1) == 0) {
                    float xDif = _e1.getX(0) - _e2.getX(0);
                    float yDif = _e1.getY(0) - _e2.getY(0);
                    float max = Math.max(Math.abs(xDif), Math.abs(yDif));
                    if (Math.abs(xDif) > Math.abs(yDif)) {
                        if (xDif < 0) {
                            if (!mNextMotion.equals(LEFT) && !mNextMotion.equals(RIGHT)) {
                                if(mayNextMotion) {
                                    mNextMotion = RIGHT;//dir
                                    mayNextMotion = false;
                                }
                                Log.i("snake", RIGHT);
                            } else {
                                Log.i("snake", "right/left detected");
                            }
                        } else {
                            if (!mNextMotion.equals(LEFT) && !mNextMotion.equals(RIGHT)) {
                                if(mayNextMotion) {
                                    mNextMotion = LEFT;//dir
                                    mayNextMotion = false;
                                }
                                Log.i("snake", LEFT);
                            } else {
                                Log.i("snake", "left/right detected");
                            }
                        }
                    } else {
                        if (yDif < 0) {
                            if (!mNextMotion.equals(UP) && !mNextMotion.equals(DOWN)) {
                                if(mayNextMotion) {
                                    mNextMotion = UP;//dir
                                    mayNextMotion = false;
                                }
                                Log.i("snake", UP);
                            } else {
                                Log.i("snake", "down/up detected");
                            }
                        } else {
                            if (!mNextMotion.equals(UP) && !mNextMotion.equals(DOWN)) {
                                if(mayNextMotion) {
                                    mNextMotion = DOWN;//dir
                                    mayNextMotion = false;
                                }
                                Log.i("snake", DOWN);
                            } else {
                                Log.i("snake", "up/down detected");
                            }
                        }
                    }
                    //start motion method call
                    return true;
                }
                return false;
            }

            /**
             * This method detects a long press and switch between
             * playing the game and go on pause
             * @param _e
             */
            @Override
            public void onLongPress(MotionEvent _e) {
                //Long press is used for start the game and go on pause
                if (firstLongPress) {
                    if (StartScreen.testMode)
                        Toast.makeText(GameView.this, "continue with game...", Toast.LENGTH_SHORT).show();
                    if (snakeList.get(0).getCoorX() == SViewWidth / 2)
                        //Because the message should only be shown once
                        //for the player at the beginning
                        Toast.makeText(GameView.this, R.string.game_toast_go_on_pause, Toast.LENGTH_SHORT).show();
                    gamePause = false;
                    firstLongPress = false;
                    playGame();
                } else {
                    if (StartScreen.testMode)
                        Toast.makeText(GameView.this, "pause...", Toast.LENGTH_SHORT).show();
                    gamePause = true;
                    firstLongPress = true;
                }
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (StartScreen.testMode)
            Toast.makeText(GameView.this, "onResume...", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPrefs = getSharedPreferences(StartScreen.SHARED_PREFS, MODE_PRIVATE);
        delayTime = slowestDelayTime - (sharedPrefs.getInt(Settings.OPTIONS_SEEKBAR_TAKEOFFSPEED, 0));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (StartScreen.testMode)
            Toast.makeText(GameView.this, "onPause...", Toast.LENGTH_SHORT).show();
        gamePause = true;
        firstLongPress = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * This method checks if the game is over
     * The game is over, if the snake runs across the border or it runs against its tail.
     *
     * @return boolean
     */
    private boolean gameOver() {
        Log.i(TAG, "gameOver!");
        Coordinates headCoordinate = snakeList.get(0);

        if (headCoordinate.getCoorX() >= SViewWidth - dimensionSnakeHeadHeight / 4 ||
                headCoordinate.getCoorX() <= dimensionSnakeHeadHeight / 4 ||
                headCoordinate.getCoorY() <= dimensionSnakeHeadHeight / 4 ||
                headCoordinate.getCoorY() >= SViewHeight - dimensionSnakeHeadHeight / 4) {
            playGameOverSound();
            return true;
        }

        for (int i = 1; i <= (snakeList.size() - 2); i++) {
            //i = 1 because first Node is the head itself
            Coordinates coor = snakeList.get(i);

            int rangeXBodyMin = coor.getCoorX() - (dimensionSnakeBodyWidth / 2);
            int rangeXBodyMax = coor.getCoorX() + (dimensionSnakeBodyWidth / 2);

            int rangeYBodyMin = coor.getCoorY() - (dimensionSnakeBodyWidth / 2);
            int rangeYBodyMax = coor.getCoorY() + (dimensionSnakeBodyWidth / 2);

            if (snakeList != null) {
                if (headCoordinate.getCoorX() >= rangeXBodyMin && headCoordinate.getCoorX() <= rangeXBodyMax && headCoordinate.getCoorY() >= rangeYBodyMin && headCoordinate.getCoorY() <= rangeYBodyMax) {
                    playGameOverSound();
                    return true;
                }
            }
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

    /**
     * This method coordinates the gameplay
     * increases the gamePoints and lets the snake move faster
     */
    private void playGame() {
        Log.i(TAG, "playGame!");
        //This is for playing the game
        if (!gameOver() && !gamePause) {
            moveSnake();
            if (!isFruitEaten()) {
                cutTail();
            } else {
                setNextFruit();
                delayTime = delayTime * 0.975f;
                if (gamePoints == 0) {
                    gamePoints = 100;
                } else {
                    gamePoints += gamePointsBonus;
                    gamePointsBonus *= 1.05f;
                }
            }
            drawGameView();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playGame();
                }
            }, ((int) delayTime));
        } else {
            SharedPreferences sharedPrefs = getSharedPreferences(StartScreen.SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();

            if (gamePoints > sharedPrefs.getInt(StartScreen.GAME_VIEW_HI_SCORE, -1)) {
                editor.putInt(StartScreen.GAME_VIEW_HI_SCORE, (int) gamePoints);
            }
            editor.putInt(StartScreen.GAME_VIEW_LAST_SCORE, (int) gamePoints);
            editor.apply();

            if (gameOver() && !gamePause) {
                Log.i(TAG, "gameOver() && !gamePause");
                DialogFragment dialog = new GameViewDialogFragment();
                dialog.show(getFragmentManager(), "Game View Dialog Fragment");
            }
        }
    }

    /**
     * Returns the random coordinates for the fruit.
     *
     * @return x and y-Coordinates
     */
    private Coordinates getRandomCoordinates() {
        //new Thomas
        int widthRangeMin = dimensionFruit /* / 2 */;
        int widthRangeMax = SViewWidth - dimensionFruit /* / 2 */;
        int heightRangeMin = dimensionFruit /* / 2 */;
        int heightRangeMax = SViewHeight - dimensionFruit /* / 2 */;

        int xCoor = 0;
        int yCoor = 0;

        do {
            xCoor = randomInRange(SViewWidth);
        } while (!(xCoor >= widthRangeMin && xCoor <= widthRangeMax));

        do {
            yCoor = randomInRange(SViewHeight);
        } while (!(yCoor >= heightRangeMin && yCoor <= heightRangeMax));

        return new Coordinates(xCoor, yCoor);
    }

    private int randomInRange(int max) {
        //new Thomas
        return (int) (Math.random() * (max + 1));
    }

    private void setNextFruit() {
        Coordinates fruitCoordinate = getRandomCoordinates();

        if (isValidFruitCoordinate(fruitCoordinate)) {
            snakeList.set(snakeList.size() - 1, fruitCoordinate);
        } else {
            setNextFruit();
        }
    }

    private boolean isValidFruitCoordinate(Coordinates fruitCoordinate) {
        for (int i = 0; i <= (snakeList.size() - 2); i++) {
            //run over the Snake and look if the Fruit is not in the Snake
            Coordinates coor = snakeList.get(i);

            int rangeXBodyMin = coor.getCoorX() - (dimensionSnakeBodyWidth / 2) - (dimensionFruit / 2);
            int rangeXBodyMax = coor.getCoorX() + (dimensionSnakeBodyWidth / 2) + (dimensionFruit / 2);

            int rangeYBodyMin = coor.getCoorY() - (dimensionSnakeBodyWidth / 2) - (dimensionFruit / 2);
            int rangeYBodyMax = coor.getCoorY() + (dimensionSnakeBodyWidth / 2) + (dimensionFruit / 2);

            if (snakeList != null) {
                if (fruitCoordinate.getCoorX() >= rangeXBodyMin && fruitCoordinate.getCoorX() <= rangeXBodyMax && fruitCoordinate.getCoorY() >= rangeYBodyMin && fruitCoordinate.getCoorY() <= rangeYBodyMax) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Draws the GameView
     * The given ListNodes contains the Coordinates-object for the Body and the fruit
     */
    private void drawGameView() {
        Log.i(TAG, "drawGameView!");
        //new Thomas
        TextView teViewPoints = (TextView) findViewById(R.id.game_text_view_score);
        teViewPoints.setText(String.valueOf((int) (gamePoints)));

        if (sHolder != null) {
            Canvas c = sHolder.lockCanvas();

            SharedPreferences sharedPrefs = getSharedPreferences(StartScreen.SHARED_PREFS, MODE_PRIVATE);

            c.drawColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_BACKGROUND_COLOR, StartScreen.DEFAULT_GAME_VIEW_BACKGROUND_COLOR));

            //The fruit
            Paint fruit = new Paint();
            fruit.setStrokeWidth(1.0f);
            fruit.setColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_FRUIT_COLOUR, StartScreen.DEFAULT_GAME_VIEW_FRUIT_COLOR));

            //The head
            Paint head = new Paint();
            head.setStrokeWidth(1.0f);
            head.setColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_SNAKE_HEAD_COLOR, StartScreen.DEFAULT_GAME_VIEW_SNAKE_HEAD_COLOR));

            //The tail
            Paint tail = new Paint();
            tail.setStrokeWidth(1.0f);
            tail.setColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_SNAKE_BODY_COLOR, StartScreen.DEFAULT_GAME_VIEW_SNAKE_BODY_COLOR));

            //The eye
            Paint eye = new Paint();
            eye.setStrokeWidth(1.0f);
            eye.setColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_SNAKE_EYE_COLOR, StartScreen.DEFAULT_GAME_VIEW_SNAKE_EYE_COLOR));

            //The Tongue
            Paint tongue = new Paint();
            tongue.setStrokeWidth(5.0f);
            tongue.setColor(sharedPrefs.getInt(StartScreen.GAME_VIEW_SNAKE_TONGUE_COLOR, StartScreen.DEFAULT_GAME_VIEW_SNAKE_TONGUE_COLOR));


            /**
             * This method sets the snake and the foot
             * The first element of the list is the head, the last is the foot
             * Between is the tail of the snake.
             */
            double factorDimensionSnakeHeadWidth = 0.3;
            double factorDimensionSnakeHeadHeight = 0.5;
            int headDelayToBody = 5;
            int eyesDelayToBody = 5;


            for (int i = snakeList.size() - 1; i >= 0; i--) {
                //First of all draw the Fruit
                //At last draw the Head
                Coordinates coor = snakeList.get(i);
                if (i == 0) {
                    //Draw the head of the snake
                    //in all four directions
                    if (mNextMotion.equals(LEFT) || mNextMotion.equals(RIGHT)) {
                        if (mNextMotion.equals(RIGHT)) {
                            //Draw the Head
                            c.drawOval(new RectF(coor.getCoorX() - dimensionSnakeHeadHeight / 2 - headDelayToBody, coor.getCoorY() + dimensionSnakeHeadWidth / 2, coor.getCoorX() + dimensionSnakeHeadHeight / 2 - headDelayToBody, coor.getCoorY() - dimensionSnakeHeadWidth / 2), head);
                            //Draw the Eyes
                            c.drawOval(new RectF(coor.getCoorX() + ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) - eyesDelayToBody - snakeHeadEye, coor.getCoorY() - ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) + snakeHeadEye, coor.getCoorX() + ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) - eyesDelayToBody + snakeHeadEye, coor.getCoorY() - ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) - snakeHeadEye), eye);
                            c.drawOval(new RectF(coor.getCoorX() + ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) - eyesDelayToBody - snakeHeadEye, coor.getCoorY() + ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) + snakeHeadEye, coor.getCoorX() + ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) - eyesDelayToBody + snakeHeadEye, coor.getCoorY() + ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) - snakeHeadEye), eye);
                        } else {
                            //Draw the Head
                            c.drawOval(new RectF(coor.getCoorX() - dimensionSnakeHeadHeight / 2 + headDelayToBody, coor.getCoorY() + dimensionSnakeHeadWidth / 2, coor.getCoorX() + dimensionSnakeHeadHeight / 2 + headDelayToBody, coor.getCoorY() - dimensionSnakeHeadWidth / 2), head);
                            //Draw the Eyes
                            c.drawOval(new RectF(coor.getCoorX() - ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) + eyesDelayToBody - snakeHeadEye, coor.getCoorY() - ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) + snakeHeadEye, coor.getCoorX() - ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) + eyesDelayToBody + snakeHeadEye, coor.getCoorY() - ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) - snakeHeadEye), eye);
                            c.drawOval(new RectF(coor.getCoorX() - ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) + eyesDelayToBody - snakeHeadEye, coor.getCoorY() + ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) + snakeHeadEye, coor.getCoorX() - ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) + eyesDelayToBody + snakeHeadEye, coor.getCoorY() + ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) - snakeHeadEye), eye);
                        }
                    } else {
                        if (mNextMotion.equals(UP)) {
                            //Draw the Head
                            c.drawOval(new RectF(coor.getCoorX() - dimensionSnakeHeadWidth / 2, coor.getCoorY() + dimensionSnakeHeadHeight / 2 - headDelayToBody, coor.getCoorX() + dimensionSnakeHeadWidth / 2, coor.getCoorY() - dimensionSnakeHeadHeight / 2 - headDelayToBody), head);
                            //Draw the Eyes
                            c.drawOval(new RectF(coor.getCoorX() - ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) - snakeHeadEye, coor.getCoorY() + ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) - eyesDelayToBody + snakeHeadEye, coor.getCoorX() - ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) + snakeHeadEye, coor.getCoorY() + ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) - eyesDelayToBody - snakeHeadEye), eye);
                            c.drawOval(new RectF(coor.getCoorX() + ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) - snakeHeadEye, coor.getCoorY() + ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) - eyesDelayToBody + snakeHeadEye, coor.getCoorX() + ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) + snakeHeadEye, coor.getCoorY() + ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) - eyesDelayToBody - snakeHeadEye), eye);
                        } else {
                            //Draw the Head
                            c.drawOval(new RectF(coor.getCoorX() - dimensionSnakeHeadWidth / 2, coor.getCoorY() + dimensionSnakeHeadHeight / 2 + headDelayToBody, coor.getCoorX() + dimensionSnakeHeadWidth / 2, coor.getCoorY() - dimensionSnakeHeadHeight / 2 + headDelayToBody), head);
                            //Draw the Eyes
                            c.drawOval(new RectF(coor.getCoorX() - ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) - snakeHeadEye, coor.getCoorY() - ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) + eyesDelayToBody + snakeHeadEye, coor.getCoorX() - ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) + snakeHeadEye, coor.getCoorY() - ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) + eyesDelayToBody - snakeHeadEye), eye);
                            c.drawOval(new RectF(coor.getCoorX() + ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) - snakeHeadEye, coor.getCoorY() - ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) + eyesDelayToBody + snakeHeadEye, coor.getCoorX() + ((int) (dimensionSnakeHeadWidth * factorDimensionSnakeHeadWidth)) + snakeHeadEye, coor.getCoorY() - ((int) (dimensionSnakeHeadHeight * factorDimensionSnakeHeadHeight)) + eyesDelayToBody - snakeHeadEye), eye);
                        }
                    }
                    //Draw the Fruit
                } else if (i == snakeList.size() - 1) {
                    c.drawRect(new RectF(coor.getCoorX() - dimensionFruit / 2, coor.getCoorY() + dimensionFruit / 2, coor.getCoorX() + dimensionFruit / 2, coor.getCoorY() - dimensionFruit / 2), fruit);
                    if (tongueCounter > 0) {
                        switch (mNextMotion) {
                            case LEFT: {
                                c.drawLine(snakeList.get(0).getCoorX(), snakeList.get(0).getCoorY(), snakeList.get(0).getCoorX() - ((int) (dimensionSnakeHeadHeight * 1.5f)), snakeList.get(0).getCoorY() + 5, tongue);
                                c.drawLine(snakeList.get(0).getCoorX(), snakeList.get(0).getCoorY(), snakeList.get(0).getCoorX() - ((int) (dimensionSnakeHeadHeight * 1.5f)), snakeList.get(0).getCoorY() - 5, tongue);
                                playFruitSound(false);
                            }
                            break;

                            case RIGHT: {
                                c.drawLine(snakeList.get(0).getCoorX(), snakeList.get(0).getCoorY(), snakeList.get(0).getCoorX() + ((int) (dimensionSnakeHeadHeight * 1.5f)), snakeList.get(0).getCoorY() + 5, tongue);
                                c.drawLine(snakeList.get(0).getCoorX(), snakeList.get(0).getCoorY(), snakeList.get(0).getCoorX() + ((int) (dimensionSnakeHeadHeight * 1.5f)), snakeList.get(0).getCoorY() - 5, tongue);
                                playFruitSound(true);
                            }
                            break;

                            case UP: {
                                c.drawLine(snakeList.get(0).getCoorX(), snakeList.get(0).getCoorY(), snakeList.get(0).getCoorX() + 5, snakeList.get(0).getCoorY() + ((int) (dimensionSnakeHeadHeight * 1.5f)), tongue);
                                c.drawLine(snakeList.get(0).getCoorX(), snakeList.get(0).getCoorY(), snakeList.get(0).getCoorX() - 5, snakeList.get(0).getCoorY() + ((int) (dimensionSnakeHeadHeight * 1.5f)), tongue);
                                playFruitSound(false);
                            }
                            break;

                            case DOWN: {
                                c.drawLine(snakeList.get(0).getCoorX(), snakeList.get(0).getCoorY(), snakeList.get(0).getCoorX() + 5, snakeList.get(0).getCoorY() - ((int) (dimensionSnakeHeadHeight * 1.5f)), tongue);
                                c.drawLine(snakeList.get(0).getCoorX(), snakeList.get(0).getCoorY(), snakeList.get(0).getCoorX() - 5, snakeList.get(0).getCoorY() - ((int) (dimensionSnakeHeadHeight * 1.5f)), tongue);
                                playFruitSound(true);
                            }
                            break;
                            default: {

                            }
                        }
                        tongueCounter--;

                    }
                } else {
                    //Draw the body of the snake
                    c.drawRect(new RectF(coor.getCoorX() - dimensionSnakeBodyWidth / 2, coor.getCoorY() + dimensionSnakeBodyWidth / 2, coor.getCoorX() + dimensionSnakeBodyWidth / 2, coor.getCoorY() - dimensionSnakeBodyWidth / 2), tail);
                }
                Log.i(TAG, "draw fruit --> ");
            }
            sHolder.unlockCanvasAndPost(c);
        }
    }

    private void playFruitSound(final boolean right) {
        Log.i(TAG, "should play sound... ");
        if (StartScreen.testMode)
            Toast.makeText(getApplication(), "should playFruitSound...", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPrefs = getSharedPreferences(StartScreen.SHARED_PREFS, MODE_PRIVATE);
        if (sharedPrefs.getInt(Settings.OPTIONS_SWITCH_PLAY_SOUND, 1) == 1) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (right) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.drawable.right);
                        mediaPlayer.start();
                    } else {
                        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.drawable.left);
                        mediaPlayer.start();
                    }
                }
            });
        }
    }

    private void playGameOverSound() {
        Log.i(TAG, "should play sound... ");
        if (StartScreen.testMode)
            Toast.makeText(getApplication(), "should playGameOverSound...", Toast.LENGTH_SHORT).show();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.drawable.gameover);
                mediaPlayer.start();
            }
        });
    }

    private boolean isFruitEaten() {
        //new Thomas
        int rangeXFruitMin = snakeList.get(snakeList.size() - 1).getCoorX() - (dimensionFruit / 2) - (dimensionSnakeHeadWidth / 4);
        int rangeXFruitMax = snakeList.get(snakeList.size() - 1).getCoorX() + (dimensionFruit / 2) + (dimensionSnakeHeadWidth / 4);

        int rangeYFruitMin = snakeList.get(snakeList.size() - 1).getCoorY() - (dimensionFruit / 2) - (dimensionSnakeHeadWidth / 4);
        int rangeYFruitMax = snakeList.get(snakeList.size() - 1).getCoorY() + (dimensionFruit / 2) + (dimensionSnakeHeadWidth / 4);

        if (snakeList != null) {
            if (snakeList.get(0).getCoorX() >= rangeXFruitMin && snakeList.get(0).getCoorX() <= rangeXFruitMax && snakeList.get(0).getCoorY() >= rangeYFruitMin && snakeList.get(0).getCoorY() <= rangeYFruitMax) {
                tongueCounter = 1;
                return true;
            }
        }
        return false;
    }

    private void moveSnake() {
        //new Thomas
        Coordinates head = snakeList.get(0);

        int headX = head.getCoorX();
        int headY = head.getCoorY();
        switch (mNextMotion) {
            case LEFT: {
                snakeList.add(0, new Coordinates(headX - dimensionSnakeBodyWidth, headY));
                mayNextMotion = true;
            }
            break;

            case RIGHT: {
                snakeList.add(0, new Coordinates(headX + dimensionSnakeBodyWidth, headY));
                mayNextMotion = true;
            }
            break;

            case UP: {
                snakeList.add(0, new Coordinates(headX, headY + dimensionSnakeBodyWidth));
                mayNextMotion = true;
            }
            break;

            case DOWN: {
                snakeList.add(0, new Coordinates(headX, headY - dimensionSnakeBodyWidth));
                mayNextMotion = true;
            }
            break;
            default:
        }
    }

    private void cutTail() {
        //new Thomas
        snakeList.remove(snakeList.size() - 2);
    }

    //Sets the first Snake in the GameField
    private void setSnakeAtBeginning() {
        //new Thomas
        int headPointX = SViewWidth / 2;
        int headPointY = SViewHeight / 2;
        int direction = (int) (Math.random() * 5);


        //set the Snake with a lenght of three
        snakeList.add(new Coordinates(headPointX, headPointY));
        switch (direction) {
            case 1: {
                snakeList.add(new Coordinates(headPointX + dimensionSnakeBodyWidth, headPointY));
                snakeList.add(new Coordinates(headPointX + dimensionSnakeBodyWidth + dimensionSnakeBodyWidth, headPointY));
                mNextMotion = LEFT;
            }
            break;

            case 2: {
                snakeList.add(new Coordinates(headPointX - dimensionSnakeBodyWidth, headPointY));
                snakeList.add(new Coordinates(headPointX - dimensionSnakeBodyWidth - dimensionSnakeBodyWidth, headPointY));
                mNextMotion = RIGHT;
            }
            break;

            case 3: {
                snakeList.add(new Coordinates(headPointX, headPointY + dimensionSnakeBodyWidth));
                snakeList.add(new Coordinates(headPointX, headPointY + dimensionSnakeBodyWidth + dimensionSnakeBodyWidth));
                mNextMotion = DOWN;
            }
            break;

            case 4: {
                snakeList.add(new Coordinates(headPointX, headPointY - dimensionSnakeBodyWidth));
                snakeList.add(new Coordinates(headPointX, headPointY - dimensionSnakeBodyWidth - dimensionSnakeBodyWidth));
                mNextMotion = UP;
            }
            break;

            default: {
                snakeList.add(new Coordinates(headPointX, headPointY - dimensionSnakeBodyWidth));
                snakeList.add(new Coordinates(headPointX, headPointY - dimensionSnakeBodyWidth - dimensionSnakeBodyWidth));
                mNextMotion = UP;
            }
        }

        //set the first Fruit
        snakeList.add(getRandomCoordinates());
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        SharedPreferences sharedPrefs = getSharedPreferences(StartScreen.SHARED_PREFS, MODE_PRIVATE);
        if (sharedPrefs.getInt(Settings.OPTIONS_SWITCH_CONTROL, -1) == 1) {

            float accelDataX = event.values[0];
            float accelDataY = event.values[1];
            float accelDataZ = event.values[2];

            float accurancy = 3.0f;
            if (sharedPrefs.getInt(Settings.OPTIONS_SWITCH_CONTROL_INVERT, -1) == 0) {
                if (accelDataX > (accurancy)) {
                    if (!mNextMotion.equals(LEFT) && !mNextMotion.equals(RIGHT)) {
                        if(mayNextMotion) {
                            mNextMotion = RIGHT;//dir
                            mayNextMotion = false;
                        }
                        Log.i("snake", RIGHT);
                    } else {
                        Log.i("snake", "right/left detected");
                    }
                } else if (accelDataX < (-accurancy)) {
                    if (!mNextMotion.equals(LEFT) && !mNextMotion.equals(RIGHT)) {
                        if(mayNextMotion) {
                            mNextMotion = LEFT;//dir
                            mayNextMotion = false;
                        }
                        Log.i("snake", LEFT);
                    } else {
                        Log.i("snake", "left/right detected");
                    }
                }

                if (accelDataY < (-accurancy)) {
                    if (!mNextMotion.equals(UP) && !mNextMotion.equals(DOWN)) {
                        if(mayNextMotion) {
                            mNextMotion = UP;//dir
                            mayNextMotion = false;
                        }
                        Log.i("snake", UP);
                    } else {
                        Log.i("snake", "down/up detected");
                    }
                } else if (accelDataY > (accurancy)) {
                    if (!mNextMotion.equals(UP) && !mNextMotion.equals(DOWN)) {
                        if(mayNextMotion) {
                            mNextMotion = DOWN;//dir
                            mayNextMotion = false;
                        }
                        Log.i("snake", DOWN);
                    } else {
                        Log.i("snake", "up/down detected");
                    }
                }


            } else {


                if (accelDataX < (-accurancy)) {
                    if (!mNextMotion.equals(LEFT) && !mNextMotion.equals(RIGHT)) {
                        if(mayNextMotion) {
                            mNextMotion = RIGHT;//dir
                            mayNextMotion = false;
                        }
                        Log.i("snake", RIGHT);
                    } else {
                        Log.i("snake", "right/left detected");
                    }
                } else if (accelDataX > (accurancy)) {
                    if (!mNextMotion.equals(LEFT) && !mNextMotion.equals(RIGHT)) {
                        if(mayNextMotion) {
                            mNextMotion = LEFT;//dir
                            mayNextMotion = false;
                        }
                        Log.i("snake", LEFT);
                    } else {
                        Log.i("snake", "left/right detected");
                    }
                }

                if (accelDataY > (accurancy)) {
                    if (!mNextMotion.equals(UP) && !mNextMotion.equals(DOWN)) {
                        if(mayNextMotion) {
                            mNextMotion = UP;//dir
                            mayNextMotion = false;
                        }
                        Log.i("snake", UP);
                    } else {
                        Log.i("snake", "down/up detected");
                    }
                } else if (accelDataY < (-accurancy)) {
                    if (!mNextMotion.equals(UP) && !mNextMotion.equals(DOWN)) {
                        if(mayNextMotion) {
                            mNextMotion = DOWN;//dir
                            mayNextMotion = false;
                        }
                        Log.i("snake", DOWN);
                    } else {
                        Log.i("snake", "up/down detected");
                    }
                }
            }


            if (StartScreen.testMode) {
                TextView tvX = (TextView) findViewById(R.id.textView_x);
                TextView tvY = (TextView) findViewById(R.id.textView_y);
                TextView tvZ = (TextView) findViewById(R.id.textView_z);

                tvX.setText(Float.toString(accelDataX));
                tvY.setText(Float.toString(accelDataY));
                tvZ.setText(Float.toString(accelDataZ));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
