package org.onebusaway.android.report.ui;

import org.onebusaway.android.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class TestSocialActivity extends AppCompatActivity {

    public static final String HAS_EXTRA_BUTTON = ".hasExtraButton";

    public static final String EXTRA_BUTTON_NAME = ".extraButtonName";

    public static final String EXTRA_BUTTON_ACTION = ".extraButtonAction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_extra_intent);

        boolean hasExtraButton = getIntent().getBooleanExtra(HAS_EXTRA_BUTTON, false);

        if (hasExtraButton) {

            String buttonName = getIntent().getStringExtra(EXTRA_BUTTON_NAME);

            final Intent targetActionIntent = getIntent().getParcelableExtra(EXTRA_BUTTON_ACTION);

            Button button = (Button) findViewById(R.id.button);
            button.setText(buttonName);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(targetActionIntent);
                }
            });

        }
    }
}
