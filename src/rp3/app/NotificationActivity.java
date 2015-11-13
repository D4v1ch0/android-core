package rp3.app;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Window;

import java.util.Locale;

import rp3.core.R;
import rp3.util.NotificationPusher;

/**
 * Created by magno_000 on 23/06/2015.
 */
public class NotificationActivity extends BaseActivity {

    TextToSpeech t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notification);

        String title = getIntent().getExtras().getString(NotificationPusher.TAG_TITLE);
        final String message = getIntent().getExtras().getString(NotificationPusher.TAG_MESSAGE);
        String footer = getIntent().getExtras().getString(NotificationPusher.TAG_FOOTER);

        setTextViewText(R.id.title, title);
        setTextViewText(R.id.message, message);
        setTextViewText(R.id.footer, footer);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    try {
                        t1.setLanguage(new Locale("es", "ES"));
                        t1.speak(message, TextToSpeech.QUEUE_FLUSH, null);
                    } catch (Exception ex)
                    {}
                }
            }
        });

    }
}
