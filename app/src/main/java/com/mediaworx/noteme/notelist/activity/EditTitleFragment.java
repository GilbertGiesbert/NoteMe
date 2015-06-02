package com.mediaworx.noteme.notelist.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mediaworx.noteme.R;


public class EditTitleFragment extends Fragment implements View.OnClickListener{

    protected String TAG = EditTitleFragment.class.getSimpleName();

    static class ViewHolder {

        public Button bt_noteListTitle;
        public EditText et_noteListTitle;
        public ImageButton ib_noteListTitle;
    }

    private ViewHolder viewHolder;

    private int layoutContainerId;

    private EditTitleFragmentListener listener;

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach()");
        super.onAttach(activity);

        try {
            listener = (EditTitleFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " + EditTitleFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");

        String s_layoutResourceId = getResources().getResourceEntryName(layoutContainerId);// to view in debugger
        Log.d(TAG, "s_layoutResourceId="+s_layoutResourceId);


        View rootView = inflater.inflate(layoutContainerId, container, false);
        initTitle(rootView);
        return rootView;
    }


    public void initTitle(View rootView){
        Log.d(TAG, "initTitle()");

        viewHolder = new ViewHolder();

        viewHolder.bt_noteListTitle = (Button) rootView.findViewById(R.id.bt_noteList_title);
        viewHolder.bt_noteListTitle.setOnClickListener(this);
        viewHolder.et_noteListTitle = (EditText) rootView.findViewById(R.id.et_noteList_title);
        viewHolder.ib_noteListTitle = (ImageButton) rootView.findViewById(R.id.ib_noteList_title_confirm);
        viewHolder.ib_noteListTitle.setOnClickListener(this);

        switchTitleToReadMode();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick()");

        int viewId = v.getId();
        String s_viewId = getResources().getResourceEntryName(viewId);// to view in debugger
        Log.d(TAG, "viewId="+s_viewId);

        switch(viewId) {

            case R.id.bt_noteList_title:
                listener.onEditTitle();
                switchTitleToEditMode();
                break;

            case R.id.ib_noteList_title_confirm:

                if (isTitleInEditMode())
                    storeTitle();
                switchTitleToReadMode();
                break;
        }
    }

    public void switchTitleToReadMode(){
        Log.d(TAG, "switchTitleToReadMode()");

        viewHolder.bt_noteListTitle.setText(listener.getOriginalTitle());

        viewHolder.bt_noteListTitle.setVisibility(View.VISIBLE);
        viewHolder.et_noteListTitle.setVisibility(View.GONE);
        viewHolder.ib_noteListTitle.setVisibility(View.GONE);
    }

    public  void switchTitleToEditMode(){
        Log.d(TAG, "switchTitleToEditMode()");

        viewHolder.bt_noteListTitle.setVisibility(View.GONE);
        viewHolder.et_noteListTitle.setVisibility(View.VISIBLE);
        viewHolder.ib_noteListTitle.setVisibility(View.VISIBLE);

        viewHolder.et_noteListTitle.setText(listener.getOriginalTitle());
        viewHolder.et_noteListTitle.requestFocus();
    }

    public void storeTitle(){
        Log.d(TAG, "storeTitle()");

        String title = viewHolder.et_noteListTitle.getText().toString();
        title = title.trim();
        Log.d(TAG, "title="+title);

        String oldTitle = listener.getOriginalTitle();
        Log.d(TAG, "oldTitle="+oldTitle);

        boolean storeTitle = !title.isEmpty() && !title.equals(oldTitle);

        if(storeTitle)
            listener.onConfirmTitle(title);
    }

    public boolean isTitleInEditMode(){
        Log.d(TAG, "isTitleInEditMode()");

        return viewHolder.et_noteListTitle.getVisibility() == View.VISIBLE;
    }

    public void setLayoutContainerId(int layoutContainerId) {
        this.layoutContainerId = layoutContainerId;
    }
}