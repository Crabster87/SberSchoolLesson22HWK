package crabster.rudakov.sberschoollesson22hwk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.lang.ref.WeakReference;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private final WeakReference<SmsReceiveListener> smsReceiveListener;

    public SmsBroadcastReceiver(SmsReceiveListener smsReceiveListener) {
        this.smsReceiveListener = new WeakReference<>(smsReceiveListener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String smsBody = getSmsBody(intent);
            SmsReceiveListener listener = smsReceiveListener.get();
            if (listener != null)
                listener.onReceivedCode(extractCode(smsBody));
            Log.d("MESSAGE RECEIVED", "WAS RECEIVED CODE: " + smsBody);
        }
    }

    private String getSmsBody(Intent intent) {
        SmsMessage[] messageChunks = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        StringBuilder smsBody = new StringBuilder();
        for (SmsMessage x: messageChunks) {
            smsBody.append(x.getMessageBody());
        }
        return smsBody.toString();
    }

    private String extractCode(String smsBody) {
        int firstDigitIndex = smsBody.indexOf(":") + 2;
        int endPointIndex = smsBody.indexOf(".", firstDigitIndex);
        return smsBody.subSequence(firstDigitIndex, endPointIndex).toString();
    }

}
