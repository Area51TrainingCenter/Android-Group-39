package pe.area51.myfirstfragmentapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnShowWelcomeClickListener {

    private WelcomeFragment welcomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        welcomeFragment = (WelcomeFragment) fragmentManager.findFragmentById(R.id.fragment_welcome);
        final LoginFragment loginFragment = (LoginFragment) fragmentManager.findFragmentById(R.id.fragment_login);
        loginFragment.setOnShowWelcomeClickListener(this);
    }

    @Override
    public void onShowWelcomeClick(String name) {
        welcomeFragment.setWelcomeMessage(getString(R.string.welcome_message, name));
    }
}
