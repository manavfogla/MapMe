package manavs.ee461l.utexas.edu.mapme;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyActivity extends FragmentActivity {

    private GoogleMap googleMap;
    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/geocode/";
    private static final String OUT_JSON = "/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        try {
            initializeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        initializeMap();
    }

    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void onClickSearch (View view) {
        EditText textView = (EditText) findViewById(R.id.input_address);

        GetCoordinatesTask getLatLng = new GetCoordinatesTask();
        getLatLng.execute(textView.getText().toString());
        double[] coordinates = null;
        try {
            coordinates = getLatLng.get();
        }
        catch(Exception e){}
        String formattedAddr = getLatLng.getFormattedAddress();
        System.out.println(formattedAddr);
        updateMap(coordinates, formattedAddr);
    }

    public void updateMap(double[] coordinates, String formattedAddress) {
        if (coordinates!=null) {
            double lat = coordinates[0];
            double lng = coordinates[1];
            MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng)).title(formattedAddress);
            googleMap.addMarker(marker);

            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(lat, lng)).zoom(12).build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void onClickSatellite(View view){
        initializeMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void onClickNormal(View view){
        initializeMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void onClickTerrain(View view){
        initializeMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }
}
