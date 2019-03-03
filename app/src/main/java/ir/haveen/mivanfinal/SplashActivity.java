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
            preferences.setFirstRunOff();
            binding.setFirstRun(true);
//            preferences.setLocaleLang("fa");
            click();
        } else {
            loadData();
        }

    }

    //init clickable option for choose language
    private void click() {
        binding.btnFA.setOnClickListener((v) -> firstRun("fa"));
        binding.btnKU.setOnClickListener((v) -> firstRun("ku"));
        binding.btnEN.setOnClickListener((v) -> firstRun("en"));
    }

    //first run works
    private void firstRun(String lang) {
        preferences.setLocaleLang(lang);
        preferences.restartApp(this);
    }

    //load data from server
    private void loadData() {
        binding.setFirstRun(false);//off first run
        Database.getINSTANCE(this.getApplication()).dao(); // copy local database
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
