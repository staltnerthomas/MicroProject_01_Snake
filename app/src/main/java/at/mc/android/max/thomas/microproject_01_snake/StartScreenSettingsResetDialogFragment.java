package at.mc.android.max.thomas.microproject_01_snake;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by thomasstaltner on 31/03/15.
 */
public class StartScreenSettingsResetDialogFragment extends DialogFragment {
    public static final String TAG = "ResetSettings Fragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder = builder.setMessage(R.string.start_view_fragment_text);

        builder = builder.setNegativeButton(R.string.start_view_fragment_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "- button pressed...");
            }
        });

        builder = builder.setNeutralButton(R.string.start_view_fragment_neutral,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "o button pressed...");
                SharedPreferences sharedPrefs = getActivity().getSharedPreferences(StartScreen.SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();

                editor.putInt(StartScreen.GAME_VIEW_BACKGROUND_COLOUR, 0xffff0000);
                editor.putInt(StartScreen.GAME_VIEW_SNAKE_HEAD_COLOUR, 0xf000ff00);
                editor.putInt(StartScreen.GAME_VIEW_SNAKE_BODY_COLOUR, 0xf00000ff);
                editor.putInt(StartScreen.GAME_VIEW_SNAKE_EYE_COLOUR,  0xf0000000);
                editor.putInt(StartScreen.GAME_VIEW_FRUIT_COLOUR,      0xf0ffff00);

                editor.commit();
            }
        });

        builder = builder.setPositiveButton(R.string.start_view_fragment_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "+ button pressed...");
                SharedPreferences sharedPrefs = getActivity().getSharedPreferences(StartScreen.SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();

                editor.putInt(StartScreen.GAME_VIEW_BACKGROUND_COLOUR, 0xffff0000);
                editor.putInt(StartScreen.GAME_VIEW_SNAKE_HEAD_COLOUR, 0xf000ff00);
                editor.putInt(StartScreen.GAME_VIEW_SNAKE_BODY_COLOUR, 0xf00000ff);
                editor.putInt(StartScreen.GAME_VIEW_SNAKE_EYE_COLOUR,  0xf0000000);
                editor.putInt(StartScreen.GAME_VIEW_FRUIT_COLOUR,      0xf0ffff00);
                editor.putInt(StartScreen.GAME_VIEW_LAST_SCORE,        0);
                editor.putInt(StartScreen.GAME_VIEW_HI_SCORE,          0);

                editor.commit();

                Intent i = new Intent(getActivity(), StartScreen.class);
                startActivity(i);
            }
        });

        AlertDialog dialog = builder.show();
        TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        dialog.show();
        return null;
    }
}
