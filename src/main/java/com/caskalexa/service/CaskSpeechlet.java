package com.caskalexa.service;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CaskSpeechlet implements Speechlet {

    @Autowired
    MenuService menuService;

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

        if ( intentName.equals("BeersTodayIntent") ) {

            String beersListSpeech;

            try {
                beersListSpeech = menuService.topBeersSpeech();
            } catch (IOException e) {
                PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
                speech.setText("I'm sorry, I'm having trouble reading the menu!");
                SpeechletResponse problemResponse = SpeechletResponse.newTellResponse(speech);
                return problemResponse;
            }

            SimpleCard card = new SimpleCard();
            card.setTitle("Cask Beers");
            card.setContent(beersListSpeech);

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(beersListSpeech);

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