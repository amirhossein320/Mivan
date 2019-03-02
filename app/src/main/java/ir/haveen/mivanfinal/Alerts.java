package ir.haveen.mivanfinal;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
            restartApp();
        });
        //set local to kurdish
        view.findViewById(R.id.btnKU).setOnClickListener(v -> {
            preferences.setLocaleLang("ku");
            restartApp();
        });
        //set local to english
        view.findViewById(R.id.btnEN).setOnClickListener(v -> {
            preferences.setLocaleLang("en");
            restartApp();
        });
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }

    public void aboutAlert() {

    }

    private void restartApp() {

        Intent intent = new Intent(context, context.getClass());
        context.finish();
        context.startActivity(intent);

//        Intent intent = context.getIntent();
//        context.finish();
//        context.startActivity(intent);


//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);

//        Intent mStartActivity = new Intent(context, context.getClass());
//        int mPendingIntentId = 123456;
//        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
//        context.finish();
//        int pid = android.os.Process.myPid();
//        android.os.Process.killProcess(pid);
//        System.exit(0);
//

//        Intent i = context.getBaseContext().getPackageManager().
//                getLaunchIntentForPackage(context.getBaseContext().getPackageName());
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        context.finish();
//        int pid = android.os.Process.myPid();
//        android.os.Process.killProcess(pid);
//        context.startActivity(i);
    }
}
