package ir.haveen.mivanfinal;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Preferences preferences = new Preferences(this);

        if (!preferences.IsFirstRun()) {
            setLocale(this, preferences.getLang());
        }
    }


    public void setLocale(Context context, String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

}
