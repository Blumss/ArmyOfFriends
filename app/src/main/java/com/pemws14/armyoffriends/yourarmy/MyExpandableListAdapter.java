package com.pemws14.armyoffriends.yourarmy;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pemws14.armyoffriends.R;

import java.util.ArrayList;

/**
 * Created by Schnabeltier on 07.12.2014.
 */
/**
 * A Custom adapter to create Parent view (Used grouprow.xml) and Child View((Used childrow.xml).
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter
{
    //Initialize variables
    private static final String STR_CHECKED = " has Checked!";
    private static final String STR_UNCHECKED = " has unChecked!";
    private int ParentClickStatus=-1;
    private int ChildClickStatus=-1;
    private ArrayList<ParentRow> parents;
    private Activity mActivity;

    private LayoutInflater inflater;

    public MyExpandableListAdapter(Activity activity, ArrayList<ParentRow> parents)
    {   mActivity = activity;
        this.parents = parents;
        // Create Layout Inflator
        inflater = mActivity.getLayoutInflater();
    }


    // This Function used to inflate parent rows view

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parentView)
    {
        final ParentRow parent = parents.get(groupPosition);

        // Inflate grouprow.xml file for parent rows
        convertView = inflater.inflate(R.layout.your_army_group_row_layout, parentView, false);

        // Get grouprow.xml file elements and set values
        ((TextView) convertView.findViewById(R.id.text1)).setText(parent.getText1());
        ((TextView) convertView.findViewById(R.id.text)).setText(parent.getText2());
        ImageView image=(ImageView)convertView.findViewById(R.id.image);

        image.setImageResource(
                mActivity.getResources().getIdentifier(
                        "com.androidexample.customexpandablelist:drawable/setting"+parent.getName(),null,null));

        //ImageView rightcheck=(ImageView)convertView.findViewById(R.id.rightcheck);

        //Log.i("onCheckedChanged", "isChecked: "+parent.isChecked());

        // Change right check image on parent at runtime
        if(parent.isChecked()==true){
//            rightcheck.setImageResource(
//                    mActivity.getResources().getIdentifier(
//                            "com.androidexample.customexpandablelist:drawable/rightcheck",null,null));
        }
        else{
//            rightcheck.setImageResource(
//                    mActivity.getResources().getIdentifier(
//                            "com.androidexample.customexpandablelist:drawable/button_check",null,null));
        }
        return convertView;
    }


    // This Function used to inflate child rows view
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parentView)
    {
        final ParentRow parent = parents.get(groupPosition);
        final ChildRow child = parent.getChildren().get(childPosition);

        // Inflate childrow.xml file for child rows
        convertView = inflater.inflate(R.layout.your_army_child_row_layout, parentView, false);

        // Get childrow.xml file elements and set values
        ((TextView) convertView.findViewById(R.id.text1)).setText(child.getText1());
        ImageView image=(ImageView)convertView.findViewById(R.id.image);
        image.setImageResource(
                mActivity.getResources().getIdentifier(
                        "com.androidexample.customexpandablelist:drawable/setting"+parent.getName(),null,null));

        return convertView;
    }


    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        //Log.i("Childs", groupPosition+"=  getChild =="+childPosition);
        return parents.get(groupPosition).getChildren().get(childPosition);
    }

    //Call when child row clicked
    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        /****** When Child row clicked then this function call *******/

        //Log.i("Noise", "parent == "+groupPosition+"=  child : =="+childPosition);
        if( ChildClickStatus!=childPosition)
        {
            ChildClickStatus = childPosition;

            Toast.makeText(mActivity.getApplicationContext(), "Parent :" + groupPosition + " Child :" + childPosition,
                    Toast.LENGTH_LONG).show();
        }

        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        int size=0;
        if(parents.get(groupPosition).getChildren()!=null)
            size = parents.get(groupPosition).getChildren().size();
        return size;
    }


    @Override
    public Object getGroup(int groupPosition)
    {
        Log.i("Parent", groupPosition + "=  getGroup ");

        return parents.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return parents.size();
    }

    //Call when parent row clicked
    @Override
    public long getGroupId(int groupPosition)
    {
        Log.i("Parent", groupPosition+"=  getGroupId "+ParentClickStatus);

        if(groupPosition==2 && ParentClickStatus!=groupPosition){

            //Alert to user
            Toast.makeText(mActivity.getApplicationContext(), "Parent :"+groupPosition ,
                    Toast.LENGTH_LONG).show();
        }

        ParentClickStatus=groupPosition;
        if(ParentClickStatus==0)
            ParentClickStatus=-1;

        return groupPosition;
    }

    @Override
    public void notifyDataSetChanged()
    {
        // Refresh List rows
        super.notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty()
    {
        return ((parents == null) || parents.isEmpty());
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }



    /******************* Checkbox Checked Change Listener ********************/

    private final class CheckUpdateListener implements CompoundButton.OnCheckedChangeListener
    {
        private final ParentRow parent;

        private CheckUpdateListener(ParentRow parent)
        {
            this.parent = parent;
        }
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            Log.i("onCheckedChanged", "isChecked: "+isChecked);
            parent.setChecked(isChecked);

            //((MyExpandableListAdapter)getExpandableListAdapter()).notifyDataSetChanged();
            MyExpandableListAdapter.this.notifyDataSetChanged();
            final Boolean checked = parent.isChecked();
            Toast.makeText(mActivity.getApplicationContext(),
                    "Parent : "+parent.getName() + " " + (checked ? STR_CHECKED : STR_UNCHECKED),
                    Toast.LENGTH_LONG).show();
        }
    }
    /***********************************************************************/

}
