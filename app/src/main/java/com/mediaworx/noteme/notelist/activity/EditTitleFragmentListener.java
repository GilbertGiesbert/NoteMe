package com.mediaworx.noteme.notelist.activity;


public interface EditTitleFragmentListener {

    public void onConfirmTitle(String confirmedTitle);

    public void onEditTitle();

    public String getOriginalTitle();
}
