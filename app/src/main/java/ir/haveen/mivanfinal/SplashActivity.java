package ir.haveen.mivanfinal;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ir.haveen.mivanfinal.database.Database;
import ir.haveen.mivanfinal.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private Preferences preferences;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(this);
        preferences.setLocalToApp(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        if (preferences.IsFirstRun()) {//check first run
            binding.setFirstRun(true);
            click();
        } else {
            loadData();
        }

    }

    //init clickable option for choose language
    private void click() {

        binding.btnFA.setOnClickListener((v) -> {
            preferences.setLocaleLang("fa");
            preferences.setFirstRunOff();
            loadData();
        });
        binding.btnKU.setOnClickListener((v) -> {
            preferences.setLocaleLang("ku");
            preferences.setFirstRunOff();
            loadData();
        });
        binding.btnEN.setOnClickListener((v) -> {
            preferences.setLocaleLang("en");
            preferences.setFirstRunOff();
            loadData();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        preferences.setLocalToApp(this);
    }

    //load data from server
    private void loadData() {

        binding.setFirstRun(false);//off first run
        Database.getINSTANCE(this).dao(); // copy local database
        nextActivity();
    }

    // start main activity and send data to him
    private void nextActivity() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Intent in = new Intent(this, MainActivity.class);
                startActivity(in);
                finish();
            }

        }).start();
    }


}
