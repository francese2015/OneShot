package utility;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import watchout.MatchActivity;

/**
 * Created by Damiano on 02/05/15.
 *
 * Classe per gestire le push notification
 */
public class Receiver extends ParsePushBroadcastReceiver {

    private static final String TAG = "PUSH";
    private String alert;


    @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.e("Push", "Clicked");
        Intent i = new Intent(context, MatchActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive " + intent.getExtras().getString("com.parse.Data"));
        super.onReceive(context, intent);
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        Log.i(TAG, "onPushReceive " + intent.getExtras().getString("com.parse.Data"));
        super.onPushReceive(context, intent);
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        Log.i(TAG, "onPushDismiss " + intent.getExtras().getString("com.parse.Data"));
        super.onPushDismiss(context, intent);
    }
}

