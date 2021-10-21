package crabster.rudakov.sberschoollesson22hwk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SmsReceiveListener {

    private SmsBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    private void registerReceiver() {
        receiver = new SmsBroadcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);

        registerReceiver(receiver, intentFilter);
        Log.d("RECEIVER", "WAS REGISTERED RECEIVER: " + receiver.getClass());
    }

    private void unregisterReceiver() {
        unregisterReceiver(receiver);
        receiver = null;
    }

    @Override
    public void onReceivedCode(String code) {
        ((TextView) findViewById(R.id.text_view)).setText(code);
        Log.d("CODE", "WAS RECEIVED CODE: " + code);
    }

}

//Никому не сообщайте код: 8789789. После подтверждения произойдет авторизация в системе