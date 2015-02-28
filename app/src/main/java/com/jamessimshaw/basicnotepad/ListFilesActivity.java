package com.jamessimshaw.basicnotepad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;


public class ListFilesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_files);

        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "BasicNotepad");
        final ListView fileListView = (ListView) findViewById(R.id.fileListView);
        ArrayList<String> filenames = new ArrayList<String>();
        for (File file : directory.listFiles()) {
            filenames.add(file.getName());
        }

        fileListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filenames));
        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = (String) parent.getItemAtPosition(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("filename", filename);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
