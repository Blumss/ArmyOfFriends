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


public class ExpandableList extends  ExpandableListActivity
{

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
            final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter(ExpandableList.this, parents);

            // Set Adapter to ExpandableList Adapter
            this.setListAdapter(mAdapter);
        }
        else
        {
            // Refresh ExpandableListView data
            ((MyExpandableListAdapter)getExpandableListAdapter()).notifyDataSetChanged();
        }
    }
}
