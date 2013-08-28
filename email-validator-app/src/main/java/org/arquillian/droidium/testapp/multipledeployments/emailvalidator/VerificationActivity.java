package org.arquillian.droidium.testapp.multipledeployments.emailvalidator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VerificationActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();

            String email = extras.getString("email");
            String applicationName = extras.getString("applicationName");
            String packageName = extras.getString("packageName");

            if(email == null || email.equals("") || applicationName == null || applicationName.equals("") ||
                    packageName == null || packageName.equals("")) {
                // failed
                verificationFailed("One of fields is null or empty");
                return;
            }

            setContentView(R.layout.verification_activity);

            TextView text = (TextView) findViewById(R.id.textView_text);
            Button positive = (Button) findViewById(R.id.button_positive);
            Button negative = (Button) findViewById(R.id.button_negative);

            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verificationSucceeded();
                }
            });

            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verificationFailed("User decision - negative");
                }
            });

            text.setText("Application '" + applicationName + "' (pkg: " + packageName + ") wants you to verify the " +
                    "email '" + email + "' is yours. Is this your email?");



        } else {
            verificationFailed("Extras are null!");
            return;
        }
    }

    private void verificationSucceeded() {
        setResult(RESULT_OK);
        finish();
    }

    private void verificationFailed(String error) {
        setResult(RESULT_CANCELED);
        Log.e("VerificationActivity", error);
        finish();
    }
}
