package com.mediaworx.noteme.common.sorting;

import com.mediaworx.noteme.R;
import com.mediaworx.noteme.common.application.App;

public enum SortType {

    ALPHABETICALLY          (R.string.alphabetically),
    ALPHABETICALLY_REVERSE  (R.string.alphabeticallyReverse),

    CHECKED_FIRST           (R.string.checkedFirst),
    UNCHECKED_FIRST         (R.string.uncheckedFirst),

    DATE_CREATED            (R.string.dateCreated),
    DATE_CREATED_REVERSE    (R.string.dateCreatedReverse),

    LAST_EDITED_TOP         (R.string.lastEditedTop),
    LAST_EDITED_BOTTOM      (R.string.lastEditedBottom),

    TITLE                   (R.string.title),
    TITLE_REVERSE           (R.string.titleReverse);


    private int labelId;

    private SortType(int nameId){
        this.labelId = nameId;
    }

    public String getLabel(){
        return App.getAppContext().getResources().getString(labelId);
    }
}