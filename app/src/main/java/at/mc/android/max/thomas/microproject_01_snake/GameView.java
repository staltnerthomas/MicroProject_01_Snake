package at.mc.android.max.thomas.microproject_01_snake;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class GameView extends Activity implements View.OnTouchListener {

    SurfaceView gameSurfaceView = null;
    private int SViewWidth;
    private int SViewHeight;

    private int snakeWidth;
    private int snakeHead;
    private int fruitWidth = snakeWidth;
    private int colourFruit = 0x000000;

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

    private void setFruit() {
        int x;
        int y;

        do {
            x = (int) (Math.random() * SViewWidth);
        } while (x < fruitWidth / 2 || x > SViewWidth - fruitWidth / 2 );

        do {
            y = (int) (Math.random() * SViewHeight);
        } while (y < fruitWidth / 2 || y > SViewHeight - fruitWidth / 2 );

        Paint p = new Paint();
        p.setStrokeWidth(6.0f);
        p.setColor(colourFruit);

        //Set the Fruit
//         mHolder mh =

    }
}
