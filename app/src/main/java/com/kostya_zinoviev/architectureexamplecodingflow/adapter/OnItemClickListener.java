package com.kostya_zinoviev.architectureexamplecodingflow.adapter;

import android.view.View;

import com.kostya_zinoviev.architectureexamplecodingflow.model.Note;

public interface OnItemClickListener {
    //for recyclerview
    void onClickNote(Note clickedNote, int position, View view);
}
