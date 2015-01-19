package com.pemws14.armyoffriends.army;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.text.Html;

import com.pemws14.armyoffriends.GameMechanics;
import com.pemws14.armyoffriends.R;
import com.pemws14.armyoffriends.database.DbHelper;
import com.pemws14.armyoffriends.database.DbSoldier;
import com.pemws14.armyoffriends.drawer.BaseActivity;


import java.util.ArrayList;
import java.util.List;


public class ArmyActivity extends BaseActivity implements View.OnCreateContextMenuListener, ExpandableListView.OnChildClickListener {
    ExpandableListView mList;
    ExpandableListAdapter mAdapter;
    boolean mFinishedStart = false;
    private ArrayList<ParentRow> parents;
    private DbHelper db;

    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = this.getResources();
        Drawable divider = res.getDrawable(R.drawable.list_divider);

        //<-- IN EVERY ACTIVITY WITH DRAWER
        super.set();
        // catches Frame, where to insert actual ActivityView
        ViewGroup parent = (ViewGroup) findViewById(R.id.content_frame);
        //View of new activity
        view = LayoutInflater.from(this).inflate(R.layout.activity_your_army, parent, false);
        parent.addView(view);
        //--> IN EVERY ACTIVITY WITH DRAWER
        mList = (ExpandableListView) findViewById(R.id.yourArmyListView);
        mList.setGroupIndicator(null);
        mList.setDivider(divider);
        mList.setChildDivider(divider);
        mList.setDividerHeight(1);
        registerForContextMenu(mList);
        //Creating static data in arraylist
        final ArrayList<ParentRow> dummyList = buildDummyData();

        // Adding ArrayList data to ExpandableListView values
        loadHosts(dummyList);
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return false;
    }


    @Override
    protected void onRestoreInstanceState(Bundle state){
        ensureList();
        super.onRestoreInstanceState(state);
    }

    /**
     * Provide the adapter for the expandable list.
     */
    public void setListAdapter(ExpandableListAdapter adapter) {
        synchronized (this) {
            ensureList();
            mAdapter = adapter;
            mList.setAdapter(adapter);
        }
    }

    public ExpandableListView getExpandableListView(){
        ensureList();
        return mList;
    }
    public ExpandableListAdapter getExpandableListAdapter() {
        return mAdapter;
    }

    private void ensureList(){
        if (mList != null){
            return;
        }
        //setContentView(com.android.internal.R.layout.expandable_list_content);
        //setContentView(R.layout.activity_your_army);
    }

    /**
     * Gets the ID of the currently selected group or child.
     *
     * @return The ID of the currently selected group or child.
     */
    public long getSelectedId() {
        return mList.getSelectedId();
    }

    /**
     * Gets the position (in packed position representation) of the currently
     * selected group or child. Use
     * {@link ExpandableListView#getPackedPositionType},
     * {@link ExpandableListView#getPackedPositionGroup}, and
     * {@link ExpandableListView#getPackedPositionChild} to unpack the returned
     * packed position.
     *
     * @return A packed position representation containing the currently
     *         selected group or child's position and type.
     */
    public long getSelectedPosition() {
        return mList.getSelectedPosition();
    }

    /**
     * Sets the selection to the specified child. If the child is in a collapsed
     * group, the group will only be expanded and child subsequently selected if
     * shouldExpandGroup is set to true, otherwise the method will return false.
     *
     * @param groupPosition The position of the group that contains the child.
     * @param childPosition The position of the child within the group.
     * @param shouldExpandGroup Whether the child's group should be expanded if
     *            it is collapsed.
     * @return Whether the selection was successfully set on the child.
     */
    public boolean setSelectedChild(int groupPosition, int childPosition, boolean shouldExpandGroup) {
        return mList.setSelectedChild(groupPosition, childPosition, shouldExpandGroup);
    }

    /**
     * Sets the selection to the specified group.
     * @param groupPosition The position of the group that should be selected.
     */
    public void setSelectedGroup(int groupPosition) {
        mList.setSelectedGroup(groupPosition);
    }

    private void createRanks(ParentRow parent, String rank){
        parent.setName(rank);
        parent.setText1(rank);
        //parent.setLevelNextRank("Disable App On \nBattery Low");
        parent.setChildren(new ArrayList<ChildRow>());
    };

    private ArrayList<ParentRow> buildDummyData()
    {
        db = new DbHelper(getApplicationContext());

        // Creating ArrayList of type parent class to store parent class objects
        final ArrayList<ParentRow> list = new ArrayList<ParentRow>();
        for (int i = 10; i > 0; i--)
        {
            //Create parent class object
            final ParentRow parent = new ParentRow();
            String[] ranks = getResources().getStringArray(R.array.army_ranks);

            // Set values in parent class object
            createRanks(parent, ranks[i-1]);
            parent.setText2("" + parent.getChildren().size());

            // Create ChildRow class object
            List<DbSoldier> soldiers = db.getSoldiersWithRank(i);
            for(DbSoldier s:soldiers){
                ChildRow child = new ChildRow();
                child.setName(s.getName());
                child.setLevel("Level " + s.getLevel());
                int levelForRankUp = GameMechanics.getLevelForRankUp(s.getLevel());
                child.setLevelNextRank(levelForRankUp > 0 ? String.valueOf(levelForRankUp) + " " + Html.fromHtml("&#8593;") : "");
                parent.getChildren().add(child);
            }
            //Add Child class object to parent class object
            parent.setText2("" + parent.getChildren().size());

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
            final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter(ArmyActivity.this, parents);

            // Set Adapter to MyExpandableList Adapter
            this.setListAdapter(mAdapter);
        }
        else
        {
            // Refresh ExpandableListView data
            ((MyExpandableListAdapter)getExpandableListAdapter()).notifyDataSetChanged();
        }
    }
}
