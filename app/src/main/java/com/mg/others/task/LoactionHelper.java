package com.mg.others.task;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;

/**
 * Created by wuqiyan on 17/6/23.
 */

public class LoactionHelper {
    public static  class LocModel{
        public double lat;
        public double lon;
    }
    public static LocModel GetUserLocation(Context context){
       LocModel model = new LocModel();
       model.lat=0.0;
        model.lon=0.0;
       try {

        //获取地理位置管理器
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        String locationProvider="";
        if(providers.contains(LocationManager.GPS_PROVIDER)){
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        }else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }
        //获取Location
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if(location != null){
            model.lat = location.getLatitude();
            model.lon = location.getLongitude();

        }

       }catch (Exception e){
           e.printStackTrace();
       }
       return model;
    }
}
