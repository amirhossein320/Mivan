package ir.haveen.mivanfinal;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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

import ir.haveen.mivanfinal.adapter.FlodingPlaceItem;
import ir.haveen.mivanfinal.adapter.PlaceItem;
import ir.haveen.mivanfinal.databinding.ActivityMapsBinding;
import ir.haveen.mivanfinal.model.db.DetailsItem;
import ir.haveen.mivanfinal.model.view.PlaceViewModel;
import ir.haveen.mivanfinal.net.Api;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final float DEFAULT_ZOOM = 14.6f;
    private GoogleMap mMap;
    private Preferences preferences;
    private ActivityMapsBinding binding;
    private final int REQUEST_CODE = 427;
    private int id;
    private LatLng piranshahr;
    private Location location;
    private LatLng myLocation;
    private List<DetailsItem> array;
    private int resImage;
    private FlodingPlaceItem foldingAdapter;
    private PlaceItem placeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(this);
        preferences.setLocalToApp(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps);

        checkPermision();
        id = getIntent().getIntExtra("id", 0);
        resImage = getIntent().getIntExtra("resImage", R.mipmap.place);


        PlaceViewModel placeViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
        placeViewModel.getPlaces(id).observe(this, detailsItems -> {

            array = detailsItems;
            //load map
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            piranshahr = new LatLng(36.6996255, 45.1403253);

            //set layout manager to recycler
            binding.recyclerItems.setLayoutManager(new LinearLayoutManager(this));
            //set adapter by type of group
            if (detailsItems.get(0).getGroupId() == 1) {
                foldingAdapter = new FlodingPlaceItem(detailsItems);
                binding.recyclerItems.setAdapter(foldingAdapter);
                foldingAdapter.Onclick(mMap, myLocation);
            } else {
                placeAdapter = new PlaceItem(detailsItems);
                binding.recyclerItems.setAdapter(placeAdapter);
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        float MAX_ZOOM = 20.0f;
        mMap.setMaxZoomPreference(MAX_ZOOM);
        float MIN_ZOOM = 11.0f;
        mMap.setMinZoomPreference(MIN_ZOOM);

        //map move to piranshahr
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(piranshahr, DEFAULT_ZOOM));

        loadMap(); //load all places of group


        LatLng origin = new LatLng(36.706382, 45.1387739);
        LatLng dest = new LatLng(36.7067615, 45.147292);

        FetchUrl fetchUrl = new FetchUrl();
        fetchUrl.execute(getUrl(origin, dest));


        GpsTracker gpsTracker = new GpsTracker(this);
        location = gpsTracker.getLocation();
        if (gpsTracker.canGetLocation()) {
            if (location != null) {
                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(this, "no location", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void loadMap() throws SecurityException {
        //show all places on map
        for (DetailsItem data : array) {
            LatLng place = new LatLng(data.getWidth(), data.getHeight());
            mMap.addMarker(new MarkerOptions().position(place).title(data.getName()).
                    icon(BitmapDescriptorFactory.fromResource(resImage)));
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

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
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //get for permission
    private void checkPermision() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        for (String permission :
                permissions) {
            if (!checkPermissionGranted(permission))
                ActivityCompat.requestPermissions(MapsActivity.this, permissions, REQUEST_CODE);
        }
    }

    //check permission granted
    private boolean checkPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }


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


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
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
}
