package edu.luc.etl.cs313.android.simplestopwatch.android;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;

import java.io.IOException;
import java.util.Locale;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.common.Constants;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.ConcreteStopwatchModelFacade;
import edu.luc.etl.cs313.android.simplestopwatch.model.StopwatchModelFacade;


/**
 * A thin adapter component for the stopwatch.
 *
 * @author laufer
 */
public class StopwatchAdapter extends Activity implements StopwatchModelListener {

    private static String TAG = "stopwatch-android-activity";

    /**
     * The state-based dynamic model.
     */
    private StopwatchModelFacade model;

    /**
     * Setter method for the state-based dynamic model.
     *
     * @param model The model to set.
     */
    protected void setModel(final StopwatchModelFacade model) {
        this.model = model;
    }

    /**
     * Implementation of the method called when an Activity is started.
     *
     * @param savedInstanceState The instance containing data from before the Activity was last terminated/ended.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inject dependency on view so this adapter receives UI events
        setContentView(R.layout.activity_main);
        // inject dependency on model into this so model receives UI events
        this.setModel(new ConcreteStopwatchModelFacade());
        // inject dependency on this into model to register for UI updates
        model.setModelListener(this);

        OrientationEventListener orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                int epsilon = 10;
                int leftLandscape = 90;
                int rightLandscape = 270;

                if(epsilonCheck(orientation, leftLandscape, epsilon) || epsilonCheck(orientation, rightLandscape, epsilon)) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }
            private boolean epsilonCheck(int orientation, int landscapeMode, int epsilon) {
                return orientation > landscapeMode - epsilon && orientation < landscapeMode + epsilon;
            }
        };
        orientationEventListener.enable();
    }

    public void changeOrientation(int orientation) {
        setRequestedOrientation(orientation);
    }

    /**
     *
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    /**
     *
     */
    @Override
    protected void onStart() {
        super.onStart();
        model.start();
    }

    // TODO remaining lifecycle methods

    /**
     * Updates the seconds and minutes in the UI.
     *
     * @param time The time to update to the UI.
     */
    public void onTimeUpdate(final int time) {
        // UI adapter responsibility to schedule incoming events on UI thread
        runOnUiThread(() -> {
            final TextView tvS = findViewById(R.id.seconds);
            // final TextView tvM = findViewById(R.id.minutes);
            final var locale = Locale.getDefault();
            tvS.setText(String.format(locale,"%02d", time));
            // tvM.setText(String.format(locale,"%02d", time / Constants.SEC_PER_MIN));
        });
    }

    /**
     * Updates the state name in the UI.
     *
     * @param stateId The state name to update in the UI.
     */
    public void onStateUpdate(final int stateId) {
        // UI adapter responsibility to schedule incoming events on UI thread
        runOnUiThread(() -> {
            final TextView stateName = findViewById(R.id.stateName);
            stateName.setText(getString(stateId));
        });
    }

    /**
     * Prepares media player to play the alarm sound.
     * To change the alarm being played, change RingtoneManager.
     *
     * @param notification_sound The sound to use for the notification (from RingtoneManager).
     */
    public void soundAlarm(final int notification_sound) {
        final Uri defaultSoundUri = RingtoneManager.getDefaultUri(notification_sound);
        final MediaPlayer player = new MediaPlayer();
        final Context context = getApplicationContext();

        try {
            player.setDataSource(context, defaultSoundUri);
            player.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build());
            player.prepare();
            player.setOnCompletionListener(MediaPlayer::release);
            player.start();
        } catch(final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Forwards the event listener methods to the model.
     *
     * @param view The view of the state machine.
     */
    public void onButton(final View view) {
        model.onButton();
    }

    /**
     * Using EditText object, obtains the user number for the UI.
     * Still ensures that this new number is less than Constants.SEC_MAX.
     * */
    @Override
    public int getUserRuntime() {
        String stringNum = ((EditText)findViewById(R.id.userTime)).getText().toString();
        if (!stringNum.isEmpty()) {
            int intNum = Integer.parseInt(stringNum);
            if (intNum > Constants.SEC_MAX) {
                intNum = Constants.SEC_MAX;
            }
            return intNum;
        } else {
            return Constants.UI_DEFAULT;
        }
    }
}
