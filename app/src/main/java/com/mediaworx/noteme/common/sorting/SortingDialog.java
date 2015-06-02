package com.mediaworx.noteme.common.sorting;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.mediaworx.noteme.R;

import java.util.Arrays;

public class SortingDialog extends DialogFragment {

    private static final String TAG = SortingDialog.class.getSimpleName();

    private static final int NO_PRESELECT = -1;

    private SortType[] selectableSortTypes;

    private SortType sortTypeToPreselect;

    private SortingDialogListener dialogListener;

    @Override
    public void onAttach(Activity activity){
        Log.d(TAG, "onAttach()");

        super.onAttach(activity);

        try {
            // Instantiate the SortingDialogListener so we can send events to the host
            dialogListener = (SortingDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement "+SortingDialogListener.class.getSimpleName());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Log.d(TAG, "onCreateDialog()");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.sortList));

        builder.setSingleChoiceItems(getLabels(), getPreselection(), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick()");
                Log.d(TAG, "which=" + which);

                Log.d(TAG, "sortTypeToPreselect=" + sortTypeToPreselect);

                SortType sortTypeSelected = Arrays.asList(selectableSortTypes).get(which);
                Log.d(TAG, "sortTypeSelected=" + sortTypeSelected);

                if(sortTypeSelected != sortTypeToPreselect){

                    sortTypeToPreselect = sortTypeSelected;
                    dialogListener.onSortTypeSelected(sortTypeSelected);
                }
                dismiss();
            }
        });
        return builder.create();
    }

    private String[] getLabels(){
        Log.d(TAG, "getLabels()");

        String[] labels = new String[selectableSortTypes.length];

        for(int i = 0; i < labels.length; i++)
            labels[i] = selectableSortTypes[i].getLabel();

        return labels;
    }

    private int getPreselection(){
        Log.d(TAG, "getPreselection()");

        if(sortTypeToPreselect != null)
            return Arrays.asList(selectableSortTypes).indexOf(sortTypeToPreselect);

        else
            return NO_PRESELECT;
    }
    
    public void doNotPreselect(SortType sortTypeNotToPreselect){
        Log.d(TAG, "doNotPreselect()");
        Log.d(TAG, "sortTypeNotToPreselect=" + sortTypeNotToPreselect);

        if(sortTypeToPreselect != null && sortTypeToPreselect.equals(sortTypeNotToPreselect))
            sortTypeToPreselect = null;
    }

    public void setSelectableSortTypes(SortType[] selectableSortTypes) {
        this.selectableSortTypes = selectableSortTypes;
    }

    public void setSortTypeToPreselect(SortType sortTypeToPreselect) {
        this.sortTypeToPreselect = sortTypeToPreselect;
    }
}