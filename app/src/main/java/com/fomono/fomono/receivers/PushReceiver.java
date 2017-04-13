package com.fomono.fomono.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fomono.fomono.R;
import com.fomono.fomono.activities.FomonoActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by David on 4/5/2017.
 */

public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "PushReceiver";
    public static final String intentAction = "com.parse.push.intent.RECEIVE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            Log.d(TAG, "Receiver intent null");
        } else {
            // Parse push message and handle accordingly
            processPush(context, intent);
        }
    }

    private void processPush(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "got action " + action);
        if (action.equals(intentAction)) {
            String channel = intent.getExtras().getString("com.parse.Channel");
            try {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                Log.d(TAG, "got action " + action + " on channel " + channel + " with: " + json.toString());
                JSONObject eventData = json.getJSONObject("eventData");
                String apiName = eventData.getString("apiName");
                String id = eventData.getString("id");
                String text = eventData.getString("text");
                createNotification(context, apiName, id, text);
            } catch (JSONException ex) {
                Log.d(TAG, "JSON failed!");
            }
        }
    }

    public static final int NOTIFICATION_ID = 1;
    // Create a local dashboard notification to tell user about the event
    // See: http://guides.codepath.com/android/Notifications
    private void createNotification(Context context, String apiName, String id, String text) {
        // First let's define the intent to trigger when notification is selected
        // Start out by creating a normal intent (in this case to open an activity)
        Intent intent = new Intent(context, FomonoActivity.class);
        intent.setAction(FomonoActivity.ACTION_DETAIL);
        intent.putExtra("apiName", apiName);
        intent.putExtra("id", id);
        // Next, let's turn this into a PendingIntent using
        //   public static PendingIntent getActivity(Context context, int requestCode,
        //       Intent intent, int flags)
        int requestID = (int) System.currentTimeMillis(); //unique requestID to differentiate between various notification with same NotifId
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; // cancel old intent and create new one
        PendingIntent pIntent = PendingIntent.getActivity(context, requestID, intent, flags);
        // Now we can attach the pendingIntent to a new notification using setContentIntent
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(text)
                .setContentText(context.getString(R.string.notif_action_text))
                .setContentIntent(pIntent)
                .setAutoCancel(true); // Hides the notification after its been selected
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    // Handle push notification by invoking activity directly
    // See: http://guides.codepath.com/android/Using-Intents-to-Create-Flows
//    private void launchSomeActivity(Context context, String datavalue) {
//        Intent pupInt = new Intent(context, ShowPopUp.class);
//        pupInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        pupInt.putExtra("data", datavalue);
//        context.getApplicationContext().startActivity(pupInt);
//    }
//
//    // Handle push notification by sending a local broadcast
//    // to which the activity subscribes to
//    // See: http://guides.codepath.com/android/Starting-Background-Services#communicating-with-a-broadcastreceiver
//    private void triggerBroadcastToActivity(Context context, String datavalue) {
//        Intent intent = new Intent(intentAction);
//        intent.putExtra("data", datavalue);
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//    }
}
