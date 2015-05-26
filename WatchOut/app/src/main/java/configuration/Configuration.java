package configuration;

import android.content.Context;
import android.graphics.Typeface;

import fragments.ClanFragment;

import com.example.cosimoalessandro.watchout.R;

/**
 * Created by CosimoAlessandro on 04/04/2015.
 */
public class Configuration {

    public static String WS_BASE_URL = "http://prova.it/test/%s";

    public static String DEVICE_NAME = "ANDROID";

    public static final int FRAGMENT_SIGNUP = 0;
    public static final int FRAGMENT_LOST_PASSWORD = 1;
    public static final int FRAGMENT_FAQ = 2;
    public static final int FRAGMENT_HOME = 3;


    public static final Class[] fragments = new Class[]{
            /*SignupFragment.class,
            LostPasswordFragment.class,
            FAQFragment.class,*/
            ClanFragment.class
    };

    public static final int[] menu = new int[]{
            R.string.menu_modifica,
            R.string.menu_faq,
            R.string.termini_condizioni,
            R.string.menu_segnala,
            R.string.logout,

    };

    public static Typeface getDefaultRegularTypeface(Context context){
        return Typeface.createFromAsset(context.getAssets(), "helvetica_neue_regular.otf");
    }

    public static Typeface getDefaultMediumTypeface(Context context){
        return Typeface.createFromAsset(context.getAssets(), "helvetica_neue_medium.otf");
    }

}