package ir.haveen.mivanfinal;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ir.haveen.mivanfinal.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private Preferences preferences;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(this);
        preferences.setLocalToApp(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.setOnClick(new ViewHandler()); // for item click in view

    }

    @Override
    protected void onResume() {
        super.onResume();
        preferences.setLocalToApp(this);
    }


}
