package com.jamessimshaw.basicnotepad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by james on 2/26/15.
 */
public class NoteFileHelper {
    public static final int FILE_AUTOSAVE = 0;
    public static final int FILE_EXTERNAL = 1;
    private static final String AUTOSAVE_FILENAME = "autosave.txt";
    public final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private Note mNote;

    DialogInterface.OnClickListener mSaveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            saveNote(mContext, mNote, FILE_EXTERNAL);
        }
    };


    public void saveNote(Context context, Note note, int saveFlag) {
        mContext = context;
        mNote = note;

        if (saveFlag == FILE_AUTOSAVE) {
            FileOutputStream outputStream;

            try {
                outputStream = context.openFileOutput(AUTOSAVE_FILENAME, Context.MODE_PRIVATE);
                outputStream.write(note.getNoteText().getBytes());
                outputStream.close();
            }
            catch (IOException e) {
                Log.e(TAG, "Exception Caught:  ", e);
                //Create alert dialog and prompt to save
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getString(R.string.autosaveFailAlertText));
                builder.setPositiveButton(context.getString(R.string.savePromptSaveOption), mSaveListener);
                builder.setNegativeButton(context.getString(R.string.savePromptNoSaveOption), null);
                builder.create().show();
            }
        }
        else {

        }
    }

    public Note loadNote(Context context, int loadFlag) {
        if (loadFlag == FILE_AUTOSAVE) {
            FileInputStream inputStream;
            String inputString = "";

            try {
                inputStream = context.openFileInput(AUTOSAVE_FILENAME);
                byte[] buffer = new byte[1024];
                int length;
                while ((inputStream.read(buffer)) != -1) {
                    inputString += new String(buffer);
                }
                mNote = new Note(inputString);
            } catch (IOException e) {
                Log.e(TAG, "Exception Caught:  ", e);
            }
        }
        return mNote;
    }

    public boolean autosaveNoteAvailable(Context context) {
        File file = new File(context.getFilesDir(), AUTOSAVE_FILENAME);

        return file.exists();
    }
}
