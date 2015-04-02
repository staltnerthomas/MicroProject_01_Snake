package at.mc.android.max.thomas.microproject_01_snake;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

public class GameViewDialogFragment extends DialogFragment {
    public static final String TAG = "GameViewDialogFragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder = builder.setMessage(R.string.game_view_fragment_text);

        builder = builder.setPositiveButton(R.string.game_view_fragment_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "+ button pressed...");
                Intent i = new Intent(getActivity(), GameView.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        builder = builder.setNegativeButton(R.string.game_view_fragment_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "- button pressed...");
                Intent i = new Intent(getActivity(), StartScreen.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
//        builder = builder.setPositiveButton(R.string.game_view_fragment_positive, this);
//        builder = builder.setNegativeButton(R.string.game_view_fragment_negative, this);

        AlertDialog dialog = builder.show();
        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);

        dialog.setInverseBackgroundForced(false);
        dialog.show();
        return null;
    }
}