package com.srmn.xwork.androidlib.gis;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiler on 2016/3/14.
 */
public class AMapHelper {

    public static Marker drawMarkerOnView(AMap aMap, BitmapDescriptor icon, LatLng centerPosition) {
        return aMap.addMarker(new MarkerOptions()
                .position(centerPosition)
                .icon(icon));
    }

    public static Marker drawMarkerOnView(AMap aMap, int iconResourseID, LatLng centerPosition) {
        return drawMarkerOnView(aMap, (BitmapDescriptorFactory.fromResource(iconResourseID)), centerPosition);
    }

    public static Polyline drawLineOnView(AMap aMap, List<LatLng> lines, int startIconResourseID, int endIconResourseID, int color) {
        aMap.addMarker(new MarkerOptions()
                .position(lines.get(0))
                .icon(BitmapDescriptorFactory.fromResource(startIconResourseID)));

        aMap.addMarker(new MarkerOptions()
                .position(lines.get(lines.size() - 1))
                .icon(BitmapDescriptorFactory.fromResource(endIconResourseID)));


        return aMap.addPolyline((new PolylineOptions()).addAll(lines).color(color));
    }

    public static void setViewFit(AMap aMap, List<LatLng> points, int zoom) {
        LatLngBounds.Builder build = new LatLngBounds.Builder();

        for (LatLng item : points) {
            build.include(item);
        }
        // 移动地图，所有marker自适应显示。LatLngBounds与地图边缘zoom像素的填充区域
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(build.build(), zoom));
    }

}