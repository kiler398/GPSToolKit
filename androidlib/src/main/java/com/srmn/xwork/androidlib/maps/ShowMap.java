package com.srmn.xwork.androidlib.maps;

import android.content.Intent;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.*;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.srmn.xwork.androidlib.R;
import com.srmn.xwork.androidlib.gis.AMapHelper;
import com.srmn.xwork.androidlib.gis.ShowMarker;
import com.srmn.xwork.androidlib.ui.BaseActivity;
import com.srmn.xwork.androidlib.ui.MyApplication;
import com.srmn.xwork.androidlib.utils.GsonUtil;

import java.util.List;

public abstract class ShowMap extends BaseActivity implements AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.OnMapLoadedListener, AMap.InfoWindowAdapter, AMap.OnMapClickListener {

    private com.amap.api.maps.AMap aMap;
    private MapView mapView;
    private Handler mainhandler = new Handler();
    private Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(this);
        } catch (RemoteException e) {
            MyApplication.getInstance().showLongToastMessage(e.getMessage());
            e.printStackTrace();
        }
        setContentView(R.layout.activity_show_map);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        hideActionBar();

        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
            aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
            aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
            aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
            aMap.setOnMapClickListener(this);
        }

    }

    private void show() {
        Intent intent = getIntent();
        if (intent.hasExtra("showMarkers")) {
            String json = intent.getStringExtra("showMarkers");
            Gson gson = GsonUtil.getGson();
            List<ShowMarker> markers = gson.fromJson(json, new TypeToken<List<ShowMarker>>() {
            }.getType());

            AMapHelper.showMarkersOnView(aMap, markers);
        }
    }

    private void setUpMap() {
        aMap.getUiSettings().setZoomControlsEnabled(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(false);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        currentMarker = marker;
        marker.showInfoWindow();
    }

    @Override
    public void onMapLoaded() {
        setUpMap();
        show();
    }

    // 点击非marker区域，将显示的InfoWindow隐藏
    @Override
    public void onMapClick(LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.hideInfoWindow();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        currentMarker = marker;
        return false;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if (getInfoWindowView() == 0)
            return null;
        View infoWindow = getLayoutInflater().inflate(getInfoWindowView(), null);
        render(marker, infoWindow);
        return infoWindow;
    }

    public abstract int getInfoWindowView();

    /**
     * 自定义infowinfow窗口，动态修改内容的
     */
    public abstract void render(Marker marker, View view);


    @Override
    public View getInfoContents(Marker marker) {
        if (getInfoWindowView() == 0)
            return null;
        View infoContent = getLayoutInflater().inflate(getInfoWindowView(), null);
        render(marker, infoContent);
        return infoContent;
    }
}
