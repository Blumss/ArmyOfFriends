package com.pemws14.armyoffriends.yourarmy;

/**
 * Created by Schnabeltier on 07.12.2014.
 */
import com.pemws14.armyoffriends.R;
import java.util.ArrayList;
import android.os.Bundle;
import android.app.ExpandableListActivity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ExpandableList extends  ExpandableListActivity
{
    //Initialize variables
    private static final String STR_CHECKED = " has Checked!";
    private static final String STR_UNCHECKED = " has unChecked!";
    private int ParentClickStatus=-1;
    private int ChildClickStatus=-1;
    private ArrayList<ParentRow> parents;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Resources res = this.getResources();
        Drawable divider = res.getDrawable(R.drawable.list_divider);

        // Set ExpandableListView values

        getExpandableListView().setGroupIndicator(null);
        getExpandableListView().setDivider(divider);
        getExpandableListView().setChildDivider(divider);
        getExpandableListView().setDividerHeight(1);
        registerForContextMenu(getExpandableListView());

        //Creating static data in arraylist
        final ArrayList<ParentRow> dummyList = buildDummyData();

        // Adding ArrayList data to ExpandableListView values
        loadHosts(dummyList);
    }

    /**
     * here should come your data service implementation
     * @return
     */
    private ArrayList<ParentRow> buildDummyData()
    {
        // Creating ArrayList of type parent class to store parent class objects
        final ArrayList<ParentRow> list = new ArrayList<ParentRow>();
        for (int i = 1; i < 4; i++)
        {
            //Create parent class object
            final ParentRow parent = new ParentRow();

            // Set values in parent class object
            if(i==1){
                parent.setName("" + i);
                parent.setText1("Parent 0");
                parent.setText2("Disable App On \nBattery Low");
                parent.setChildren(new ArrayList<ChildRow>());

                // Create ChildRow class object
                final ChildRow child = new ChildRow();
                child.setName("" + i);
                child.setText1("Child 0");

                //Add Child class object to parent class object
                parent.getChildren().add(child);
            }
            else if(i==2){
                parent.setName("" + i);
                parent.setText1("Parent 1");
                parent.setText2("Auto disable/enable App \n at specified time");
                parent.setChildren(new ArrayList<ChildRow>());

                final ChildRow child = new ChildRow();
                child.setName("" + i);
                child.setText1("Child 0");
                parent.getChildren().add(child);
                final ChildRow child1 = new ChildRow();
                child1.setName("" + i);
                child1.setText1("Child 1");
                parent.getChildren().add(child1);
            }
            else if(i==3){
                parent.setName("" + i);
                parent.setText1("Parent 1");
                parent.setText2("Show App Icon on \nnotification bar");
                parent.setChildren(new ArrayList<ChildRow>());

                final ChildRow child = new ChildRow();
                child.setName("" + i);
                child.setText1("Child 0");
                parent.getChildren().add(child);
                final ChildRow child1 = new ChildRow();
                child1.setName("" + i);
                child1.setText1("Child 1");
                parent.getChildren().add(child1);
                final ChildRow child2 = new ChildRow();
                child2.setName("" + i);
                child2.setText1("Child 2");
                parent.getChildren().add(child2);
                final ChildRow child3 = new ChildRow();
                child3.setName("" + i);
                child3.setText1("Child 3");
                parent.getChildren().add(child3);
            }

            //Adding Parent class object to ArrayList
            list.add(parent);
        }
        return list;
    }


    private void loadHosts(final ArrayList<ParentRow> newParents)
    {
        if (newParents == null)
            return;

        parents = newParents;

        // Check for ExpandableListAdapter object
        if (this.getExpandableListAdapter() == null)
        {
            //Create ExpandableListAdapter Object
            final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();

            // Set Adapter to ExpandableList Adapter
            this.setListAdapter(mAdapter);
        }
        else
        {
            // Refresh ExpandableListView data
            ((MyExpandableListAdapter)getExpandableListAdapter()).notifyDataSetChanged();
        }
    }

    /**
     * A Custom adapter to create Parent view (Used grouprow.xml) and Child View((Used childrow.xml).
     */
    private class MyExpandableListAdapter extends BaseExpandableListAdapter
    {


        private LayoutInflater inflater;

        public MyExpandableListAdapter()
        {
            // Create Layout Inflator
            inflater = LayoutInflater.from(ExpandableList.this);
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
                    getResources().getIdentifier(
                            "com.androidexample.customexpandablelist:drawable/setting"+parent.getName(),null,null));

            ImageView rightcheck=(ImageView)convertView.findViewById(R.id.rightcheck);

            //Log.i("onCheckedChanged", "isChecked: "+parent.isChecked());

            // Change right check image on parent at runtime
            if(parent.isChecked()==true){
                rightcheck.setImageResource(
                        getResources().getIdentifier(
                                "com.androidexample.customexpandablelist:drawable/rightcheck",null,null));
            }
            else{
                rightcheck.setImageResource(
                        getResources().getIdentifier(
                                "com.androidexample.customexpandablelist:drawable/button_check",null,null));
            }

            // Get grouprow.xml file checkbox elements
            CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            checkbox.setChecked(parent.isChecked());

            // Set CheckUpdateListener for CheckBox (see below CheckUpdateListener class)
            checkbox.setOnCheckedChangeListener(new CheckUpdateListener(parent));

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
                    getResources().getIdentifier(
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

                Toast.makeText(getApplicationContext(), "Parent :"+groupPosition + " Child :"+childPosition ,
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
            Log.i("Parent", groupPosition+"=  getGroup ");

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
                Toast.makeText(getApplicationContext(), "Parent :"+groupPosition ,
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

        private final class CheckUpdateListener implements OnCheckedChangeListener
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

                ((MyExpandableListAdapter)getExpandableListAdapter()).notifyDataSetChanged();

                final Boolean checked = parent.isChecked();
                Toast.makeText(getApplicationContext(),
                        "Parent : "+parent.getName() + " " + (checked ? STR_CHECKED : STR_UNCHECKED),
                        Toast.LENGTH_LONG).show();
            }
        }
        /***********************************************************************/

    }
}
