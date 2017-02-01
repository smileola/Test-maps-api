package com.prototype.skate.skate.location;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Agent on 1/19/2017.
 */

public class SkateSpot extends  Spot{


    public SkateSpot(LatLng l, String name, String description){
        this.name = name;
        this.description = description;
        this.latlng = l;
        this.marker = new MarkerOptions()
                .position(l)
                .title(name)
                .snippet(description)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
    }


}
