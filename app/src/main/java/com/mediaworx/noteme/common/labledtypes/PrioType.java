package com.mediaworx.noteme.common.labledtypes;


import com.mediaworx.noteme.R;
import com.mediaworx.noteme.common.application.App;

public enum PrioType {

    LOW     (R.string.prio_low),
    MEDIUM  (R.string.prio_medium),
    HIGH    (R.string.prio_high),
    CRITICAL(R.string.prio_critical);

    private int labelId;

    private PrioType(int labelId){
        this.labelId = labelId;
    }

    public String getLabel(){
        return App.getAppContext().getResources().getString(labelId);
    }
}