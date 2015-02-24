package com.jamessimshaw.basicnotepad;

/**
 * Created by james on 2/23/15.
 */
public class Note {
    private String mNoteText;

    public Note() {
        mNoteText = "";
    }

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
