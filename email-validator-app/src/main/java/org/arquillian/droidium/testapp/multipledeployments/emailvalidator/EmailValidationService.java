package org.arquillian.droidium.testapp.multipledeployments.emailvalidator;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

public class EmailValidationService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IEmailValidationService.Stub mBinder = new IEmailValidationService.Stub() {

        @Override
        public Bundle getEmailValidatorIntent(String email, String applicationName, String packageName) throws RemoteException {
            Bundle options = new Bundle();

            Intent intent = new Intent(EmailValidationService.this, VerificationActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("applicationName", applicationName);
            intent.putExtra("packageName", packageName);

            PendingIntent pendingIntent = PendingIntent.getActivity(EmailValidationService.this, 0, intent, 0);

            Bundle result = new Bundle();

            result.putParcelable("VALIDATION_INTENT", pendingIntent);

            return result;
        }
    };
}
