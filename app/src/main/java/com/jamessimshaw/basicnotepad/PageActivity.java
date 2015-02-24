package com.jamessimshaw.basicnotepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class PageActivity extends ActionBarActivity {
    private Note mCurrentNote;
    private EditText mNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        mNoteText = (EditText) findViewById(R.id.noteText);
        mCurrentNote = new Note(); //TODO: Restore previously used note
        createNewNote();
    }

    @Override
    protected void onStop() {
        //TODO: Add auto save of note to private storage area
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
            return true;
        }
        else if (id == R.id.action_save) {
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
                        //TODO: Save note to external storage if available otherwise display an error
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
        mNoteText.setText("");
    }

    private void saveCurrentNote() {
        //TODO: Add in ability to save notes to documents folder
    }

    private boolean hasNoteTextChanged() {
        return !mCurrentNote.getNoteText().equals(mNoteText.getText().toString());
    }
}
