package fragments;
import android.app.PendingIntent;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cosimoalessandro.watchout.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.List;
import managers.FragmentsManager;
public class HomeFragment extends MainFragment implements LocationListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GoogleMap googleMap;
    private  TextView message;
    private LocationManager locationManager;
    private String bestProvider;
    private double latitude;
    private double longitude;
    private LocationListener locationListener;
    private ParseUser user;
    private static View rootView;
    //Variabili di Prova per mostrare il nome utente
    /*private TextView titleTextView;
    private TextView emailTextView;
    private TextView nameTextView;*/
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProximityFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onStart() {
        super.onStart();
        if(FragmentsManager.getInstance().getActualFragment() != this)
            return;
        this.setPageTitle(getString(R.string.home_fragment));
        // Set up the profile page based on the current user.
       /* ParseUser user = ParseUser.getCurrentUser();
        showProfile(user);*/
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // View view = inflater.inflate(R.layout.fragment_home, null, true);
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is  */
        }
        user = ParseUser.getCurrentUser();
        message = (TextView) rootView.findViewById(R.id.message);
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            Toast.makeText(getFragmentActivity(), "Attivare il GPS", Toast.LENGTH_SHORT).show();
        }
        FragmentManager myFM = getChildFragmentManager();
        SupportMapFragment myMAPF = (SupportMapFragment) myFM.findFragmentById(R.id.googleMap);
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getFragmentManager().findFragmentById(R.id.googleMap);
        googleMap = myMAPF.getMap();
        googleMap.setMyLocationEnabled(true);
//        Log.d("MY LOCATION", googleMap.getMyLocation().getLatitude() + "");
        locationManager = (LocationManager) getFragmentActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW); // Chose your desired power consumption level.
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // Choose your accuracy requirement.
        criteria.setSpeedRequired(true); // Chose if speed for first location fix is required.
        bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }else {message.setText("Ricerca GPS in corso...");}
        locationManager.requestLocationUpdates(bestProvider, 90000, 0, this);
        //Prova per mostrare il nome dell'utente loggato
       /* titleTextView = (TextView) view.findViewById(R.id.profile_title);
        emailTextView = (TextView) view.findViewById(R.id.profile_email);
        nameTextView = (TextView) view.findViewById(R.id.profile_name);
        titleTextView.setText(R.string.profile_title_logged_in);*/
        this.initTabBar(rootView);
        return rootView;
    }
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getFragmentActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getFragmentActivity(), 0).show();
            return false;
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        googleMap.clear();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        // locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);
        updateLocation();
        getUsersLocation();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
    }
    public void getUsersLocation(){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        final ParseGeoPoint userLocation = new ParseGeoPoint(latitude, longitude);
        //query.whereNear("location", userLocation);
        query.whereWithinKilometers("geopoint", userLocation, 1);
        query.whereNotEqualTo("objectId", user.getObjectId());
        query.setLimit(10);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null) {
                    if(parseUsers.size()==0){
                        message.setText("Non ci sono Watcher nei paraggi");
                    }
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    for(int i =0; i< parseUsers.size();i++) {
                        double lat = parseUsers.get(i).getParseGeoPoint("geopoint").getLatitude();
                        double lng = parseUsers.get(i).getParseGeoPoint("geopoint").getLongitude();
                        LatLng LatLng = new LatLng(lat, lng);
                        googleMap.addMarker(new MarkerOptions()
                                .position(LatLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)));
                        message.setText("Attenzione! Ci sono Watcher nei paraggi");
                        Log.d("lat", Double.toString(lat));
                    }
                } else {
                    Log.d("Errore query", "Errore");
                }
            }
        });
    }
    public void updateLocation() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        final ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
// Retrieve the object by id
        query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
            public void done(ParseUser userUp, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    userUp.put("geopoint", point);
                    userUp.saveInBackground();
                } else {
                    Log.d("errore", "errore" +
                            "");
                }
            }
        });
    }
    /**
     * Shows the profile of the given user.
     *
     * @param user
     */
//    private void showProfile(ParseUser user) {
//        if (user != null) {
//            emailTextView.setText(user.getEmail());
//            String fullName = user.getString("name");
//            if (fullName != null) {
//                nameTextView.setText(fullName);
//            }
//        }
//    }
}