package pe.area51.myfirstfragmentapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnShowWelcomeClickListener {

    private final static String FRAGMENT_TAG_LOGIN_FRAGMENT = "fragment_login";
    private final static String FRAGMENT_TAG_WELCOME_FRAGMENT = "fragment_welcome";

    private WelcomeFragment welcomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final LoginFragment loginFragment;
        final WelcomeFragment welcomeFragment;
        if (savedInstanceState == null) {
            /*
            Creamos los fragments e iniciamos la transacción cuando este Activity se cree
            por primera vez.
             */
            loginFragment = new LoginFragment();
            welcomeFragment = new WelcomeFragment();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container_1, loginFragment, FRAGMENT_TAG_LOGIN_FRAGMENT)
                    .replace(R.id.fragment_container_2, welcomeFragment, FRAGMENT_TAG_WELCOME_FRAGMENT)
                    .commit();
        } else {
            /*
            Si el Activity tiene un estado anterior, no es necesario crear los fragments o ejecutar nuevamente
            la transacción, puesto que en el proceso de recreación de estados, se recrea también el estado
            del FragmentManager.
             */
            loginFragment = (LoginFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG_LOGIN_FRAGMENT);
            welcomeFragment = (WelcomeFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG_WELCOME_FRAGMENT);
        }
        this.welcomeFragment = welcomeFragment;
        loginFragment.setOnShowWelcomeClickListener(this);
    }

    @Override
    public void onShowWelcomeClick(String name) {
        welcomeFragment.setWelcomeMessage(getString(R.string.welcome_message, name));
    }
}
