package ir.haveen.mivanfinal;

import android.content.Intent;
import android.view.View;

public class ViewHandler {

    public void mainViewItemClick(View view, int position) {
        Intent in = new Intent(view.getContext(), MapsActivity.class);
        in.putExtra("position", position);
        view.getContext().startActivity(in);
    }

    public void setLocaleApp(Preferences preferences, String language) {
        preferences.setLocaleLang(language);
    }
}
