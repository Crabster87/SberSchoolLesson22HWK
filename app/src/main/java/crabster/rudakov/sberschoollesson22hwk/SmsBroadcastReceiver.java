package crabster.rudakov.sberschoollesson22hwk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                } catch (IllegalStateException e) {
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
        for (SmsMessage x : messageChunks) {
            smsBody.append(x.getMessageBody());
        }
        return smsBody.toString();
    }

    /**
     * Метод извлекает код(последовательность цифр любой длины отличной от 0)
     * из тела полученного СМС-сообщения, выбрасывая исключение в случае
     * отсутствия совпадений
     */
    private String extractCode(String smsBody) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(smsBody);
        if (matcher.find())
            return matcher.group();
        else throw new IllegalStateException();
    }

}
