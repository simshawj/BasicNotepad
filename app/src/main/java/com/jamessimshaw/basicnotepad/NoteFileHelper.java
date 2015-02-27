package com.jamessimshaw.basicnotepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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
    View mFilenameDialogView;

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
            try {
                FileOutputStream outputStream;
                outputStream = mContext.openFileOutput(AUTOSAVE_FILENAME,
                        Context.MODE_PRIVATE);
                outputStream.write(mNote.getNoteText().getBytes());
                outputStream.close();
            }
            catch (IOException e) {
                Log.e(TAG, "Exception Caught:  ", e);
                //Create alert dialog and prompt to save
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(mContext.getString(R.string.autosaveFailAlertText));
                builder.setPositiveButton(mContext.getString(R.string.savePromptSaveOption),
                        mSaveListener);
                builder.setNegativeButton(mContext.getString(R.string.savePromptNoSaveOption),
                        null);
                builder.create().show();
            }
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();  //TODO: See if there's a better way to do this
            mFilenameDialogView = inflater.inflate(R.layout.dialog_filename, null);
            builder.setView(mFilenameDialogView);
            builder.setPositiveButton(mContext.getString(R.string.savePromptSaveOption),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText editText = (EditText)mFilenameDialogView.findViewById(R.id.dialogFilenameText);
                            String filename = editText.getText().toString();
                            if (isExternalStorageAvailable()) {
                                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), filename);
                                if (file.exists()) {
                                    // Prompt to overwrite
                                }
                                else {
                                    //write File
                                    Log.i(TAG, "File Does Not Exist");
                                }
                            }
                        }
                    });
            builder.create().show();
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

    public boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
