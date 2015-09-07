package com.dinodemo.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.overlay.BusRouteOverlay;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.dinodemo.R;
import java.util.List;

public class MapActivity extends AppCompatActivity
    implements LocationSource, AMapLocationListener, View.OnClickListener, AMap.OnMapClickListener,
    AMap.OnMarkerClickListener, PoiSearch.OnPoiSearchListener, RadioGroup.OnCheckedChangeListener,
    RouteSearch.OnRouteSearchListener, GeocodeSearch.OnGeocodeSearchListener {
  private static final String TAG = "Tag";
  @Bind(R.id.map) MapView mapView;
  @Bind(R.id.showMaps) Button showMaps;
  @Bind(R.id.searchMaps) Button searchMaps;
  @Bind(R.id.queryMaps) Button queryMaps;

  private AMap aMap;
  private LocationManagerProxy mAMapLocationManager;
  private LocationSource.OnLocationChangedListener mListener;
  private PopupWindow mPopupWindow;
  private View mPopView;
  private PopupWindow searchPopupWindow;
  private PopupWindow queryPopupWindow;
  private View mSearchView;
  private EditText keyWordET;
  private Button searchBtn;
  private ImageView ivDelete;
  // 要输入的poi搜索关键字
  private String keyWord = "";
  // 搜索时进度条
  private ProgressDialog progDialog = null;
  // Poi查询条件类
  private PoiSearch.Query query;
  private double latitude;
  private double longitude;
  private String district;
  private View mQueryView;
  private EditText setStart;
  private EditText setEnd;
  private Button goQuery;

  private int routeType;
  private boolean isStart;
  private boolean isEnd;
  private GeocodeSearch mGeocodeSearch;
  private LatLonPoint startPoint;
  private LatLonPoint endPoint;
  private RouteSearch routeSearch;
  private int busMode = RouteSearch.BusDefault;// 公交默认模式
  private int drivingMode = RouteSearch.DrivingDefault;// 驾车默认模式
  private int walkMode = RouteSearch.WalkDefault;// 步行默认模式
  private Button addEndPlaceBtn;
  private Button addStartPlaceBtn;
  private String cityCode;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.map_pager);
    ButterKnife.bind(this);
    mapView.onCreate(savedInstanceState);// 必须要写
    initAMap();
    showMaps.setOnClickListener(this);
    searchMaps.setOnClickListener(this);
    queryMaps.setOnClickListener(this);
  }

  private void initAMap() {
    if (aMap == null) {
      aMap = mapView.getMap();
    }
    mGeocodeSearch = new GeocodeSearch(this);
    mGeocodeSearch.setOnGeocodeSearchListener(this);
    setUpMap();
    mAMapLocationManager.setGpsEnable(true);
    aMap.getUiSettings().setCompassEnabled(true);
    setUpMapListener();
  }

  private void setUpMapListener() {
    aMap.setOnMapClickListener(this);
    final LatLng[] position = new LatLng[1];
    // 设置marker可拖拽事件监听器
    aMap.setOnMarkerDragListener(new AMap.OnMarkerDragListener() {
      @Override public void onMarkerDragStart(Marker marker) {
        position[0] = marker.getPosition();
        Toast.makeText(MapActivity.this, "现在的位置:" + position[0], Toast.LENGTH_SHORT).show();
        Toast.makeText(MapActivity.this, "轻点拖~~~疼~~", Toast.LENGTH_SHORT).show();
      }

      @Override public void onMarkerDrag(Marker marker) {

      }

      @Override public void onMarkerDragEnd(Marker marker) {
        position[0] = marker.getPosition();
        Toast.makeText(MapActivity.this, "现在的位置:" + position[0], Toast.LENGTH_SHORT).show();
      }
    });
    aMap.setOnMarkerClickListener(this);
  }

  @Override public void onMapClick(LatLng latLng) {
    MarkerOptions markerOptions = new MarkerOptions();
    // 设置Marker的坐标，为我们点击地图的经纬度坐标
    markerOptions.position(latLng);
    // 设置Marker的可见性
    markerOptions.visible(true);
    // 设置Marker是否可以被拖拽，这里先设置为false，之后会演示Marker的拖拽功能
    markerOptions.draggable(true);
    // 将Marker添加到地图上去
    aMap.addMarker(markerOptions);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.showMaps:
        showPopupWindow();
        break;
      case R.id.normal:
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        break;
      case R.id.satellite:
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        break;
      case R.id.night:
        aMap.setMapType(AMap.MAP_TYPE_NIGHT);
        break;
      case R.id.tosee:
        Toast.makeText(this, "停下来看看妹子吧~~", Toast.LENGTH_SHORT).show();
        break;
      case R.id.searchMaps:
        searchPopupWindow();
        break;
      /**
       * 搜索附近按钮
       */
      case R.id.btnSearch:
        searchButton();
        break;
      case R.id.queryMaps:
        queryPopupWindow();
        break;
      case R.id.addStartPlaceBtn:
        isStart = true;
        isEnd = false;
        addPoint(setStart.getText().toString().trim());
        break;
      case R.id.addEndPlaceBtn:
        isStart = false;
        isEnd = true;
        addPoint(setEnd.getText().toString().trim());
        break;
      /**
       * 查询路线按钮
       */
      case R.id.goQuery:
        searchRouteResult(startPoint, endPoint);
        break;
    }
  }

  private void addPoint(String address) {
    // 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
    GeocodeQuery query = new GeocodeQuery(address, cityCode);
    mGeocodeSearch.getFromLocationNameAsyn(query);
  }

  /**
   * 显示进度框
   */
  private void showProgressDialog() {
    if (progDialog == null) progDialog = new ProgressDialog(this);
    progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progDialog.setIndeterminate(false);
    progDialog.setCancelable(false);
    progDialog.setMessage("正在搜索:\n" + keyWord);
    progDialog.show();
  }

  /**
   * 隐藏进度框
   */
  private void dismissProgressDialog() {
    if (progDialog != null) {
      progDialog.dismiss();
    }
  }

  private void searchButton() {
    doSearchQuery();
  }

  private void doSearchQuery() {
    showProgressDialog();// 显示进度框
    // 第一个参数表示搜索字符串，第二个参数表示POI搜索类型
    // 第三个参数表示POI搜索区域
    query = new PoiSearch.Query(keyWord, keyWordET.getText().toString().trim(), district);
    PoiSearch poiSearch = new PoiSearch(this, query);
    // 搜索定位点附近5000米范围
    poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 5000));
    poiSearch.setOnPoiSearchListener(this);
    poiSearch.searchPOIAsyn();
  }

  @Override public void onPoiSearched(PoiResult poiResult, int rCode) {
    dismissProgressDialog();// 隐藏对话框
    if (rCode == 0) {
      // 搜索POI的结果
      if (poiResult != null && poiResult.getQuery() != null) {
        // 是否是同一条
        if (poiResult.getQuery().equals(query)) {
          // 取得搜索到的poiitems有多少页
          int resultPages = poiResult.getPageCount();
          // 取得第一页的poiitem数据，页数从数字0开始
          List poiItems = poiResult.getPois();
          // 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
          List suggestionCities = poiResult.getSearchSuggestionCitys();

          dismissProgressDialog();// 隐藏对话框
          if (poiItems != null && poiItems.size() > 0) {
            aMap.clear();//清理之前的图标
            PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
            poiOverlay.removeFromMap();
            poiOverlay.addToMap();
            poiOverlay.zoomToSpan();
          } else if (suggestionCities != null && suggestionCities.size() > 0) {
            showSuggestCity(suggestionCities);
          } else {
            Toast.makeText(this, "没有搜索到相关数据", Toast.LENGTH_SHORT).show();
          }
        }
      } else {
        dismissProgressDialog();// 隐藏对话框
        Toast.makeText(this, "没有搜索到相关数据", Toast.LENGTH_SHORT).show();
      }
    } else {
      dismissProgressDialog();// 隐藏对话框
      Toast.makeText(this, "搜索失败,请检查网络连接！", Toast.LENGTH_SHORT).show();
    }
  }

  @Override public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int rCode) {
  }

  /**
   * poi没有搜索到数据，返回一些推荐城市的信息
   */
  private void showSuggestCity(List<SuggestionCity> cities) {
    String infomation = "推荐城市\n";
    for (int i = 0; i < cities.size(); i++) {
      infomation += "城市名称:"
          + cities.get(i).getCityName()
          + "城市区号:"
          + cities.get(i).getCityCode()
          + "城市编码:"
          + cities.get(i).getAdCode()
          + "\n";
    }
    Toast.makeText(this, infomation, Toast.LENGTH_SHORT).show();
  }

  private void searchPopupWindow() {
    mSearchView = LayoutInflater.from(this).inflate(R.layout.search_pop, null);
    keyWordET = (EditText) mSearchView.findViewById(R.id.keyword);
    searchBtn = (Button) mSearchView.findViewById(R.id.btnSearch);
    ivDelete = (ImageView) mSearchView.findViewById(R.id.ivDelete);
    searchPopupWindow = new PopupWindow(mSearchView, ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT, true);
    showBasePopupWindow(searchPopupWindow, searchMaps);
    searchBtn.setOnClickListener(this);
    ivDelete.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        keyWordET.setText("");
      }
    });
    keyWordET.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override public void afterTextChanged(Editable s) {
        ivDelete.setVisibility(s.length() == 0 ? View.GONE : View.VISIBLE);
      }
    });
  }

  @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
    switch (checkedId) {
      case R.id.bus_rb:
        routeType = 1;// 标识为公交模式
        break;
      case R.id.car_rb:
        routeType = 2;// 标识为驾车模式
        break;
      case R.id.walk_rb:
        routeType = 3;// 标识为步行模式
        break;
    }
  }

  private void queryPopupWindow() {
    mQueryView = LayoutInflater.from(this).inflate(R.layout.route_pop, null);
    setStart = (EditText) mQueryView.findViewById(R.id.setStart);
    setEnd = (EditText) mQueryView.findViewById(R.id.setEnd);
    RadioGroup group = (RadioGroup) mQueryView.findViewById(R.id.search_type_rg);
    goQuery = (Button) mQueryView.findViewById(R.id.goQuery);
    addStartPlaceBtn = (Button) mQueryView.findViewById(R.id.addStartPlaceBtn);
    addEndPlaceBtn = (Button) mQueryView.findViewById(R.id.addEndPlaceBtn);
    queryPopupWindow = new PopupWindow(mQueryView, ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT, true);
    queryPopupWindow.setFocusable(true);
    queryPopupWindow.setOutsideTouchable(true);
    queryPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    int[] location = new int[2];
    mQueryView.getLocationOnScreen(location);
    queryPopupWindow.showAtLocation(mQueryView, Gravity.CENTER, location[0], location[1] - queryPopupWindow.getHeight());
    queryPopupWindow.update();
    group.setOnCheckedChangeListener(this);
    addStartPlaceBtn.setOnClickListener(this);
    addEndPlaceBtn.setOnClickListener(this);
    goQuery.setOnClickListener(this);
  }

  private void showBasePopupWindow(PopupWindow mBasePopupWindow, Button mBaseButton) {
    mBasePopupWindow.setFocusable(true);
    mBasePopupWindow.setOutsideTouchable(true);
    mBasePopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    mBasePopupWindow.showAsDropDown(mBaseButton, 0, 15);
    mBasePopupWindow.update();
  }

  private void showPopupWindow() {
    mPopView = LayoutInflater.from(this).inflate(R.layout.item_pop, null);
    Button normal = (Button) mPopView.findViewById(R.id.normal);
    Button satellite = (Button) mPopView.findViewById(R.id.satellite);
    Button night = (Button) mPopView.findViewById(R.id.night);
    ImageView toSee = (ImageView) mPopView.findViewById(R.id.tosee);
    mPopupWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT, true);
    showBasePopupWindow(mPopupWindow, showMaps);
    normal.setOnClickListener(this);
    satellite.setOnClickListener(this);
    night.setOnClickListener(this);
    toSee.setOnClickListener(this);
  }

  private void setUpMap() {
    // 设置定位监听。如果不设置此定位资源则定位按钮不可点击。
    aMap.setLocationSource(this);
    // 设置默认定位按钮是否显示
    aMap.getUiSettings().setMyLocationButtonEnabled(true);
    // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    aMap.setMyLocationEnabled(true);
    //设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
    aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    // 自定义系统定位蓝点
    MyLocationStyle myLocationStyle = new MyLocationStyle();
    // 自定义定位蓝点图标
    myLocationStyle.myLocationIcon(
        BitmapDescriptorFactory.fromResource(R.mipmap.navi_map_gps_locked));
    // 自定义精度范围的圆形边框颜色
    myLocationStyle.strokeColor(Color.parseColor("#81d4fa"));
    // 设置圆形的填充颜色
    myLocationStyle.radiusFillColor(Color.parseColor("#33b3e5fe"));
    //自定义精度范围的圆形边框宽度
    myLocationStyle.strokeWidth(5);
    // 将自定义的 myLocationStyle 对象添加到地图上
    aMap.setMyLocationStyle(myLocationStyle);
    // 构造 LocationManagerProxy 对象
    mAMapLocationManager = LocationManagerProxy.getInstance(MapActivity.this);
  }

  @Override public void onLocationChanged(AMapLocation aMapLocation) {
    latitude = aMapLocation.getLatitude();
    longitude = aMapLocation.getLongitude();
    district = aMapLocation.getDistrict();
    cityCode = aMapLocation.getCityCode();
    if (mListener != null && aMapLocation != null) {
      mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
    }
    Toast.makeText(MapActivity.this, "现在的位置:" + aMapLocation.getExtras(), Toast.LENGTH_SHORT)
        .show();
  }

  @Override protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override protected void onPause() {
    super.onPause();
    mapView.onPause();
    deactivate();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  /**
   * 方法必须重写
   */
  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  /**
   * 激活定位
   */
  @Override public void activate(OnLocationChangedListener listener) {
    mListener = listener;
    if (mAMapLocationManager == null) {
      mAMapLocationManager = LocationManagerProxy.getInstance(this);
      // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
      // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
      // 在定位结束后，在合适的生命周期调用destroy()方法
      // 其中如果间隔时间为-1，则定位只定一次
      // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
      mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 10,
          this);
    }
  }

  /**
   * 停止定位
   */
  public void deactivate() {
    mListener = null;
    if (mAMapLocationManager != null) {
      mAMapLocationManager.removeUpdates(this);
      mAMapLocationManager.destroy();
    }
    mAMapLocationManager = null;
  }

  /**
   * 方法被弃
   */
  @Override public void onLocationChanged(Location location) {
  }

  @Override public void onStatusChanged(String provider, int status, Bundle extras) {
  }

  @Override public void onProviderEnabled(String provider) {
  }

  @Override public void onProviderDisabled(String provider) {
  }

  @Override public boolean onMarkerClick(Marker marker) {
    return false;
  }

  /**
   * 开始搜索路径规划方案
   */
  public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
    showProgressDialog();
    RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
    routeSearch = new RouteSearch(this);
    routeSearch.setRouteSearchListener(this);
    if (routeType == 1) {// 公交路径规划
      // 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
      RouteSearch.BusRouteQuery query =
          new RouteSearch.BusRouteQuery(fromAndTo, busMode, cityCode, 0);
      routeSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
    } else if (routeType == 2) {// 驾车路径规划
      // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
      RouteSearch.DriveRouteQuery query =
          new RouteSearch.DriveRouteQuery(fromAndTo, drivingMode, null, null, "");
      routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    } else if (routeType == 3) {// 步行路径规划
      RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, walkMode);
      routeSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
    }
  }

  @Override public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
    // 路径规划中公交模式
    progDialog.dismiss();
    if (i == 0) {
      if (busRouteResult != null
          && busRouteResult.getPaths() != null
          && busRouteResult.getPaths().size() > 0) {
        BusPath busPath = busRouteResult.getPaths().get(0);
        aMap.clear();//清理之前的图标
        BusRouteOverlay routeOverlay =
            new BusRouteOverlay(this, aMap, busPath, busRouteResult.getStartPos(),
                busRouteResult.getTargetPos());
        routeOverlay.removeFromMap();
        routeOverlay.addToMap();
        routeOverlay.zoomToSpan();
      } else {
        Toast.makeText(this, "没有搜索到结果", Toast.LENGTH_SHORT).show();
      }
    } else {
      Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
    }
  }

  @Override public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
    // 路径规划中驾车模式
    progDialog.dismiss();
    if (i == 0) {
      if (driveRouteResult != null
          && driveRouteResult.getPaths() != null
          && driveRouteResult.getPaths().size() > 0) {
        DrivePath drivePath = driveRouteResult.getPaths().get(0);
        aMap.clear();//清理之前的图标
        DrivingRouteOverlay drivingRouteOverlay =
            new DrivingRouteOverlay(this, aMap, drivePath, driveRouteResult.getStartPos(),
                driveRouteResult.getTargetPos());
        drivingRouteOverlay.removeFromMap();
        drivingRouteOverlay.addToMap();
        drivingRouteOverlay.zoomToSpan();
      } else {
        dismissProgressDialog();// 隐藏对话框
        Toast.makeText(this, "没有搜索到结果", Toast.LENGTH_SHORT).show();
      }
    } else {
      dismissProgressDialog();// 隐藏对话框
      Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
    }
  }

  @Override public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
    // 路径规划中步行模式
    progDialog.dismiss();
    if (i == 0) {
      if (walkRouteResult != null
          && walkRouteResult.getPaths() != null
          && walkRouteResult.getPaths().size() > 0) {
        WalkPath walkPath = walkRouteResult.getPaths().get(0);

        aMap.clear();//清理之前的图标
        WalkRouteOverlay walkRouteOverlay =
            new WalkRouteOverlay(this, aMap, walkPath, walkRouteResult.getStartPos(),
                walkRouteResult.getTargetPos());
        walkRouteOverlay.removeFromMap();
        walkRouteOverlay.addToMap();
        walkRouteOverlay.zoomToSpan();
      } else {
        progDialog.dismiss();// 隐藏对话框
        Toast.makeText(this, "没有搜索到结果", Toast.LENGTH_SHORT).show();
      }
    } else {
      progDialog.dismiss();// 隐藏对话框
      Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
    }
  }

  @Override public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
  }

  @Override public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
    if (i == 0) {
      if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null
          && geocodeResult.getGeocodeAddressList().size() > 0) {
        LatLonPoint latLonPoint = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
        if (isStart) {
          startPoint = new LatLonPoint(latLonPoint.getLatitude(), latLonPoint.getLongitude());
          Toast.makeText(this, "加入起点成功", Toast.LENGTH_SHORT).show();
        } else if (isEnd) {
          endPoint = new LatLonPoint(latLonPoint.getLatitude(), latLonPoint.getLongitude());
          Toast.makeText(this, "加入终点成功", Toast.LENGTH_SHORT).show();
        }
      } else {
        Toast.makeText(this, "没有结果", Toast.LENGTH_SHORT).show();
      }
    } else {
      Toast.makeText(this, "网络连接失败", Toast.LENGTH_SHORT).show();
    }
  }
}
