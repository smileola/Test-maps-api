package com.prototype.skate.skate.activities;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.prototype.skate.skate.R;
import com.prototype.skate.skate.dialogs.CreateSpotDialogFragment;
import com.prototype.skate.skate.factories.SkateSpotFactory;
import com.prototype.skate.skate.location.SkateSpot;
import com.prototype.skate.skate.location.Spot;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        com.google.android.gms.location.LocationListener,GoogleMap.OnMapLongClickListener,
        CreateSpotDialogFragment.CreateSpotDialogListener,GoogleMap.OnInfoWindowClickListener {

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final String TAG = "MyActivity";
    private static final int DEFAULT_ZOOM = 15;
    /*private static final float LOCATION_REFRESH_DISTANCE = 10;
    private static final long LOCATION_REFRESH_TIME = 6000;*/
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private Location mCurrentLocation;
    private CameraPosition mCameraPosition;
    private GoogleApiClient mGoogleApiClient;
    private LatLng selectedLatLng;
    // Request Object
    private LocationRequest mLocationRequest;
    // Factory
    private SkateSpotFactory mSkateSpotFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //building google api client
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        //instantiating the factory
        mSkateSpotFactory = mSkateSpotFactory.getInstance();
    }

    private  synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        createLocationRequest();
    }

    private void getDeviceLocation(){
        //GET permissions if we don't have it
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1337);
        }

        if(mLocationPermissionGranted){
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            updateLocationUI();
        }
    }
    private void updateLocationUI(){
        if (mMap == null){
            return;
        }
        if(mLocationPermissionGranted){
            try{
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }catch (SecurityException e ){
                Log.d("e",e.toString());
            }
        } else{
            try{
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mCurrentLocation = null;
            }catch (SecurityException e ){
                Log.d("e",e.toString());
            }
        }
    }
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    //use this one fo the marker placement
    private void updateMarkers(){
        if(mMap==null){
            return;
        }
        if(mLocationPermissionGranted){
            try{
                PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                        .getCurrentPlace(mGoogleApiClient, null);
                result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                        /*for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                            // Add a marker for each place near the device's current location, with an
                            // info window showing place information.
                            String attributions = (String) placeLikelihood.getPlace().getAttributions();
                            String snippet = (String) placeLikelihood.getPlace().getAddress();
                            if (attributions != null) {
                                snippet = snippet + "\n" + attributions;
                            }

                            mMap.addMarker(new MarkerOptions()
                                    .position(placeLikelihood.getPlace().getLatLng())
                                    .title((String) placeLikelihood.getPlace().getName())
                                    .snippet(snippet));
                        }
                        // Release the place likelihood buffer.
                        likelyPlaces.release();*/
                    }
                });
            }catch(SecurityException e){
            }
        } else {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(-34, 151))
                    .title(getString(R.string.default_info_title))
                    .snippet(getString(R.string.default_info_snippet)));
        }

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        if(mCameraPosition !=null){
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        }else if (mCurrentLocation != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()) , DEFAULT_ZOOM));
        }else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-34, 151), DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View InfoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
                TextView title = (TextView)InfoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());
                TextView snippet = (TextView)InfoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());
                return InfoWindow;
            }
        });
        mMap.setOnMapLongClickListener(this);
    }

    //Permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case 1337: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getDeviceLocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    //Location listener implement
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateMarkers();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mCurrentLocation);
            super.onSaveInstanceState(outState);
        }
    }
    //map action listeners
    @Override
    public void onMapLongClick(LatLng latLng) {
        CreateSpotDialogFragment mDialogFragment = new CreateSpotDialogFragment();
        mDialogFragment.show(getSupportFragmentManager(),"mDialogFragment");
        selectedLatLng = latLng;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Dialog listenedDialog = dialog.getDialog();
        EditText editName = (EditText)listenedDialog.findViewById(R.id.spot_name);
        EditText editDescription = (EditText) listenedDialog.findViewById(R.id.spot_description);

        String name = editName.getText().toString();
        String description = editDescription.getText().toString();

        Spot newSkateSpot = mSkateSpotFactory.makeSpot(selectedLatLng, name,description);
        mMap.addMarker(newSkateSpot.getMarker());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}
