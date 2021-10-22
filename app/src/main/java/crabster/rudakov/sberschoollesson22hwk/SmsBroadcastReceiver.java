package crabster.rudakov.sberschoollesson22hwk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private final WeakReference<SmsReceiveListener> smsReceiveListener;

    public SmsBroadcastReceiver(SmsReceiveListener smsReceiveListener) {
        this.smsReceiveListener = new WeakReference<>(smsReceiveListener);
    }

    /**
     * В случае соответствия интента условиям интент-фильтра метод получает
     * из него входящее СМС-сообщение, и преобразует его в строку, которую
     * затем передаёт в 'SmsReceiveListener' для установки в TextView, в
     * случае возникновения исключения показывается Toast-сообщение
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String smsBody = getSmsBody(intent);
            SmsReceiveListener listener = smsReceiveListener.get();
            if (listener != null)
                try {
                    listener.onReceivedCode(extractCode(smsBody));
                } catch (IndexOutOfBoundsException e) {
                    listener.onReceivedCode(context.getString(R.string.code_text_view));
                    Toast.makeText(context, "Неверный формат сообщения!", Toast.LENGTH_SHORT).show();
                }
            Log.d("MESSAGE RECEIVED", "WAS RECEIVED MESSAGE: \"" + smsBody + "\"");
        }
    }

    /**
     * Метод извлекает из интента массив, состоящий из кусков полученного
     * СМС-сообщения, и преобразует его в строку
     */
    private String getSmsBody(Intent intent) {
        SmsMessage[] messageChunks = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        StringBuilder smsBody = new StringBuilder();
        for (SmsMessage x: messageChunks) {
            smsBody.append(x.getMessageBody());
        }
        return smsBody.toString();
    }

    /**
     * Метод извлекает код из тела полученного СМС-сообщения определённого формата,
     * выбрасывая исключение в случае, если какой-либо из индексов равен -1
     */
    private String extractCode(String smsBody) throws IndexOutOfBoundsException {
        int firstDigitIndex = smsBody.indexOf(":") + 2;
        int endPointIndex = smsBody.indexOf(".", firstDigitIndex);
        return smsBody.subSequence(firstDigitIndex, endPointIndex).toString();
    }

}
