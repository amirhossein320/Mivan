package ir.haveen.mivanfinal;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.haveen.mivanfinal.adapter.NaturePlaceItem;
import ir.haveen.mivanfinal.adapter.ItemClickListener;
import ir.haveen.mivanfinal.adapter.PlaceItem;
import ir.haveen.mivanfinal.databinding.ActivityMapsBinding;
import ir.haveen.mivanfinal.model.db.DetailsItem;
import ir.haveen.mivanfinal.model.view.PlaceViewModel;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, ItemClickListener {

    private final float MAX_ZOOM = 20.0f;
    private final float MIN_ZOOM = 11.0f;
    private final float DEFAULT_ZOOM = 14.6f;
    private final int REQUEST_CODE = 427;
    private final SlidingUpPanelLayout.PanelState COLLAPSED = SlidingUpPanelLayout.PanelState.COLLAPSED;
    private final SlidingUpPanelLayout.PanelState EXPANDED = SlidingUpPanelLayout.PanelState.EXPANDED;

    private GoogleMap mMap;
    private Preferences preferences;
    private ActivityMapsBinding binding;
    private LatLng piranshahr;
    private Location location;
    private LatLng myLocation;
    private NaturePlaceItem foldingAdapter;
    private PlaceItem placeAdapter;
    private SlidingUpPanelLayout slidingLayout;
    private List<DetailsItem> array;

    private int id;
    private String resImage;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(this);
        preferences.setLocalToApp(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps);
        slidingLayout = binding.slidingLayout; // initialize sliding_ui_panel

        if (checkPermission()) { // check permission
            setUiAndData();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setMaxZoomPreference(MAX_ZOOM);
        mMap.setMinZoomPreference(MIN_ZOOM);

        //map move to piranshahr
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(piranshahr, DEFAULT_ZOOM));

        loadMap(); //load all places of group

        GpsTracker gpsTracker = new GpsTracker(this);
        location = gpsTracker.getLocation();
        if (gpsTracker.canGetLocation()) {
            if (location != null) {
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(this, R.string.TagLocation, Toast.LENGTH_SHORT).show();
            }
        }

        gpsTracker.onLocationChanged(location);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        if (!shouldShowRequestPermissionRationale(permissions[i])) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            finish();
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    }else{
                        setUiAndData();
                    }
                }
            }
        }
    }

    //get for permission
    private boolean checkPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        for (String permission :
                permissions) {
            if (!checkPermissionGranted(permission))
                ActivityCompat.requestPermissions(MapsActivity.this, permissions, REQUEST_CODE);
            else
                return true;

        }
        return false;
    }

    //check permission granted
    private boolean checkPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    // set ui settings and set data to ui
    private void setUiAndData() {
        // get data sent by intent
        id = getIntent().getIntExtra("id", 0);
        resImage = getIntent().getStringExtra("resImage");
        title = getIntent().getStringExtra("tag");

        binding.titleGroup.setText(title);
        binding.iconTitle.setImageResource(getResources().getIdentifier(resImage, "mipmap", getPackageName()));

        // get load data from db
        PlaceViewModel placeViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
        placeViewModel.getPlaces(id).observe(this, detailsItems -> {

            array = detailsItems; // set data to field object

            //load map
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            piranshahr = new LatLng(36.6996255, 45.1403253);

            //set layout manager to recycler
            binding.recyclerItems.setLayoutManager(new LinearLayoutManager(this));
            //set adapter by type of group
            if (id == 1) {
                foldingAdapter = new NaturePlaceItem(detailsItems, this);
                binding.recyclerItems.setAdapter(foldingAdapter);
            } else {
                placeAdapter = new PlaceItem(detailsItems, this, resImage);
                binding.recyclerItems.setAdapter(placeAdapter);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (slidingLayout.getPanelState() == EXPANDED) {
            slidingLayout.setPanelState(COLLAPSED);
        } else if (slidingLayout.getPanelState() == COLLAPSED) {
            super.onBackPressed();
        }
    }

    // recycler item click
    @Override
    public void onItemClick(View item, int position) {
        if (location != null) {
            mMap.clear();
            loadMap();
//            myLocation = new LatLng(array.get(position + 1).getWidth(), array.get(position + 1).getHeight());
            LatLng dest = new LatLng(array.get(position).getWidth(), array.get(position).getHeight());

            FetchUrl fetchUrl = new FetchUrl();
            fetchUrl.execute(getUrl(myLocation, dest));
            slidingLayout.setPanelState(COLLAPSED);
        } else {
            Toast.makeText(this, R.string.TagLocation, Toast.LENGTH_SHORT).show();
        }
    }

    // natureDetails click listener
    @Override
    public void natureDetailsClickListener(View view, int position) {
        Toast.makeText(this, array.get(position).getName(), Toast.LENGTH_SHORT).show();
    }


    //draw all places on map
    private void loadMap() throws SecurityException {
        for (DetailsItem data : array) {
            LatLng place = new LatLng(data.getWidth(), data.getHeight());
            int height = 60;
            int width = 60;
            Bitmap bitmapDraw = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier(resImage, "mipmap", getPackageName()));
            Bitmap smallMarker = Bitmap.createScaledBitmap(bitmapDraw, width, height, false);
            mMap.addMarker(new MarkerOptions().position(place).title(data.getName()).
                    icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        }

        mMap.setMyLocationEnabled(true);
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    // Parsing the data in non-ui thread
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    // download data
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    //make url for download path between places
    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + "key=" + "AIzaSyAOqTp27q2gQa1hmY_iUQX4pqIX0dZa5Ko";

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }


}
