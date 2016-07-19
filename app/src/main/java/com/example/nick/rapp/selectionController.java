package com.example.nick.rapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class selectionController extends AppCompatActivity {
    ExpandableListView testView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_screen);
        testView = (ExpandableListView) findViewById(R.id.testList);
        List<String> headings = new ArrayList<String>();
        List<String> L1 = new ArrayList<String>();
        List<String> L2 = new ArrayList<String>();
        HashMap<String, List<String>> childList = new HashMap<String, List<String>>();

        String heading_items[] = getResources().getStringArray(R.array.header_titles);
        String l1[] = getResources().getStringArray(R.array.tests);
        String l2[] = getResources().getStringArray(R.array.students);

        for (String title: heading_items){
            headings.add(title);
        }
        for (String title : l1){
            L1.add(title);
        }

        for (String title : l2){
            L2.add(title);
        }
        childList.put(headings.get(0), L1);
        childList.put(headings.get(1), L2);
        listAdapter listAdapter = new listAdapter(headings, childList, this);
        testView.setAdapter(listAdapter);
    }
}
