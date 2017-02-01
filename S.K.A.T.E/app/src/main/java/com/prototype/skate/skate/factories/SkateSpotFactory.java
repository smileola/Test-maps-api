package com.prototype.skate.skate.factories;

import com.google.android.gms.maps.model.LatLng;
import com.prototype.skate.skate.location.SkateSpot;
import com.prototype.skate.skate.location.Spot;

/**
 * Created by Agent on 1/19/2017.
 */

public class SkateSpotFactory implements SpotFactory {
    //Implementation of the Singleton design pattern
    private SkateSpotFactory(){

    }

    private static class SkateSpotFactoryHolder{
        private static final SkateSpotFactory INSTANCE = new SkateSpotFactory();
    }

    public static SkateSpotFactory getInstance(){
        return SkateSpotFactoryHolder.INSTANCE;
    }

    @Override
    public Spot makeSpot(LatLng l, String n ,String g) {
        return new SkateSpot(l,n,g);
    }
}
