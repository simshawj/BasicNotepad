package com.jamessimshaw.basicnotepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class PageActivity extends Activity {
    public final String TAG = this.getClass().getSimpleName();
    private static final int LOAD_FILE_SELECTION = 1;

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

        if (id == R.id.action_load) {
            Intent intent = new Intent(this, ListFilesActivity.class);
            startActivityForResult(intent, LOAD_FILE_SELECTION);


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
        mNoteFileHelper = new NoteFileHelper();
    }

    private boolean hasNoteTextChanged() {
        return !mNote.getNoteText().equals(mNoteText.getText().toString());
    }

    private void updateNote() {
        mNote.setNoteText(mNoteText.getText().toString());
    }

    private void updateScreen() {
        mNoteText.setText(mNote.getNoteText());
    }

    private void loadNoteFromFile(final String filename) {
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
                    Note tempNote = mNoteFileHelper.loadNote(PageActivity.this,
                            filename);
                    if (tempNote != null)
                        mNote = tempNote;
                }
            });
            builder.setNegativeButton(getString(R.string.savePromptNoSaveOption), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Note tempNote = mNoteFileHelper.loadNote(PageActivity.this,
                            filename);
                    if (tempNote != null)
                        mNote = tempNote;
                }
            });
            builder.create().show();
        }
        else{
            Note tempNote = mNoteFileHelper.loadNote(this, filename);
            if (tempNote != null)
                mNote = tempNote;
        }
        updateScreen();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case (LOAD_FILE_SELECTION):
                String filename;
                if (resultCode == Activity.RESULT_OK) {
                    filename = data.getStringExtra("filename");
                    loadNoteFromFile(filename);
                }
                break;
        }
    }
}
