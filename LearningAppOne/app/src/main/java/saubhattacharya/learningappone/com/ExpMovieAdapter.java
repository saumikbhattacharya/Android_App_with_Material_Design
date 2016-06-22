package saubhattacharya.learningappone.com;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

public class ExpMovieAdapter extends BaseExpandableListAdapter{

    ArrayList<String> movieList;
    LayoutInflater inflater;
    Context context;

    HashMap<String,ArrayList<String>> ChildRowItem;

    public ExpMovieAdapter (ArrayList<String> List, HashMap<String,ArrayList<String>> ChildItem, Context c)
    {
        this.context = c;
        this.movieList = List;
        this.ChildRowItem = ChildItem;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return this.movieList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.ChildRowItem.get(this.movieList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.movieList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.ChildRowItem.get(this.movieList.get(groupPosition)).get(childPosition);
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
        if (convertView == null)
            convertView = inflater.inflate(R.layout.expandable_list_group, parent, false);

        String str_groupheader = (String)getGroup(groupPosition);
        TextView groupheader = (TextView)convertView.findViewById(R.id.listgroupheader);
        groupheader.setText(str_groupheader);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.expandable_list_item, parent, false);

        String group_item = (String)getChild(groupPosition,childPosition);
        TextView groupitem = (TextView)convertView.findViewById(R.id.grouplistitemval);
        groupitem.setText(group_item);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
