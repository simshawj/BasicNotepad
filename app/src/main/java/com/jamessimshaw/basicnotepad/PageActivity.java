package com.jamessimshaw.basicnotepad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class PageActivity extends ActionBarActivity {
    public final String TAG = this.getClass().getSimpleName();
    private final String AUTOSAVE_FILENAME = "autosave.txt";

    private Note mCurrentNote;
    private EditText mNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        mNoteText = (EditText) findViewById(R.id.noteText);
        if (autosaveNoteAvailable()) {
            restoreAutosavedNote();
        }
        else {
            createNewNote();
        }
    }

    @Override
    protected void onStop() {
        autosaveCurrentNote();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_load) {
            if (hasNoteTextChanged()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.savePromptMessage));
                builder.setCancelable(true);
                builder.setPositiveButton(getString(R.string.savePromptSaveOption), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveCurrentNote();
                    }
                });
                builder.setNegativeButton(getString(R.string.savePromptNoSaveOption), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadNote();
                    }
                });
                builder.create().show();
            }
            else{
                loadNote();
            }
            return true;
        }
        else if (id == R.id.action_save) {
            saveCurrentNote();
            return true;
        }
        else if (id == R.id.action_new) {
            if (hasNoteTextChanged()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.savePromptMessage));
                builder.setCancelable(true);
                builder.setPositiveButton(getString(R.string.savePromptSaveOption), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveCurrentNote();
                    }
                });
                builder.setNegativeButton(getString(R.string.savePromptNoSaveOption), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createNewNote();
                    }
                });
                builder.create().show();
            }
            else{
                createNewNote();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNewNote() {
        mCurrentNote = new Note();
        mNoteText.setText(mCurrentNote.getNoteText());
    }

    private void loadNote() {
        Log.i(TAG, "In loadNote()");
        //TODO: Prompt user where note is saved and load the note
        //String newNoteText = TODO
        //mCurrentNote= new Note(newNoteText);
        //mNoteText.setText(mCurrentNote.getText());
    }

    private void saveCurrentNote() {
        Log.i(TAG, "In saveCurrentNote()");
        //TODO: Add in ability to save notes to documents folder
    }

    private boolean hasNoteTextChanged() {
        return !mCurrentNote.getNoteText().equals(mNoteText.getText().toString());
    }

    private void autosaveCurrentNote() {
        FileOutputStream outputStream;

        Log.i(TAG, "In autosaveCurrentNote()");

        mCurrentNote.setNoteText(mNoteText.getText().toString());
        try {
            outputStream = openFileOutput(AUTOSAVE_FILENAME, Context.MODE_PRIVATE);
            outputStream.write(mCurrentNote.getNoteText().getBytes());
            outputStream.close();
        }
        catch (IOException e) {
            Log.e(TAG, "Exception Caught:  ", e);
            //Create alert dialog and prompt to save
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.autosaveFailAlertText));
            builder.setCancelable(true);
            builder.setPositiveButton(getString(R.string.savePromptSaveOption), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveCurrentNote();
                }
            });
            builder.setNegativeButton(getString(R.string.savePromptNoSaveOption), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Do nothing intentionally
                }
            });
            builder.create().show();
        }

    }

    private void restoreAutosavedNote() {
        Log.i(TAG, "In restoreAutosavedNote()");
        FileInputStream inputStream;
        String inputString = "";

        try {
            inputStream = openFileInput(AUTOSAVE_FILENAME);
            byte[] buffer = new byte[1024];
            int length;
            while ((inputStream.read(buffer)) != -1) {
                inputString += new String(buffer);
            }
            mCurrentNote = new Note(inputString);
            mNoteText.setText(inputString);
        }
        catch (IOException e) {
            Log.e(TAG, "Exception Caught:  ", e);

        }
    }

    private boolean autosaveNoteAvailable() {
        File file = new File(this.getFilesDir(), AUTOSAVE_FILENAME);

        Log.i(TAG, "Checking if autosave file exists");

        return file.exists();
    }
}
