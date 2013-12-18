package in.ac.dtu.subtlenews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.TextView;

import org.json.JSONException;

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
        AlertDialog summaryBox = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme))
                .setMessage(text)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();

                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        finish();
                    }
                })
                .show();
        TextView summaryText = (TextView) summaryBox.findViewById(android.R.id.message);
        summaryText.setTextSize(12);


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
/*
    @Override
    public void onBackPressed() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
*/
}
