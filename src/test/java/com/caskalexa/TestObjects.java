package com.caskalexa;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.User;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static com.caskalexa.service.CaskSpeechlet.AMAZON_CANCEL_INTENT;
import static com.caskalexa.service.CaskSpeechlet.AMAZON_STOP_INTENT;

public class TestObjects {

    public static IntentRequest stop() {
        return intent(AMAZON_STOP_INTENT);
    }

    public static IntentRequest cancel() {
        return intent(AMAZON_CANCEL_INTENT);
    }

    public static IntentRequest intent(String intent) {
        return IntentRequest.builder()
                .withIntent(Intent.builder()
                        .withName(intent)
                        .build())
                .withRequestId(UUID.randomUUID().toString())
                .build();
    }

    public static Session session() {
        return Session.builder()
                .withSessionId(UUID.randomUUID().toString())
                .withUser(user())
                .build();
    }

    public static User user() {
        return User.builder()
                .withUserId("User1")
                .build();

    }

    public static LaunchRequest launchRequest() {
        return LaunchRequest.builder()
                .withRequestId(UUID.randomUUID().toString())
                .withTimestamp(new Date())
                .withLocale(Locale.UK)
                .build();
    }
}
