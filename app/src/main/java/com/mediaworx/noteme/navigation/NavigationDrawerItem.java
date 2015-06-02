package com.mediaworx.noteme.navigation;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.mediaworx.noteme.R;
import com.mediaworx.noteme.about.AboutActivity;
import com.mediaworx.noteme.common.application.App;
import com.mediaworx.noteme.helpfeedback.HelpFeedbackActivity;
import com.mediaworx.noteme.notelist.activity.NoteListOverviewActivity;
import com.mediaworx.noteme.settings.AppSettingsActivity;

public enum NavigationDrawerItem {

    NOTELISTOVERVIEW (R.drawable.home,     R.string.title_activity_noteListOverview,   NoteListOverviewActivity.class),
    SETTINGS         (R.drawable.gear,     R.string.title_activity_appSettings,        AppSettingsActivity.class),
    HELPFEEDBACK     (R.drawable.question, R.string.title_activity_helpFeedback,       HelpFeedbackActivity.class),
    ABOUT            (R.drawable.info,     R.string.title_activity_about,              AboutActivity.class);

    private static final String TAG = NavigationDrawerItem.class.getSimpleName();

    private int iconId;
    private int labelId;
    private Class targetActivityClass;

    private NavigationDrawerItem(int iconId, int labelId, Class<? extends Activity> targetActivityClass){
        this.iconId = iconId;
        this.labelId = labelId;
        this.targetActivityClass = targetActivityClass;
    }

    public Drawable getIcon(){
        return App.getAppContext().getResources().getDrawable(iconId);
    }

    public int getIconId(){
        return iconId;
    }

    public String getLabel(){
        return App.getAppContext().getResources().getString(labelId);
    }

    public Class getTargetActivityClass(){
        return targetActivityClass;
    }

    public int getPosition(){
        return ordinal();
    }

    public static NavigationDrawerItem valueOf(Class<? extends Activity> activityClass){
        Log.d(TAG, "valueOf()");

        for(NavigationDrawerItem item: NavigationDrawerItem.values()){
            if(item.getTargetActivityClass() == activityClass){
                return item;
            }
        }
        return null;
    }
}