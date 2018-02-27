package pe.area51.myfirstfragmentapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by alumno on 12/21/16.
 */

public class WelcomeFragment extends Fragment {

    private TextView textViewWelcomeMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        textViewWelcomeMessage = (TextView) view.findViewById(R.id.textview_welcome_message);
        return view;
    }

    public void setWelcomeMessage(final String welcomeMessage) {
        textViewWelcomeMessage.setText(welcomeMessage);
    }
}
