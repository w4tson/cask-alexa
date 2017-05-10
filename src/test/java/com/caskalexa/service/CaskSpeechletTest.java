package com.caskalexa.service;

import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.caskalexa.TestObjects.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CaskSpeechletTest {

    @InjectMocks
    CaskSpeechlet caskSpeechlet;

    @Mock
    MenuService menuService;

    @Test
    public void shouldExitWhenUsersSayCancel() throws SpeechletException {
        SpeechletResponse response = this.caskSpeechlet.onIntent(cancel(), session());
        assertThat(response.getShouldEndSession()).isTrue();
    }

    @Test
    public void shouldExitWhenUsersSayStop() throws SpeechletException {
        SpeechletResponse response = this.caskSpeechlet.onIntent(stop(), session());
        assertThat(response.getShouldEndSession()).isTrue();
    }

    @Test
    public void shouldReturnListOfBeersWhenUserAsksForMenu() throws Exception {
        String speech = "The beers today are Beer1, Beer2 etc etc";
        when(menuService.topBeersSpeech()).thenReturn(speech);
        SpeechletResponse speechletResponse = this.caskSpeechlet.onIntent(intent(CaskSpeechlet.BEERS_TODAY_INTENT), session());
        verify(menuService).topBeersSpeech();
        assertThat(speechletResponse.getShouldEndSession()).isTrue();
        assertThat(speechletResponse.getOutputSpeech()).isInstanceOf(PlainTextOutputSpeech.class);

        PlainTextOutputSpeech plainTextOutputSpeech = (PlainTextOutputSpeech) speechletResponse.getOutputSpeech();
        assertThat(plainTextOutputSpeech.getText()).isEqualTo(speech);
    }

    @Test
    public void shouldReturnIntroSpeechWhenLaunched() throws Exception {
        SpeechletResponse speechletResponse = caskSpeechlet.onLaunch(launchRequest(), session());
        assertThat(speechletResponse.getShouldEndSession()).isFalse();
        PlainTextOutputSpeech outputSpeech = (PlainTextOutputSpeech)speechletResponse.getOutputSpeech();
        assertThat(outputSpeech.getText()).isEqualTo(CaskSpeechlet.INTRO + CaskSpeechlet.HOW_TO);

    }
}