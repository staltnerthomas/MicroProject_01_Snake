package at.mc.android.max.thomas.microproject_01_snake;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by thomasstaltner on 31/03/15.
 */
public class GameViewDialogFragment extends DialogFragment {
    public static final String TAG = "Snake01 GaView Fragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder = builder.setMessage(R.string.game_view_fragment_text);

        builder = builder.setPositiveButton(R.string.game_view_fragment_continue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "+ button pressed...");
                Intent i = new Intent(getActivity(), GameView.class);
                startActivity(i);
            }
        });

        builder = builder.setNegativeButton(R.string.game_view_fragment_back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "- button pressed...");
                Intent i = new Intent(getActivity(), StartScreen.class);
                startActivity(i);
            }
        });
        return builder.create();
    }
}
