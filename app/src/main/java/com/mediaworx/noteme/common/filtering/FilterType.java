package com.mediaworx.noteme.common.filtering;

import com.mediaworx.noteme.R;
import com.mediaworx.noteme.common.application.App;

import java.util.ArrayList;

public enum FilterType {

    FILTER_NOFILTER(R.string.noFilter),
    FILTER_CHECKED  (R.string.checked),
    FILTER_UNCHECKED(R.string.unchecked);


    private int labelId;

    private FilterType(int nameId){
        this.labelId = nameId;
    }

    public static ArrayList<String> labels(){

        ArrayList<String> labels = new ArrayList<>();

        for(FilterType type: FilterType.values()){
            labels.add(type.getLabel());
        }
        return labels;
    }

    public String getLabel(){
        return App.getAppContext().getResources().getString(labelId);
    }
}