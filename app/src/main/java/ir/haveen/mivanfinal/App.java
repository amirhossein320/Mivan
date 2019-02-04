package ir.haveen.mivanfinal;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Preferences preferences = new Preferences(this);

        if (!preferences.IsFirstRun()) {
            preferences.setLocalToApp();
        }

    }

    //change locale app
//    public void setLocale(String language) {
//
//        Locale locale = new Locale(language);
//        Locale.setDefault(locale);
//
//        Resources res = getResources();
//        Configuration config = new Configuration(res.getConfiguration());
//        if (Build.VERSION.SDK_INT >= 17) { // for api 17 or hig
//            config.setLocale(locale);
//            createConfigurationContext(config);
//        } else { //for api 16
//            config.locale = locale;
//            res.updateConfiguration(config, res.getDisplayMetrics());
//        }
//
//    }

    public void setLocale(Context context, String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

}
