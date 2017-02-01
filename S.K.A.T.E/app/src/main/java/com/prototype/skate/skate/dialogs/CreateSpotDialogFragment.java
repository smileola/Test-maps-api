package com.prototype.skate.skate.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.prototype.skate.skate.R;

/**
 * Created by Agent on 1/19/2017.
 */

public class CreateSpotDialogFragment extends DialogFragment {
    // Interface With Listeners
    public interface CreateSpotDialogListener{
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    CreateSpotDialogListener mListener;
    // Context because activity is deprecated
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (CreateSpotDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement DialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Getting the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder .setView(inflater.inflate(R.layout.dialog_add_spot, null))
                .setTitle(R.string.add_spot_title)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Add the Spot
                        mListener.onDialogPositiveClick(CreateSpotDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User cancelled the dialog
                        mListener.onDialogNegativeClick(CreateSpotDialogFragment.this);
                    }
                });
        return builder.create();
    }
}
