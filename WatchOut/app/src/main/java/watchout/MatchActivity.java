package watchout;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosimoalessandro.watchout.FinalActivity;
import com.example.cosimoalessandro.watchout.R;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TimeZone;
import utility.Chronometer;
import utility.Receiver;
/**
 * Created by Damiano on 02/05/15.
 */
public class MatchActivity extends Activity {
    private TextView mTextField;
    private String messageContent;
    private String alert;
    private long seconds;
    private long millisDiff;
    private Chronometer mChronometer;
    private String matchID;
    private ParseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        mTextField=(TextView)findViewById(R.id.mTextField);
        user = ParseUser.getCurrentUser();

        Button pushButton = (Button) findViewById(R.id.push);
        pushButton.setOnClickListener(mResetListener);


        Intent intent = getIntent();
        alert = intent.getExtras().getString("com.parse.Data");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", user.getObjectId());
        //mTextField.setText("Alert Message is: " + alert);
        ParseCloud.callFunctionInBackground("get_match", params, new FunctionCallback<ParseObject>() {
            public void done(ParseObject match, ParseException e) {
                if (e == null) {
                    Date currentDate = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:s");
                    TimeZone timeZone = TimeZone.getDefault();
                    sdf.setTimeZone(timeZone);
                    // Calculates the difference in milliseconds.
                    millisDiff = currentDate.getTime() - match.getCreatedAt().getTime();
                    mChronometer = (Chronometer) findViewById(R.id.chronometer1);

                    Log.d("Time", Long.toString(millisDiff));

                    seconds = (long) (millisDiff / 1000);
                    //mChronometer.setBase(millisDiff);
                    //mChronometer.setBase(millisDiff);
                    mChronometer.start();

                    matchID = match.getObjectId();


                    mTextField.setText("notification: " + sdf.format(match.getCreatedAt().getTime()) + " current date: " + currentDate + " differenza in secondi: " + seconds);
                    // result is "Hello world!"
                    //Toast.makeText(getFragmentActivity(), result, Toast.LENGTH_LONG).show();
                }
            }
        });


        //IF you want to stop your chrono after X seconds or minutes.
     /*   mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer chronometer) {
                if (chronometer.getText().toString().equalsIgnoreCase("00:20:00")) { //When reaches 5 seconds.
                    //Define here what happens when the Chronometer reaches the time above.
                    chronometer.stop();
                    Toast.makeText(getBaseContext(),"Reached the end.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });*/
        /*//Prova CountDownTimer
        new CountDownTimer(30000, 1000) {
        //CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long
            public void onTick(long millisUntilFinished) {
                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }
            public void onFinish() {
                mTextField.setText("done!");
            }
        }
                .start();*/
    }

    View.OnClickListener mResetListener = new View.OnClickListener() {
        public void onClick(View v) {
            mChronometer.stop();

            Log.d("tempo", Long.toString(mChronometer.getTimeElapsed()));

            updateTime(mChronometer.getTimeElapsed());

            Intent intent = new Intent(MatchActivity.this, FinalActivity.class);
            MatchActivity.this.startActivity(intent);

            finish();
        }
    };


    public void updateTime(final long time) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");

        query.getInBackground(matchID, new GetCallback<ParseObject>() {
            public void done(ParseObject matchUp, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.

                    if(matchUp.getString("user_id1").equals(user.getObjectId())){
                        matchUp.put("time1", time);
                        matchUp.saveInBackground();
                    }
                    else if(matchUp.getString("user_id2").equals(user.getObjectId())){
                        matchUp.put("time2", time);
                        matchUp.saveInBackground();
                    }

                } else {
                    Log.d("errore", "errore" +
                            "");
                }
            }
        });
    }



}