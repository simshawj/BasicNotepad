package com.jamessimshaw.basicnotepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class PageActivity extends Activity {
    public final String TAG = this.getClass().getSimpleName();

    private Note mNote;
    private EditText mNoteText;
    private NoteFileHelper mNoteFileHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        mNoteFileHelper = new NoteFileHelper();
        mNoteText = (EditText) findViewById(R.id.noteText);
        if (mNoteFileHelper.autosaveNoteAvailable(this)) {
            mNote = mNoteFileHelper.loadNote(this, NoteFileHelper.FILE_AUTOSAVE);
            mNoteText.setText(mNote.getNoteText());
        }
        else {
            createNewNote();
        }
    }

    @Override
    protected void onStop() {
        updateNote();
        mNoteFileHelper.saveNote(this, mNote, NoteFileHelper.FILE_AUTOSAVE);
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
                        updateNote();
                        mNoteFileHelper.saveNote(PageActivity.this,
                                mNote,
                                NoteFileHelper.FILE_EXTERNAL);
                        mNote = mNoteFileHelper.loadNote(PageActivity.this,
                                NoteFileHelper.FILE_EXTERNAL);
                    }
                });
                builder.setNegativeButton(getString(R.string.savePromptNoSaveOption), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNote = mNoteFileHelper.loadNote(PageActivity.this,
                                NoteFileHelper.FILE_EXTERNAL);
                    }
                });
                builder.create().show();
            }
            else{
                mNote = mNoteFileHelper.loadNote(this, NoteFileHelper.FILE_EXTERNAL);
            }
            return true;
        }
        else if (id == R.id.action_save) {
            updateNote();
            mNoteFileHelper.saveNote(this, mNote, NoteFileHelper.FILE_EXTERNAL);
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
                        updateNote();
                        mNoteFileHelper.saveNote(PageActivity.this,
                                mNote,
                                NoteFileHelper.FILE_EXTERNAL);
                        createNewNote();
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
        mNote = new Note();
        mNoteText.setText(mNote.getNoteText());
    }

    private boolean hasNoteTextChanged() {
        return !mNote.getNoteText().equals(mNoteText.getText().toString());
    }

    private void updateNote() {
        mNote.setNoteText(mNoteText.getText().toString());
    }
}
