package com.example.ottot.mineseeker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by ottot on 2/21/2017.
 */

public class Congrats extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.congrats,null);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        getActivity().finish();
                        break;
                }
            }

        };
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.youWin)
                .setView(v)
                .setMessage(R.string.backtotheMenu)
                .setPositiveButton("Yeah I'm scared", listener)
                .setNegativeButton("Nah let me enjoy the view",listener)
                .create();
    }
}
