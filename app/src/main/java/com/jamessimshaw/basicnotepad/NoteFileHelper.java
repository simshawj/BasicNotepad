package com.jamessimshaw.basicnotepad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

    public void saveNote(final Context context, final Note note, int saveFlag) {
        if (saveFlag == FILE_AUTOSAVE) {
            try {
                FileOutputStream fileOutputStream;
                fileOutputStream = context.openFileOutput(AUTOSAVE_FILENAME,
                        Context.MODE_PRIVATE);
                fileOutputStream.write(note.getNoteText().getBytes());
                fileOutputStream.close();
            }
            catch (IOException e) {
                Log.e(TAG, "Exception Caught:  ", e);
                //Create alert dialog and prompt to save
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getString(R.string.autosaveFailAlertText));
                builder.setPositiveButton(context.getString(R.string.savePromptSaveOption),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveNote(context, note, FILE_EXTERNAL);
                            }
                        });
                builder.setNegativeButton(context.getString(R.string.savePromptNoSaveOption),
                        null);
                builder.create().show();
            }
        }
        else {
            //TODO: Refactor this code block
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View filenameDialogView = inflater.inflate(R.layout.dialog_filename, null);
            builder.setView(filenameDialogView);
            builder.setPositiveButton(context.getString(R.string.savePromptSaveOption),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText editText = (EditText) filenameDialogView.
                                    findViewById(R.id.dialogFilenameText);
                            String filename = editText.getText().toString();
                            if (isExternalStorageAvailable()) {
                                final File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "BasicNotepad");
                                final File file = new File(directory, filename);
                                directory.mkdirs();                       //Creates directories if they don't exist
                                if (file.exists()) {
                                    // Prompt to overwrite
                                    AlertDialog.Builder overwriteBuilder = new AlertDialog.Builder(context);
                                    overwriteBuilder.setMessage(context.getString(R.string.overwritePromptMessage));
                                    overwriteBuilder.setPositiveButton(context.getString(R.string.overwritePromptAffirmative),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    writeTextToFile(context, note.getNoteText(), file);
                                                }
                                            });
                                    overwriteBuilder.setNegativeButton(context.getString(R.string.overwritePromptNegative),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    saveNote(context, note, FILE_EXTERNAL);
                                                }
                                            });
                                    overwriteBuilder.create().show();
                                }
                                else {
                                    writeTextToFile(context, note.getNoteText(), file);
                                }
                            }
                            else {
                                Toast.makeText(context, context.getString(R.string.externalStorageUnavailableToastMessage), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            builder.create().show();
        }
    }

    private void writeTextToFile(final Context context, String string, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(string.getBytes());
            fileOutputStream.close();
        }
        catch(IOException e) {
            Log.d(TAG, context.getString(R.string.ioExceptionWritingLogMessage));
        }
    }

    public Note loadNote(final Context context, int loadFlag) {
        Note note = null;
        if (loadFlag == FILE_AUTOSAVE) {
            String inputString = "";
            FileInputStream inputStream;
            try {
                inputStream = context.openFileInput(AUTOSAVE_FILENAME);
                byte[] buffer = new byte[1024];
                while ((inputStream.read(buffer)) != -1) {
                    inputString += new String(buffer).trim();
                }
                inputStream.close();
                note = new Note(inputString);
            } catch (IOException e) {
                Toast.makeText(context, context.getString(R.string.loadFileIOErrorToast), Toast.LENGTH_LONG).show();
            }
            context.deleteFile(AUTOSAVE_FILENAME);
        }
        return note;
    }

    public Note loadNote(final Context context, String filename) {
        Note note = null;
        String inputString = "";
        try {
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "BasicNotepad");
            File file = new File(directory, filename);
            FileInputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            while ((inputStream.read(buffer)) != -1) {
                inputString += new String(buffer).trim();
            }
            inputStream.close();
            note = new Note(inputString);
        } catch (IOException e) {
            Toast.makeText(context, context.getString(R.string.loadFileIOErrorToast), Toast.LENGTH_LONG).show();
        }

        return note;
    }

    public boolean autosaveNoteAvailable(final Context context) {
        File file = new File(context.getFilesDir(), AUTOSAVE_FILENAME);

        return file.exists();
    }

    public boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
