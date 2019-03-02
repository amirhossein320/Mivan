package ir.haveen.mivanfinal;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                new Alerts(this).langaugeAlert();
                break;
            case R.id.about:
                new Alerts(this).aboutAlert();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
