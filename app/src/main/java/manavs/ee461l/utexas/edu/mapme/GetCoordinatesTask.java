package manavs.ee461l.utexas.edu.mapme;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import android.os.AsyncTask;

/**
 * Created by Manav on 10/15/14.
 */
public class GetCoordinatesTask extends AsyncTask<String, Void, double[]>{

    String formattedAddress;
    private static final String LOG_TAG = "ExampleApp";
    private double[] coordinates;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/geocode";
    private static final String OUT_JSON = "/json";
    // Method to geocode address

    @Override
    protected double[] doInBackground(String... params) {

        String address = params[0];
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + OUT_JSON);
            sb.append("?address=" + URLEncoder.encode(address, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Maps API URL", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Maps API", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        double[] coordinates = new double[2];
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            double lng = ((JSONArray)jsonObj.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            double lat = ((JSONArray)jsonObj.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            formattedAddress = ((JSONArray)jsonObj.get("results")).getJSONObject(0)
                    .getString("formatted_address");

            Log.d("latitude", "" + lat);
            Log.d("longitude", "" + lng);
            coordinates[0] = lat;
            coordinates[1] = lng;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return coordinates;
    }

    public String getFormattedAddress(){
        return formattedAddress;
    }
}
