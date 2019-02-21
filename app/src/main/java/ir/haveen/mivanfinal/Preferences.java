package ir.haveen.mivanfinal;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

import ir.haveen.mivanfinal.database.Database;
import ir.haveen.mivanfinal.net.Api;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class Preferences {

    private static final String LANG = "Lang";
    private Context context;
    private SharedPreferences preferences;
    private String FIRST_RUN = "FirstRun";
    private Database database;

    public Preferences(Context context) {
        this.context = context;
        setPreferences();// init preference
    }

    //get shared preference
    public SharedPreferences getPreferences() {
        return preferences;
    }

    //int sharedPreference
    public void setPreferences() {
        preferences = preferences = context.getSharedPreferences("settings", MODE_PRIVATE);
    }

    // get for shared preference editor
    public SharedPreferences.Editor getEditor() {
        return preferences.edit();
    }

    // check first run of app
    public boolean IsFirstRun() {
        return preferences.getBoolean(FIRST_RUN, true);
    }

    //first run off
    public void setFirstRunOff() {
        getEditor().putBoolean(FIRST_RUN, false).apply();
    }

    //set local app language
    public void setLocaleLang(String language) {
        getEditor().putString(LANG, language).apply();
    }

    //change local of app
    public void setLocalToApp(AppCompatActivity activity) {

        Locale locale = new Locale("fa");
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().
                updateConfiguration(config,
                        activity.getBaseContext().getResources().getDisplayMetrics());
    }

    //get local app language
    public String getLang() {
        return getPreferences().getString(LANG, "fa");
    }


    //init database
    private void setDatabase() {
        this.database = Room.databaseBuilder(context, Database.class, "data").allowMainThreadQueries().build();
    }

    //get database
    public Database getDatabase() {
        setDatabase();
        return database;
    }

    //return api
    public Api api(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(Api.class);
    }
}
