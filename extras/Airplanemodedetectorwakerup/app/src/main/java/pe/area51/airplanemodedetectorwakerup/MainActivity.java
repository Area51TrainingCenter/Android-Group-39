package pe.area51.airplanemodedetectorwakerup;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_wakeup_airplane_mode_detector_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcast();
            }
        });
    }

    private void sendBroadcast() {
        final Intent intent = new Intent();
        /*
        Desde Android Oreo (API 26), los broadcast receiver no podrán recibir intents implícitos
        (con ciertas excepciones).
        Más información: "https://developer.android.com/guide/components/broadcast-exceptions.html".
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String packageName = "pe.area51.airplanemodedetector";
            final String className = "pe.area51.airplanemodedetector.MyBroadcastReceiver";
            intent.setComponent(new ComponentName(packageName, className));
        } else {
            /*
            Enviamos mediante broadcast el Intent con action "pe.area51.wakerup.WAKE_UP".
            Adicionalmente le asignamos el flag "FLAG_INCLUDE_STOPPED_PACKAGES" al Intent,
            esto para hacer que la aplicación que tenga el componente BroadcastReceiver
            que responda a este action que enviamos pueda recibirlo incluso si se encuentra
            en estado detenido (Stopped Package). Recordar que una aplicación está en estado
            detenido si nunca se ha ejecutado alguno de sus componentes o si se ha forzado su cierre.
            Recordar además que a partir de Android 3.1 todos los broadcast enviados por las aplicaciones
            del sistema EXCLUYEN los paquetes en estado detenido. Es por esa razón que si nuestra
            aplicación se encuentra en estado detenido entonces no recibirá los broadcast enviados
            por las aplicaciones del sistema. Enviando este broadcast haremos que la aplicación que
            lo reciba de encontrarse en estado detenido, ya no se encuentre en estado detenido. Esto
            es así porque al recibir este broadcast necesariamente se activará el componente BroadcastReceiver,
            por lo que se habrá ejecutado un componente de esa aplicación, pasando a estado no detenido.
            */
            intent.setAction("pe.area51.wakerup.WAKE_UP");
            intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        }
        sendBroadcast(intent);
    }
}
