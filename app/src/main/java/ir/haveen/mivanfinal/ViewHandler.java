package ir.haveen.mivanfinal;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import ir.haveen.mivanfinal.model.db.DetailsItem;

public class ViewHandler {

    public void mainViewItemClick(View view, int position) {
        Intent in = new Intent(view.getContext(), MapsActivity.class);
        in.putExtra("id", position);
//        in.putExtra("resImage", image);
        view.getContext().startActivity(in);
    }

    public void setLocaleApp(Preferences preferences, String language) {
        preferences.setLocaleLang(language);
    }

    public void  onItemClick(View view){
        Toast.makeText(view.getContext(), "amir", Toast.LENGTH_SHORT).show();
    }
}
