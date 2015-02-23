package com.jamessimshaw.basicnotepad;

import android.net.Uri;

/**
 * Created by james on 2/23/15.
 */
public class Note {
    String mNoteText;

    public Note(String noteText) {
        mNoteText = noteText;
    }

    public String getNoteText() {

        return mNoteText;
    }

    public void setNoteText(String noteText) {
        mNoteText = noteText;
    }
}
