package pe.area51.myfirstfragmentapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by alumno on 12/19/16.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText editTextName;
    private OnShowWelcomeClickListener onShowWelcomeClickListener;

    public void setOnShowWelcomeClickListener(OnShowWelcomeClickListener onShowWelcomeClickListener) {
        this.onShowWelcomeClickListener = onShowWelcomeClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        final Button buttonShowWelcome = (Button) view.findViewById(R.id.button_show_welcome);
        buttonShowWelcome.setOnClickListener(this);
        editTextName = (EditText) view.findViewById(R.id.edittext_user_name);
        return view;
    }

    @Override
    public void onClick(View view) {
        final String name = editTextName.getText().toString();
        if (onShowWelcomeClickListener != null) {
            onShowWelcomeClickListener.onShowWelcomeClick(name);
        }
    }

    public interface OnShowWelcomeClickListener {

        void onShowWelcomeClick(String name);

    }
}
