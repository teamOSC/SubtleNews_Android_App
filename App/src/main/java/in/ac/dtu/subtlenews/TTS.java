package in.ac.dtu.subtlenews;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * Created by saurav on 15/12/13.
 */
public class TTS extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private String text = "Hello World";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            text = extras.getString("TEXT");
        }

        tts = new TextToSpeech(this, this);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    //private void speakOut() {
    //}

}
