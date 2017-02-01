package com.prototype.skate.skate.location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Agent on 1/19/2017.
 */

public abstract class Spot {
    protected LatLng latlng;
    protected MarkerOptions marker;
    protected String name,description;

    public LatLng getLatlng() {
        return latlng;
    }

    public MarkerOptions getMarker() {
        return marker;
    }
}
