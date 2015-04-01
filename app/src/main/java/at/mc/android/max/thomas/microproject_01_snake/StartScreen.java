package at.mc.android.max.thomas.microproject_01_snake;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class StartScreen extends Activity implements View.OnClickListener, ColorPickerDialog.OnColorChangedListener {

    public static final String TAG = "StartScreen";
    public static final String SHARED_PREFS = "Preverences_Name";

    public static final String GAME_VIEW_BACKGROUND_COLOR = "Backgroundcolor_of_GameView";
    public static final String GAME_VIEW_SNAKE_HEAD_COLOR = "Snake_Head_color_of_GameView";
    public static final String GAME_VIEW_SNAKE_BODY_COLOR = "Snake_Body_color_of_GameView";
    public static final String GAME_VIEW_SNAKE_EYE_COLOR = "Snake_Eye_color_of_GameView";
    public static final String GAME_VIEW_FRUIT_COLOUR = "Fruit_color_of_GameView";
    public static final String GAME_VIEW_SNAKE_TONGUE_COLOR = "Snake_Tongue_color_of_GameView";

    public static final String GAME_VIEW_HI_SCORE = "High_Score";
    public static final String GAME_VIEW_LAST_SCORE = "Last_Score";

    public static final int DEFAULT_GAME_VIEW_BACKGROUND_COLOR = 0xff55A955;
    public static final int DEFAULT_GAME_VIEW_SNAKE_HEAD_COLOR = 0xf0c800f0;
    public static final int DEFAULT_GAME_VIEW_SNAKE_BODY_COLOR = 0xf01d00f5;
    public static final int DEFAULT_GAME_VIEW_SNAKE_EYE_COLOR = 0xfffffad6;
    public static final int DEFAULT_GAME_VIEW_FRUIT_COLOR = 0xf0ffe642;
    public static final int DEFAULT_GAME_VIEW_SNAKE_TONGUE_COLOR = 0xffff0000;

    //For testing the Game
    public static boolean testMode = false;
    public static boolean localeEnglish = true;
    Button b = null;
    TextView highScore = null;
    TextView lastScore = null;
    TextView startControl1 = null;
    TextView startControl2 = null;
    private Menu optionsMenu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (testMode || localeEnglish) {
            Locale locale = new Locale("en_US");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            this.getApplicationContext().getResources().updateConfiguration(config, null);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        b = (Button) findViewById(R.id.start_button_start);
        b.setOnClickListener(this);

        highScore = (TextView) findViewById(R.id.start_text_view_high_score_value);
        lastScore = (TextView) findViewById(R.id.start_text_view_last_score_value);
        startControl1 = (TextView) findViewById(R.id.start_textView_control_1);
        startControl2 = (TextView) findViewById(R.id.start_textView_control_2__sizeofthesnake);

        //Set the default color of the Snakes tongue
        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(GAME_VIEW_SNAKE_TONGUE_COLOR, DEFAULT_GAME_VIEW_SNAKE_TONGUE_COLOR);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highScore.setText(Integer.toString(sharedPrefs.getInt(GAME_VIEW_HI_SCORE, -1)));
        lastScore.setText(Integer.toString(sharedPrefs.getInt(GAME_VIEW_LAST_SCORE, -1)));
        if (testMode) {
            startControl1.setText((Integer.toString(sharedPrefs.getInt(Settings.OPTIONS_SEEKBAR_TAKEOFFSPEED, 500))));
            startControl2.setText((Integer.toString(sharedPrefs.getInt(Settings.OPTIONS_SEEKBAR_SIZE_OF_THE_SNAKE, 0))));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_screen, menu);

        // we need this for inflate the Textcolor when changing the colors
        optionsMenu = menu;
        changeOptionsMenuColor(menu);
        return true;
    }

    private void changeOptionsMenuColor(Menu menu) {
        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        /*
        The following code sets "only" the color of the text in the
        options in the StartScreen to the current color of the value
         */
        //Set color of text background menu
        MenuItem item = menu.findItem(R.id.background_color);
        String st = getResources().getString(R.string.background_color_text);
        SpannableString s = new SpannableString(st);
        int newColor = sharedPrefs.getInt(StartScreen.GAME_VIEW_BACKGROUND_COLOR, DEFAULT_GAME_VIEW_BACKGROUND_COLOR);
        s.setSpan(new ForegroundColorSpan(newColor), 0, s.length(), 0);
        item.setTitle(s);

        //Set color of text head menu
        item = menu.findItem(R.id.snake_head_color);
        st = getResources().getString(R.string.snake_head_color_text);
        s = new SpannableString(st);
        newColor = sharedPrefs.getInt(StartScreen.GAME_VIEW_SNAKE_HEAD_COLOR, DEFAULT_GAME_VIEW_SNAKE_HEAD_COLOR);
        s.setSpan(new ForegroundColorSpan(newColor), 0, s.length(), 0);
        item.setTitle(s);

        //Set color of text body menu
        item = menu.findItem(R.id.snake_body_color);
        st = getResources().getString(R.string.snake_body_color_text);
        s = new SpannableString(st);
        newColor = sharedPrefs.getInt(StartScreen.GAME_VIEW_SNAKE_BODY_COLOR, DEFAULT_GAME_VIEW_SNAKE_BODY_COLOR);
        s.setSpan(new ForegroundColorSpan(newColor), 0, s.length(), 0);
        item.setTitle(s);

        //Set color of text eye menu
        item = menu.findItem(R.id.snake_eye_color);
        st = getResources().getString(R.string.snake_eye_color_text);
        s = new SpannableString(st);
        newColor = sharedPrefs.getInt(StartScreen.GAME_VIEW_SNAKE_EYE_COLOR, DEFAULT_GAME_VIEW_SNAKE_EYE_COLOR);
        s.setSpan(new ForegroundColorSpan(newColor), 0, s.length(), 0);
        item.setTitle(s);

        //Set color of text fruit menu
        item = menu.findItem(R.id.snake_foot_color);
        st = getResources().getString(R.string.snake_foot_color_text);
        s = new SpannableString(st);
        newColor = sharedPrefs.getInt(StartScreen.GAME_VIEW_FRUIT_COLOUR, DEFAULT_GAME_VIEW_FRUIT_COLOR);
        s.setSpan(new ForegroundColorSpan(newColor), 0, s.length(), 0);
        item.setTitle(s);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Log.i(TAG, "SettingsButton pressed...");
                Intent i = new Intent(this, Settings.class);
                startActivity(i);
            }
            break;

            case R.id.background_color: {
                Log.i(TAG, "Background-Button pressed...");
                SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

                ColorPickerDialog color = new ColorPickerDialog(this, this, GAME_VIEW_BACKGROUND_COLOR, sharedPrefs.getInt(GAME_VIEW_BACKGROUND_COLOR, DEFAULT_GAME_VIEW_BACKGROUND_COLOR), Color.WHITE);
                color.show();

            }
            break;

            case R.id.snake_head_color: {
                Log.i(TAG, "Head-Button pressed...");
                SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

                ColorPickerDialog color = new ColorPickerDialog(this, this, GAME_VIEW_SNAKE_HEAD_COLOR, sharedPrefs.getInt(GAME_VIEW_SNAKE_HEAD_COLOR, DEFAULT_GAME_VIEW_SNAKE_HEAD_COLOR), Color.WHITE);
                color.show();
            }
            break;

            case R.id.snake_body_color: {
                Log.i(TAG, "Body-Button pressed...");
                SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

                ColorPickerDialog color = new ColorPickerDialog(this, this, GAME_VIEW_SNAKE_BODY_COLOR, sharedPrefs.getInt(GAME_VIEW_SNAKE_BODY_COLOR, DEFAULT_GAME_VIEW_SNAKE_BODY_COLOR), Color.WHITE);
                color.show();
            }
            break;

            case R.id.snake_eye_color: {
                Log.i(TAG, "Eye-Button pressed...");
                SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

                ColorPickerDialog color = new ColorPickerDialog(this, this, GAME_VIEW_SNAKE_EYE_COLOR, sharedPrefs.getInt(GAME_VIEW_SNAKE_EYE_COLOR, DEFAULT_GAME_VIEW_SNAKE_EYE_COLOR), Color.WHITE);
                color.show();
            }
            break;

            case R.id.snake_foot_color: {
                Log.i(TAG, "Foot-Button pressed...");
                SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

                ColorPickerDialog color = new ColorPickerDialog(this, this, GAME_VIEW_FRUIT_COLOUR, sharedPrefs.getInt(GAME_VIEW_FRUIT_COLOUR, DEFAULT_GAME_VIEW_FRUIT_COLOR), Color.WHITE);
                color.show();
            }
            break;

            case R.id.action_settings_placeholder: {
                Log.i(TAG, "Settings-Placeholder pressed...");
            }
            break;

            case R.id.action_settings_reset: {
                Log.i(TAG, "SettingsReset pressed...");
                DialogFragment dialog = new StartScreenSettingsResetDialogFragment();
                dialog.show(getFragmentManager(), "Game View Dialog Fragment");
            }
            break;

            default: {
                Log.i(TAG, "unknown SettingsButton pressed...");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View _v) {
        switch (_v.getId()) {
            case R.id.start_button_start: {
                Intent i = new Intent(this, GameView.class);
                startActivity(i);
            }
            break;
            default: {
                Log.e(TAG, "StartScreen::undefined button pressed...");
            } //switch id //text comment
        }

    }

    @Override
    public void colorChanged(String key, int color) {
        if (testMode)
            Toast.makeText(this, "colorChanged...", Toast.LENGTH_SHORT).show();
        String info = "key: " + key.toString() + " color: " + Integer.toHexString(color);
        Log.i(TAG, info);

        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(key, color);
        editor.apply();

        changeOptionsMenuColor(optionsMenu);
    }
}
