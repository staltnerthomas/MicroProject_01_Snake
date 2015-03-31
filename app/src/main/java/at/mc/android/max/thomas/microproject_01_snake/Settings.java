package at.mc.android.max.thomas.microproject_01_snake;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;


public class Settings extends Activity {
    public static final String TAG = "Snake 01 OptionsScreen";

    public static final String OPTIONS_SWITCH_CONTROL  = "Switch between Motion and Scroll";
    public static final String OPTIONS_SWITCH_CONTROL_INVERT  = "Switch Motion between regular and invert";
    public static final String OPTIONS_SEEKBAR_TAKEOFFSPEED  = "TakeoffSpeed for Snake";


    Switch optionsSwitchControl = null;
    Switch optionsSwitchControlInvert = null;
    SeekBar optionsSeekbarTakeoffspeed = null;
    TextView optionsTextViewControlInvert = null;
    TextView optionsTextViewSeverity = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Options onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        optionsSwitchControl = (Switch) findViewById(R.id.options_switch_control);
        optionsSwitchControlInvert = (Switch) findViewById(R.id.options_switch_control_invert);
        optionsSeekbarTakeoffspeed = (SeekBar) findViewById(R.id.options_seekbar_takeoffspeed);
        optionsTextViewControlInvert = (TextView) findViewById(R.id.options_text_view_control_invert);
        optionsTextViewSeverity = (TextView) findViewById(R.id.options_text_view_severity);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Options onResume...");

        optionsSwitchControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    optionsSwitchControlInvert.setClickable(true);
                    optionsSwitchControlInvert.setAlpha(1f);
                    optionsTextViewControlInvert.setAlpha((1f));
                } else {
                    optionsSwitchControlInvert.setClickable(false);
                    optionsSwitchControlInvert.setAlpha(.5f);
                    optionsTextViewControlInvert.setAlpha((.5f));
                }
            }
        });


        SharedPreferences sharedPrefs = getSharedPreferences(StartScreen.SHARED_PREFS , MODE_PRIVATE);
        optionsSeekbarTakeoffspeed.setProgress(sharedPrefs.getInt(OPTIONS_SEEKBAR_TAKEOFFSPEED, 500));
        setTakeOffSpeedText(sharedPrefs.getInt(OPTIONS_SEEKBAR_TAKEOFFSPEED, 500));

        optionsSeekbarTakeoffspeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(seekBar.getId() == R.id.options_seekbar_takeoffspeed) {
                    setTakeOffSpeedText(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        if(sharedPrefs.getInt(OPTIONS_SWITCH_CONTROL, 0) == 1){
            //Switch
            optionsSwitchControl.setChecked(true);
            optionsSwitchControlInvert.setClickable(true);
            optionsSwitchControlInvert.setAlpha(1f);
            optionsTextViewControlInvert.setAlpha((1f));
        } else {
            optionsSwitchControl.setChecked(false);
            optionsSwitchControlInvert.setClickable(false);
            optionsSwitchControlInvert.setAlpha(.5f);
            optionsTextViewControlInvert.setAlpha((.5f));
        }

        if(sharedPrefs.getInt(OPTIONS_SWITCH_CONTROL_INVERT, 0) == 1){
            //Switch
            optionsSwitchControlInvert.setChecked(true);
        } else {
            optionsSwitchControlInvert.setChecked(false);
        }
    }

    private void setTakeOffSpeedText(int progress) {
        if(progress < 100){
            optionsTextViewSeverity.setText(R.string.options_text_view_severity_1);
        } else if(progress < 200){
            optionsTextViewSeverity.setText(R.string.options_text_view_severity_2);
        } else if(progress < 300){
            optionsTextViewSeverity.setText(R.string.options_text_view_severity_3);
        } else {
            optionsTextViewSeverity.setText(R.string.options_text_view_severity_4);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Options onDestroy...");

        SharedPreferences sharedPrefs = getSharedPreferences(StartScreen.SHARED_PREFS , MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        if(optionsSwitchControl.isChecked()) {
            editor.putInt(OPTIONS_SWITCH_CONTROL, 1);
        } else {
            editor.putInt(OPTIONS_SWITCH_CONTROL, 0);
        }

        if(optionsSwitchControlInvert.isChecked()) {
            editor.putInt(OPTIONS_SWITCH_CONTROL_INVERT, 1);
        } else {
            editor.putInt(OPTIONS_SWITCH_CONTROL_INVERT, 0);
        }

        editor.putInt(OPTIONS_SEEKBAR_TAKEOFFSPEED, optionsSeekbarTakeoffspeed.getProgress());

        editor.commit();

    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_settings, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
