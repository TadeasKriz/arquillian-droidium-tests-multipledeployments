package org.arquillian.droidium.testapp.multipledeployments.instrumented;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.arquillian.droidium.testapp.multipledeployments.emailvalidator.IEmailValidationService;

public class MainActivity extends Activity {

    IEmailValidationService mEmailValidationService;

    ServiceConnection mEmailValidationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mEmailValidationService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mEmailValidationService = IEmailValidationService.Stub.asInterface(service);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean p = bindService(new Intent("org.arquillian.droidium.testapp.multipledeployments.emailvalidator.IEmailValidationService"),
                mEmailValidationServiceConnection, Context.BIND_AUTO_CREATE);
        setContentView(R.layout.main_activity);

        Button validate = (Button) findViewById(R.id.button_validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editText_email);
                String email = editText.getText().toString();
                String applicationName = getString(R.string.app_name);
                String packageName = getPackageName();

                try {
                    Bundle response = mEmailValidationService.getEmailValidatorIntent(email, applicationName, packageName);
                    PendingIntent validationIntent = response.getParcelable("VALIDATION_INTENT");
                    startIntentSenderForResult(validationIntent.getIntentSender(), 1001, new Intent(),
                            0, 0, 0);
                } catch(IntentSender.SendIntentException e) {
                    TextView output = (TextView) findViewById(R.id.textView_output);
                    output.setText(e.getMessage());
                } catch(RemoteException e) {
                    TextView output = (TextView) findViewById(R.id.textView_output);
                    output.setText(e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001) {
            TextView output = (TextView) findViewById(R.id.textView_output);
            if(resultCode == RESULT_OK) {
                output.setText("User has verified this email!");
            } else {
                output.setText("User didn't verify the email!");
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mEmailValidationServiceConnection != null) {
            unbindService(mEmailValidationServiceConnection);
        }
    }
}
