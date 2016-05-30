package com.ghostofchaos.especialdish.Fragments;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ghostofchaos.especialdish.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Ghost on 30.03.2016.
 */
public class FragmentMap extends Fragment {

    static final int LOCATION_REFRESH_TIME = 10;
    static final int LOCATION_REFRESH_DISTANCE = 10;
    private MapView mapView;
    public static GoogleMap map;
    private LocationManager mLocationManager;
    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);
        context = getContext();
        mapView = (MapView) root.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        // Updates the location and zoom of the MapView
        map.setMyLocationEnabled(true);
        Location myLocation = map.getMyLocation();

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);

        if (map != null) {
            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.marker_info, null);
                    TextView view = (TextView) linearLayout.findViewById(R.id.info);
                    view.setText(marker.getTitle());
                    return linearLayout;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });

            /*DownloadModels.getInstance();
            DownloadModels.setPerPage(100);
            DownloadModels.setPage(0);
            DownloadModels.setHost("http://osoboebludo.com/api/?notabs&json&content_id=16&page=");
            DownloadModels.loadObjects(getActivity(), map);*/

    /*      Marker kiel = map.addMarker(new MarkerOptions()
                    .position(KIEL)
                    .title("Kiel")
                    .snippet("Kiel is cool");*/

            Location lastKnownLocationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocationGPS != null) {
                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lastKnownLocationGPS.getLatitude(), lastKnownLocationGPS.getLongitude())));
            } else {
                Location loc = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(loc.getLatitude(), loc.getLongitude())));
            }

            if (myLocation != null) {
                double dLatitude = myLocation.getLatitude();
                double dLongitude = myLocation.getLongitude();

                // Move the camera instantly to hamburg with a zoom of 15.
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 15));
            }
            // Zoom in, animating the camera.
            map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }
        return root;
    }

    private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

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
    };

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
