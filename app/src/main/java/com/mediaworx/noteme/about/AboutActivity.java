package com.mediaworx.noteme.about;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.TextView;

import com.mediaworx.noteme.R;
import com.mediaworx.noteme.navigation.NavigationDrawerActivity;

public class AboutActivity extends NavigationDrawerActivity {

    private static final String TAG = AboutActivity.class.getSimpleName();

    @Override
    protected int getMainContentLayoutId() {
        return R.layout.about_activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(pInfo != null){

            String template = getResources().getString(R.string.about_versionInfo);
            String smiley = getSmiley();
            String text = String.format(template, pInfo.versionName, smiley);


            TextView tv = (TextView) findViewById(R.id.about_versionInfo);
            tv.setText(text);

            Spannable spanText = new SpannableString(tv.getText());

            int multiplyTextSizeBy = 2;
            int spanStart = text.length() - smiley.length();
            int spanEnd = text.length();

            spanText.setSpan(new RelativeSizeSpan(multiplyTextSizeBy), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tv.setText(spanText);
        }
    }

    private String getSmiley() {
        Integer unicode = 0x1F60A;
        return  new String(Character.toChars(unicode));
    }
}