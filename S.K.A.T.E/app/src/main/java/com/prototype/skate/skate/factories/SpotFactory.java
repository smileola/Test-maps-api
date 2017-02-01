package com.prototype.skate.skate.factories;

import com.google.android.gms.maps.model.LatLng;
import com.prototype.skate.skate.location.Spot;

/**
 * Created by Agent on 1/19/2017.
 */

public interface SpotFactory {
    public Spot makeSpot(LatLng l ,String n ,String g);
}
