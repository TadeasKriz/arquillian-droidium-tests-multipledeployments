package org.arquillian.droidium.testapp.multipledeployments.emailvalidator;

import android.os.Bundle;

interface IEmailValidationService {
    /**
     * Returns a pending intent to launch the email validation.
     * @param email Email to be validated.
     * @return Bundle containing the following key-value pairs
     *         "RESPONSE_CODE" with int value, RESULT_OK(0) if success, other response codes on
     *              failure as listed above.
     *         "VALIDATOR_INTENT" - PendingIntent to start the validator
     *
     * The Pending intent should be launched with startIntentSenderForResult.
     *
     */
    Bundle getEmailValidatorIntent(String email, String applicationName, String packageName);
}