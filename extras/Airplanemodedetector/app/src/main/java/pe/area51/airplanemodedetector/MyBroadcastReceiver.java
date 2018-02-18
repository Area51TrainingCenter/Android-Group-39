package pe.area51.airplanemodedetector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        El Java annotation "StringRes" indica que la variable tipo "int"
        identificada por "message" va a tener por valor el índice de un
        recurso tipo "String".
        */
        @StringRes final int message;
        final String intentAction = intent.getAction();
        if (intentAction != null) {
            switch (intentAction) {
                case Intent.ACTION_AIRPLANE_MODE_CHANGED:
                    /*
                    El intent "android.intent.action.AIRPLANE_MODE" contiene un valor booleano identificado
                    por la llave "state". Esto está indicado en la documentación.
                     */
                    final boolean isAirplaneOn = intent.getBooleanExtra("state", false);
                    if (isAirplaneOn) {
                        message = R.string.airplane_mode_on;
                    } else {
                        message = R.string.airplane_mode_off;
                    }
                    break;
                case "pe.area51.wakerup.WAKE_UP":
                    message = R.string.wake_up;
                    break;
                default:
                    message = R.string.unknown_action;
                    break;
            }
        } else {
            message = R.string.null_action;
        }
        showMessage(context, message);
    }

    private static void showMessage(final Context context, @StringRes final int stringId) {
        showMessage(context, context.getString(stringId));
    }

    private static void showMessage(final Context context, final String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
