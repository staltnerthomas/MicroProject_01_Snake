package at.mc.android.max.thomas.microproject_01_snake;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class GameView extends Activity implements SurfaceHolder.Callback, View.OnTouchListener {

    SurfaceView gameSurfaceView = null;

    private int SViewWidth;
    private int SViewHeight;

    private int snakeWidth;
    private int snakeHead;
    private int fruitWidth = snakeWidth;
    private int colourFruit = 0xff0000;

    private SurfaceHolder sHolder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);

        //Set Screen to Fullscreen
        View v = findViewById(android.R.id.content).getRootView();
        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        //get Display with and heigth
        gameSurfaceView = (SurfaceView) findViewById(R.id.game_surface_view);
        SViewWidth = gameSurfaceView.getWidth();
        SViewHeight = gameSurfaceView.getHeight();

        setFruit();



    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }



    //Set a new Fruit into the game-view
    private void setFruit() {
        int x;
        int y;





        //Set the Fruit
        sHolder = gameSurfaceView.getHolder();
        if(sHolder != null) {
            Canvas c = sHolder.lockCanvas();

            Paint p = new Paint();
            p.setStrokeWidth(6.0f);
            p.setColor(colourFruit);

            do {
                x = (int) (Math.random() * SViewWidth);
            } while (x < fruitWidth / 2 || x > SViewWidth - fruitWidth / 2 );

            do {
                y = (int) (Math.random() * SViewHeight);
            } while (y < fruitWidth / 2 || y > SViewHeight - fruitWidth / 2 );

            x = 20;
            y = 20;


            c.drawOval(new RectF(x-5f, y+5f, x+5f, y-5f),p);
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
