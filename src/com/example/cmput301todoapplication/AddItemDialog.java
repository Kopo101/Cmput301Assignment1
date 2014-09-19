package com.example.cmput301todoapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

public class AddItemDialog extends DialogFragment {
	
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        EditText input = new EditText(App.getContext());
        input.setId(0);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        final EditText savedText = (EditText) input.findViewById(0);
        builder.setMessage(R.string.dialog_add_item)
               .setPositiveButton(R.string.item_added, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Save item
                	   String text = savedText.getText().toString();
                	   DialogFragmentListener activity = (DialogFragmentListener) getActivity();
                	   activity.onReturnValue(text);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
