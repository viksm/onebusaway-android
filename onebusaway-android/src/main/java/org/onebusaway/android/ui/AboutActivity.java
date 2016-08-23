package org.onebusaway.android.ui;

import com.google.android.gms.common.GoogleApiAvailability;

import org.onebusaway.android.R;
import org.onebusaway.android.io.ObaAnalytics;
import org.onebusaway.android.util.UIUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * An Activity that displays version, license, and contributor information
 */
public class AboutActivity extends AppCompatActivity {

    TextView mTextView;

    public static void start(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(
                R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTextView = (TextView) findViewById(R.id.about_text);
        String versionString = "";
        int versionCode = 0;
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionString = info.versionName;
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder builder = new StringBuilder();
        // Version info
        builder.append("v")
                .append(versionString)
                .append(" (")
                .append(versionCode)
                .append(")\n\n");

        // Majority of content from string resource
        builder.append(getString(R.string.about_text));
        String googleOssLicense = null;

        // License info for Google Play Services, if available
        try {
            GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
            googleOssLicense = gaa.getOpenSourceSoftwareLicenseInfo(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (googleOssLicense != null) {
            builder.append(googleOssLicense);
        }

        builder.append("\n\n");

        new GoogleOssAsyncTask(builder.toString()).execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ObaAnalytics.reportActivityStart(this);
    }

    /**
     * Loads the OSS license info from Google Play Services asynchronously, as it currently takes
     * a long time to load (see #600)
     */
    private class GoogleOssAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mProgressDialog;

        private String mMainText;

        /**
         * @param mainText main text that appears above the Google OSS
         */
        public GoogleOssAsyncTask(String mainText) {
            mMainText = mainText;
        }

        @Override
        protected void onPreExecute() {
            if (UIUtils.canManageDialog(AboutActivity.this)) {
                mProgressDialog = ProgressDialog.show(AboutActivity.this, "",
                        "Loading...", true);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // We must modify the TextView from a UI Thread, so can't do it here
            return null;
        }

        @Override
        protected void onPostExecute(Void results) {
            // We must modify the TextView from a UI Thread, so do it before hiding progress bar
            mTextView.setText(mMainText);
            if (UIUtils.canManageDialog(AboutActivity.this) && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            super.onPostExecute(results);
        }
    }
}
