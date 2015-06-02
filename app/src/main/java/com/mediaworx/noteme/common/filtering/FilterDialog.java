package com.mediaworx.noteme.common.filtering;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.mediaworx.noteme.R;

import java.util.Arrays;

public class FilterDialog extends DialogFragment {

    private static final String TAG = FilterDialog.class.getSimpleName();

    private static final int NO_PRESELECT = -1;

    private FilterType[] selectableFilters;

    private FilterType filterToPreselect;

    private FilterDialogListener dialogListener;

    @Override
    public void onAttach(Activity activity){
        Log.d(TAG, "onAttach()");

        super.onAttach(activity);

        try {
            // Instantiate the FilterDialogListener so we can send events to the host
            dialogListener = (FilterDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement "+FilterDialogListener.class.getSimpleName());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Log.d(TAG, "onCreateDialog()");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.filterList));

        builder.setSingleChoiceItems(getLabels(), getPreselection(), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick()");
                Log.d(TAG, "which=" + which);

                Log.d(TAG, "filterToPreselect=" + filterToPreselect);

                FilterType filterSelected = Arrays.asList(selectableFilters).get(which);
                Log.d(TAG, "filterSelected=" + filterSelected);

                if(filterSelected != filterToPreselect){

                    filterToPreselect = filterSelected;
                    dialogListener.onFilterSelected(filterSelected);
                }
                dismiss();
            }
        });
        return builder.create();
    }

    private String[] getLabels(){
        Log.d(TAG, "getLabels()");

        String[] labels = new String[selectableFilters.length];

        for(int i = 0; i < labels.length; i++)
            labels[i] = selectableFilters[i].getLabel();
        
        return labels;
    }

    private int getPreselection(){
        Log.d(TAG, "getPreselection()");

        if(filterToPreselect != null)
            return Arrays.asList(selectableFilters).indexOf(filterToPreselect);
        
        else
            return NO_PRESELECT;
    }

    public void doNotPreselect(FilterType filterNotToPreselect){
        Log.d(TAG, "doNotPreselect()");
        Log.d(TAG, "filterNotToPreselect=" + filterNotToPreselect);

        if(filterToPreselect != null && filterToPreselect.equals(filterNotToPreselect))
            filterToPreselect = null;
    }

    public void setSelectableFilters(FilterType[] selectableFilters) {
        this.selectableFilters = selectableFilters;
    }

    public void setFilterToPreselect(FilterType filterToPreselect) {
        this.filterToPreselect = filterToPreselect;
    }
}