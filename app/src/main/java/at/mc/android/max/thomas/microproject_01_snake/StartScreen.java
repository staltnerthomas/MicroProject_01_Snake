package at.mc.android.max.thomas.microproject_01_snake;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class StartScreen extends Activity implements View.OnClickListener, ColorPickerDialog.OnColorChangedListener {

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
    TextView highScore = null;
    TextView lastScore = null;

    TextView startControl1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        b = (Button) findViewById(R.id.start_button_start);
        b.setOnClickListener(this);

        highScore = (TextView) findViewById(R.id.start_text_view_high_score_value);
        lastScore = (TextView) findViewById(R.id.start_text_view_last_score_value);
        startControl1 = (TextView) findViewById(R.id.start_textView_control_1);


        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS , MODE_PRIVATE);
//        sharedPrefs.getInt(GAME_VIEW_BACKGROUND_COLOUR, 0x88888888);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(GAME_VIEW_BACKGROUND_COLOUR, 0xffff0000);
        editor.putInt(GAME_VIEW_SNAKE_HEAD_COLOUR, 0xff00ff00);
        editor.putInt(GAME_VIEW_SNAKE_BODY_COLOUR, 0xff0000ff);
        editor.putInt(GAME_VIEW_SNAKE_EYE_COLOUR,  0xff000000);
        editor.putInt(GAME_VIEW_FRUIT_COLOUR,      0xffffff00);
//        editor.putInt(GAME_VIEW_LAST_SCORE, 0);
//        editor.putInt(GAME_VIEW_HI_SCORE, 0);

        editor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS , MODE_PRIVATE);
        highScore.setText(Integer.toString(sharedPrefs.getInt(GAME_VIEW_HI_SCORE, -1)));
        lastScore.setText(Integer.toString(sharedPrefs.getInt(GAME_VIEW_LAST_SCORE, -1)));
        startControl1.setText((Integer.toString(sharedPrefs.getInt(Settings.OPTIONS_SEEKBAR_TAKEOFFSPEED, 500))));
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
        switch(item.getItemId()){
            case R.id.action_settings:{
                Log.i(TAG, "SettingsButton pressed...");
                Intent i = new Intent(this, Settings.class);
                startActivity(i);
            } break;

            case R.id.background_color:{
                Log.i(TAG, "Background-Button pressed...");
//                final ColorPickerDialog newColor = new ColorPickerDialog(this,this, "picker", Color.BLACK,Color.WHITE);
//                newColor.show();
//                DialogFragment dialog = new ColorPickerDialog(getApplicationContext(), new ColorPickerDialog.OnColorChangedListener() {
//                    @Override
//                    public void colorChanged(String key, int _color) {
//                        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS , MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPrefs.edit();
//                        editor.putInt(GAME_VIEW_BACKGROUND_COLOUR, newColor.toString());
//
//                    }
//                }, GAME_VIEW_BACKGROUND_COLOUR, 0x00ff00ff);
//                dialog.show(getFragmentManager(), "ColorPicker Dialog Fragment");
            } break;

            case R.id.snake_head_color:{
                Log.i(TAG, "Head-Button pressed...");
                SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS , MODE_PRIVATE);

                ColorPickerDialog color = new ColorPickerDialog(this,this, GAME_VIEW_SNAKE_HEAD_COLOUR,sharedPrefs.getInt(GAME_VIEW_SNAKE_HEAD_COLOUR, 0xffffffff), Color.WHITE);
                color.show();

            } break;

            case R.id.snake_body_color:{
                Log.i(TAG, "Body-Button pressed...");
            } break;

            case R.id.snake_foot_color:{
                Log.i(TAG, "Foot-Button pressed...");
            } break;
            default:{
                Log.i(TAG, "unknown SettingsButton pressed...");
            }
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
                Log.e(TAG, "StartScreen::undefined button pressed...");
            } //switch id //text comment
        }

    }

    @Override
    public void colorChanged(String key, int color) {
        Toast.makeText(this, "colorChanged...", Toast.LENGTH_SHORT).show();
        String info = "key: " + key.toString() + " color: " + Integer.toHexString(color);
        Log.i(TAG, info);

        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS , MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(key, color);
        editor.commit();
    }
}
