package com.mediaworx.noteme.common.dragdrop;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

public class StableArrayAdapter<Z> extends ArrayAdapter<Z> {

    private static final String TAG = StableArrayAdapter.class.getSimpleName();

    private final int INVALID_ID = -1;

    protected List<Z> itemList;

    private HashMap<Z, Integer> mIdMap;

    public StableArrayAdapter(Context context, int rowResourceId, List<Z> itemList) {
        super(context, rowResourceId, itemList);
        this.itemList = itemList;
        refreshIdMap();
    }

    public void notifyDataSetChanged(boolean listCountChanged) {
        Log.d(TAG, "notifyDataSetChanged()");
        Log.d(TAG, "listCountChanged="+listCountChanged);

        if(listCountChanged)
            refreshIdMap();

        super.notifyDataSetChanged();
    }

    private void refreshIdMap(){
        Log.d(TAG, "refreshIdMap()");

        mIdMap = new HashMap<>();

        int listSize = itemList.size();

        for (int i = 0; i < listSize; ++i) {
            mIdMap.put(itemList.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        Z item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}