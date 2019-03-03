package ir.haveen.mivanfinal;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

public class Alerts {

    private Activity context;
    private Preferences preferences;

    public Alerts(Activity context) {
        this.context = context;
        preferences = new Preferences(context);
    }

    // create alert for choose language
    public void langaugeAlert() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.language_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //set local to persian
        view.findViewById(R.id.btnFA).setOnClickListener(v -> {
            preferences.setLocaleLang("fa");
            preferences.restartApp(context);
        });
        //set local to kurdish
        view.findViewById(R.id.btnKU).setOnClickListener(v -> {
            preferences.setLocaleLang("ku");
            preferences.restartApp(context);
        });
        //set local to english
        view.findViewById(R.id.btnEN).setOnClickListener(v -> {
            preferences.setLocaleLang("en");
            preferences.restartApp(context);
        });
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }

    public void aboutAlert() {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.about_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //get g_mail app for send email
        view.findViewById(R.id.gmail).setOnClickListener(v -> {

        });
        //get telegram app for contact by telegram
        view.findViewById(R.id.telegram).setOnClickListener(v -> {

        });

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }


}
