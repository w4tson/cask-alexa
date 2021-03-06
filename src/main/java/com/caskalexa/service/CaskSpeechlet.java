package com.caskalexa.service;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class CaskSpeechlet implements Speechlet {

    public static final String INTRO = "Welcome to Cask Beers. ";
    public static final String HOW_TO = "Find out more about drinks by asking: what is on?";
    public static final String AMAZON_HELP_INTENT = "AMAZON.HelpIntent";
    public static final String AMAZON_STOP_INTENT = "AMAZON.StopIntent";
    public static final String AMAZON_CANCEL_INTENT = "AMAZON.CancelIntent";
    public static final String HELP_SPEECH = "Cask Beers can tell you more information about the daily beer menu. You can ask for me info by asking: What is on the menu?";
    public static final String HELP_REPROMPT = "You can ask for me info by asking: What is on the menu?";
    public static final String BEERS_TODAY_INTENT = "BeersTodayIntent";

    @Autowired
    private MenuService menuService;

    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        log.info("User {} opened Cask Beers app", session.getUser().getUserId());
        return ask(INTRO + HOW_TO, HOW_TO);
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {

        Intent intent = request.getIntent();
        if (intent == null)
            throw new SpeechletException("Unrecognized intent");

        String intentName = intent.getName();

        switch (intentName) {
            case BEERS_TODAY_INTENT:

                String beersListSpeech;

                try {
                    beersListSpeech = menuService.topBeersSpeech();
                } catch (IOException e) {
                    return respond("I'm sorry, I'm having trouble reading the menu!");
                }

                SimpleCard card = new SimpleCard();
                card.setTitle("Cask Beers");
                card.setContent(beersListSpeech);

                PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
                speech.setText(beersListSpeech);

                SpeechletResponse response = SpeechletResponse.newTellResponse(speech, card);
                return response;
            case AMAZON_HELP_INTENT:
                return ask(HELP_SPEECH, HELP_REPROMPT);
            case AMAZON_CANCEL_INTENT:
            case AMAZON_STOP_INTENT:
                return new SpeechletResponse();
            default:
                throw new SpeechletException("I don't understand that intent.");
        }
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {

    }

    private SpeechletResponse respond(String text) {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(text);
        return SpeechletResponse.newTellResponse(speech);
    }

    private SpeechletResponse ask(String text, String repromptText) {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(text);
        Reprompt reprompt = new Reprompt();
        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText(repromptText);
        reprompt.setOutputSpeech(repromptSpeech);
        return SpeechletResponse.newAskResponse(speech, reprompt);
    }
}