package com.example.nick.rapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Nick on 7/19/2016.
 */
public class listAdapter extends BaseExpandableListAdapter {


    private List<String> headers;
    private HashMap<String, List<String>> children;
    private Context ctx;

    public listAdapter(List<String> headers, HashMap<String, List<String>> children, Context ctx) {
        this.headers = headers;
        this.children = children;
        this.ctx = ctx;
    }

    @Override
    public int getGroupCount() {
        return headers.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return children.get(headers.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return children.get(headers.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) this.getGroup(groupPosition);

        if (convertView == null){
            LayoutInflater layoutInflator =
                    (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflator.inflate(R.layout.parent_layout, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.testHeading);
      //  TextView textView = (TextView) convertView.findViewById(R.id.child);
        textView.setTypeface(null, Typeface.BOLD);

        textView.setText(title);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String title = (String) this.getChild(groupPosition, childPosition);

        if (convertView == null){
            LayoutInflater layoutInflator =
                    (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflator.inflate(R.layout.child_layout, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.child);
        //  TextView textView = (TextView) convertView.findViewById(R.id.child);
     //   textView.setTypeface(null, Typeface.BOLD);

        textView.setText(title);

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
