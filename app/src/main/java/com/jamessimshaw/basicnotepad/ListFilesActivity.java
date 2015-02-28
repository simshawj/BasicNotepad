package com.jamessimshaw.basicnotepad;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;


public class ListFilesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_files);

        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "BasicNotepad");
        ArrayList<File> files = new ArrayList<File>();
        for (File file : directory.listFiles()) {
            files.add(file);
        }
    }
}
