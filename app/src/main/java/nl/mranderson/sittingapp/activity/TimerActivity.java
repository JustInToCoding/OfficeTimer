//package nl.mranderson.sittingapp.activity;
//
//import android.app.Activity;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.ActivityRecognition;
//
//import java.util.concurrent.TimeUnit;
//
//import co.mobiwise.materialintro.animation.MaterialIntroListener;
//import co.mobiwise.materialintro.shape.Focus;
//import co.mobiwise.materialintro.view.MaterialIntroView;
//import nl.mranderson.sittingapp.Constants;
//import nl.mranderson.sittingapp.MaterialIntroUtils;
//import nl.mranderson.sittingapp.R;
//import nl.mranderson.sittingapp.UserPreference;
//import nl.mranderson.sittingapp.Utils;
//import nl.mranderson.sittingapp.custom.CircularSeekBar;
//import nl.mranderson.sittingapp.service.ActivityRecognitionIntentService;
//import nl.mranderson.sittingapp.service.TimerService;
//
///**
// * A placeholder fragment containing a simple view.
// */
//public class TimerActivity extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
//
//    private TextView countDownText;
//    private TextView messageText;
//
//    private GoogleApiClient mGApiClient;
//    private long millisUntilFinished;
//    private BroadcastReceiver timerCountdownReceiver;
//    private BroadcastReceiver sensorReceiver;
//    private boolean sensor;
//    private boolean introShown;
//    private CircularSeekBar circularSeekbar;
//    private Button stopButton;
//
//    public TimerActivity() {
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_timer);
//
//        //Ads
//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//
//        countDownText = (TextView) this.findViewById(R.id.countdown);
//        messageText = (TextView) this.findViewById(R.id.messageText);
//        stopButton = (Button) this.findViewById(R.id.bStop);
//        stopButton.setOnClickListener(this);
//
//        int time = Constants.TIMER_SELECTED_TIME;
//        final int millisStarted = (time * 1000) * 60;
//
//        circularSeekbar = (CircularSeekBar) this.findViewById(R.id.seekBar2);
//        circularSeekbar.initDrawable(R.drawable.stickman_sitting_1);
//        circularSeekbar.hideProgressMarker();
//        circularSeekbar.stopTouching();
//        circularSeekbar.invalidate();
//
//        if (!Constants.IS_TIMER_SERVICE_RUNNING) {
//            startTimerService(time);
//        }
//
//        SharedPreferences prefs = this.getSharedPreferences(UserPreference.MY_PREFS_NAME, this.MODE_PRIVATE);
//        sensor = prefs.getBoolean("sensor", true);
//        if (sensor)
//            startSensors();
//
//        introShown = prefs.getBoolean("introShown", false);
//
//        timerCountdownReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if (intent.getExtras() != null) {
//                    millisUntilFinished = intent.getLongExtra("countdown", 0);
//
//                    countDownText.setText(String.format("%02d:%02d",
//                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
//                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
//                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
//                    ));
//
//                    setMessages(millisStarted, millisUntilFinished);
//                }
//            }
//        };
//        this.registerReceiver(timerCountdownReceiver, new IntentFilter(Constants.COUNTDOWN_TIME_BROADCAST));
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        //TODO introShown can be removed?
//        if (introShown) {
//            showStartTutorial();
//        }
//    }
//
//    private void showStartTutorial() {
//        final boolean[] opened1 = {true, true, true, true};
//        MaterialIntroView.Builder test = MaterialIntroUtils.getTimerTimeText(this);
//        test.setInfoText(getString(R.string.tutorial_timer_time_text))
//                .setTarget(countDownText)
//                .setFocusType(Focus.MINIMUM)
//                .setListener(new MaterialIntroListener() {
//                    @Override
//                    public void onUserClicked(String s) {
//                        if (opened1[0]) {
//                            opened1[0] = false;
//                            circularSeekbar.initDrawable(R.drawable.stickman_sitting_2);
//                            circularSeekbar.calculated = false;
//                            circularSeekbar.invalidate();
//
//                            MaterialIntroView.Builder test2 = MaterialIntroUtils.getTimerCircleButton(TimerActivity.this);
//                            test2.setInfoText(getString(R.string.tutorial_timer_circle))
//                                    .setTarget(circularSeekbar)
//                                    .setListener(new MaterialIntroListener() {
//                                        @Override
//                                        public void onUserClicked(String s) {
//                                            if (opened1[1]) {
//                                                opened1[1] = false;
//                                                MaterialIntroView.Builder test4 = MaterialIntroUtils.getTimerCircleButton2(TimerActivity.this);
//                                                test4.setInfoText(getString(R.string.tutorial_timer_circle_2))
//                                                        .setTarget(circularSeekbar)
//                                                        .setListener(new MaterialIntroListener() {
//                                                            @Override
//                                                            public void onUserClicked(String s) {
//                                                                if (opened1[2]) {
//                                                                    opened1[2] = false;
//                                                                    circularSeekbar.initDrawable(R.drawable.stickman_sitting_1);
//                                                                    circularSeekbar.calculated = false;
//                                                                    circularSeekbar.invalidate();
//
//                                                                    MaterialIntroView.Builder test5 = MaterialIntroUtils.getTimerCircleButton3(TimerActivity.this);
//                                                                    test5.setInfoText(getString(R.string.tutorial_timer_circle_3))
//                                                                            .setTarget(circularSeekbar)
//                                                                            .setListener(new MaterialIntroListener() {
//                                                                                @Override
//                                                                                public void onUserClicked(String s) {
//                                                                                    if (opened1[3]) {
//                                                                                        opened1[3] = false;
//                                                                                        MaterialIntroView.Builder test3 = MaterialIntroUtils.getTimerStopButton(TimerActivity.this);
//                                                                                        test3.setInfoText(getString(R.string.tutorial_stop_button))
//                                                                                                .setTarget(stopButton)
//                                                                                                .setFocusType(Focus.ALL)
//                                                                                                .enableDotAnimation(true)
//                                                                                                .show();
//                                                                                    }
//                                                                                }
//                                                                            });
//                                                                    test5.show();
//                                                                }
//                                                            }
//                                                        });
//                                                test4.show();
//                                            }
//                                        }
//                                    });
//                            test2.show();
//                        }
//                    }
//                });
//        test.show();
//    }
//
//    private void setMessages(long millisStarted, long millisUntilFinished) {
//        //Starting point
//        if (millisUntilFinished > (millisStarted * 0.50)) {
//            messageText.setText(R.string.messages_starting);
//        }
//
//        //Half way
//        if (millisUntilFinished <= (millisStarted * 0.50)) {
//            messageText.setText(R.string.messages_middle);
//        }
//
//        //End is near!
//        if (millisUntilFinished <= (millisStarted * 0.25)) {
//            messageText.setText(R.string.messages_end);
//        }
//    }
//
//    private void stopTimerService() {
//        this.sendBroadcast(new Intent(Constants.COUNTDOWN_STOP_BROADCAST));
//    }
//
//    private void startTimerService(int time) {
//        Intent timerServiceIntent = new Intent(this, TimerService.class);
//        timerServiceIntent.putExtra("time", time);
//        this.startService(timerServiceIntent);
//    }
//
//    @Override
//    public void onClick(View v) {
//        // Stop the service
//        stopTimerService();
//
//        // Replace fragment
//        super.onBackPressed();
//    }
//
//    private void startSensors() {
//        //Check Google Play Service Available
//        if (Utils.isPlayServiceAvailable(this)) {
//            mGApiClient = new GoogleApiClient.Builder(this)
//                    .addApi(ActivityRecognition.API)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .build();
//            //Connect to Google API
//            mGApiClient.connect();
//        }
//    }
//
//    private void stopSensors() {
//        if (mGApiClient != null)
//            mGApiClient.disconnect();
//    }
//
//    // Connection callback from play service.
//    @Override
//    public void onConnected(Bundle bundle) {
//        PendingIntent mActivityRecongPendingIntent = PendingIntent
//                .getService(this, 0, new Intent(this, ActivityRecognitionIntentService.class), PendingIntent.FLAG_UPDATE_CURRENT);
//        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGApiClient, 0, mActivityRecongPendingIntent);
//
//        circularSeekbar.initDrawable(R.drawable.stickman_sitting_2);
//        circularSeekbar.calculated = false;
//        circularSeekbar.invalidate();
//
//
//        sensorReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if (intent.getBooleanExtra("walking", false)) {
//                    messageText.setText(getString(R.string.messages_moving));
//                    circularSeekbar.initDrawable(R.drawable.stickman_walk_2);
//                    circularSeekbar.calculated = false;
//                    circularSeekbar.invalidate();
//                } else {
//                    circularSeekbar.initDrawable(R.drawable.stickman_sitting_2);
//                    circularSeekbar.calculated = false;
//                    circularSeekbar.invalidate();
//                }
//            }
//        };
//
//        this.registerReceiver(sensorReceiver, new IntentFilter(Constants.SENSOR_BROADCAST));
//    }
//
//    // Connection callback from play service.
//    @Override
//    public void onConnectionSuspended(int i) {
//        //TODO show user this feature doesn't work right now.
//    }
//
//    // Connection callback from play service.
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        //TODO show user this feature doesn't work right now.
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        unregisterReceiver(timerCountdownReceiver);
//
//        if (Utils.isPlayServiceAvailable(this) && sensor)
//            //TODO from add to app
//            unregisterReceiver(sensorReceiver);
//
//        stopSensors();
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (Constants.IS_TIMER_SERVICE_RUNNING) {
//            // Stop the service
//            stopTimerService();
//        }
//        super.onBackPressed();
//    }
//
//}