package crabster.rudakov.sberschoollesson22hwk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SmsReceiveListener {

    private SmsBroadcastReceiver receiver;

    /**
     * Помимо стандартных действий происходит регистрация 'SmsBroadcastReceiver',
     * а также производится запрос разрешения на чтение СМС-сообщений
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                1);

        registerReceiver();
    }

    /**
     * Помимо стандартных действий производится отмена регистрации
     *'SmsBroadcastReceiver'
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    /**
     * Метод создаёт объект 'SmsBroadcastReceiver', IntentFilter к нему и
     * осуществляет его регистрацию
     */
    private void registerReceiver() {
        receiver = new SmsBroadcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);

        registerReceiver(receiver, intentFilter);
        Log.d("RECEIVER", "WAS REGISTERED RECEIVER: " + receiver.getClass());
    }

    /**
     * Метод отменяет регистрацию 'SmsBroadcastReceiver'
     */
    private void unregisterReceiver() {
        unregisterReceiver(receiver);
        receiver = null;
    }

    /**
     * Метод устанавливает полученный из СМС-сообщения код в TextView
     */
    @Override
    public void onReceivedCode(String code) {
        ((TextView) findViewById(R.id.text_view)).setText(code);
        Log.d("CODE", "WAS RECEIVED CODE: " + code);
    }

}

//Никому не сообщайте код: 8789789. После подтверждения произойдет авторизация в системе