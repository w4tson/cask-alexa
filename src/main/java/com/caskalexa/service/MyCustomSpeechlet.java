package com.caskalexa.service;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import org.springframework.stereotype.Service;

@Service
public class MyCustomSpeechlet implements Speechlet {

    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText("yo yo yo");
        return SpeechletResponse.newTellResponse(speech);
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {

        Intent intent = request.getIntent();
        if (intent == null)
            throw new SpeechletException("Unrecognized intent");

        String intentName = intent.getName();

        if ( intentName.equals("HelloIntent") ) {

            String speechText = "Hello, World.  I am a Spring Boot custom skill.";

            SimpleCard card = new SimpleCard();
            card.setTitle("Hello World");
            card.setContent(speechText);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            SpeechletResponse response = SpeechletResponse.newTellResponse(speech, card);
            return response;
        }
        else {
            throw new SpeechletException("I don't understand that intent.");
        }
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {

    }
}