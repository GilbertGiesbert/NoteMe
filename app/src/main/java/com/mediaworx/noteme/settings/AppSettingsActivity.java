package com.mediaworx.noteme.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.mediaworx.noteme.R;
import com.mediaworx.noteme.common.application.PreferencesManager;
import com.mediaworx.noteme.common.storage.StorageHelper;
import com.mediaworx.noteme.common.storage.StorageType;
import com.mediaworx.noteme.navigation.NavigationDrawerActivity;

public class AppSettingsActivity extends NavigationDrawerActivity{

    private static final String TAG = AppSettingsActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // storage type selection
        Spinner storageTypeSpinner = (Spinner) findViewById(R.id.storageType_spinner);
        storageTypeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, StorageType.labelsAsArray()));

        // set selected storage type
        int pos = 0;
        for (String storageLabel: StorageType.labelsAsArray()) {
            if (PreferencesManager.getStorageType().getLabel().equalsIgnoreCase(storageLabel)) {
                storageTypeSpinner.setSelection(pos);
                break;
            }
            pos++;
        }
        storageTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                StorageType storageType = StorageType.values()[pos];
                PreferencesManager.setStorageType(storageType);

            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // delete storage button
        final Button bt_appsettings_deleteContent = (Button) findViewById(R.id.bt_appsettings_deleteContent);

        bt_appsettings_deleteContent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDeleteContentDialog();
            }
        });

    }


    public void showDeleteContentDialog() {
        Log.d(TAG, "showDeleteContentDialog()");
        new AlertDialog.Builder(this)
                .setMessage(R.string.storage_deleteContentConfirmation)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // delete content
                        StorageHelper.deleteContent();
                        AppSettingsActivity.this.finish();

                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }



    @Override
    protected int getMainContentLayoutId() {
        return R.layout.appsettings_activity;
    }
}