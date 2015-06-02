package com.mediaworx.noteme.common.dropdown;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mediaworx.noteme.common.application.App;

import java.util.List;


public abstract class DropDownFragment<Z> extends Fragment {

    private static final String TAG = DropDownFragment.class.getSimpleName();

    private static final int NO_ID = -1;

    // flag that helps to avoid first time selection of dropdown
    // which is NOT triggered by user but caused by initiation of dropdown
    protected boolean dropDownInitialized;

    protected Spinner spinner;

    protected int layoutContainerId = NO_ID;
    protected int dropdownViewId = NO_ID;
    protected Z preselected;
    protected List<Z> selectables;

    protected OnDropDownSelectedListener<Z> listener;

    protected abstract List<String> getLabels();

    protected abstract int getPositionToPreselect();

    public void setLayoutIds(int layoutContainerId, int dropdownViewId){
        Log.d(TAG, "setLayoutIds()");
        Log.d(TAG, "layoutContainerId="+ App.getResourceName(layoutContainerId));
        Log.d(TAG, "dropdownViewId="+App.getResourceName(dropdownViewId));

        this.layoutContainerId = layoutContainerId;
        this.dropdownViewId = dropdownViewId;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach()");
        super.onAttach(activity);

        try {
            listener = (OnDropDownSelectedListener<Z>) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " + OnDropDownSelectedListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");

        View rootView;

        if(layoutContainerId != NO_ID && dropdownViewId != NO_ID){

            rootView = inflater.inflate(layoutContainerId, container, false);
            spinner = (Spinner) rootView.findViewById(dropdownViewId);

        }else{

            // needs no layout definitions in xml
            spinner = new Spinner(getActivity());
            rootView = spinner;
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected()");
                Log.d(TAG, "dropDownInitialized=" + dropDownInitialized);

                // see comments at declaration of 'blockedDropdownAtInitiation'
                if (!dropDownInitialized)
                    dropDownInitialized = true;

                else
                    listener.onDropDownSelected(selectables.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected()");
            }
        });

        if(selectables != null)
            setAdapter();

        return rootView;
    }

    protected void setAdapter(){
        Log.d(TAG, "setAdapter()");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, getLabels());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(getPositionToPreselect());
    }

    public void updateSelectables(Z preselected, List<Z> selectables){
        Log.d(TAG, "updateSelectables()");
        Log.d(TAG, "preselected="+preselected);
        Log.d(TAG, "selectables="+selectables);

        this.preselected = preselected;
        this.selectables = selectables;

        if(spinner != null)
            setAdapter();
    }
}