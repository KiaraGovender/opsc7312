package com.demoapp.opsc7312task2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


/** CODE ATTRIBUTION
 * Custom dialog + sending information to activity, Coding in Flow
 * https://codinginflow.com/tutorials/android/custom-dialog-interface
 * **/
// get input from user to name favourite
public class FavouritesDialog extends AppCompatDialogFragment {
    private EditText editFav;
    private FavouriteDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle("New Favourite")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String fav = editFav.getText().toString();
                        listener.applyTexts(fav);
                    }
                });
        editFav = view.findViewById(R.id.edtFav);
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (FavouriteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    public interface FavouriteDialogListener {
        void applyTexts(String favourite);
    }
}

