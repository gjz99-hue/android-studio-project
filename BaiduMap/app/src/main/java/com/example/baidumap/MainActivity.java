package com.example.baidumap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.LocaleList;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends AppCompatActivity {
    private MapView mMapView = null;

    LocationClient mLocationClient;  //定位客户端
    MapView mapView;  //Android Widget地图控件
    BaiduMap baiduMap;
    boolean isFirstLocate = true;

    TextView tv_Lat;  //纬度
    TextView tv_Lon;  //经度
    TextView tv_Add;  //地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {
            requestLocation();
        }

        mMapView = (MapView) findViewById(R.id.bmapView);
    }
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {  //初始化
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        tv_Lat = findViewById(R.id.tv_Lat);
        tv_Lon = findViewById(R.id.tv_Lon);
        tv_Add = findViewById(R.id.tv_Add);

        LocationClientOption option = new LocationClientOption();
        //设置扫描时间间隔
        option.setScanSpan(1000);
        //设置定位模式，三选一
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        /*option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);*/
        //设置需要地址信息
        option.setIsNeedAddress(true);
        //保存定位参数
        mLocationClient.setLocOption(option);
    }


    //内部类，百度位置监听器
    private class MyLocationListener  implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            tv_Lat.setText(bdLocation.getLatitude()+"");
            tv_Lon.setText(bdLocation.getLongitude()+"");
            tv_Add.setText(bdLocation.getAddrStr());
            if(bdLocation.getLocType()==BDLocation.TypeGpsLocation || bdLocation.getLocType()==BDLocation.TypeNetWorkLocation){
                navigateTo(bdLocation);
            }
        }
    }
    private void navigateTo(BDLocation bdLocation) {
        if(isFirstLocate){
            LatLng ll = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
}
