package com.mediaworx.noteme.common.labledtypes;


import com.mediaworx.noteme.R;
import com.mediaworx.noteme.common.application.App;

public enum ColorType {

    BLUE  (R.string.blue,   R.color.light_blue),
    CYAN  (R.string.cyan,   R.color.light_cyan),
    GREEN (R.string.green,  R.color.light_green),
    PINK  (R.string.pink,   R.color.light_pink),
    RED   (R.string.red,    R.color.light_red),
    WHITE (R.string.white,  R.color.white),
    YELLOW(R.string.yellow, R.color.light_yellow);

    private int labelId;
    private int colorId;

    private ColorType(int labelId, int colorId){
        this.labelId = labelId;
        this.colorId = colorId;
    }

    public String getLabel(){
        return App.getAppContext().getResources().getString(labelId);
    }

    public int getColorValue(){
        return App.getAppContext().getResources().getColor(colorId);
    }
}