package at.mc.android.max.thomas.microproject_01_snake;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class StartScreen extends Activity implements View.OnClickListener {

    public static final String TAG                          = "Snake 01 StartScreen";
    public static final String SHARED_PREFS                 = "Preverences_Name";
    public static final String GAME_VIEW_BACKGROUND_COLOUR  = "Backgroundcolor_of_GameView";
    public static final String GAME_VIEW_SNAKE_HEAD_COLOUR  = "Snake_Head_color_of_GameView";
    public static final String GAME_VIEW_SNAKE_EYE_COLOUR   = "Snake_Eye_color_of_GameView";
    public static final String GAME_VIEW_SNAKE_BODY_COLOUR  = "Snake_Body_color_of_GameView";
    public static final String GAME_VIEW_FRUIT_COLOUR       = "Fruit_color_of_GameView";
    public static final String GAME_VIEW_HI_SCORE           = "High_Score";
    public static final String GAME_VIEW_LAST_SCORE         = "Last_Score";
    Button b = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        b = (Button) findViewById(R.id.start_button_start);
        b.setOnClickListener(this);


        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS , MODE_PRIVATE);
        sharedPrefs.getInt(GAME_VIEW_BACKGROUND_COLOUR, 0x88888888);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(GAME_VIEW_BACKGROUND_COLOUR, 0xffff0000);

        editor.putInt(GAME_VIEW_SNAKE_HEAD_COLOUR, 0xff00ff00);
        editor.putInt(GAME_VIEW_SNAKE_BODY_COLOUR, 0xff0000ff);
        editor.putInt(GAME_VIEW_SNAKE_EYE_COLOUR,  0xff000000);
        editor.putInt(GAME_VIEW_FRUIT_COLOUR,      0xffffff00);
        editor.putInt(GAME_VIEW_HI_SCORE,          0x0);
        editor.putInt(GAME_VIEW_LAST_SCORE,        0x0);

        editor.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View _v) {
        switch (_v.getId()){
            case R.id.start_button_start: {
                Intent i = new Intent(this, GameView.class);
                startActivity(i);
            } break;
            default:{
                Log.e(TAG, "StartScreen undefined button pressed...");
            } //switch id //text comment
        }

    }
}
