package com.example.infogames;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpListAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> listGroup;
    HashMap<String, List<String>> listItem;
    Boolean[] access;

    public ExpListAdapter(Context context, List<String> listGroup,
                          HashMap<String, List<String>>listItem) {
        this.context = context;
        this.listGroup = listGroup;
        this.listItem = listItem;
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listItem.get(this.listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listItem.get(this.listGroup.get(groupPosition)).get(childPosition);
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
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String group = (String) getGroup(i);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService((Context.LAYOUT_INFLATER_SERVICE));
            view = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView textView = view.findViewById(R.id.list_parent);
        textView.setText(group);
        if (access[i])
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check, 0);
        else
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock, 0);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String child = (String) getChild(i, i1);
        if (view ==null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView textView = view.findViewById(R.id.list_child);
        textView.setText(child);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public Boolean[] getAccess() {
        return access;
    }

    public void setAccess(Boolean[] access) {
        this.access = access;
    }
}
